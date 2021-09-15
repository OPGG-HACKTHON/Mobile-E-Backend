package opgg.backend.gmakersserver.domain.profile.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.merakianalytics.orianna.types.core.league.LeagueEntry;
import com.merakianalytics.orianna.types.core.summoner.Summoner;
import lombok.*;
import opgg.backend.gmakersserver.domain.account.entity.Account;
import opgg.backend.gmakersserver.domain.auditing.BaseEntity;
import opgg.backend.gmakersserver.domain.leagueposition.entity.LeaguePosition;
import opgg.backend.gmakersserver.domain.leagueposition.entity.Queue;
import opgg.backend.gmakersserver.domain.preferkeyword.entity.PreferKeyword;
import opgg.backend.gmakersserver.domain.preferchampion.entity.PreferChampion;
import opgg.backend.gmakersserver.domain.preferline.entity.PreferLine;
import opgg.backend.gmakersserver.domain.profile.controller.request.ProfileRequest;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static opgg.backend.gmakersserver.domain.leagueposition.entity.Queue.*;

@Entity
@Getter
@Table(
		name = "PROFILE",
		indexes = @Index(name = "idx_profile", columnList = "summoner_name")
)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Profile extends BaseEntity {

	@Builder
	public Profile(Account account, boolean isCertified, Integer authProfileIconId, String summonerAccountId, Queue preferQueue, String description,SummonerInfo summonerInfo) {
		this.account 			= account;
		this.isCertified		= isCertified;
		this.authProfileIconId	= authProfileIconId;
		this.summonerAccountId 	= summonerAccountId;
		this.preferQueue 		= preferQueue;
		this.description 		= description;
		this.summonerInfo 		= summonerInfo;
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

	@Column(name = "DESCRIPTION")
	private String description;

	@OneToMany(fetch = LAZY, cascade = ALL, mappedBy = "profile")
	private List<PreferKeyword> preferKeywords = new ArrayList<>();

	@OneToMany(fetch = LAZY, cascade = ALL, mappedBy = "profile")
	private List<LeaguePosition> leaguePositions = new ArrayList<>();

	@OneToMany(fetch = LAZY, cascade = ALL, mappedBy = "profile")
	private List<PreferChampion> preferChampions = new ArrayList<>();

	@OneToMany(fetch = LAZY, cascade = ALL, mappedBy = "profile")
	private List<PreferLine> preferLines = new ArrayList<>();

	@Embedded
	private SummonerInfo summonerInfo;

	public static Profile of(Account account, Summoner summoner, ProfileRequest.Create profileRequest) {
		return Profile.builder()
				.account(account)
				.isCertified(false)
				.authProfileIconId(null)
				.summonerAccountId(summoner.getAccountId())
				.summonerInfo(SummonerInfo.of(summoner))
				.description(profileRequest.getDescription())
				.build();
	}

	public void changeAuthProfileIconId(Integer summonerProfileIconId) {
		this.authProfileIconId = summonerProfileIconId;
	}

	public void changeProfileIconId(Integer summonerProfileIconId) {
		summonerInfo.changeProfileIconId(summonerProfileIconId);
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

	public void changeDescription(String description) {
		this.description = description;
	}

	public void changeLeaguePosition(LeagueEntry leagueEntry, Summoner summoner) {
		this.leaguePositions.stream()
				.filter(leaguePosition -> leaguePosition.getQueue() == valueOf(String.valueOf(leagueEntry.getQueue())))
				.findFirst()
				.ifPresent(leaguePosition -> leaguePosition.changeLeaguePosition(leagueEntry, summoner));
	}

	public int getRandomIconId() {
		int profileIconId = summonerInfo.getProfileIconId();
		Random random = new Random();
		random.setSeed(System.currentTimeMillis());
		int iconId = profileIconId;
		while (iconId == profileIconId) {
			iconId = random.nextInt(28);
		}
		return iconId;
	}

	public boolean getAuthConfirm(int summonerProfileIconId) {
		return isCertified || isReliable(summonerProfileIconId);
	}

	public boolean isReliable(int summonerProfileIconId) {
		if (isAuthorizable(summonerProfileIconId)) {
			changeIsCertified(true);
			changeAuthProfileIconId(-1);
			return true;
		}
		return false;
	}

}
