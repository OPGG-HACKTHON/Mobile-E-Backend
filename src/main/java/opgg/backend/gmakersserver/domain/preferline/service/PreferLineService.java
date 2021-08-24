package opgg.backend.gmakersserver.domain.preferline.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import opgg.backend.gmakersserver.domain.preferline.entity.PreferLine;
import opgg.backend.gmakersserver.domain.preferline.repository.PreferLineRepository;
import opgg.backend.gmakersserver.domain.profile.controller.request.ProfileRequest;
import opgg.backend.gmakersserver.domain.profile.entity.Profile;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PreferLineService {

	private final PreferLineRepository preferLineRepository;

	@Transactional
	public void createPreferLine(List<ProfileRequest.Create.PreferLine> preferLines, Profile profile) {
		createPreferLines(preferLines, profile);
	}

	@Transactional
	public void updatePreferLine(List<ProfileRequest.Create.PreferLine> preferLines, Profile profile) {
		preferLineRepository.deletePreferLineByProfile(profile);
		createPreferLines(preferLines, profile);
	}

	@Transactional
	public void createPreferLines(List<ProfileRequest.Create.PreferLine> preferLines, Profile profile) {
		preferLines.forEach(preferLine -> preferLineRepository.save(PreferLine.builder()
				.profile(profile)
				.line(preferLine.getLine())
				.priority(preferLine.getPriority())
				.build()));
	}

}
