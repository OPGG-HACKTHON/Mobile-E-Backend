package opgg.backend.gmakersserver.domain.account.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
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
import opgg.backend.gmakersserver.domain.account.dto.SignInDto;
import opgg.backend.gmakersserver.domain.account.dto.SignUpDto;
import opgg.backend.gmakersserver.domain.account.service.AccountService;
import opgg.backend.gmakersserver.error.exception.response.ExceptionResponseInfo;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "account", description = "the Account API")
public class AccountController {

	private final AccountService accountService;

	@Operation(summary = "회원가입", tags = {"account"})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "회원가입 성공"),
			@ApiResponse(responseCode = "400", description = "회원가입 실패"),
			@ApiResponse(responseCode = "4003", description = "중복된 아이디",
					content = @Content(schema = @Schema(implementation = ExceptionResponseInfo.class)))
	})
	@PostMapping("/accounts")
	@ResponseStatus(HttpStatus.CREATED)
	public String signUp(@Valid @RequestBody SignUpDto signUpDto) {
		accountService.signUp(signUpDto);
		return "signUp success";
	}

    @GetMapping("/accounts")
    public String signIn(@Valid @RequestBody SignInDto signInDto) {
        accountService.signIn(signInDto);
        return "signIn success";
    }
}
