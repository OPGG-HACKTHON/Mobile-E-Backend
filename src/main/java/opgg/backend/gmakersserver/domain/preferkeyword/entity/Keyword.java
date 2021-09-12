package opgg.backend.gmakersserver.domain.preferkeyword.entity;

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
    ORIENTED_OBJECT("오브젝트지향"),

    //MID
    ROAMING("로밍"),
    ASSASSIN("암살자"),

    //AD
    GROWTH_TYPE("성장형"),

    //SUP
    UTILITY_TYPE("유틸형"),

    //공통
    BATTLE_NATION("전투민족"),
    ORIENTED_TOWARDS_FIGHTING("한타지향");

    private final String krKeyword;

}
