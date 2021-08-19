package opgg.backend.gmakersserver.error.exception.preferline;

import opgg.backend.gmakersserver.error.exception.common.BusinessException;
import opgg.backend.gmakersserver.error.exception.common.ExceptionStatus;

public class PreferLinePriorityDuplicateException extends BusinessException {

	public PreferLinePriorityDuplicateException() {
		super(ExceptionStatus.PREFER_LINE_PRIORITY_DUPLICATE_EXCEPTION);
	}

}
