package opgg.backend.gmakersserver.domain.summoner.controller.response;

import com.merakianalytics.orianna.types.core.league.LeagueEntry;
import com.merakianalytics.orianna.types.core.summoner.Summoner;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import opgg.backend.gmakersserver.domain.leagueposition.entity.Queue;
import opgg.backend.gmakersserver.domain.leagueposition.entity.Tier;
import opgg.backend.gmakersserver.domain.leagueposition.entity.TierLevel;

import static opgg.backend.gmakersserver.domain.leagueposition.entity.Tier.UNRANKED;
import static opgg.backend.gmakersserver.domain.leagueposition.entity.TierLevel.NONE;

@Getter
@NoArgsConstructor
public class SummonerResponse {

    @Builder
    public SummonerResponse(int level, Integer profileIconId, String summonerId, String summonerName, int leaguePoint, Tier tier, TierLevel tierLevel, Queue queue) {
        this.level          = level;
        this.profileIconId  = profileIconId;
        this.summonerId     = summonerId;
        this.summonerName   = summonerName;
        this.leaguePoint    = leaguePoint;
        this.tier           = tier;
        this.tierLevel      = tierLevel;
        this.queue          = queue;
    }

    private int level;
    private Integer profileIconId;
    private String summonerId;
    private String summonerName;
    private int leaguePoint;
    private Tier tier;
    private TierLevel tierLevel;
    private Queue queue;

    public static SummonerResponse fromInitSummonerResponse(Summoner summoner) {
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

    public static SummonerResponse of(Summoner summoner, LeagueEntry leagueEntry) {
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
