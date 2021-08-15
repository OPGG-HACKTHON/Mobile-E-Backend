package opgg.backend.gmakersserver.domain.leagueposition.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TierLevel {

    //Todo: insert시 level로 
    IV(4), III(3), II(2), I(1);

    public final int level;

}
