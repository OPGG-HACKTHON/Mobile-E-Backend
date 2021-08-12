package opgg.backend.gmakersserver.domain.profile.controller;

import static org.springframework.http.HttpStatus.*;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import opgg.backend.gmakersserver.domain.profile.controller.request.ProfileRequest;
import opgg.backend.gmakersserver.domain.profile.service.ProfileService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProfileController {

	private final ProfileService profileService;

	@PostMapping("/profile")
	@ResponseStatus(CREATED)
	public String createProfile(@Valid @RequestBody ProfileRequest.Create create, @AuthenticationPrincipal
			Long id) {
		profileService.createProfile(create, id);
		return "Create Profile";
	}

	@PatchMapping("/profile/auth")
	public String authProfile(@Valid @RequestBody ProfileRequest.Auth auth, @AuthenticationPrincipal Long id) {
		return profileService.authProfile(auth, id);
	}

}