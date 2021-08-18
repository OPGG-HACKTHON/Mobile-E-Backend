package opgg.backend.gmakersserver.domain.profile.controller;

import lombok.RequiredArgsConstructor;
import opgg.backend.gmakersserver.domain.profile.controller.request.ProfileRequest;
import opgg.backend.gmakersserver.domain.profile.controller.response.ProfileDetailResponse;
import opgg.backend.gmakersserver.domain.profile.controller.response.ProfileFindResponse;
import opgg.backend.gmakersserver.domain.profile.controller.response.ProfileResponse;
import opgg.backend.gmakersserver.domain.profile.entity.Profile;
import opgg.backend.gmakersserver.domain.profile.service.ProfileService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProfileController {

	private final ProfileService profileService;

	@PostMapping("/profiles")
	@ResponseStatus(CREATED)
	public String createProfile(@Valid @RequestBody ProfileRequest.Create create, @AuthenticationPrincipal
			Long id) {
		profileService.createProfile(create, id);
		return "Create Profile";
	}

	@PatchMapping("/profiles/auth")
	public ProfileResponse.Auth authProfile(@Valid @RequestBody ProfileRequest.Auth auth, @AuthenticationPrincipal Long id) {
		return profileService.authProfile(auth, id);
	}

	@PatchMapping("/profiles/auth-confirm")
	public ProfileResponse.AuthConfirm authConfirm(@Valid @RequestBody ProfileRequest.Auth auth, @AuthenticationPrincipal Long id) {
		return profileService.authConfirm(auth, id);
	}

	@GetMapping("/profiles")
	public List<ProfileFindResponse> getProfiles(@AuthenticationPrincipal Long id) {
		return profileService.getProfiles(id);
	}

	@GetMapping("/profiles/{id}")
	public ProfileDetailResponse getProfile(@PathVariable("id") Long profileId, @AuthenticationPrincipal Long id) {
		return profileService.getProfile(profileId, id);
	}

}