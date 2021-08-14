package opgg.backend.gmakersserver.domain.profile.repository;

import opgg.backend.gmakersserver.domain.profile.entity.Profile;

import java.util.Optional;

public interface ProfileRepositoryCustom {

    Profile findByAccountAndSummonerName(Long accountId, String summonerName);

    Optional<Profile> findBySummonerIdAndAccountId(String summonerId, Long accountId);

}
