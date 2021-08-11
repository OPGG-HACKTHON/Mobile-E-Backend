package opgg.backend.gmakersserver.domain.account.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import opgg.backend.gmakersserver.domain.account.controller.request.SignInRequest;
import opgg.backend.gmakersserver.domain.account.controller.request.SignUpRequest;
import opgg.backend.gmakersserver.domain.account.controller.response.TokenResponse;
import opgg.backend.gmakersserver.domain.account.entity.Account;
import opgg.backend.gmakersserver.domain.account.service.AccountService;
import opgg.backend.gmakersserver.jwt.JjwtService;
import opgg.backend.gmakersserver.jwt.JwtFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "account", description = "the Account API")
public class AccountController {

	private final JjwtService jjwtService;
	private final AccountService accountService;

	@PostMapping("/accounts/sign-up")
	@ResponseStatus(HttpStatus.CREATED)
	public String signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
		accountService.signUp(signUpRequest);
		return "signUp success";
	}

	@PostMapping("/accounts/sign-in")
	public ResponseEntity<TokenResponse> authorize(@Valid @RequestBody SignInRequest signInRequest) {
		Account account = accountService.login(signInRequest);
		String jwt = jjwtService.createToken(account);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
		return new ResponseEntity<>(new TokenResponse(jwt), httpHeaders, HttpStatus.OK);
	}

}
