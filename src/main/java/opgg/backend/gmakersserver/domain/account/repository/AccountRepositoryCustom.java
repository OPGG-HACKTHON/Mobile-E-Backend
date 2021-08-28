package opgg.backend.gmakersserver.domain.account.repository;

import opgg.backend.gmakersserver.domain.account.entity.Account;

import java.util.Optional;

public interface AccountRepositoryCustom {

    Optional<Account> findByUsername(String username);

    Optional<Account> findOneWithRoleByUsername(String username);

    Optional<Account> findByAccountId(Long accountId);

}
