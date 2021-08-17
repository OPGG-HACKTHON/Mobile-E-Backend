package opgg.backend.gmakersserver.domain.profile.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import opgg.backend.gmakersserver.domain.account.entity.Account;
import opgg.backend.gmakersserver.domain.account.entity.QAccount;
import opgg.backend.gmakersserver.domain.profile.controller.response.ProfileFindResponse;
import opgg.backend.gmakersserver.domain.profile.controller.response.QProfileFindResponse;
import opgg.backend.gmakersserver.domain.profile.entity.Profile;

import java.util.List;
import java.util.Optional;

import static opgg.backend.gmakersserver.domain.account.entity.QAccount.account;
import static opgg.backend.gmakersserver.domain.leagueposition.entity.QLeaguePosition.leaguePosition;
import static opgg.backend.gmakersserver.domain.preferchampion.entity.QPreferChampion.preferChampion;
import static opgg.backend.gmakersserver.domain.preferline.entity.QPreferLine.preferLine;
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

    @Override
    public List<ProfileFindResponse> findProfileMainByAccount(Account account) {
        return queryFactory
                    .select(new QProfileFindResponse(
                            QAccount.account.accountId,
                            QAccount.account.username,
                            profile.profileId,
                            profile.isCertified,
                            profile.summonerAccountId,
                            profile.summonerInfo.profileIconId,
                            profile.summonerInfo.summonerId,
                            profile.summonerInfo.summonerName,
                            profile.preferQueue,
                            leaguePosition.level,
                            leaguePosition.queue,
                            leaguePosition.tier,
                            leaguePosition.tierLevel,
                            preferLine.line,
                            preferLine.priority
                    )).distinct()
                    .from(QAccount.account)
                    .join(profile).on(QAccount.account.accountId.eq(profile.account.accountId))
                    .leftJoin(leaguePosition).on(profile.profileId.eq(leaguePosition.profile.profileId))
                    .join(preferChampion).on(profile.profileId.eq(preferChampion.profile.profileId))
                    .leftJoin(preferLine).on(profile.profileId.eq(preferLine.profile.profileId))
                    .where(QAccount.account.activated.eq(true))
                    .orderBy(profile.summonerInfo.summonerName.asc(), leaguePosition.queue.asc(), preferLine.priority.asc())
                    .fetch();

    }
}
