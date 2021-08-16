package opgg.backend.gmakersserver.domain.profile.controller.request;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import opgg.backend.gmakersserver.domain.leagueposition.entity.Queue;
import opgg.backend.gmakersserver.domain.preferline.entity.Line;

public class ProfileRequest {

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Create {

		@NotEmpty
		private final List<PreferChampion> preferChampions = new ArrayList<>();
		@NotEmpty
		private final List<PreferLine> preferLines = new ArrayList<>();
		@NotBlank
		private String summonerName;

		@NotNull
		private Queue preferQueue;

		@Override
		public String toString() {
			return "Create{" +
					"preferChampions=" + preferChampions +
					", preferLines=" + preferLines +
					", summonerName='" + summonerName + '\'' +
					", preferQueue=" + preferQueue +
					'}';
		}

		@Getter
		@AllArgsConstructor
		@NoArgsConstructor
		public static class PreferChampion {

			@NotNull
			private int priority;

			@NotBlank
			private int championId;

		}

		@Getter
		@AllArgsConstructor
		@NoArgsConstructor
		public static class PreferLine {

			@NotNull
			private int priority;

			@NotBlank
			private Line line;

		}

	}

	@Getter
	@Setter
	public static class Auth {

		@NotBlank
		private String summonerId;

	}

}
