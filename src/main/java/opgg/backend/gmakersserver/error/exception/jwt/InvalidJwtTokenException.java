package opgg.backend.gmakersserver.error.exception.jwt;

import opgg.backend.gmakersserver.error.exception.common.BusinessException;
import opgg.backend.gmakersserver.error.exception.common.ExceptionStatus;

public class InvalidJwtTokenException extends BusinessException {

	public InvalidJwtTokenException() {
		super(ExceptionStatus.INVALID_JWT_TOKEN_EXCEPTION);
	}

}
