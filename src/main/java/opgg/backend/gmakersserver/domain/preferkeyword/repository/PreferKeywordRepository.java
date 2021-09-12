package opgg.backend.gmakersserver.domain.preferkeyword.repository;

import opgg.backend.gmakersserver.domain.preferkeyword.entity.PreferKeyword;
import opgg.backend.gmakersserver.domain.profile.entity.Profile;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PreferKeywordRepository extends JpaRepository<PreferKeyword, Long> {

	void deletePreferKeywordByProfile(Profile profile);

}
