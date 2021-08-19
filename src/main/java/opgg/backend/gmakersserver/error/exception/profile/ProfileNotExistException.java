package opgg.backend.gmakersserver.error.exception.profile;

import opgg.backend.gmakersserver.error.exception.common.BusinessException;
import opgg.backend.gmakersserver.error.exception.common.ExceptionStatus;

public class ProfileNotExistException extends BusinessException {

    public ProfileNotExistException() {
        super(ExceptionStatus.PROFILE_NOT_EXIST_EXCEPTION);
    }

}
