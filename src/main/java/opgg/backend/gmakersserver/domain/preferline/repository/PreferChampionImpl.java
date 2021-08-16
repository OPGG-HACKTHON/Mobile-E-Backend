package opgg.backend.gmakersserver.domain.preferline.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PreferChampionImpl implements PreferLineRepositoryCustom{

	private final JPAQueryFactory queryFactory;

}
