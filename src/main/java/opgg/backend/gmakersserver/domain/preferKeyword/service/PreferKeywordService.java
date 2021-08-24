package opgg.backend.gmakersserver.domain.preferKeyword.service;

import lombok.RequiredArgsConstructor;
import opgg.backend.gmakersserver.domain.preferKeyword.entity.Keyword;
import opgg.backend.gmakersserver.domain.preferKeyword.entity.PreferKeyword;
import opgg.backend.gmakersserver.domain.preferKeyword.repository.PreferKeywordRepository;
import opgg.backend.gmakersserver.domain.profile.controller.request.ProfileRequest;
import opgg.backend.gmakersserver.domain.profile.entity.Profile;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class PreferKeywordService {

    private final PreferKeywordRepository preferKeywordRepository;

    @Transactional
    public void createPreferKeyword(ProfileRequest.Create profileRequest, Profile profile) {

        profileRequest.getPreferKeyword().forEach(requestPreferKeyword -> preferKeywordRepository.save(
                PreferKeyword.builder()
                .keyword(requestPreferKeyword)
                .profile(profile)
                .build())
        );
    }
}
