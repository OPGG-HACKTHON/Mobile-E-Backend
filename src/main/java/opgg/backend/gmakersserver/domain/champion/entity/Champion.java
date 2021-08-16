package opgg.backend.gmakersserver.domain.champion.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import opgg.backend.gmakersserver.domain.profile.entity.Profile;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Table(name = "CHAMPION")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Champion {

    @Builder
    public Champion(Profile profile, int championLevel, int championPoints) {
        this.championLevel = championLevel;
        this.championPoints = championPoints;
        changeProfile(profile);
    }

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CHAMPION_ID")
    private Long championId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "PROFILE_ID")
    private Profile profile;

    @Column(name = "CHAMPION_LEVEL")
    private int championLevel;

    @Column(name = "CHAMPION_POINTS")
    private int championPoints;

    public void changeProfile(Profile profile) {
        this.profile = profile;
        profile.getChampions().add(this);
    }

}
