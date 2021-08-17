package opgg.backend.gmakersserver.domain.profile.repository;

import java.util.List;

import opgg.backend.gmakersserver.domain.account.entity.Account;
import opgg.backend.gmakersserver.domain.profile.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile , Long>, ProfileRepositoryCustom {

	List<Profile> findByAccount(Account account);
}
