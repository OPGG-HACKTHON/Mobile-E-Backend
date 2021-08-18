package opgg.backend.gmakersserver.error.exception.preferchampion;

import opgg.backend.gmakersserver.error.exception.common.BusinessException;
import opgg.backend.gmakersserver.error.exception.common.ExceptionStatus;

public class PreferChampionBoundsException extends BusinessException {

	public PreferChampionBoundsException() {
		super(ExceptionStatus.PREFER_CHAMPION_BOUNDS_EXCEPTION);
	}
}
