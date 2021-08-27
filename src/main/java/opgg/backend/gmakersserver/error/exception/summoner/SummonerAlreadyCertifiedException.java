package opgg.backend.gmakersserver.error.exception.summoner;

import opgg.backend.gmakersserver.error.exception.common.BusinessException;
import opgg.backend.gmakersserver.error.exception.common.ExceptionStatus;

public class SummonerAlreadyCertifiedException extends BusinessException {

    public SummonerAlreadyCertifiedException() {
        super(ExceptionStatus.SUMMONER_ALREADY_CERTIFIED_EXCEPTION);
    }

}
