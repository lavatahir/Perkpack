package perkpack.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import perkpack.models.Account;
import perkpack.models.Perk;
import perkpack.repositories.AccountRepository;
import perkpack.repositories.PerkRepository;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("recommended")
public class RecommendedPerksRestController {
    @Autowired
    private PerkRepository perkRepository;

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping
    public List<Perk> getRecommendedPerks() {
        Account currentUser = getUser();

        if (currentUser == null || currentUser.getTopCategory() == null) {
            return new ArrayList<Perk>();
        }
        
        return perkRepository.findByCategory(currentUser.getTopCategory());
    }

    private Account getUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = ((org.springframework.security.core.userdetails.User)auth.getPrincipal()).getUsername();
        return accountRepository.findByEmail(email);
    }
}
