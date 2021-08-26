package opgg.backend.gmakersserver.domain.account.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import opgg.backend.gmakersserver.domain.account.controller.request.SignInRequest;
import opgg.backend.gmakersserver.domain.account.controller.request.SignUpRequest;
import opgg.backend.gmakersserver.domain.account.entity.Account;
import opgg.backend.gmakersserver.domain.account.entity.Role;
import opgg.backend.gmakersserver.domain.account.repository.AccountRepository;
import opgg.backend.gmakersserver.error.exception.account.AccountDuplicateIdException;
import opgg.backend.gmakersserver.error.exception.account.AccountNotFoundException;
import opgg.backend.gmakersserver.error.exception.account.AccountPasswordNotMatchException;
import opgg.backend.gmakersserver.infra.jwt.JjwtService;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final JjwtService jjwtService;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signUp(SignUpRequest signUpRequest) {
        accountRepository.findByUsername(signUpRequest.getUsername()).ifPresent(account -> {
            throw new AccountDuplicateIdException(account.getUsername());
        });
        accountRepository.save(Account.builder()
                                        .username(signUpRequest.getUsername())
                                        .password(passwordEncoder.encode(signUpRequest.getPassword()))
                                        .role(Role.ROLE_USER)
                                        .activated(true)
                                        .build());
    }

    @Transactional(readOnly = true)
    public String getJwtToken(SignInRequest signInRequest){
        Account account = accountRepository.findByUsername(signInRequest.getUsername())
                .orElseThrow(AccountNotFoundException::new);
        if (!passwordEncoder.matches(signInRequest.getPassword(), account.getPassword())) {
            throw new AccountPasswordNotMatchException();
        }
        return jjwtService.createToken(account);
    }

}
