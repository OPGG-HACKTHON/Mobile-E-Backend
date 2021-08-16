package opgg.backend.gmakersserver.error.exception.s3;

import opgg.backend.gmakersserver.error.exception.common.BusinessException;
import opgg.backend.gmakersserver.error.exception.common.ExceptionStatus;

public class NotImageFileException extends BusinessException {

	public NotImageFileException() {
		super(ExceptionStatus.NOT_IMAGE_FILE_EXCEPTION);
	}
}
