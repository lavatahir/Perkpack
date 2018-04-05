package perkpack.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import perkpack.authentication.CustomUserDetails;
import perkpack.models.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import perkpack.repositories.AccountRepository;

import javax.validation.Valid;

@RestController
@RequestMapping("account")
public class AccountRestController {

    @Autowired
    private AccountRepository accountRepository;

    @PostMapping
    public Account createUser(@Valid @RequestBody Account account)
    {
        return accountRepository.save(account);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Account> getUser(@PathVariable("id") Long id)
    {
        Account account = accountRepository.findOne(id);

        if(account == null)
        {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().body(account);
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.GET)
    public ResponseEntity<Account> checkIfUserIsAuthenticated()
    {
        Account loggedInAccount = getLoggedInAccount();
        if(loggedInAccount == null)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok().body(loggedInAccount);
    }

    private Account getLoggedInAccount() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            // If someone is logged in auth.getPrincipal() returns a CustomUserDetails object
            if ((auth.getPrincipal() instanceof CustomUserDetails) || (auth.getPrincipal() instanceof User)) {
                String email = ((org.springframework.security.core.userdetails.User) auth.getPrincipal()).getUsername();
                return accountRepository.findByEmail(email);
            }

            // Otherwise it returns a String
            return null;
        }

        return null;
    }
}
