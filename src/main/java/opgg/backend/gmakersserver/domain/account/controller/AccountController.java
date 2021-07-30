package opgg.backend.gmakersserver.domain.account.controller;

import lombok.RequiredArgsConstructor;
import opgg.backend.gmakersserver.domain.account.dto.SignInDto;
import opgg.backend.gmakersserver.domain.account.dto.SignUpDto;
import opgg.backend.gmakersserver.domain.account.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

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
