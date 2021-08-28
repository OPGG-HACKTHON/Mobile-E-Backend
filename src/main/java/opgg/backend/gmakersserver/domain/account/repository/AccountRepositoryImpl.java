package opgg.backend.gmakersserver.domain.account.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import opgg.backend.gmakersserver.domain.account.entity.Account;

import java.util.Optional;

import static com.querydsl.jpa.JPAExpressions.selectFrom;
import static opgg.backend.gmakersserver.domain.account.entity.QAccount.account;
import static opgg.backend.gmakersserver.domain.profile.entity.QProfile.profile;

@RequiredArgsConstructor
public class AccountRepositoryImpl implements AccountRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Account> findByUsername(String username) {
        return Optional.ofNullable(queryFactory
                                        .selectFrom(account)
                                        .where(account.username.eq(username))
                                        .fetchOne());
    }

    @Override
    public Optional<Account> findOneWithRoleByUsername(String username) {
        return Optional.ofNullable(queryFactory
                                        .selectFrom(account)
                                        .where(account.username.eq(username))
                                        .fetchOne());
    }

    @Override
    public Optional<Account> findByAccountId(Long accountId) {
        return Optional.ofNullable(queryFactory
                                        .selectFrom(account)
                                        .where(account.accountId.eq(accountId))
                                        .fetchOne());
    }

}
