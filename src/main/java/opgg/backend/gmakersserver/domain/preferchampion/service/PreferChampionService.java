package opgg.backend.gmakersserver.domain.preferchampion.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.merakianalytics.orianna.types.core.championmastery.ChampionMastery;
import com.merakianalytics.orianna.types.core.staticdata.Champion;
import com.merakianalytics.orianna.types.core.summoner.Summoner;

import lombok.RequiredArgsConstructor;
import opgg.backend.gmakersserver.domain.preferchampion.entity.PreferChampion;
import opgg.backend.gmakersserver.domain.preferchampion.repository.PreferChampionRepository;
import opgg.backend.gmakersserver.domain.profile.controller.request.ProfileRequest;
import opgg.backend.gmakersserver.domain.profile.entity.Profile;

@Service
@RequiredArgsConstructor
public class PreferChampionService {

	private final PreferChampionRepository preferChampionRepository;

	@Transactional
	public void createPreferChampion(ProfileRequest.Create profileRequest, Profile profile) {
		Summoner summoner = Summoner.named(profileRequest.getSummonerName()).get();
		profileRequest.getPreferChampions().forEach(requestPreferChampion -> {
			int championId = requestPreferChampion.getChampionId();
			ChampionMastery mastery = ChampionMastery.forSummoner(summoner).withChampion(
					Champion.withId(championId).get()).get();
			preferChampionRepository.save(PreferChampion.builder()
					.profile(profile)
					.championLevel(mastery.getLevel())
					.championPoints(mastery.getPoints())
					.priority(requestPreferChampion.getPriority())
					.build());
		});
	}

}
