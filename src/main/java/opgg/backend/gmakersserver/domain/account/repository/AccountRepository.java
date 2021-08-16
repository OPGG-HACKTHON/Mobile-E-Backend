package opgg.backend.gmakersserver.domain.account.repository;

import opgg.backend.gmakersserver.domain.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long>, AccountRepositoryCustom {

}
