package opgg.backend.gmakersserver.domain.profile.controller.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import opgg.backend.gmakersserver.domain.leagueposition.entity.Queue;
import opgg.backend.gmakersserver.domain.leagueposition.entity.Tier;
import opgg.backend.gmakersserver.domain.preferline.entity.Line;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class ProfileFindResponse {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PreferLine {

        private Line line;
        private int preferLinePriority;
    }

    private int index = 0;

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
    private List<PreferLine> preferLines = new ArrayList<>();

    @QueryProjection
    @Builder
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
        this.preferLines.add(new PreferLine(line, preferLinePriority));
    }

    public List<ProfileFindResponse> convert(List<ProfileFindResponse> profileFindResponseList) {
        List<ProfileFindResponse> response = new ArrayList<>();

        while (profileFindResponseList.size() > index) {
            ProfileFindResponse profileFindResponse = profileFindResponseList.get(index++);
            ProfileFindResponse profileFindResponseNext = profileFindResponseList.get(index++);
            profileFindResponse.getPreferLines().add(profileFindResponseNext.getPreferLines().get(0));

            response.add(profileFindResponse);
        }

        return response;
    }

}
