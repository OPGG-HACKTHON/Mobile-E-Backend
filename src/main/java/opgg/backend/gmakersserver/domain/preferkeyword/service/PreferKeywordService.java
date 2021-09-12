package opgg.backend.gmakersserver.domain.preferkeyword.service;

import lombok.RequiredArgsConstructor;
import opgg.backend.gmakersserver.domain.preferkeyword.entity.Keyword;
import opgg.backend.gmakersserver.domain.preferkeyword.repository.PreferKeywordRepository;
import opgg.backend.gmakersserver.domain.profile.entity.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static opgg.backend.gmakersserver.domain.preferkeyword.entity.PreferKeyword.of;

@Service
@RequiredArgsConstructor
public class PreferKeywordService {

    private final PreferKeywordRepository preferKeywordRepository;

    @Transactional
    public void createPreferKeyword(List<Keyword> keywords, Profile profile) {
        preferKeywordRepository.saveAll(of(keywords, profile));
    }

    @Transactional
    public void updatePreferKeyword(List<Keyword> keywords, Profile profile) {
        preferKeywordRepository.deletePreferKeywordByProfile(profile);
        createPreferKeyword(keywords, profile);
    }

}
