package opgg.backend.gmakersserver.domain.leagueposition.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import opgg.backend.gmakersserver.domain.profile.entity.Profile;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Table(name = "LEAGUE_POSITION")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LeaguePosition {

    @Builder
    public LeaguePosition(Profile profile, Tier tier, int tierLevel, int level, int winGames, int loseGames, int winRate, int leaguePoint, int championLevel, int championPoints, Queue queue) {
        this.profile = profile;
        this.tier = tier;
        this.tierLevel = tierLevel;
        this.level = level;
        this.winGames = winGames;
        this.loseGames = loseGames;
        this.winRate = winRate;
        this.leaguePoint = leaguePoint;
        this.queue = queue;
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

    public void changeProfile(Profile profile) {
        this.profile = profile;
        profile.getLeaguePosition().add(this);
    }
}
