package opgg.backend.gmakersserver.domain.preferchampion.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PreferChampionImpl implements PreferChampionRepositoryCustom{

	private final JPAQueryFactory queryFactory;

}
