package opgg.backend.gmakersserver.domain.profile.repository;

import static opgg.backend.gmakersserver.domain.account.entity.QAccount.*;
import static opgg.backend.gmakersserver.domain.leagueposition.entity.QLeaguePosition.*;
import static opgg.backend.gmakersserver.domain.preferchampion.entity.QPreferChampion.*;
import static opgg.backend.gmakersserver.domain.preferline.entity.QPreferLine.*;
import static opgg.backend.gmakersserver.domain.profile.entity.QProfile.*;

import java.util.List;
import java.util.Optional;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import opgg.backend.gmakersserver.domain.account.entity.Account;
import opgg.backend.gmakersserver.domain.account.entity.QAccount;
import opgg.backend.gmakersserver.domain.profile.controller.response.ProfileDetailResponse;
import opgg.backend.gmakersserver.domain.profile.controller.response.ProfileFindResponse;
import opgg.backend.gmakersserver.domain.profile.controller.response.QProfileDetailResponse;
import opgg.backend.gmakersserver.domain.profile.controller.response.QProfileFindResponse;
import opgg.backend.gmakersserver.domain.profile.entity.Profile;
import opgg.backend.gmakersserver.domain.profile.entity.QProfile;

@RequiredArgsConstructor
public class ProfileRepositoryImpl implements ProfileRepositoryCustom {

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
				.where(
						QAccount.account.activated.eq(true),
						QAccount.account.accountId.eq(account.getAccountId()),
						profile.preferQueue.eq(leaguePosition.queue)
				)
				.orderBy(profile.summonerInfo.summonerName.asc(), leaguePosition.queue.asc(), preferLine.priority.asc())
				.fetch();
	}

	@Override
	public List<ProfileDetailResponse> findProfileDetailByAccountAndProfile(Account account, Profile profile) {
		return queryFactory.select(new QProfileDetailResponse(
						QAccount.account.accountId,
						QAccount.account.username,
						QProfile.profile.profileId,
						QProfile.profile.isCertified,
						QProfile.profile.summonerAccountId,
						QProfile.profile.preferQueue,
						QProfile.profile.summonerInfo.profileIconId,
						QProfile.profile.summonerInfo.summonerId,
						QProfile.profile.summonerInfo.summonerName,
						leaguePosition.level,
						leaguePosition.queue,
						leaguePosition.tier,
						leaguePosition.tierLevel,
						leaguePosition.loseGames,
						leaguePosition.loseGames,
						leaguePosition.winGames,
						leaguePosition.winRate,
						preferChampion.championName,
						preferChampion.championId,
						preferChampion.championPoints,
						preferChampion.championLevel,
						preferChampion.priority,
						preferLine.line,
						preferLine.priority
				)).distinct()
				.from(QAccount.account)
				.join(QProfile.profile).on(QAccount.account.accountId.eq(QProfile.profile.account.accountId))
				.leftJoin(leaguePosition).on(QProfile.profile.profileId.eq(leaguePosition.profile.profileId))
				.join(preferChampion).on(QProfile.profile.profileId.eq(preferChampion.profile.profileId))
				.leftJoin(preferLine).on(QProfile.profile.profileId.eq(preferLine.profile.profileId))
				.where(QAccount.account.activated.eq(true)
						.and(QProfile.profile.preferQueue.eq(leaguePosition.queue))
						.and(QProfile.profile.profileId.eq(profile.getProfileId())))
				.orderBy(QProfile.profile.summonerInfo.summonerName.asc(), preferChampion.championName.asc(),
						leaguePosition.queue.asc(), preferLine.priority.asc())
				.fetch();
	}
}
