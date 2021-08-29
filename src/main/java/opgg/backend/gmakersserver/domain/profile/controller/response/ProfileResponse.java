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

		public static Auth from (int profileId) {
			return new Auth(profileId);
		}

	}

	@Getter
	@AllArgsConstructor
	public static class AuthConfirm {
		private final boolean isCertified;

		public static AuthConfirm from (boolean isAuthConfirm) {
			return new AuthConfirm(isAuthConfirm);
		}

	}

}
