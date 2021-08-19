package opgg.backend.gmakersserver.domain.profile.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import opgg.backend.gmakersserver.domain.leagueposition.entity.Queue;
import opgg.backend.gmakersserver.domain.preferline.entity.PreferLine;
import opgg.backend.gmakersserver.domain.account.entity.Account;
import opgg.backend.gmakersserver.domain.auditing.BaseEntity;
import opgg.backend.gmakersserver.domain.preferchampion.entity.PreferChampion;
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
	private Profile(Account account, boolean isCertified, Integer authProfileIconId, String summonerAccountId, List<LeaguePosition> leaguePosition, List<PreferChampion> preferChampions, List<PreferLine> preferLines, SummonerInfo summonerInfo) {
		this.account = account;
		this.isCertified = isCertified;
		this.authProfileIconId = authProfileIconId;
		this.summonerAccountId = summonerAccountId;
		this.leaguePosition = leaguePosition;
		this.preferChampions = preferChampions;
		this.preferLines = preferLines;
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

	@Column(name = "CERTIFIED")
	private boolean isCertified;

	@Column(name = "AUTH_PROFILE_ICON_ID")
	private Integer authProfileIconId;

	@Column(name = "SUMMONER_ACCOUNT_ID")
	private String summonerAccountId;

	@Column(name = "PREFER_QUEUE")
	@Enumerated(EnumType.STRING)
	private Queue preferQueue;

	@OneToMany(fetch = LAZY, cascade = ALL, mappedBy = "profile")
	private List<LeaguePosition> leaguePosition = new ArrayList<>();

	@OneToMany(fetch = LAZY, cascade = ALL, mappedBy = "profile")
	private List<PreferChampion> preferChampions = new ArrayList<>();

	@OneToMany(fetch = LAZY, cascade = ALL, mappedBy = "profile")
	private List<PreferLine> preferLines = new ArrayList<>();

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

	public void changePreferQueue(Queue preferQueue) {
		this.preferQueue = preferQueue;
	}

}
