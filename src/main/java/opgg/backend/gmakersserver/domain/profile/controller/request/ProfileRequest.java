package opgg.backend.gmakersserver.domain.profile.controller.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class ProfileRequest {

	@Getter
	@Setter
	public static class Create {

		@NotBlank
		private String summonerName;

		private List<Integer> preferChampionIds = new ArrayList<>();

		private List<String> preferPositions = new ArrayList<>();

	}

	@Getter
	@Setter
	public static class Auth {

		@NotBlank
		private String summonerId;

	}

}
