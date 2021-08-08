package opgg.backend.gmakersserver.error.exception.jwt;

import opgg.backend.gmakersserver.error.exception.common.BusinessException;
import opgg.backend.gmakersserver.error.exception.common.ExceptionStatus;

public class UnsupportedJwtTokenException extends BusinessException {

	public UnsupportedJwtTokenException() {
		super(ExceptionStatus.UNSUPPORTED_JWT_TOKEN_EXCEPTION);
	}

}
