package opgg.backend.gmakersserver.domain.profile.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import opgg.backend.gmakersserver.domain.account.entity.Account;
import opgg.backend.gmakersserver.domain.profile.entity.Profile;

import java.util.Optional;

import static opgg.backend.gmakersserver.domain.account.entity.QAccount.account;
import static opgg.backend.gmakersserver.domain.profile.entity.QProfile.profile;

@RequiredArgsConstructor
public class ProfileRepositoryImpl implements ProfileRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Profile findByAccountAndSummonerName(Long accountId, String summonerName) {
        return queryFactory
                    .selectFrom(profile)
                    .where(
                            profile.account.accountId.eq(accountId),
                            profile.summonerInfo.summonerName.eq(summonerName)
                    )
                    .fetchOne();
    }

    @Override
    public Optional<Profile> findBySummonerIdAndAccountId(String summonerId, Long accountId) {
        return Optional.ofNullable(queryFactory
                                        .selectFrom(profile)
                                        .join(profile.account, account).fetchJoin()
                                        .where(
                                                profile.summonerInfo.summonerId.eq(summonerId),
                                                profile.account.accountId.eq(accountId)
                                        )
                                        .fetchOne());
    }

    @Override
    public long countByAccount(Account account) {
        return queryFactory
                    .selectFrom(profile)
                    .where(profile.account.accountId.eq(account.getAccountId()))
                    .fetchCount();
    }
}
