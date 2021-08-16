package opgg.backend.gmakersserver.error.exception.profile;

import static opgg.backend.gmakersserver.error.exception.common.ExceptionStatus.*;

import opgg.backend.gmakersserver.error.exception.common.BusinessException;
import opgg.backend.gmakersserver.error.exception.common.ExceptionStatus;

public class ProfileExistException extends BusinessException {

	public ProfileExistException() {
		super(PROFILE_EXIST_EXCEPTION);
	}

}
