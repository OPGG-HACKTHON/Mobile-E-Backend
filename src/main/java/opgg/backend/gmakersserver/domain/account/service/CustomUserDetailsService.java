package opgg.backend.gmakersserver.domain.account.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import opgg.backend.gmakersserver.domain.account.entity.Account;
import opgg.backend.gmakersserver.domain.account.repository.AccountRepository;
import opgg.backend.gmakersserver.error.exception.account.AccountNotFoundException;

@Component("userDetailsService")
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final AccountRepository accountRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(final String username) {
		return accountRepository.findOneWithRoleByUsername(username)
				.map(account -> createUser(username, account))
				.orElseThrow(AccountNotFoundException::new);
	}

	private User createUser(String username, Account user) {
		if (!user.isActivated()) {
			throw new RuntimeException(username + " -> 활성화되어 있지 않습니다.");
		}
		return new User(user.getUsername(),
				user.getPassword(),
				new ArrayList<>(List.of(new SimpleGrantedAuthority(String.valueOf(user.getRole())))));
	}

}