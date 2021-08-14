package opgg.backend.gmakersserver.domain.profile.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

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

}
