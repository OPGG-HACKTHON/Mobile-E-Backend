package opgg.backend.gmakersserver.domain.leagueposition.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.merakianalytics.orianna.types.core.league.LeagueEntry;
import com.merakianalytics.orianna.types.core.summoner.Summoner;

import lombok.*;
import opgg.backend.gmakersserver.domain.profile.entity.Profile;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static opgg.backend.gmakersserver.domain.leagueposition.entity.Queue.*;
import static opgg.backend.gmakersserver.domain.leagueposition.entity.Tier.*;

@Entity
@Getter
@Table(name = "LEAGUE_POSITION")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LeaguePosition {

    @Builder
    public LeaguePosition(Profile profile, Tier tier, int tierLevel, int level, int winGames, int loseGames, int winRate, int leaguePoint, Queue queue) {
        this.tier        = tier;
        this.tierLevel   = tierLevel;
        this.level       = level;
        this.winGames    = winGames;
        this.loseGames   = loseGames;
        this.winRate     = winRate;
        this.leaguePoint = leaguePoint;
        this.queue       = queue;
        setProfile(profile);
    }

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LEAGUE_POSITION_ID")
    private Long leaguePositionId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "PROFILE_ID")
    private Profile profile;

    @Column(name = "TIER")
    @Enumerated(EnumType.STRING)
    private Tier tier;

    @Column(name = "TIER_LEVEL")
    private int tierLevel;

    @Column(name = "LEVEL")
    private int level;

    @Column(name = "WIN_GAMES")
    private int winGames;

    @Column(name = "LOSE_GAMES")
    private int loseGames;

    @Column(name = "WIN_RATE")
    private int winRate;

    @Column(name = "LEAGUE_POINT")
    private int leaguePoint;

    @Column(name = "QUEUE")
    @Enumerated(EnumType.STRING)
    private Queue queue;

    private static int getWinRate(int wins, int losses) {
        return (int)Math.floor((double)wins / (wins + losses) * 100);
    }

    public static LeaguePosition of(LeagueEntry leagueEntry, Profile profile, Summoner summoner) {
        return LeaguePosition.builder()
                .profile(profile)
                .tier(Tier.valueOf(String.valueOf(leagueEntry.getTier())))
                .tierLevel(TierLevel.valueOf(String.valueOf(leagueEntry.getDivision())).getLevel())
                .level(summoner.getLevel())
                .winGames(leagueEntry.getWins())
                .loseGames(leagueEntry.getLosses())
                .winRate(getWinRate(leagueEntry.getWins(), leagueEntry.getLosses()))
                .leaguePoint(leagueEntry.getLeaguePoints())
                .queue(Queue.valueOf(String.valueOf(leagueEntry.getQueue())))
                .build();
    }

    public static LeaguePosition from(Profile profile, Summoner summoner) {
        return LeaguePosition.builder()
                .profile(profile)
                .tier(UNRANKED)
                .tierLevel(0)
                .level(summoner.getLevel())
                .winGames(0)
                .loseGames(0)
                .winRate(0)
                .leaguePoint(0)
                .queue(NONE)
                .build();
    }

    public boolean isQueueMatch(Queue requestQueue) {
        return this.queue == requestQueue;
    }

    public void setProfile(Profile profile){
        this.profile = profile;
        profile.getLeaguePositions().add(this);
    }

    public void changeLeaguePosition(LeagueEntry leagueEntry, Summoner summoner) {
        this.tier        = Tier.valueOf(String.valueOf(leagueEntry.getTier()));
        this.tierLevel   = TierLevel.valueOf(String.valueOf(leagueEntry.getDivision())).getLevel();
        this.level       = summoner.getLevel();
        this.winGames    = leagueEntry.getWins();
        this.loseGames   = leagueEntry.getLosses();
        this.winRate     = getWinRate(leagueEntry.getWins(), leagueEntry.getLosses());
        this.leaguePoint = leagueEntry.getLeaguePoints();
    }

}
