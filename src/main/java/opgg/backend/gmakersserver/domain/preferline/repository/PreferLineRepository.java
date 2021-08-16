package opgg.backend.gmakersserver.domain.preferline.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import opgg.backend.gmakersserver.domain.preferline.entity.PreferLine;

public interface PreferLineRepository extends JpaRepository<PreferLine, Long>, PreferLineRepositoryCustom {



}
