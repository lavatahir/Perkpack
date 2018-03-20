package perkpack.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import perkpack.models.Account;
import perkpack.models.Category;
import perkpack.models.Perk;
import perkpack.models.PerkVote;
import perkpack.repositories.AccountRepository;
import perkpack.repositories.CategoryRepository;
import perkpack.repositories.PerkRepository;
import perkpack.repositories.PerkVoteRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class PerkRestController {
    private final PerkRepository perkRepository;
    private final CategoryRepository categoryRepository;
    private final AccountRepository accountRepository;
    private final PerkVoteRepository perkVoteRepository;

    @Autowired
    public PerkRestController(PerkRepository perkRepository, CategoryRepository categoryRepository, AccountRepository accountRepository, PerkVoteRepository perkVoteRepository) {
        this.perkRepository = perkRepository;
        this.accountRepository = accountRepository;
        this.categoryRepository = categoryRepository;
        this.perkVoteRepository = perkVoteRepository;
        this.setupCategories();
    }

    private void setupCategories() {
        Path file = Paths.get("./categories.txt");

        try (InputStream in = new BufferedInputStream(Files.newInputStream(file))) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line = null;

            while ((line = reader.readLine()) != null) {
                Category lineCategory = new Category(line);
                categoryRepository.save(lineCategory);
            }
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    @RequestMapping(value = "/perks/vote", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity changeScore(@RequestBody PerkVote perkVote) {
        Perk perkInRepository = perkRepository.findByName(perkVote.getName());
        Account account = getUser();

        if (perkInRepository == null) {
            return ResponseEntity.badRequest().build();
        }

        if(perkInRepository.vote(perkVote, account))
        {
            perkVoteRepository.save(perkVote);
            accountRepository.save(account);
            return ResponseEntity.ok().body(perkRepository.save(perkInRepository));

        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Account already voted");
    }

    @RequestMapping(value = "/perkedit", method = RequestMethod.PATCH)
    public ResponseEntity<Perk> editPerk(@RequestParam(value = "id") Long id, @RequestParam(value = "name", required = false) String name,
                                          @RequestParam(value = "description", required = false) String description) {
        Perk perkInRepository = perkRepository.findOne(id);

        if (perkInRepository == null) {
            return ResponseEntity.badRequest().build();
        }

        if (name != null && !name.isEmpty()) {
            perkInRepository.setName(name);
        }

        if (description != null && !description.isEmpty()) {
            perkInRepository.setDescription(description);
        }

        Perk updatedPerk = perkRepository.save(perkInRepository);

        return ResponseEntity.ok().body(updatedPerk);
    }

    private Account getUser()
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = ((org.springframework.security.core.userdetails.User)auth.getPrincipal()).getUsername();
        return accountRepository.findByEmail(email);
    }
}
