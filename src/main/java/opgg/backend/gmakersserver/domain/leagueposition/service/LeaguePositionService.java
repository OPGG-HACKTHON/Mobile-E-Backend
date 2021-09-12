package opgg.backend.gmakersserver.domain.leagueposition.service;

import static opgg.backend.gmakersserver.domain.leagueposition.entity.LeaguePosition.*;
import static opgg.backend.gmakersserver.domain.leagueposition.entity.Queue.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.merakianalytics.orianna.types.core.league.LeagueEntry;
import com.merakianalytics.orianna.types.core.league.LeaguePositions;
import com.merakianalytics.orianna.types.core.summoner.Summoner;

import lombok.RequiredArgsConstructor;
import opgg.backend.gmakersserver.domain.leagueposition.entity.LeaguePosition;
import opgg.backend.gmakersserver.domain.leagueposition.entity.Queue;
import opgg.backend.gmakersserver.domain.leagueposition.repository.LeaguePositionRepository;
import opgg.backend.gmakersserver.domain.profile.controller.request.ProfileRequest;
import opgg.backend.gmakersserver.domain.profile.entity.Profile;

@Service
@RequiredArgsConstructor
public class LeaguePositionService {

	private final LeaguePositionRepository leaguePositionRepository;

	@Transactional
	public void createLeaguePosition(Summoner summoner, Profile profile) {
		summoner.getLeaguePositions()
				.stream()
				.map(leagueEntry -> of(leagueEntry, profile, summoner))
				.forEach(leaguePositionRepository::save);
		leaguePositionRepository.save(from(profile, summoner));
	}

	@Transactional
	public void updateLeaguePosition(Summoner summoner, Profile profile) {
		Queue preferQueue = profile.getPreferQueue();

		summoner.getLeaguePositions().stream()
				.filter(leagueEntry -> preferQueue == valueOf(String.valueOf(leagueEntry.getQueue()))) // 내 선호큐에 맞는 데이터 leagueEntry
				.findFirst()
				.ifPresent(leagueEntry -> profile.changeLeaguePosition(leagueEntry, summoner));
	}

	@Transactional(readOnly = true)
	public Queue getPreferQueue(Queue preferQueue, Profile profile) {
		return leaguePositionRepository.findByProfile(profile)
				.stream()
				.filter(leaguePosition -> leaguePosition.isQueueMatch(preferQueue))
				.map(LeaguePosition::getQueue)
				.findFirst()
				.orElse(NONE);
	}

}
