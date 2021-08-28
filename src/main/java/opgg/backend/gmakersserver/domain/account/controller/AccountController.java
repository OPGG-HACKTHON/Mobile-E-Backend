package opgg.backend.gmakersserver.domain.account.controller;

import lombok.RequiredArgsConstructor;
import opgg.backend.gmakersserver.domain.account.controller.request.SignInRequest;
import opgg.backend.gmakersserver.domain.account.controller.request.SignUpRequest;
import opgg.backend.gmakersserver.domain.account.controller.response.TokenResponse;
import opgg.backend.gmakersserver.domain.account.service.AccountService;
import opgg.backend.gmakersserver.infra.jwt.JwtFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AccountController {

	private final AccountService accountService;

	@PostMapping("/accounts/sign-up")
	@ResponseStatus(HttpStatus.CREATED)
	public String signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
		accountService.signUp(signUpRequest);
		return "signUp success";
	}

	@PostMapping("/accounts/sign-in")
	public ResponseEntity<TokenResponse> authorize(@Valid @RequestBody SignInRequest signInRequest) {
		String jwtToken = accountService.getJwtToken(signInRequest);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwtToken);
		return new ResponseEntity<>(new TokenResponse(jwtToken), httpHeaders, HttpStatus.OK);
	}

}
