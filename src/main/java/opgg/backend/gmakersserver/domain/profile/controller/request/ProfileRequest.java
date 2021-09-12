package opgg.backend.gmakersserver.domain.profile.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import opgg.backend.gmakersserver.domain.leagueposition.entity.Queue;
import opgg.backend.gmakersserver.domain.preferKeyword.entity.Keyword;
import opgg.backend.gmakersserver.domain.preferline.entity.Line;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProfileRequest {

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Create {

		@NotEmpty
		@Size(min = 1, max = 3)
		private final List<PreferChampion> preferChampions = new ArrayList<>();

		@NotEmpty
		@Size(min = 2, max = 2)
		private final List<PreferLine> preferLines = new ArrayList<>();

		@Size(max = 3)
		private List<Keyword> preferKeywords = new ArrayList<>();

		@NotBlank
		private String summonerName;

		@NotBlank
		private String description;

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

			@Override
			public boolean equals(Object o) {
				if (this == o)
					return true;
				if (o == null || getClass() != o.getClass())
					return false;
				PreferLine that = (PreferLine)o;
				return getPriority() == that.getPriority() && getLine() == that.getLine();
			}

			@Override
			public int hashCode() {
				return Objects.hash(getPriority(), getLine());
			}

		}

	}

	@Getter
	@Setter
	public static class Auth {

		@NotBlank
		private String summonerId;

	}

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Update {

		@Size(max = 3)
		private final List<Create.PreferChampion> preferChampions = new ArrayList<>();

		@Size(max = 2)
		private final List<Create.PreferLine> preferLines = new ArrayList<>();

		@NotBlank
		private String summonerName;

		private String description;

		private Queue preferQueue;

		@Size(max = 3)
		private List<Keyword> preferKeywords = new ArrayList<>();

	}

}
