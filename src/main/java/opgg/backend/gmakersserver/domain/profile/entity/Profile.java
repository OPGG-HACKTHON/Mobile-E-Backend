package opgg.backend.gmakersserver.domain.profile.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import opgg.backend.gmakersserver.domain.Position.entity.Position;
import opgg.backend.gmakersserver.domain.account.entity.Account;
import opgg.backend.gmakersserver.domain.auditing.BaseEntity;
import opgg.backend.gmakersserver.domain.champion.entity.Champion;
import opgg.backend.gmakersserver.domain.leagueposition.entity.LeaguePosition;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Table(name = "PROFILE")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Profile extends BaseEntity {

	@Builder
	public Profile(Account account, boolean isCertified, Integer authProfileIconId, String summonerAccountId, List<LeaguePosition> leaguePosition, List<Champion> champions, List<Position> positions, SummonerInfo summonerInfo) {
		this.isCertified = isCertified;
		this.authProfileIconId = authProfileIconId;
		this.summonerAccountId = summonerAccountId;
		this.leaguePosition = leaguePosition;
		this.champions = champions;
		this.positions = positions;
		this.summonerInfo = summonerInfo;
		changeAccount(account);
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

	@Column(name = "SUMMONER_ACCOUNT_ID")
	private String summonerAccountId;

	@OneToMany(fetch = LAZY, cascade = ALL, mappedBy = "profile")
	private List<LeaguePosition> leaguePosition = new ArrayList<>();

	@OneToMany(fetch = LAZY, cascade = ALL, mappedBy = "profile")
	private List<Champion> champions = new ArrayList<>();

	@OneToMany(fetch = LAZY, cascade = ALL, mappedBy = "profile")
	private List<Position> positions = new ArrayList<>();

	@Embedded
	private SummonerInfo summonerInfo;

	public void changeAuthProfileIconId(Integer summonerProfileIconId) {
		this.authProfileIconId = summonerProfileIconId;
	}

	public void changeIsCertified(boolean isCertified) {
		this.isCertified = isCertified;
	}

	public boolean isAuthorizable(int summonerProfileIconId) {
		return authProfileIconId != null && authProfileIconId == summonerProfileIconId;
	}

	public void changeAccount(Account account) {
		this.account = account;
		account.getProfile().add(this);
	}

}
