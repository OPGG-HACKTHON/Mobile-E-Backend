package opgg.backend.gmakersserver.error.exception.profile;

import opgg.backend.gmakersserver.error.exception.common.BusinessException;
import opgg.backend.gmakersserver.error.exception.common.ExceptionStatus;

public class ProfileNotMatchException extends BusinessException {

	public ProfileNotMatchException() {
		super(ExceptionStatus.PROFILE_NOT_MATCH_EXCEPTION);
	}

}
