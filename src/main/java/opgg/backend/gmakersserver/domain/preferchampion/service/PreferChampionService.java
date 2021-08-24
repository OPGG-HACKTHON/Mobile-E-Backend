package opgg.backend.gmakersserver.domain.preferchampion.service;

import java.util.List;

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
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PreferChampionService {

	private final PreferChampionRepository preferChampionRepository;

	@Transactional
	public void createPreferChampion(List<ProfileRequest.Create.PreferChampion> preferChampions, String summonerName,
			Profile profile) {
		createPreferChampions(preferChampions, summonerName, profile);
	}

	@Transactional
	public void updatePreferChampion(List<ProfileRequest.Create.PreferChampion> updatePreferChampions, String summonerName,
			Profile profile) {
		preferChampionRepository.deletePreferChampionByProfile(profile);
		createPreferChampions(updatePreferChampions, summonerName, profile);
	}

	@Transactional
	public void createPreferChampions(List<ProfileRequest.Create.PreferChampion> updatePreferChampions,
			String summonerName,
			Profile profile) {
		Summoner summoner = Summoner.named(summonerName).get();
		updatePreferChampions.forEach(updatePreferChampion -> {
			ChampionMastery mastery = ChampionMastery.forSummoner(summoner).withChampion(
					Champion.withId(updatePreferChampion.getChampionId()).get()).get();
			preferChampionRepository.save(PreferChampion.builder()
					.profile(profile)
					.championId(mastery.getChampion().getId())
					.championName(mastery.getChampion().getName())
					.championLevel(mastery.getLevel())
					.championPoints(mastery.getPoints())
					.priority(updatePreferChampion.getPriority())
					.build());
		});
	}

}
