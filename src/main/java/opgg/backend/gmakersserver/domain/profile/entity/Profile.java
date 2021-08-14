package opgg.backend.gmakersserver.domain.profile.entity;

import static javax.persistence.FetchType.*;

import javax.persistence.Column;
import javax.persistence.Embedded;
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
	public Profile(Account account, boolean isCertified, Integer authProfileIconId,
			SummonerInfo summonerInfo) {
		this.account = account;
		this.isCertified = isCertified;
		this.authProfileIconId = authProfileIconId;
		this.summonerInfo = summonerInfo;
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

	@Column(name = "AUTH_PROFILE_ICON_ID")
	private Integer authProfileIconId;

	@Embedded
	private SummonerInfo summonerInfo;

	public void changeAuthProfileIconId(Integer summonerProfileIconId) {
		this.authProfileIconId = summonerProfileIconId;
	}

	public void changeIsCertified(boolean isCertified) {
		this.isCertified = isCertified;
	}

	public boolean isAuthenticable(int summonerProfileIconId) {
		return authProfileIconId != null && authProfileIconId == summonerProfileIconId;
	}

}
