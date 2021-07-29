package opgg.backend.gmakersserver.security;

import opgg.backend.gmakersserver.domain.account.entity.Account;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class AccountContext extends User {
    private final Account account;

    public Account getAccount() {
        return account;
    }

    public AccountContext(Account account, Collection<? extends GrantedAuthority> authorities){
        super(account.getLoginId(), account.getPassword(), authorities);
        this.account = account;
    }
}
