package opgg.backend.gmakersserver.domain.profile.repository;

import java.util.List;
import java.util.Optional;

import opgg.backend.gmakersserver.domain.account.entity.Account;
import opgg.backend.gmakersserver.domain.profile.controller.response.ProfileDetailResponse;
import opgg.backend.gmakersserver.domain.profile.controller.response.ProfileFindResponse;
import opgg.backend.gmakersserver.domain.profile.entity.Profile;

public interface ProfileRepositoryCustom {

    Optional<Profile> findByAccountAndSummonerName(Long accountId, String summonerName);

    Optional<Profile> findBySummonerIdAndAccountId(String summonerId, Long accountId);

    long countByAccount(Account account);

    List<ProfileFindResponse> findProfileMainByAccount(Account account);

    List<ProfileDetailResponse> findProfileDetailByAccountAndProfile(Account account, Profile profile);

    List<ProfileFindResponse> findProfileBySummonerName(String summonerName);

    Optional<Profile> findByAccountAndProfileId(Account account, Long profileId);

}
