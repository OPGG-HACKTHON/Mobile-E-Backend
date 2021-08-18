package opgg.backend.gmakersserver.domain.profile.repository;

import java.util.List;

import opgg.backend.gmakersserver.domain.account.entity.Account;
import opgg.backend.gmakersserver.domain.profile.controller.response.ProfileDetailResponse;
import opgg.backend.gmakersserver.domain.profile.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProfileRepository extends JpaRepository<Profile , Long>, ProfileRepositoryCustom {

	List<Profile> findByAccount(Account account);

	@Query(value = "select t1.account_id          as account_id,"
			+ "    t1.username            as username,"
			+ "    t1.profile_id          as profile_id,"
			+ "    t1.certified           as certified,"
			+ "    t1.summoner_account_id as summoner_account_id,"
			+ "    t1.profile_icon_id     as profile_icon_id,"
			+ "    t1.summoner_id         as summoner_id,"
			+ "    t1.summoner_name       as summoner_name,"
			+ "    t1.prefer_queue        as prefer_queue,"
			+ "    t1.level               as level,"
			+ "    t1.queue               as queue,"
			+ "    t1.tier                as tier,"
			+ "    t1.tier_level          as tier_level,"
			+ "    t1.league_point        as league_point,"
			+ "    t1.lose_games          as lose_games,"
			+ "    t1.win_games           as win_games,"
			+ "    t1.win_rate            as win_rate,"
			+ "    t2.champion_name       as champion_name,"
			+ "    t2.champion_id         as champion_id,"
			+ "    t2.champion_level      as champion_level,"
			+ "    t2.champion_points     as champion_points,"
			+ "    t2.pc_priority         as pc_priority,"
			+ "    t2.pl_line             as pl_line,"
			+ "    t2.pl_priority         as pl_priority,"
			+ "    t1.create_date		  as create_date,"
			+ "    t1.last_modified_date  as last_modified_date"
			+ "    from ("
			+ "                    select distinct a.account_id,"
			+ "                    a.username,"
			+ "                    p.profile_id,"
			+ "                    p.certified,"
			+ "                    p.summoner_account_id,"
			+ "                    p.profile_icon_id,"
			+ "                    p.summoner_id,"
			+ "                    p.summoner_name,"
			+ "                    p.prefer_queue,"
			+ "					   p.create_date,"
			+ "				 	   p.last_modified_date,"
			+ "                    lp.level,"
			+ "                    lp.queue,"
			+ "                    lp.tier,"
			+ "                    lp.tier_level,"
			+ "                    lp.league_point,"
			+ "                    lp.lose_games,"
			+ "                    lp.win_games,"
			+ "                    lp.win_rate"
			+ "                            from account a"
			+ "                    join profile p on a.account_id = p.account_id"
			+ "                    left join league_position lp on p.profile_id = lp.profile_id"
			+ "                    where a.activated = true"
			+ "                    order by p.summoner_name, lp.queue"
			+ "                    ) t1"
			+ "    join"
			+ "            ("
			+ "                    select distinct p.profile_id,"
			+ "                    GROUP_CONCAT(distinct pc.champion_name order by pc.priority)   as champion_name,"
			+ "    GROUP_CONCAT(distinct pc.champion_id order by pc.priority)     as champion_id,"
			+ "    GROUP_CONCAT(distinct pc.champion_level order by pc.priority)  as champion_level,"
			+ "    GROUP_CONCAT(distinct pc.champion_points order by pc.priority) as champion_points,"
			+ "    GROUP_CONCAT(distinct pc.priority order by pc.priority)        as pc_priority,"
			+ "    GROUP_CONCAT(distinct pl.line order by pl.priority)            as pl_line,"
			+ "    GROUP_CONCAT(distinct pl.priority order by pl.priority)        as pl_priority"
			+ "    from account a"
			+ "    join profile p on a.account_id = p.account_id"
			+ "    join prefer_champion pc on p.profile_id = pc.profile_id"
			+ "    join prefer_line pl on p.profile_id = pl.profile_id"
			+ "    where p.profile_id = :profileId and a.account_id = :accountId"
			+ "    group by p.profile_id"
			+ "         ) t2 on t1.profile_id = t2.profile_id", nativeQuery = true)
	List<Profile> findProfileDetailNativeByAccountIdAndProfileId(@Param("accountId") Long accountId, @Param("profileId") Long profileId);

}
