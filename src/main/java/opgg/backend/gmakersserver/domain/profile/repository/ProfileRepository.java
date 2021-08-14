package opgg.backend.gmakersserver.domain.profile.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import opgg.backend.gmakersserver.domain.account.entity.Account;
import opgg.backend.gmakersserver.domain.profile.entity.Profile;

public interface ProfileRepository extends JpaRepository<Profile , Long> {

	@Query("select p from Profile p join fetch p.account where p.account = :account and p.summonerInfo.summonerName = :summonerName")
	Profile findByAccountAndSummonerName(Account account, String summonerName);

	@Query("select p from Profile p join fetch p.account where p.summonerInfo.summonerId = :summonerId and p.account.accountId = :accountId")
	Optional<Profile> findBySummonerIdAndAccountId(@Param("summonerId") String summonerId, @Param("accountId") Long accountId);

}
