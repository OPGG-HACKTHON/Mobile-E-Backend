package opgg.backend.gmakersserver.domain.profile.controller.request;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

public class ProfileRequest {

	@Getter
	@Setter
	public static class Create {

		@NotBlank
		private String summonerName;

	}

	@Getter
	@Setter
	public static class Auth {

		@NotBlank
		private String summonerId;

	}

}
