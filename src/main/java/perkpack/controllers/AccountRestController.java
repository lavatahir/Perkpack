package perkpack.controllers;

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

}
