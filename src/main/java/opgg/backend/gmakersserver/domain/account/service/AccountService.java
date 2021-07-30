package opgg.backend.gmakersserver.domain.account.service;

import lombok.RequiredArgsConstructor;
import opgg.backend.gmakersserver.domain.account.dto.SignInDto;
import opgg.backend.gmakersserver.domain.account.dto.SignUpDto;
import opgg.backend.gmakersserver.domain.account.entity.Account;
import opgg.backend.gmakersserver.domain.account.repository.AccountRepository;
import opgg.backend.gmakersserver.error.exception.common.account.AccountNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signUp(SignUpDto signUpDto) {
        Account account = Account.builder()
                .loginId(signUpDto.getLoginId())
                .password(passwordEncoder.encode(signUpDto.getPassword()))
                .build();
        accountRepository.save(account);
    }

    @Transactional(readOnly = true)
    public void signIn(SignInDto signInDto) {
        accountRepository.findByLoginId(signInDto.getLoginId()).orElseThrow(AccountNotFoundException::new);
    }

}
