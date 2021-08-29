package opgg.backend.gmakersserver.domain.preferchampion.service;

import lombok.RequiredArgsConstructor;
import opgg.backend.gmakersserver.domain.preferchampion.entity.PreferChampion;
import opgg.backend.gmakersserver.domain.preferchampion.repository.PreferChampionRepository;
import opgg.backend.gmakersserver.domain.profile.controller.request.ProfileRequest;
import opgg.backend.gmakersserver.domain.profile.entity.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static opgg.backend.gmakersserver.domain.preferchampion.entity.PreferChampion.of;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PreferChampionService {

	private final PreferChampionRepository preferChampionRepository;

	@Transactional
	public void createPreferChampion(List<ProfileRequest.Create.PreferChampion> preferChampions, String summonerName,
			Profile profile) {
		preferChampionRepository.saveAll(of(preferChampions, summonerName, profile));
	}

	@Transactional
	public void updatePreferChampion(List<ProfileRequest.Create.PreferChampion> updatePreferChampions,
			String summonerName, Profile profile) {
		preferChampionRepository.deletePreferChampionByProfile(profile);
		createPreferChampion(updatePreferChampions, summonerName, profile);
	}

}
