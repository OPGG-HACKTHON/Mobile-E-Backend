package opgg.backend.gmakersserver.domain.profile.service;

import java.util.List;
import java.util.Random;

import opgg.backend.gmakersserver.domain.preferKeyword.service.PreferKeywordService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.merakianalytics.orianna.types.core.summoner.Summoner;

import lombok.RequiredArgsConstructor;
import opgg.backend.gmakersserver.application.util.DeduplicationUtils;
import opgg.backend.gmakersserver.domain.account.entity.Account;
import opgg.backend.gmakersserver.domain.account.repository.AccountRepository;
import opgg.backend.gmakersserver.domain.leagueposition.entity.Queue;
import opgg.backend.gmakersserver.domain.leagueposition.service.LeaguePositionService;
import opgg.backend.gmakersserver.domain.preferchampion.service.PreferChampionService;
import opgg.backend.gmakersserver.domain.preferline.service.PreferLineService;
import opgg.backend.gmakersserver.domain.profile.controller.request.ProfileRequest;
import opgg.backend.gmakersserver.domain.profile.controller.response.ProfileDetailResponse;
import opgg.backend.gmakersserver.domain.profile.controller.response.ProfileFindResponse;
import opgg.backend.gmakersserver.domain.profile.controller.response.ProfileResponse;
import opgg.backend.gmakersserver.domain.profile.entity.Profile;
import opgg.backend.gmakersserver.domain.profile.entity.SummonerInfo;
import opgg.backend.gmakersserver.domain.profile.repository.ProfileRepository;
import opgg.backend.gmakersserver.error.exception.account.AccountNotFoundException;
import opgg.backend.gmakersserver.error.exception.preferchampion.PreferChampionBoundsException;
import opgg.backend.gmakersserver.error.exception.preferchampion.PreferChampionPriorityDuplicateException;
import opgg.backend.gmakersserver.error.exception.preferline.PreferLineBoundsException;
import opgg.backend.gmakersserver.error.exception.preferline.PreferLinePriorityDuplicateException;
import opgg.backend.gmakersserver.error.exception.profile.ProfileBoundsException;
import opgg.backend.gmakersserver.error.exception.profile.ProfileExistException;
import opgg.backend.gmakersserver.error.exception.profile.ProfileNotExistException;
import opgg.backend.gmakersserver.error.exception.profile.ProfileNotMatchException;
import opgg.backend.gmakersserver.error.exception.riotapi.SummonerNotFoundException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProfileService {

	private final ProfileRepository profileRepository;
	private final AccountRepository accountRepository;
	private final LeaguePositionService leaguePositionService;
	private final PreferLineService preferLineService;
	private final PreferChampionService preferChampionService;
	private final PreferKeywordService preferKeywordService;

	private List<ProfileFindResponse> getProfileFindResponses(String summonerName, Account account) {
		List<ProfileFindResponse> profileMainByAccount;
		if (!StringUtils.isBlank(summonerName)) {
			profileMainByAccount = profileRepository.findProfileBySummonerName(summonerName);
			if (CollectionUtils.isEmpty(profileMainByAccount)) {
				Summoner summoner = Summoner.named(summonerName).get();
				summoner.load();
				if (ObjectUtils.isEmpty(summoner)) {
					throw new SummonerNotFoundException();
				}
				String findSummonerName = summoner.getCoreData().getName();
				profileMainByAccount = profileRepository.findProfileBySummonerName(findSummonerName);
			}
		} else {
			profileMainByAccount = profileRepository.findProfileMainByAccount(account);
		}
		return profileMainByAccount;
	}

	private boolean isNotCreatePreferLines(List<ProfileRequest.Create.PreferLine> preferLines) {
		if (preferLines.size() > 2) {
			return true;
		}
		int size = preferLines.size();
		int distinctSize = DeduplicationUtils.deduplication(preferLines, ProfileRequest.Create.PreferLine::getPriority)
				.size();
		if (size != distinctSize) {
			throw new PreferLinePriorityDuplicateException();
		}
		return false;
	}

	private boolean isNotCreatePreferChampions(List<ProfileRequest.Create.PreferChampion> preferChampions) {
		if (preferChampions.size() > 3) {
			return true;
		}
		int size = preferChampions.size();
		int distinctSize = DeduplicationUtils.deduplication(preferChampions,
				ProfileRequest.Create.PreferChampion::getPriority).size();
		if (size != distinctSize) {
			throw new PreferChampionPriorityDuplicateException();
		}
		return false;
	}

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

		if (isNotCreatePreferChampions(profileRequest.getPreferChampions())) {
			throw new PreferChampionBoundsException();
		}

		if (isNotCreatePreferLines(profileRequest.getPreferLines())) {
			throw new PreferLineBoundsException();
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
				.description(profileRequest.getDescription())
				.build();

		profileRepository.save(profile);

		preferChampionService.createPreferChampion(profileRequest.getPreferChampions(),
				profileRequest.getSummonerName(), profile);
		preferLineService.createPreferLine(profileRequest.getPreferLines(), profile);
		leaguePositionService.createLeaguePosition(summoner, profile);
		preferKeywordService.createPreferKeyword(profileRequest, profile);

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
			iconId = getRandomIconId(profile.getSummonerInfo().getProfileIconId());
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

	public List<ProfileFindResponse> getProfiles(String summonerName, Long id) {
		Account account = accountRepository.findByAccountId(id).orElseThrow(AccountNotFoundException::new);
		List<ProfileFindResponse> profileMainByAccount = getProfileFindResponses(summonerName, account);
		if (CollectionUtils.isEmpty(profileMainByAccount)) {
			throw new ProfileNotExistException();
		}

		return new ProfileFindResponse().convert(profileMainByAccount);
	}

	public ProfileDetailResponse getProfile(Long profileId, Long id) {
		Account account = accountRepository.findByAccountId(id).orElseThrow(AccountNotFoundException::new);
		Profile profile = profileRepository.findById(profileId).orElseThrow(ProfileNotExistException::new);
		List<ProfileDetailResponse> profileDetailResponses = profileRepository.findProfileDetailByAccountAndProfile(
				account, profile);
		return new ProfileDetailResponse(profileDetailResponses);
	}

	@Transactional
	public void deleteProfile(Long profileId) {
		Profile profile = profileRepository.findById(profileId).orElseThrow(ProfileNotExistException::new);
		profileRepository.delete(profile);
	}

}