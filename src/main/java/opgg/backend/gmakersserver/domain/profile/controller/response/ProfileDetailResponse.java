package opgg.backend.gmakersserver.domain.profile.controller.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import opgg.backend.gmakersserver.domain.leagueposition.entity.Queue;
import opgg.backend.gmakersserver.domain.leagueposition.entity.Tier;
import opgg.backend.gmakersserver.domain.preferKeyword.entity.Keyword;
import opgg.backend.gmakersserver.domain.preferline.entity.Line;

import java.util.*;
import java.util.stream.Collectors;

import static opgg.backend.gmakersserver.domain.leagueposition.entity.Queue.NONE;

@Getter
@NoArgsConstructor
public class ProfileDetailResponse {

	//Account
	private Long accountId;
	private String username;
	//Profile
	private Long profileId;
	private boolean isCertified;
	private String summonerAccountId;
	private Queue preferQueue;
	//SummonerInfo
	private Integer profileIconId;
	private String summonerId;
	private String summonerName;
	//LeaguePosition
	private int level;
	private Queue queue;
	private Tier tier;
	private int tierLevel;
	private int leaguePoint;
	private int loseGames;
	private int winGames;
	private int winRate;
	private String description;
	//PreferChampion
	private List<PreferChampion> preferChampions = new ArrayList<>();
	//PreferLine
	private List<PreferLine> preferLines = new ArrayList<>();
	private List<String> preferKeywords = new ArrayList<>();

	@Builder
	@QueryProjection
	public ProfileDetailResponse(Long accountId, String username, Long profileId, boolean isCertified,
			String summonerAccountId, Queue preferQueue, Integer profileIconId, String summonerId, String summonerName,
			String description, int level, Queue queue, Tier tier, int tierLevel, int leaguePoint, int loseGames,
			int winGames, int winRate, String championName, int championId, int championPoints, int championLevel,
			int preferChampionPriority, Line line, int preferLinePriority, Keyword keyword) {
		this.accountId = accountId;
		this.username = username;
		this.profileId = profileId;
		this.isCertified = isCertified;
		this.summonerAccountId = summonerAccountId;
		this.profileIconId = profileIconId;
		this.summonerId = summonerId;
		this.summonerName = summonerName;
		this.description = description;
		this.preferQueue = preferQueue;
		this.level = level;
		this.queue = queue;
		this.tier = tier;
		this.tierLevel = tierLevel;
		this.leaguePoint = leaguePoint;
		this.loseGames = loseGames;
		this.winGames = winGames;
		this.winRate = winRate;
		this.preferChampions.add(PreferChampion.builder()
				.championName(championName)
				.championId(championId)
				.championPoints(championPoints)
				.championLevel(championLevel)
				.preferChampionPriority(preferChampionPriority)
				.build());
		this.preferLines.add(new PreferLine(line, preferLinePriority));
		this.preferKeywords.add(keyword.getKrKeyword());
	}

	public ProfileDetailResponse(List<ProfileDetailResponse> profileDetailResponses) {
		for (ProfileDetailResponse profileDetailResponse : profileDetailResponses) {
			Queue preferQueue = profileDetailResponse.getPreferQueue();
			if (preferQueue == NONE || preferQueue == profileDetailResponse.getQueue()) {
				if (preferQueue != NONE) {
					this.queue = profileDetailResponse.getQueue();
					this.tier = profileDetailResponse.getTier();
					this.tierLevel = profileDetailResponse.getTierLevel();
					this.leaguePoint = profileDetailResponse.getLeaguePoint();
					this.loseGames = profileDetailResponse.getLoseGames();
					this.winGames = profileDetailResponse.getWinGames();
					this.winRate = profileDetailResponse.getWinRate();
					this.description = profileDetailResponse.description;
				}
				this.accountId = profileDetailResponse.getAccountId();
				this.username = profileDetailResponse.getUsername();
				this.profileId = profileDetailResponse.getProfileId();
				this.isCertified = profileDetailResponse.isCertified();
				this.summonerAccountId = profileDetailResponse.getSummonerAccountId();
				this.profileIconId = profileDetailResponse.getProfileIconId();
				this.summonerId = profileDetailResponse.getSummonerId();
				this.summonerName = profileDetailResponse.getSummonerName();
				this.preferQueue = profileDetailResponse.getPreferQueue();
				this.level = profileDetailResponse.getLevel();

				PreferChampion preferChampion = profileDetailResponse.getPreferChampions().get(0);
				this.preferChampions.add(PreferChampion.builder()
						.championName(preferChampion.getChampionName())
						.championId(preferChampion.getChampionId())
						.championPoints(preferChampion.getChampionPoints())
						.championLevel(preferChampion.getChampionLevel())
						.preferChampionPriority(preferChampion.getPreferChampionPriority())
						.build());

				PreferLine preferLine = profileDetailResponse.getPreferLines().get(0);
				this.preferLines.add(new PreferLine(preferLine.getLine(), preferLine.getPreferLinePriority()));

				String preferKeyword = profileDetailResponse.getPreferKeywords().get(0);
				this.preferKeywords.add(preferKeyword);
			}
		}

		this.preferChampions = new ArrayList<>(new HashSet<>(preferChampions))
				.stream()
				.sorted(Comparator.comparingInt(o -> o.preferChampionPriority))
				.collect(Collectors.toList());
		this.preferLines = new ArrayList<>(new HashSet<>(preferLines))
				.stream()
				.sorted(Comparator.comparingInt(o -> o.preferLinePriority))
				.collect(Collectors.toList());
		this.preferKeywords = new ArrayList<>(new ArrayList<>(new HashSet<>(preferKeywords)));
	}

	@Getter
	@NoArgsConstructor
	public static class PreferChampion {

		private String championName;
		private int championId;
		private int championPoints;
		private int preferChampionPriority;
		private int championLevel;

		@Builder
		public PreferChampion(String championName, int championId, int championPoints, int preferChampionPriority,
				int championLevel) {
			this.championName = championName;
			this.championId = championId;
			this.championPoints = championPoints;
			this.preferChampionPriority = preferChampionPriority;
			this.championLevel = championLevel;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;
			PreferChampion that = (PreferChampion)o;
			return getChampionId() == that.getChampionId() && getChampionPoints() == that.getChampionPoints()
					&& getPreferChampionPriority() == that.getPreferChampionPriority()
					&& getChampionLevel() == that.getChampionLevel() && Objects.equals(getChampionName(),
					that.getChampionName());
		}

		@Override
		public int hashCode() {
			return Objects.hash(getChampionName(), getChampionId(), getChampionPoints(), getPreferChampionPriority(),
					getChampionLevel());
		}

	}

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class PreferLine {

		private Line line;
		private int preferLinePriority;

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;
			PreferLine that = (PreferLine)o;
			return getPreferLinePriority() == that.getPreferLinePriority() && getLine() == that.getLine();
		}

		@Override
		public int hashCode() {
			return Objects.hash(getLine(), getPreferLinePriority());
		}

	}

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class PreferKeyword{

		private Keyword keyword;

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;
			PreferKeyword that = (PreferKeyword)o;
			return getKeyword() == that.getKeyword();
		}

		@Override
		public int hashCode() {
			return Objects.hash(getKeyword());
		}

	}

}
