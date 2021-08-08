package opgg.backend.gmakersserver.domain.account.controller;

import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import opgg.backend.gmakersserver.domain.account.controller.request.SignInDto;
import opgg.backend.gmakersserver.domain.account.controller.request.SignUpDto;
import opgg.backend.gmakersserver.domain.account.controller.response.TokenDto;
import opgg.backend.gmakersserver.domain.account.entity.Account;
import opgg.backend.gmakersserver.domain.account.service.AccountService;
import opgg.backend.gmakersserver.error.exception.response.ExceptionResponseInfo;
import opgg.backend.gmakersserver.jwt.JwtFilter;
import opgg.backend.gmakersserver.jwt.JjwtService;

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
