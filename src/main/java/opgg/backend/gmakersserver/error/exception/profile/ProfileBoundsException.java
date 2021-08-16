package opgg.backend.gmakersserver.error.exception.profile;

import opgg.backend.gmakersserver.error.exception.common.BusinessException;
import opgg.backend.gmakersserver.error.exception.common.ExceptionStatus;

public class ProfileBoundsException extends BusinessException {

	public ProfileBoundsException() {
		super(ExceptionStatus.PROFILE_BOUNDS_EXCEPTION);
	}
}
