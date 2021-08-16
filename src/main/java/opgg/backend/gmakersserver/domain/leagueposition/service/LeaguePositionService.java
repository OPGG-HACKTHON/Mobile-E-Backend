package opgg.backend.gmakersserver.domain.leagueposition.service;

import static opgg.backend.gmakersserver.domain.leagueposition.entity.Queue.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.merakianalytics.orianna.types.core.league.LeaguePositions;
import com.merakianalytics.orianna.types.core.summoner.Summoner;

import lombok.RequiredArgsConstructor;
import opgg.backend.gmakersserver.domain.account.entity.Account;
import opgg.backend.gmakersserver.domain.leagueposition.entity.LeaguePosition;
import opgg.backend.gmakersserver.domain.leagueposition.entity.Queue;
import opgg.backend.gmakersserver.domain.leagueposition.entity.Tier;
import opgg.backend.gmakersserver.domain.leagueposition.entity.TierLevel;
import opgg.backend.gmakersserver.domain.leagueposition.repository.LeaguePositionRepository;
import opgg.backend.gmakersserver.domain.profile.controller.request.ProfileRequest;
import opgg.backend.gmakersserver.domain.profile.entity.Profile;

@Service
@RequiredArgsConstructor
public class LeaguePositionService {

	private final LeaguePositionRepository leaguePositionRepository;

	private int getWinRate(int wins, int losses) {
		return (int)Math.floor((double)wins / (wins + losses) * 100);
	}

	@Transactional
	public void createLeaguePosition(Summoner summoner, Profile profile) {
		LeaguePositions leaguePositions = summoner.getLeaguePositions();
		if (!leaguePositions.isEmpty()) {
			leaguePositions.forEach(leaguePosition -> leaguePositionRepository.save(LeaguePosition.builder()
					.profile(profile)
					.tier(Tier.valueOf(String.valueOf(leaguePosition.getTier())))
					.tierLevel(TierLevel.valueOf(String.valueOf(leaguePosition.getDivision())).getLevel())
					.level(summoner.getLevel())
					.winGames(leaguePosition.getWins())
					.loseGames(leaguePosition.getLosses())
					.winRate(getWinRate(leaguePosition.getWins(), leaguePosition.getLosses()))
					.leaguePoint(leaguePosition.getLeaguePoints())
					.queue(Queue.valueOf(String.valueOf(leaguePosition.getQueue())))
					.build()));
		}
	}

	@Transactional
	public Queue getPreferQueue(ProfileRequest.Create profileRequest, Profile profile) {
		Queue queue = NONE;
		List<LeaguePosition> leaguePositions = leaguePositionRepository.findByProfile(profile);
		if (!CollectionUtils.isEmpty(leaguePositions)) {
			for (LeaguePosition leaguePosition : leaguePositions) {
				Queue leaguePositionQueue = leaguePosition.getQueue();
				Queue profileRequestPreferQueue = profileRequest.getPreferQueue();
				if (leaguePositionQueue == profileRequestPreferQueue) {
					queue = profileRequestPreferQueue;
					break;
				}
			}
		}
		return queue;
	}

}
