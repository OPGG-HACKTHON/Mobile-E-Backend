package opgg.backend.gmakersserver.domain.preferKeyword.repository;

import opgg.backend.gmakersserver.domain.preferKeyword.entity.PreferKeyword;
import opgg.backend.gmakersserver.domain.profile.entity.Profile;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PreferKeywordRepository extends JpaRepository<PreferKeyword, Long> {

	void deletePreferKeywordByProfile(Profile profile);

}
