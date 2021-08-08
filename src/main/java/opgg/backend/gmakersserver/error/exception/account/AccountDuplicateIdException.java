package opgg.backend.gmakersserver.error.exception.account;

import opgg.backend.gmakersserver.error.exception.common.BusinessException;
import opgg.backend.gmakersserver.error.exception.common.ExceptionStatus;

public class AccountDuplicateIdException extends BusinessException {

	public AccountDuplicateIdException(String id) {
		super(ExceptionStatus.ACCOUNT_DUPLICATE_ID_EXCEPTION, id + "는 " + ExceptionStatus.ACCOUNT_DUPLICATE_ID_EXCEPTION.getMessage());
	}

}
