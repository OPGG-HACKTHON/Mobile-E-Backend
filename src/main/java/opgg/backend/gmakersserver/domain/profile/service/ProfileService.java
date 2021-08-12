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
import opgg.backend.gmakersserver.domain.profile.entity.Profile;
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
		Summoner summoner = Summoner.named(summonerName).withRegion(Region.KOREA).get();
		if (ObjectUtils.isEmpty(summoner.getProfileIcon())) {
			throw new SummonerNotFoundException();
		}

		Account account = accountRepository.findByAccountId(id).orElseThrow(AccountNotFoundException::new);
		Profile profile = profileRepository.findByAccountAndSummonerName(account, summonerName);

		if (!ObjectUtils.isEmpty(profile)) {
			throw new ProfileExistException();
		}

		int profileIconId = summoner.getProfileIcon().getId();
		String summonerId = summoner.getId();
		profileRepository.save(Profile.builder()
										.account(account)
										.isCertified(false)
										.summonerName(summonerName)
										.profileIconId(profileIconId)
										.summonerId(summonerId)
										.authProfileIconId(null)
										.build());
	}

	@Transactional
	public String authProfile(ProfileRequest.Auth auth, Long id) {
		String summonerId = auth.getSummonerId();
		Profile findProfile = profileRepository.findBySummonerIdAndAccountId(summonerId, id)
				.orElseThrow(SummonerNotFoundException::new);

		Summoner summoner = Summoner.withId(findProfile.getSummonerId()).withRegion(Region.KOREA).get();
		int summonerProfileIconId = summoner.getProfileIcon().getId();
		findProfile.changeAuthProfileIconId(summonerProfileIconId);
		int profileIconId = findProfile.getProfileIconId();

		Random random = new Random();
		random.setSeed(System.currentTimeMillis());
		int iconId = profileIconId;
		while (iconId == profileIconId) {
			iconId = random.nextInt(28);
		}
		String url = "https://ddragon.leagueoflegends.com/cdn/11.15.1/img/profileicon/" + iconId + ".png";
		return url;
	}

}
