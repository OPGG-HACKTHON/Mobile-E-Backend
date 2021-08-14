package opgg.backend.gmakersserver.domain.profile.service;

import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.merakianalytics.orianna.types.common.Region;
import com.merakianalytics.orianna.types.core.summoner.Summoner;

import lombok.RequiredArgsConstructor;
import opgg.backend.gmakersserver.domain.account.entity.Account;
import opgg.backend.gmakersserver.domain.account.repository.AccountRepository;
import opgg.backend.gmakersserver.domain.profile.controller.request.ProfileRequest;
import opgg.backend.gmakersserver.domain.profile.controller.response.ProfileResponse;
import opgg.backend.gmakersserver.domain.profile.entity.Profile;
import opgg.backend.gmakersserver.domain.profile.entity.SummonerInfo;
import opgg.backend.gmakersserver.domain.profile.repository.ProfileRepository;
import opgg.backend.gmakersserver.error.exception.account.AccountNotFoundException;
import opgg.backend.gmakersserver.error.exception.profile.ProfileExistException;
import opgg.backend.gmakersserver.error.exception.riotapi.SummonerNotFoundException;

@Service
@RequiredArgsConstructor
public class ProfileService {

	private final ProfileRepository profileRepository;
	private final AccountRepository accountRepository;

	@Transactional
	public void createProfile(ProfileRequest.Create profileRequest, Long id) {
		String summonerName = profileRequest.getSummonerName();
		Summoner summoner = Summoner.named(summonerName).get();
		if (ObjectUtils.isEmpty(summoner.getProfileIcon())) {
			throw new SummonerNotFoundException();
		}

		Account account = accountRepository.findByAccountId(id).orElseThrow(AccountNotFoundException::new);
		Profile profile = profileRepository.findByAccountAndSummonerName(account.getAccountId(), summonerName);

		if (!ObjectUtils.isEmpty(profile)) {
			throw new ProfileExistException();
		}

		int profileIconId = summoner.getProfileIcon().getId();
		String summonerId = summoner.getId();
		SummonerInfo summonerInfo = SummonerInfo.builder()
				.summonerId(summonerId)
				.profileIconId(profileIconId)
				.summonerName(summonerName)
				.build();
		profileRepository.save(Profile.builder()
				.account(account)
				.isCertified(false)
				.summonerInfo(summonerInfo)
				.authProfileIconId(null)
				.build());
	}

	@Transactional
	public ProfileResponse authProfile(ProfileRequest.Auth auth, Long id) {
		String summonerId = auth.getSummonerId();
		Profile profile = profileRepository.findBySummonerIdAndAccountId(summonerId, id)
				.orElseThrow(SummonerNotFoundException::new);

		if (profile.isCertified()) {
			return ProfileResponse.builder()
					.iconId(-1)
					.isCertified(true)
					.build();
		}
		Summoner summoner = Summoner.withId(profile.getSummonerInfo().getSummonerId()).get();
		int summonerProfileIconId = summoner.getProfileIcon().getId();
		if (profile.isAuthenticable(summonerProfileIconId)) {
			profile.changeIsCertified(true);
			return ProfileResponse.builder()
					.iconId(-1)
					.isCertified(true)
					.build();
		}
		int profileIconId = profile.getSummonerInfo().getProfileIconId();
		int iconId = getRandomIconId(profileIconId);
		profile.changeAuthProfileIconId(iconId);
		return ProfileResponse.builder()
				.iconId(iconId)
				.isCertified(false)
				.build();
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

}
