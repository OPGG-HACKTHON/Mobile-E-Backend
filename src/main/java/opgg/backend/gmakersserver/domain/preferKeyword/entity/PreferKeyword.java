package opgg.backend.gmakersserver.domain.preferKeyword.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import opgg.backend.gmakersserver.domain.profile.entity.Profile;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Table(name = "PREFER_KEYWORD")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PreferKeyword {

    @Builder
    public PreferKeyword(String keyword, Profile profile) {
        this.keyword = keyword;
        this.profile = profile;
    }

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PREFER_KEYWORD_ID")
    private Long preferKeywordId;

    @Column(name = "KEYWORD")
    private String keyword;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "PROFILE_ID")
    private Profile profile;

}
