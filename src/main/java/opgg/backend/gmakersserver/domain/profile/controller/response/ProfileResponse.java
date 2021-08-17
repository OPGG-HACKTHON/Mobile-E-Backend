package opgg.backend.gmakersserver.domain.profile.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import opgg.backend.gmakersserver.domain.leagueposition.entity.Queue;
import opgg.backend.gmakersserver.domain.leagueposition.entity.Tier;
import opgg.backend.gmakersserver.domain.preferline.entity.Line;

public class ProfileResponse {

	@Getter
	@AllArgsConstructor
	public static class Auth {
		private final int iconId;
	}

	@Getter
	@AllArgsConstructor
	public static class AuthConfirm {
		private final boolean isCertified;
	}

	@Getter
	@AllArgsConstructor
	public static class Find {
		private final long accountId;
		private final String username;
		private final long profileId;
		private final boolean isCertified;
		private final String summonerAccountId;
		private final long profileIconId;
		private final String summonerId;
		private final String summonerName;
		private final Queue preferQueue;
		private final int level;
		private final Queue queue;
		private final Tier tier;
		private final int tierLevel;
		private final Line line;
		private final int priority;
	}

}
