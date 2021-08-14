package opgg.backend.gmakersserver.domain.profile.controller.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ProfileResponse {

	@Builder
	public ProfileResponse(boolean isCertified, int iconId) {
		this.isCertified = isCertified;
		this.iconId = iconId;
	}

	private final boolean isCertified;
	private final int iconId;

}
