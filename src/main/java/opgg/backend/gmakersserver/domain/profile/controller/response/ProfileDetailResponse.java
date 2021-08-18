package opgg.backend.gmakersserver.domain.profile.controller.response;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import opgg.backend.gmakersserver.domain.leagueposition.entity.Queue;
import opgg.backend.gmakersserver.domain.leagueposition.entity.Tier;
import opgg.backend.gmakersserver.domain.preferline.entity.Line;

@Getter
public class ProfileDetailResponse {

	//Account
	private final Long accountId;
	private final String username;

	//Profile
	private final Long profileId;
	private final boolean isCertified;
	private final String summonerAccountId;
	private final Queue preferQueue;
	//SummonerInfo
	private final Integer profileIconId;
	private final String summonerId;
	private final String summonerName;
	//LeaguePosition
	private final int level;
	private final Queue queue;
	private final Tier tier;
	private final int tierLevel;
	private final int leaguePoint;
	private final int loseGames;
	private final int winGames;
	private final int winRate;

	//PreferChampion
	private final String championName;
	private final int championId;
	private final int championPoints;
	private final int preferChampionPriority;
	//PreferLine
	private final Line line;
	private final int preferLinePriority;

	@QueryProjection
	public ProfileDetailResponse(Long accountId, String username, Long profileId, boolean isCertified,
			String summonerAccountId, Queue preferQueue, Integer profileIconId, String summonerId, String summonerName,
			int level, Queue queue, Tier tier, int tierLevel, int leaguePoint, int loseGames,
			int winGames, int winRate, String championName, int championId, int championPoints,
			int preferChampionPriority,
			Line line, int preferLinePriority) {
		this.accountId = accountId;
		this.username = username;
		this.profileId = profileId;
		this.isCertified = isCertified;
		this.summonerAccountId = summonerAccountId;
		this.profileIconId = profileIconId;
		this.summonerId = summonerId;
		this.summonerName = summonerName;
		this.preferQueue = preferQueue;
		this.level = level;
		this.queue = queue;
		this.tier = tier;
		this.tierLevel = tierLevel;
		this.leaguePoint = leaguePoint;
		this.loseGames = loseGames;
		this.winGames = winGames;
		this.winRate = winRate;
		this.championName = championName;
		this.championId = championId;
		this.championPoints = championPoints;
		this.preferChampionPriority = preferChampionPriority;
		this.line = line;
		this.preferLinePriority = preferLinePriority;
	}
}
