package opgg.backend.gmakersserver.domain.profile.service;

import com.merakianalytics.orianna.types.core.summoner.Summoner;
import lombok.RequiredArgsConstructor;
import opgg.backend.gmakersserver.domain.account.entity.Account;
import opgg.backend.gmakersserver.domain.account.repository.AccountRepository;
import opgg.backend.gmakersserver.domain.leagueposition.entity.Queue;
import opgg.backend.gmakersserver.domain.leagueposition.service.LeaguePositionService;
import opgg.backend.gmakersserver.domain.preferchampion.service.PreferChampionService;
import opgg.backend.gmakersserver.domain.preferline.service.PreferLineService;
import opgg.backend.gmakersserver.domain.profile.controller.request.ProfileRequest;
import opgg.backend.gmakersserver.domain.profile.controller.response.ProfileFindResponse;
import opgg.backend.gmakersserver.domain.profile.controller.response.ProfileResponse;
import opgg.backend.gmakersserver.domain.profile.entity.Profile;
import opgg.backend.gmakersserver.domain.profile.entity.SummonerInfo;
import opgg.backend.gmakersserver.domain.profile.repository.ProfileRepository;
import opgg.backend.gmakersserver.error.exception.account.AccountNotFoundException;
import opgg.backend.gmakersserver.error.exception.profile.ProfileBoundsException;
import opgg.backend.gmakersserver.error.exception.profile.ProfileExistException;
import opgg.backend.gmakersserver.error.exception.profile.ProfileNotExistException;
import opgg.backend.gmakersserver.error.exception.riotapi.SummonerNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ProfileService {

	private final ProfileRepository profileRepository;
	private final AccountRepository accountRepository;
	private final LeaguePositionService leaguePositionService;
	private final PreferLineService preferLineService;
	private final PreferChampionService preferChampionService;

	private boolean isNotCreateProfile(long count) {
		return count >= 3;
	}

	private int getRandomIconId(int profileIconId) {
		Random random = new Random();
		random.setSeed(System.currentTimeMillis());
		int iconId = profileIconId;
		while (iconId == profileIconId) {
			iconId = random.nextInt(28);
		}
		return iconId;
	}

	private boolean isReliable(Profile profile) {
		Summoner summoner = Summoner.withId(profile.getSummonerInfo().getSummonerId()).get();
		int summonerProfileIconId = summoner.getProfileIcon().getId();
		if (profile.isAuthorizable(summonerProfileIconId)) {
			profile.changeIsCertified(true);
			profile.changeAuthProfileIconId(-1);
			return true;
		}
		return false;
	}

	private boolean isAuthProfile(Profile profile) {
		return !profile.isCertified() && !isReliable(profile);
	}

	private boolean isAuthConfirm(Profile profile) {
		return profile.isCertified() || isReliable(profile);
	}

	@Transactional
	public void createProfile(ProfileRequest.Create profileRequest, Long id) {
		String summonerName = profileRequest.getSummonerName();
		Summoner summoner = Summoner.named(summonerName).get();
		if (ObjectUtils.isEmpty(summoner.getProfileIcon())) {
			throw new SummonerNotFoundException();
		}

		Account account = accountRepository.findByAccountId(id).orElseThrow(AccountNotFoundException::new);

		long profileCount = profileRepository.countByAccount(account);
		if (isNotCreateProfile(profileCount)) {
			throw new ProfileBoundsException();
		}

		Profile findProfile = profileRepository.findByAccountAndSummonerName(account.getAccountId(), summonerName);

		if (!ObjectUtils.isEmpty(findProfile)) {
			throw new ProfileExistException();
		}

		SummonerInfo summonerInfo = SummonerInfo.builder()
				.summonerId(summoner.getId())
				.summonerName(summoner.getName())
				.profileIconId(summoner.getProfileIcon().getId())
				.build();

		Profile profile = Profile.builder()
				.account(account)
				.isCertified(false)
				.authProfileIconId(null)
				.summonerAccountId(summoner.getAccountId())
				.summonerInfo(summonerInfo)
				.build();

		profileRepository.save(profile);

		preferChampionService.createPreferChampion(profileRequest, profile);
		preferLineService.createPreferLine(profileRequest, profile);
		leaguePositionService.createLeaguePosition(summoner, profile);

		Queue queue = leaguePositionService.getPreferQueue(profileRequest, profile);
		profile.changePreferQueue(queue);
	}

	@Transactional
	public ProfileResponse.Auth authProfile(ProfileRequest.Auth auth, Long id) {
		String summonerId = auth.getSummonerId();
		Profile profile = profileRepository.findBySummonerIdAndAccountId(summonerId, id)
				.orElseThrow(SummonerNotFoundException::new);
		int iconId = -1;
		if (isAuthProfile(profile)) {
			int profileIconId = profile.getSummonerInfo().getProfileIconId();
			iconId = getRandomIconId(profileIconId);
			profile.changeAuthProfileIconId(iconId);
		}
		return new ProfileResponse.Auth(iconId);
	}

	@Transactional
	public ProfileResponse.AuthConfirm authConfirm(ProfileRequest.Auth auth, Long id) {
		String summonerId = auth.getSummonerId();
		Profile profile = profileRepository.findBySummonerIdAndAccountId(summonerId, id)
				.orElseThrow(SummonerNotFoundException::new);
		return new ProfileResponse.AuthConfirm(isAuthConfirm(profile));
	}

	@Transactional(readOnly = true)
	public List<ProfileFindResponse> getProfiles(Long id) {
		Account account = accountRepository.findByAccountId(id).orElseThrow(AccountNotFoundException::new);
		List<ProfileFindResponse> profileMainByAccount = profileRepository.findProfileMainByAccount(account);

		if (CollectionUtils.isEmpty(profileMainByAccount)) {
			throw new ProfileNotExistException();
		}

		return profileMainByAccount;
	}

}
