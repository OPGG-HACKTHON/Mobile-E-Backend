package opgg.backend.gmakersserver.domain.summoner.service;

import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.types.core.league.LeagueEntry;
import com.merakianalytics.orianna.types.core.league.LeaguePositions;
import com.merakianalytics.orianna.types.core.summoner.Summoner;
import lombok.RequiredArgsConstructor;
import opgg.backend.gmakersserver.domain.leagueposition.entity.Queue;
import opgg.backend.gmakersserver.domain.leagueposition.entity.Tier;
import opgg.backend.gmakersserver.domain.leagueposition.entity.TierLevel;
import opgg.backend.gmakersserver.domain.profile.controller.response.ProfileFindResponse;
import opgg.backend.gmakersserver.domain.profile.entity.Profile;
import opgg.backend.gmakersserver.domain.profile.repository.ProfileRepository;
import opgg.backend.gmakersserver.domain.summoner.controller.response.SummonerResponse;
import opgg.backend.gmakersserver.error.exception.profile.ProfileExistException;
import opgg.backend.gmakersserver.error.exception.riotapi.SummonerNotFoundException;
import opgg.backend.gmakersserver.error.exception.summoner.SummonerAlreadyCertifiedException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;

import static opgg.backend.gmakersserver.domain.leagueposition.entity.Tier.UNRANKED;
import static opgg.backend.gmakersserver.domain.leagueposition.entity.TierLevel.*;

@Service
@RequiredArgsConstructor
public class SummonerService {

    private final ProfileRepository profileRepository;

    public boolean isNotExistRankData(LeaguePositions leaguePositions) {
        if (leaguePositions.size() == 0) {
            return true;
        }

        return false;
    }

    @Transactional(readOnly = true)
    public SummonerResponse getSummoner(String summonerName, Long accountId) {
        Summoner summoner = Orianna.summonerNamed(summonerName).get();
        summoner.load();

        LeaguePositions leaguePositions = summoner.getLeaguePositions();
        if (isNotExistRankData(leaguePositions)) {
            return SummonerResponse.builder()
                    .level(summoner.getLevel())
                    .profileIconId(summoner.getProfileIcon().getId())
                    .summonerId(summoner.getId())
                    .summonerName(summoner.getName())
                    .leaguePoint(0)
                    .tier(UNRANKED)
                    .tierLevel(NONE)
                    .queue(Queue.NONE)
                    .build();
        }

        if (StringUtils.isBlank(summoner.getId())) {
            throw new SummonerNotFoundException();
        }

        LeagueEntry leagueEntry = summoner.getLeaguePositions().get(0);
        List<ProfileFindResponse> profileBySummonerNames = profileRepository.findProfileBySummonerName(summoner.getName());
        if (!CollectionUtils.isEmpty(profileBySummonerNames)) {
            profileBySummonerNames.stream()
                    .filter(ProfileFindResponse::isCertified)
                    .findFirst()
                    .ifPresent(profileFindResponse -> {
                        throw new SummonerAlreadyCertifiedException();
            });
        }

        return SummonerResponse.builder()
                .level(summoner.getLevel())
                .profileIconId(summoner.getProfileIcon().getId())
                .summonerId(summoner.getId())
                .summonerName(summoner.getName())
                .leaguePoint(leagueEntry.getLeaguePoints())
                .tier(Tier.valueOf(String.valueOf(leagueEntry.getTier())))
                .tierLevel(TierLevel.valueOf(String.valueOf(leagueEntry.getDivision())))
                .queue(Queue.valueOf(String.valueOf(leagueEntry.getQueue())))
                .build();
    }
}
