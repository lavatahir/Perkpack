package perkpack.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import perkpack.models.Account;
import perkpack.repositories.AccountRepository;

@Component
public class SpringDataJpaUserDetailService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account = this.accountRepository.findByEmail(email);

        return new org.springframework.security.core.userdetails.User(
                account.getEmail(),
                account.getPassword(),
                AuthorityUtils.NO_AUTHORITIES);
    }
}
