package opgg.backend.gmakersserver.domain.account.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import opgg.backend.gmakersserver.domain.account.controller.request.SignInDto;
import opgg.backend.gmakersserver.domain.account.controller.request.SignUpDto;
import opgg.backend.gmakersserver.domain.account.controller.response.TokenDto;
import opgg.backend.gmakersserver.domain.account.entity.Account;
import opgg.backend.gmakersserver.domain.account.service.AccountService;
import opgg.backend.gmakersserver.infra.jwt.JjwtService;
import opgg.backend.gmakersserver.infra.jwt.JwtFilter;
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
	public String signUp(@Valid @RequestBody SignUpDto signUpDto) {
		accountService.signUp(signUpDto);
		return "signUp success";
	}

	@PostMapping("/accounts/sign-in")
	public ResponseEntity<TokenDto> authorize(@Valid @RequestBody SignInDto signInDto) {
		Account account = accountService.login(signInDto);
		String jwt = jjwtService.createToken(account);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
		return new ResponseEntity<>(new TokenDto(jwt), httpHeaders, HttpStatus.OK);
	}

}
