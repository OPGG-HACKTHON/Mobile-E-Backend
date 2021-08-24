package opgg.backend.gmakersserver.domain.preferKeyword.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Keyword {
    //TOP
    HEADSMAN("망나니"),
    SPLIT("스플릿"),
    TANKER("탱커"),

    //JUG
    GANKING("갱킹"),
    ORIENTEDOBJECT("오브젝트지향"),

    //MID
    ROAMING("로밍"),
    ASSASSIN("암살자"),

    //AD
    GROWTHTYPE("성장형"),

    //SUP
    UTILITYTYPE("유틸형"),

    //공통
    BATTLENATION("전투민족"),
    ORIENTEDTOWARDSFIGHTING("한타지향");

    private String krKeyword;

    Keyword(String krKeyword){
        this.krKeyword = krKeyword;
    }

}
