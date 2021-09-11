package opgg.backend.gmakersserver.domain.preferline.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import opgg.backend.gmakersserver.domain.profile.controller.request.ProfileRequest;
import opgg.backend.gmakersserver.domain.profile.entity.Profile;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Table(name = "PREFER_LINE")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PreferLine {

    @Builder
    public PreferLine(Profile profile, Line line, int priority) {
        this.line     = line;
        this.priority = priority;
        setProfile(profile);
    }

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PREFER_LINE_ID")
    private Long preferLineId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "PROFILE_ID")
    private Profile profile;

    @Column(name = "LINE")
    @Enumerated(EnumType.STRING)
    private Line line;

    @Column(name = "PRIORITY")
    private int priority;

    public static List<PreferLine> of(List<ProfileRequest.Create.PreferLine> requestPreferLines, Profile profile) {
        return requestPreferLines.stream()
                .map(preferLine -> PreferLine.of(profile, preferLine))
                .collect(Collectors.toList());
    }

    private static PreferLine of(Profile profile, ProfileRequest.Create.PreferLine preferLine) {
        return PreferLine.builder()
                .profile(profile)
                .line(preferLine.getLine())
                .priority(preferLine.getPriority())
                .build();
    }

    public void setProfile(Profile profile){
        this.profile = profile;
        profile.getPreferLines().add(this);
    }

}
