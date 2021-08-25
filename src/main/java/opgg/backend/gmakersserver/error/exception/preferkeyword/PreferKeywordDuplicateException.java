package opgg.backend.gmakersserver.error.exception.preferkeyword;

import opgg.backend.gmakersserver.error.exception.common.BusinessException;
import opgg.backend.gmakersserver.error.exception.common.ExceptionStatus;

public class PreferKeywordDuplicateException extends BusinessException {

	public PreferKeywordDuplicateException() {
		super(ExceptionStatus.PREFER_KEYWORD_DUPLICATE_EXCEPTION);
	}
}
