package opgg.backend.gmakersserver.domain.preferchampion.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import opgg.backend.gmakersserver.domain.preferchampion.entity.PreferChampion;
import opgg.backend.gmakersserver.domain.profile.entity.Profile;

public interface PreferChampionRepository extends JpaRepository<PreferChampion, Long> , PreferChampionRepositoryCustom{

	void deletePreferChampionByProfile(Profile profile);

}
