package opgg.backend.gmakersserver.domain.Position.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import opgg.backend.gmakersserver.domain.profile.entity.Profile;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Table(name = "PREFER_POSITION")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Position {

    @Builder
    public Position(Profile profile, String preferPosition) {
        this.preferPosition = preferPosition;
        changeProfile(profile);
    }

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POSITION_ID")
    private Long positionId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "PROFILE_ID")
    private Profile profile;

    @Column(name = "PREFER_POSITION")
    private String preferPosition;

    public void changeProfile(Profile profile) {
        this.profile = profile;
        profile.getPositions().add(this);
    }
}
