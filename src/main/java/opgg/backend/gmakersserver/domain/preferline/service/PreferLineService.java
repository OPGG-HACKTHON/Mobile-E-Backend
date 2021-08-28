package opgg.backend.gmakersserver.domain.preferline.service;

import lombok.RequiredArgsConstructor;
import opgg.backend.gmakersserver.domain.preferline.repository.PreferLineRepository;
import opgg.backend.gmakersserver.domain.profile.controller.request.ProfileRequest;
import opgg.backend.gmakersserver.domain.profile.entity.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static opgg.backend.gmakersserver.domain.preferline.entity.PreferLine.of;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PreferLineService {

	private final PreferLineRepository preferLineRepository;

	@Transactional
	public void createPreferLine(List<ProfileRequest.Create.PreferLine> preferLines, Profile profile) {
		preferLineRepository.saveAll(of(preferLines, profile));
	}

	@Transactional
	public void updatePreferLine(List<ProfileRequest.Create.PreferLine> preferLines, Profile profile) {
		preferLineRepository.deletePreferLineByProfile(profile);
		createPreferLine(preferLines, profile);
	}

}
