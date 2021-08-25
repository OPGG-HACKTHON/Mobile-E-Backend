package opgg.backend.gmakersserver.error.exception.preferkeyword;

import opgg.backend.gmakersserver.error.exception.common.BusinessException;
import opgg.backend.gmakersserver.error.exception.common.ExceptionStatus;

public class PreferKeywordBoundsException extends BusinessException {

	public PreferKeywordBoundsException() {
		super(ExceptionStatus.PREFER_KEYWORD_BOUNDS_EXCEPTION);
	}

}
