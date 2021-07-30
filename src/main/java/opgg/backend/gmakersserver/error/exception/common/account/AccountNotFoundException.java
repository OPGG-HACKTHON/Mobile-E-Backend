package opgg.backend.gmakersserver.error.exception.common.account;

import opgg.backend.gmakersserver.error.exception.common.BusinessException;
import opgg.backend.gmakersserver.error.exception.common.ExceptionStatus;

public class AccountNotFoundException extends BusinessException {

    public AccountNotFoundException() {
        super(ExceptionStatus.ACCOUNT_NOT_FOUND_EXCEPTION);
    }
}
