package opgg.backend.gmakersserver.error.exception.jwt;

import opgg.backend.gmakersserver.error.exception.common.BusinessException;
import opgg.backend.gmakersserver.error.exception.common.ExceptionStatus;

public class InvalidJwtSignatureException extends BusinessException {

	public InvalidJwtSignatureException() {
		super(ExceptionStatus.INVALID_JWT_SIGNATURE_EXCEPTION);
	}

}
