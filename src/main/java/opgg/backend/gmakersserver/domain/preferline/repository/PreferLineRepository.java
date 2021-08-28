package opgg.backend.gmakersserver.domain.preferline.repository;

import opgg.backend.gmakersserver.domain.preferline.entity.PreferLine;
import opgg.backend.gmakersserver.domain.profile.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PreferLineRepository extends JpaRepository<PreferLine, Long> {

	void deletePreferLineByProfile(Profile profile);

}
