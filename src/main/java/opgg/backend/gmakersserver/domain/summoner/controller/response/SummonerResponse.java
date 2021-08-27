package opgg.backend.gmakersserver.domain.summoner.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import opgg.backend.gmakersserver.domain.leagueposition.entity.Queue;
import opgg.backend.gmakersserver.domain.leagueposition.entity.Tier;
import opgg.backend.gmakersserver.domain.leagueposition.entity.TierLevel;

@NoArgsConstructor
@Getter
public class SummonerResponse {

    @Builder
    public SummonerResponse(int level, Integer profileIconId, String summonerId, String summonerName, int leaguePoint, Tier tier, TierLevel tierLevel, Queue queue) {
        this.level = level;
        this.profileIconId = profileIconId;
        this.summonerId = summonerId;
        this.summonerName = summonerName;
        this.leaguePoint = leaguePoint;
        this.tier = tier;
        this.tierLevel = tierLevel;
        this.queue = queue;
    }

    private int level;
    private Integer profileIconId;
    private String summonerId;
    private String summonerName;
    private int leaguePoint;
    private Tier tier;
    private TierLevel tierLevel;
    private Queue queue;

}
