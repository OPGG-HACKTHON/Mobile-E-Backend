package opgg.backend.gmakersserver.domain.preferchampion.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import opgg.backend.gmakersserver.domain.profile.entity.Profile;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Table(name = "PREFER_CHAMPION")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PreferChampion {

    @Builder
    public PreferChampion(Profile profile, int championLevel, int championPoints, int priority) {
        this.profile = profile;
        this.championLevel = championLevel;
        this.championPoints = championPoints;
        this.priority = priority;
    }

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PREFER_CHAMPION_ID")
    private Long preferChampionId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "PROFILE_ID")
    private Profile profile;

    @Column(name = "CHAMPION_LEVEL")
    private int championLevel;

    @Column(name = "CHAMPION_POINTS")
    private int championPoints;

    @Column(name = "PRIORITY")
    private int priority;

}
