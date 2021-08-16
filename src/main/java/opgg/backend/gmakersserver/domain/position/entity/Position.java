package opgg.backend.gmakersserver.domain.position.entity;

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
    public Position(Profile profile, PreferPosition preferPosition, int priority) {
        this.preferPosition = preferPosition;
        this.priority = priority;
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
    private PreferPosition preferPosition;

    @Column(name = "PRIORITY")
    private int priority;

    public void changeProfile(Profile profile) {
        this.profile = profile;
        profile.getPositions().add(this);
    }
}
