package opgg.backend.gmakersserver.domain.account.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import opgg.backend.gmakersserver.domain.account.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query("select a from Account a where a.username = :username")
    Optional<Account> findByUsername(@Param("username") String username);

	Optional<Account> findOneWithRoleByUsername(String username);

}
