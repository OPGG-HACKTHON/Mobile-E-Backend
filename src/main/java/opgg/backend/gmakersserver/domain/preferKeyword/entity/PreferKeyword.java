package opgg.backend.gmakersserver.domain.preferKeyword.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import opgg.backend.gmakersserver.domain.profile.entity.Profile;

import javax.persistence.*;

import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Table(name = "PREFER_KEYWORD")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PreferKeyword {

	@Builder
	public PreferKeyword(Keyword keyword, Profile profile) {
		this.keyword = keyword;
		setProfile(profile);
	}

	@Id
	@JsonIgnore
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PREFER_KEYWORD_ID")
	private Long preferKeywordId;

	@Column(name = "KEYWORD")
	@Enumerated(EnumType.STRING)
	private Keyword keyword;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "PROFILE_ID")
	private Profile profile;

	public static List<PreferKeyword> of(List<Keyword> keywords, Profile profile) {
		return keywords.stream()
				.map(keyword -> PreferKeyword.of(profile, keyword))
				.collect(Collectors.toList());
	}

	private static PreferKeyword of(Profile profile, Keyword keyword) {
		return PreferKeyword.builder()
				.keyword(keyword)
				.profile(profile)
				.build();
	}

	public void setProfile(Profile profile){
		this.profile = profile;
		profile.getPreferKeywords().add(this);
	}

}