package opgg.backend.gmakersserver.domain.preferline.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import opgg.backend.gmakersserver.domain.profile.entity.Profile;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Table(name = "PREFER_LINE")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PreferLine {

    @Builder
    public PreferLine(Profile profile, Line line, int priority) {
        this.profile = profile;
        this.line = line;
        this.priority = priority;
    }

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POSITION_ID")
    private Long positionId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "PROFILE_ID")
    private Profile profile;

    @Column(name = "LINE")
    @Enumerated(EnumType.STRING)
    private Line line;

    @Column(name = "PRIORITY")
    private int priority;

}
