package opgg.backend.gmakersserver.domain.profile.controller.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import opgg.backend.gmakersserver.domain.leagueposition.entity.Queue;
import opgg.backend.gmakersserver.domain.leagueposition.entity.Tier;
import opgg.backend.gmakersserver.domain.preferline.entity.Line;

@Getter
public class ProfileFindResponse {

    //Account
    private final Long accountId;
    private final String username;

    //Profile
    private final Long profileId;
    private final boolean isCertified;
    private final String summonerAccountId;

    //SummonerInfo
    private final Integer profileIconId;
    private final String summonerId;
    private final String summonerName;
    private final Queue preferQueue;

    //LeaguePosition
    private final int level;
    private final Queue queue;
    private final Tier tier;
    private final int tierLevel;

    //PreferLine
    private final Line line;
    private final int preferLinePriority;

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
