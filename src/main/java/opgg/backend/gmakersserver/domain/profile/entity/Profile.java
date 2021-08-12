package opgg.backend.gmakersserver.domain.profile.entity;

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

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import opgg.backend.gmakersserver.domain.account.entity.Account;

@Entity
@Getter
@Table(name = "PROFILE")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Profile {

	@Builder
	public Profile(Account account, boolean isCertified, String summonerName, Integer profileIconId,
			Integer authProfileIconId, String summonerId) {
		this.account = account;
		this.isCertified = isCertified;
		this.summonerName = summonerName;
		this.profileIconId = profileIconId;
		this.authProfileIconId = authProfileIconId;
		this.summonerId = summonerId;
	}

	@Id
	@JsonIgnore
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PROFILE_ID")
	private Long profileId;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "ACCOUNT_ID", updatable = false)
	private Account account;

	@Column(name = "CERITIFIED")
	private boolean isCertified;

	@Column(name = "SUMMONER_NAME")
	private String summonerName;

	@Column(name = "PROFILE_ICON_ID")
	private Integer profileIconId;

	@Column(name = "AUTH_PROFILE_ICON_ID")
	private Integer authProfileIconId;

	@Column(name = "SUMMONER_ID")
	private String summonerId;

	@Override
	public String toString() {
		return "Profile{" +
				"profileId=" + profileId +
				", account=" + account +
				", isCertified=" + isCertified +
				", summonerName='" + summonerName + '\'' +
				", profileIconId=" + profileIconId +
				", authProfileIconId=" + authProfileIconId +
				", summonerId='" + summonerId + '\'' +
				'}';
	}

	public void changeAuthProfileIconId(Integer authProfileIconId) {
		this.authProfileIconId = authProfileIconId;
	}

}
