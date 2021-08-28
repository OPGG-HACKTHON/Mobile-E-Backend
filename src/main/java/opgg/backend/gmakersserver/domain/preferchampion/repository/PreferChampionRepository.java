package opgg.backend.gmakersserver.domain.preferchampion.repository;

import opgg.backend.gmakersserver.domain.preferchampion.entity.PreferChampion;
import opgg.backend.gmakersserver.domain.profile.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PreferChampionRepository extends JpaRepository<PreferChampion, Long> {

	void deletePreferChampionByProfile(Profile profile);

}
