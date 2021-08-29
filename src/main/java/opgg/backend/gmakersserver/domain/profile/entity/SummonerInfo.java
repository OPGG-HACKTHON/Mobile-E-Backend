package opgg.backend.gmakersserver.domain.profile.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;

import com.merakianalytics.orianna.types.core.summoner.Summoner;

@Getter
@Embeddable
@NoArgsConstructor
public class SummonerInfo {

	@Builder
	public SummonerInfo(String summonerId, String summonerName, Integer profileIconId) {
		this.summonerId = summonerId;
		this.summonerName = summonerName;
		this.profileIconId = profileIconId;
	}

	@NotBlank
	@Column(name = "SUMMONER_ID", nullable = false)
	private String summonerId;

	@Column(name = "SUMMONER_NAME", nullable = false)
	private String summonerName;

	@Column(name = "PROFILE_ICON_ID")
	private Integer profileIconId;

	public static SummonerInfo of(Summoner summoner) {
		return SummonerInfo.builder()
				.summonerId(summoner.getId())
				.summonerName(summoner.getName())
				.profileIconId(summoner.getProfileIcon().getId())
				.build();
	}

}
