package opgg.backend.gmakersserver.domain.profile.controller;

import static org.springframework.http.HttpStatus.*;

import java.util.List;

import javax.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import opgg.backend.gmakersserver.domain.profile.controller.request.ProfileRequest;
import opgg.backend.gmakersserver.domain.profile.controller.response.ProfileDetailResponse;
import opgg.backend.gmakersserver.domain.profile.controller.response.ProfileFindResponse;
import opgg.backend.gmakersserver.domain.profile.controller.response.ProfileResponse;
import opgg.backend.gmakersserver.domain.profile.service.ProfileService;

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
	public ProfileResponse.Auth authProfile(@Valid @RequestBody ProfileRequest.Auth auth,
			@AuthenticationPrincipal Long id) {
		return profileService.authProfile(auth, id);
	}

	@PatchMapping("/profiles/auth-confirm")
	public ProfileResponse.AuthConfirm authConfirm(@Valid @RequestBody ProfileRequest.Auth auth,
			@AuthenticationPrincipal Long id) {
		return profileService.authConfirm(auth, id);
	}

	@GetMapping("/profiles")
	public List<ProfileFindResponse> getProfiles(
			@RequestParam(value = "summonerName", required = false) String summonerName,
			@AuthenticationPrincipal Long id) {
		return profileService.getProfiles(summonerName, id);
	}

	@GetMapping("/profiles/{profileId}")
	public ProfileDetailResponse getProfile(@PathVariable("profileId") Long profileId,
			@AuthenticationPrincipal Long id) {
		return profileService.getProfile(profileId, id);
	}

	@DeleteMapping("/profiles/{profileId}")
	@ResponseStatus(NO_CONTENT)
	public void deleteProfile(@PathVariable("profileId") Long profileId, @AuthenticationPrincipal Long id) {
		profileService.deleteProfile(profileId, id);
	}

	@PatchMapping("/profiles/{profileId}")
	@ResponseStatus(NO_CONTENT)
	public void updateProfile(@PathVariable("profileId") Long profileId, @RequestBody ProfileRequest.Update update,
			@AuthenticationPrincipal Long id) {
		profileService.updateProfile(profileId, update, id);
	}

}