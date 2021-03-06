package opgg.backend.gmakersserver.domain.summoner.service;

import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.types.core.league.LeagueEntry;
import com.merakianalytics.orianna.types.core.league.LeaguePositions;
import com.merakianalytics.orianna.types.core.summoner.Summoner;
import lombok.RequiredArgsConstructor;
import opgg.backend.gmakersserver.domain.profile.controller.response.ProfileFindResponse;
import opgg.backend.gmakersserver.domain.profile.repository.ProfileRepository;
import opgg.backend.gmakersserver.domain.summoner.controller.response.SummonerResponse;
import opgg.backend.gmakersserver.error.exception.riotapi.SummonerNotFoundException;
import opgg.backend.gmakersserver.error.exception.summoner.SummonerAlreadyCertifiedException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
public class SummonerService {

    private final ProfileRepository profileRepository;

    public String convertSummonerName(String summonerName) {
        if (!StringUtils.isBlank(summonerName) && summonerName.length() == 2) {
            summonerName = summonerName.charAt(0) + " " + summonerName.charAt(1);
        }

        return summonerName;
    }

    public Summoner validSummoner(String summonerName) {
        summonerName = convertSummonerName(summonerName);
        Summoner summoner = Orianna.summonerNamed(summonerName).get();
        summoner.load();

        return summoner;
    }

    @Transactional(readOnly = true)
    public SummonerResponse getSummoner(String summonerName) {
        Summoner summoner = validSummoner(summonerName);

        if (StringUtils.isBlank(summoner.getId())) {
            throw new SummonerNotFoundException();
        }

        profileRepository.findProfileBySummonerName(summoner.getName())
                .stream()
                .filter(ProfileFindResponse::isCertified)
                .findFirst()
                .ifPresent(profileFindResponse -> {
                    throw new SummonerAlreadyCertifiedException();
                });

        LeaguePositions leaguePositions = summoner.getLeaguePositions();
        if (CollectionUtils.isEmpty(leaguePositions)) {
            return SummonerResponse.fromInitSummonerResponse(summoner);
        }

        LeagueEntry leagueEntry = leaguePositions.get(0);
        return SummonerResponse.of(summoner, leagueEntry);
    }

}
