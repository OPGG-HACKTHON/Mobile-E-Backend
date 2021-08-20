package opgg.backend.gmakersserver.domain.profile.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import opgg.backend.gmakersserver.domain.profile.entity.Profile;

public interface ProfileRepository extends JpaRepository<Profile , Long>, ProfileRepositoryCustom {

}
