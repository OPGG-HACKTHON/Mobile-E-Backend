package opgg.backend.gmakersserver.domain.profile.repository;

import opgg.backend.gmakersserver.domain.account.entity.Account;
import opgg.backend.gmakersserver.domain.profile.controller.response.ProfileDetailResponse;
import opgg.backend.gmakersserver.domain.profile.controller.response.ProfileFindResponse;
import opgg.backend.gmakersserver.domain.profile.entity.Profile;

import java.util.List;
import java.util.Optional;

public interface ProfileRepositoryCustom {

    Profile findByAccountAndSummonerName(Long accountId, String summonerName);

    Optional<Profile> findBySummonerIdAndAccountId(String summonerId, Long accountId);

    long countByAccount(Account account);

    List<ProfileFindResponse> findProfileMainByAccount(Account account);

    List<ProfileDetailResponse> findProfileDetailByAccountAndProfile(Account account, Profile profile);

}
