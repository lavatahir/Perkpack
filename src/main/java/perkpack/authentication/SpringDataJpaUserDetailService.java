package perkpack.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import perkpack.models.User;
import perkpack.repositories.UserRepository;

@Component
public class SpringDataJpaUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = this.userRepository.findByEmail(email);

        CustomUserDetails userDetails  = new CustomUserDetails(
                user.getEmail(),
                user.getPassword(),
                AuthorityUtils.NO_AUTHORITIES);

        userDetails.setId(user.getId());

        return userDetails;
    }
}
