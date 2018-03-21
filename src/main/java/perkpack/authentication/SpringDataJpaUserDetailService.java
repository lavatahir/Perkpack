package perkpack.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import perkpack.models.Account;
import perkpack.repositories.AccountRepository;

@Component
public class SpringDataJpaUserDetailService implements UserDetailsService {

    @Autowired
    private AccountRepository userRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account user = this.userRepository.findByEmail(email);

        CustomUserDetails userDetails  = new CustomUserDetails(
                user.getEmail(),
                user.getPassword(),
                AuthorityUtils.NO_AUTHORITIES);

        userDetails.setId(user.getId());

        return userDetails;
    }
}
