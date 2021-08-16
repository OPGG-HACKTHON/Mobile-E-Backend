package opgg.backend.gmakersserver.domain.profile.service;

import com.merakianalytics.orianna.types.core.league.LeaguePositions;
import com.merakianalytics.orianna.types.core.summoner.Summoner;
import lombok.RequiredArgsConstructor;
import opgg.backend.gmakersserver.domain.account.entity.Account;
import opgg.backend.gmakersserver.domain.account.repository.AccountRepository;
import opgg.backend.gmakersserver.domain.leagueposition.entity.LeaguePosition;
import opgg.backend.gmakersserver.domain.leagueposition.entity.Queue;
import opgg.backend.gmakersserver.domain.leagueposition.entity.Tier;
import opgg.backend.gmakersserver.domain.leagueposition.entity.TierLevel;
import opgg.backend.gmakersserver.domain.leagueposition.repository.LeaguePositionRepository;
import opgg.backend.gmakersserver.domain.profile.controller.request.ProfileRequest;
import opgg.backend.gmakersserver.domain.profile.controller.response.ProfileResponse;
import opgg.backend.gmakersserver.domain.profile.entity.Profile;
import opgg.backend.gmakersserver.domain.profile.entity.SummonerInfo;
import opgg.backend.gmakersserver.domain.profile.repository.ProfileRepository;
import opgg.backend.gmakersserver.error.exception.account.AccountNotFoundException;
import opgg.backend.gmakersserver.error.exception.profile.ProfileExistException;
import opgg.backend.gmakersserver.error.exception.riotapi.SummonerNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class ProfileService {

	private final ProfileRepository profileRepository;
	private final AccountRepository accountRepository;
	private final LeaguePositionRepository leaguePositionRepository;

	private int getWinRate(int wins, int losses) {
		return (int) Math.floor((double) wins / (wins + losses) * 100);
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

		Account findAccount = accountRepository.findByAccountId(id).orElseThrow(AccountNotFoundException::new);
		Profile findProfile = profileRepository.findByAccountAndSummonerName(findAccount.getAccountId(), summonerName);

		if (!ObjectUtils.isEmpty(findProfile)) {
			throw new ProfileExistException();
		}

		int profileIconId = summoner.getProfileIcon().getId();
		String summonerId = summoner.getId();
		SummonerInfo summonerInfo = SummonerInfo.builder()
				.summonerId(summonerId)
				.summonerName(summonerName)
				.profileIconId(profileIconId)
				.build();

		Profile profile = Profile.builder()
				.account(findAccount)
				.isCertified(false)
				.authProfileIconId(null)
				.summonerInfo(summonerInfo)
				.build();

		profileRepository.saveAndFlush(profile);

		//TODO: Champion, Position 엔티티 생성해야 함

		LeaguePositions leaguePositions = summoner.getLeaguePositions();
		if (!leaguePositions.isEmpty()) {
			leaguePositions.forEach(leaguePosition -> {
				int leaguePoint = leaguePosition.getLeaguePoints();
				String tier = String.valueOf(leaguePosition.getTier());
				int tierLevel = TierLevel.valueOf(String.valueOf(leaguePosition.getDivision())).getLevel();
				int level = summoner.getLevel();
				int wins = leaguePosition.getWins();
				int losses = leaguePosition.getLosses();
				int winRate = getWinRate(wins, losses);

				Queue queue = Queue.valueOf(String.valueOf(leaguePosition.getQueue()));
				leaguePositionRepository.save(LeaguePosition.builder()
						.profile(profile)
						.tier(Tier.valueOf(tier))
						.tierLevel(tierLevel)
						.level(level)
						.winGames(wins)
						.loseGames(losses)
						.winRate(winRate)
						.leaguePoint(leaguePoint)
						.queue(queue)
						.build());
			});
		}
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

}
