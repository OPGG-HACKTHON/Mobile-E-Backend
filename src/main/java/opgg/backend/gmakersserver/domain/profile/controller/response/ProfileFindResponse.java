package opgg.backend.gmakersserver.domain.profile.controller.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import opgg.backend.gmakersserver.domain.leagueposition.entity.Queue;
import opgg.backend.gmakersserver.domain.leagueposition.entity.Tier;
import opgg.backend.gmakersserver.domain.preferline.entity.Line;

@Getter
public class ProfileFindResponse {

    //Account
    private Long accountId;
    private String username;

    //Profile
    private Long profileId;
    private boolean isCertified;
    private String summonerAccountId;

    //SummonerInfo
    private Integer profileIconId;
    private String summonerId;
    private String summonerName;
    private Queue preferQueue;

    //LeaguePosition
    private int level;
    private Queue queue;
    private Tier tier;
    private int tierLevel;

    //PreferLine
    private Line line;
    private int preferLinePriority;

    @QueryProjection
    public ProfileFindResponse(Long accountId, String username, Long profileId, boolean isCertified, String summonerAccountId, Integer profileIconId, String summonerId, String summonerName, Queue preferQueue, int level, Queue queue, Tier tier, int tierLevel, Line line, int preferLinePriority) {
        this.accountId = accountId;
        this.username = username;
        this.profileId = profileId;
        this.isCertified = isCertified;
        this.summonerAccountId = summonerAccountId;
        this.profileIconId = profileIconId;
        this.summonerId = summonerId;
        this.summonerName = summonerName;
        this.preferQueue = preferQueue;
        this.level = level;
        this.queue = queue;
        this.tier = tier;
        this.tierLevel = tierLevel;
        this.line = line;
        this.preferLinePriority = preferLinePriority;
    }
}
