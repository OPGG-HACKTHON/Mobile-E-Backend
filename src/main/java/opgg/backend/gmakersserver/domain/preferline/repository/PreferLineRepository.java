package opgg.backend.gmakersserver.domain.preferline.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import opgg.backend.gmakersserver.domain.preferline.entity.PreferLine;
import opgg.backend.gmakersserver.domain.profile.entity.Profile;

public interface PreferLineRepository extends JpaRepository<PreferLine, Long>, PreferLineRepositoryCustom {

	void deletePreferLineByProfile(Profile profile);

}
