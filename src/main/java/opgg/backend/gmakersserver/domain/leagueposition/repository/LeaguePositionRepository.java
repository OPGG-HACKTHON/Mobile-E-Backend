package opgg.backend.gmakersserver.domain.leagueposition.repository;

import java.util.List;

import opgg.backend.gmakersserver.domain.account.entity.Account;
import opgg.backend.gmakersserver.domain.leagueposition.entity.LeaguePosition;
import opgg.backend.gmakersserver.domain.profile.entity.Profile;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaguePositionRepository extends JpaRepository<LeaguePosition, Long>, LeaguePositionRepositoryCustom {

	List<LeaguePosition> findByProfile(Profile profile);

}
