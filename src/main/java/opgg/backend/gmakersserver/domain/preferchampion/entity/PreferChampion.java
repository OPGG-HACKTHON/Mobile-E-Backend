package opgg.backend.gmakersserver.domain.preferchampion.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.merakianalytics.orianna.types.core.championmastery.ChampionMastery;
import com.merakianalytics.orianna.types.core.staticdata.Champion;
import com.merakianalytics.orianna.types.core.summoner.Summoner;
import lombok.*;
import opgg.backend.gmakersserver.domain.profile.controller.request.ProfileRequest;
import opgg.backend.gmakersserver.domain.profile.entity.Profile;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Table(name = "PREFER_CHAMPION")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PreferChampion {

    @Builder
    public PreferChampion(Profile profile, int championId, int championLevel, int championPoints, int priority, String championName) {
        this.championId     = championId;
        this.championLevel  = championLevel;
        this.championPoints = championPoints;
        this.priority       = priority;
        this.championName   = championName;
        setProfile(profile);
    }

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PREFER_CHAMPION_ID")
    private Long preferChampionId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "PROFILE_ID")
    private Profile profile;

    @Column(name = "CHAMPION_ID")
    private int championId;

    @Column(name = "CHAMPION_NAME")
    private String championName;

    @Column(name = "CHAMPION_LEVEL")
    private int championLevel;

    @Column(name = "CHAMPION_POINTS")
    private int championPoints;

    @Column(name = "PRIORITY")
    private int priority;

    public static List<PreferChampion> of(List<ProfileRequest.Create.PreferChampion> requestPreferChampions,
                                   String summonerName, Profile profile) {
        return requestPreferChampions.stream()
                .map(preferChampion ->
                        of(getChampionMastery(summonerName,preferChampion.getChampionId()), profile, preferChampion))
                .collect(Collectors.toList());
    }

    private static ChampionMastery getChampionMastery(String summonerName, int championId) {
        Summoner summoner = Summoner.named(summonerName).get();
        return ChampionMastery.forSummoner(summoner)
                .withChampion(Champion.withId(championId).get())
                .get();
    }

    public static PreferChampion of(ChampionMastery championMastery, Profile profile,
                                    ProfileRequest.Create.PreferChampion preferChampion) {
        return builder()
                .profile(profile)
                .championId(championMastery.getChampion().getId())
                .championName(championMastery.getChampion().getName())
                .championLevel(championMastery.getLevel())
                .championPoints(championMastery.getPoints())
                .priority(preferChampion.getPriority())
                .build();
    }

    public void setProfile(Profile profile){
        this.profile = profile;
        profile.getPreferChampions().add(this);
    }

}
