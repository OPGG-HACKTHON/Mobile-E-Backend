package opgg.backend.gmakersserver.domain.account.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import opgg.backend.gmakersserver.domain.account.controller.request.SignInDto;
import opgg.backend.gmakersserver.domain.account.controller.request.SignUpDto;
import opgg.backend.gmakersserver.domain.account.entity.Account;
import opgg.backend.gmakersserver.domain.account.entity.Role;
import opgg.backend.gmakersserver.domain.account.repository.AccountRepository;
import opgg.backend.gmakersserver.error.exception.account.AccountDuplicateIdException;
import opgg.backend.gmakersserver.error.exception.account.AccountNotFoundException;
import opgg.backend.gmakersserver.error.exception.account.AccountPasswordNotMatchException;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signUp(SignUpDto signUpDto) {
        accountRepository.findByUsername(signUpDto.getUsername()).ifPresent(account -> {
            throw new AccountDuplicateIdException(account.getUsername());
        });
        accountRepository.save(Account.builder()
                                        .username(signUpDto.getUsername())
                                        .password(passwordEncoder.encode(signUpDto.getPassword()))
                                        .role(Role.ROLE_USER)
                                        .activated(true)
                                        .build());
    }

    @Transactional(readOnly = true)
    public Account login(SignInDto signInDto){
        Account account = accountRepository.findByUsername(signInDto.getUsername())
                .orElseThrow(AccountNotFoundException::new);
        if (!passwordEncoder.matches(signInDto.getPassword(), account.getPassword())) {
            throw new AccountPasswordNotMatchException();
        }
        return account;
    }

}
