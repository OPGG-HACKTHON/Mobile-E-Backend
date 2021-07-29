package opgg.backend.gmakersserver.domain.account.controller;

import lombok.RequiredArgsConstructor;
import opgg.backend.gmakersserver.domain.account.dto.SignInDto;
import opgg.backend.gmakersserver.domain.account.dto.SignUpDto;
import opgg.backend.gmakersserver.domain.account.service.AccountService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/api/account")
    @ResponseBody
    public String signUp(@RequestBody SignUpDto signUpDto) {
        accountService.signUp(signUpDto);
        return "signUp success";
    }

    @GetMapping("/api/account")
    @ResponseBody
    public String signIn(@RequestBody SignInDto signInDto) {
        accountService.signIn(signInDto);
        return "signIn success";
    }
}
