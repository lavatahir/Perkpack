package perkpack.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import perkpack.models.Account;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private SpringDataJpaUserDetailService userDetailService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userDetailService).passwordEncoder(Account.PASSWORD_ENCODER);
    }

    protected  void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/perks/vote").authenticated()
                    .anyRequest().permitAll()
                    .and()
                .formLogin()
                    .defaultSuccessUrl("/", true)
                    .permitAll()
                    .and()
                .httpBasic()
                    .and()
                .csrf().disable()
                .logout()
                .logoutSuccessUrl("/")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID");
    }
}
