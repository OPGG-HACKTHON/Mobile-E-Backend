package opgg.backend.gmakersserver.error.exception.preferchampion;

import opgg.backend.gmakersserver.error.exception.common.BusinessException;
import opgg.backend.gmakersserver.error.exception.common.ExceptionStatus;

public class PreferChampionPriorityDuplicateException extends BusinessException {

	public PreferChampionPriorityDuplicateException() {
		super(ExceptionStatus.PREFER_CHAMPION_PRIORITY_DUPLICATE_EXCEPTION);
	}
}
