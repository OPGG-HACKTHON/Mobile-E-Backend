package opgg.backend.gmakersserver.error.exception.riotapi;

import static opgg.backend.gmakersserver.error.exception.common.ExceptionStatus.*;

import opgg.backend.gmakersserver.error.exception.common.BusinessException;

public class SummonerNotFoundException extends BusinessException {

	public SummonerNotFoundException() {
		super(SUMMONER_NOT_FOUNT_EXCEPTION);
	}

}
