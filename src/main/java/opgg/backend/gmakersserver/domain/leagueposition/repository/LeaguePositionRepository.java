package opgg.backend.gmakersserver.domain.leagueposition.repository;

import opgg.backend.gmakersserver.domain.leagueposition.entity.LeaguePosition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaguePositionRepository extends JpaRepository<LeaguePosition, Long>, LeaguePositionRepositoryCustom {
}
