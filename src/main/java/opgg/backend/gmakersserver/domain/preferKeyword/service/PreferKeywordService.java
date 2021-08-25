package opgg.backend.gmakersserver.domain.preferKeyword.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import opgg.backend.gmakersserver.domain.preferKeyword.entity.Keyword;
import opgg.backend.gmakersserver.domain.preferKeyword.entity.PreferKeyword;
import opgg.backend.gmakersserver.domain.preferKeyword.repository.PreferKeywordRepository;
import opgg.backend.gmakersserver.domain.profile.entity.Profile;

@Service
@RequiredArgsConstructor
public class PreferKeywordService {

    private final PreferKeywordRepository preferKeywordRepository;

    @Transactional
    public void createPreferKeyword(List<Keyword> keywords, Profile profile) {
        createPreferKeywords(keywords, profile);
    }

    @Transactional
    public void updatePreferKeyword(List<Keyword> keywords, Profile profile) {
        preferKeywordRepository.deletePreferKeywordByProfile(profile);
        createPreferKeywords(keywords, profile);
    }

    @Transactional
    public void createPreferKeywords(List<Keyword> keywords, Profile profile) {
        keywords.forEach(keyword -> preferKeywordRepository.save(
                PreferKeyword.builder()
                        .keyword(keyword)
                        .profile(profile)
                        .build())
        );
    }

}
