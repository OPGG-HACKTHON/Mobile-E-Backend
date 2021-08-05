package opgg.backend.gmakersserver.error.exception.account;

import opgg.backend.gmakersserver.error.exception.common.BusinessException;
import opgg.backend.gmakersserver.error.exception.common.ExceptionStatus;

public class AccountPasswordNotMatchException extends BusinessException {
	public AccountPasswordNotMatchException() {
		super(ExceptionStatus.ACCOUNT_PASSWORD_NOT_MATCH_EXCEPTION);
	}
}
