package opgg.backend.gmakersserver.error.exception.jwt;

import opgg.backend.gmakersserver.error.exception.common.BusinessException;
import opgg.backend.gmakersserver.error.exception.common.ExceptionStatus;

public class ExpiredJwtTokenException extends BusinessException {

	public ExpiredJwtTokenException() {
		super(ExceptionStatus.EXPIRED_JWT_TOKEN_EXCEPTION);
	}

}
