package opgg.backend.gmakersserver.error.exception.jwt;

import opgg.backend.gmakersserver.error.exception.common.BusinessException;
import opgg.backend.gmakersserver.error.exception.common.ExceptionStatus;

public class AuthorityInfoNotFoundException extends BusinessException {
	public AuthorityInfoNotFoundException() {
		super(ExceptionStatus.AUTHORITY_INFO_NOT_FOUND_EXCEPTION);
	}
}
