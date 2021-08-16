package opgg.backend.gmakersserver.domain.preferline.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import opgg.backend.gmakersserver.domain.preferline.entity.PreferLine;
import opgg.backend.gmakersserver.domain.preferline.repository.PreferLineRepository;
import opgg.backend.gmakersserver.domain.profile.controller.request.ProfileRequest;
import opgg.backend.gmakersserver.domain.profile.entity.Profile;

@Service
@RequiredArgsConstructor
public class PreferLineService {

	private final PreferLineRepository preferLineRepository;

	@Transactional
	public void createPreferLine(ProfileRequest.Create profileRequest, Profile profile) {
		profileRequest.getPreferLines().forEach(requestPreferLine -> preferLineRepository.save(PreferLine.builder()
				.profile(profile)
				.line(requestPreferLine.getLine())
				.priority(requestPreferLine.getPriority())
				.build()));
	}

}
