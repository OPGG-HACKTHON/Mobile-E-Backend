package opgg.backend.gmakersserver.error.exception.preferline;

import opgg.backend.gmakersserver.error.exception.common.BusinessException;
import opgg.backend.gmakersserver.error.exception.common.ExceptionStatus;

public class PreferLineBoundsException extends BusinessException {

	public PreferLineBoundsException() {
		super(ExceptionStatus.PREFER_LINE_BOUNDS_EXCEPTION);
	}

}
