package opgg.backend.gmakersserver.domain.preferchampion.entity;

import static javax.persistence.FetchType.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.merakianalytics.orianna.types.core.championmastery.ChampionMastery;
import com.merakianalytics.orianna.types.core.staticdata.Champion;
import com.merakianalytics.orianna.types.core.summoner.Summoner;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import opgg.backend.gmakersserver.domain.profile.controller.request.ProfileRequest;
import opgg.backend.gmakersserver.domain.profile.entity.Profile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Table(name = "PREFER_CHAMPION")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class PreferChampion {

    @Builder
    public PreferChampion(Profile profile, int championId, int championLevel, int championPoints, int priority, String championName) {
        this.profile        = profile;
        this.championId     = championId;
        this.championLevel  = championLevel;
        this.championPoints = championPoints;
        this.priority       = priority;
        this.championName   = championName;
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

}
