package perkpack.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import perkpack.authentication.CustomUserDetails;
import perkpack.models.*;
import perkpack.repositories.*;

import javax.validation.Valid;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RestController
public class PerkRestController {
    private final PerkRepository perkRepository;
    private final CategoryRepository categoryRepository;
    private final AccountRepository accountRepository;
    private final PerkVoteRepository perkVoteRepository;
    private final CardRepository cardRepository;
    private final String categoryPath = "./categories.txt";

    @Autowired
    public PerkRestController(PerkRepository perkRepository, CategoryRepository categoryRepository, AccountRepository accountRepository, PerkVoteRepository perkVoteRepository, CardRepository cardRepository) {
        this.perkRepository = perkRepository;
        this.accountRepository = accountRepository;
        this.categoryRepository = categoryRepository;
        this.perkVoteRepository = perkVoteRepository;
        this.cardRepository = cardRepository;
        this.setupCategories(categoryPath);
    }

    private void setupCategories(String filePath) {
        ArrayList<Category> fileCategories = new ArrayList<Category>();
        Path file = Paths.get(filePath);

        // Create list of categories from file
        try (InputStream in = new BufferedInputStream(Files.newInputStream(file))) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line = null;

            while ((line = reader.readLine()) != null) {
                Category lineCategory = new Category(line);
                fileCategories.add(lineCategory);
            }
        } catch (IOException e) {
            System.out.println(e.toString());
        }

        Iterable<Category> dbCategories = categoryRepository.findAll();
        ArrayList<Category> dbCategoryList = new ArrayList<Category>();

        // Create list of categories in database
        for (Category category : dbCategories) {
            dbCategoryList.add(category);
        }

        // Insert difference in categories to database
        fileCategories.removeAll(dbCategoryList);

        categoryRepository.save(fileCategories);
    }

    @RequestMapping(value = "/perks/vote", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity changeScore(@RequestBody PerkVote perkVote) {
        Perk perkInRepository = perkRepository.findByName(perkVote.getName());
        List<PerkVote> existingPerkVotes = perkVoteRepository.findByName(perkInRepository.getName());
        Account account = getUser();

        if (perkInRepository == null) {
            return ResponseEntity.badRequest().build();
        }

        perkVote.setCategory(perkInRepository.getCategory());

        for (PerkVote vote : existingPerkVotes) {
            if (vote.getAccount().equals(account)) {
                perkInRepository.vote(perkVote, account);

                accountRepository.save(account);
                perkVoteRepository.delete(vote);

                return ResponseEntity.ok().body(perkRepository.findByName(perkVote.getName()));
            }
        }

        if (perkInRepository.vote(perkVote, account)) {
            perkVoteRepository.save(perkVote);

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

    @RequestMapping(value = "/addCardOnPerkCreation/{cardID}", method = RequestMethod.PATCH)
    public ResponseEntity<Card> addPerkToCardOnPerkCreation(@Valid @RequestBody Perk perk,
                                              @PathVariable(value = "cardID") Long cardID){
        Card c = cardRepository.findOne(cardID);

        if(c == null){
            return ResponseEntity.badRequest().build();
        }

        perkRepository.save(perk);
        c.addPerk(perk);

        Card card = cardRepository.save(c);
//        Perk p = perkRepository.findOne(perk.getId());
//        p.setCardPerkBelongsTo(card);
//        perkRepository.save(p);
        return ResponseEntity.ok().body(card);
    }

    private Account getUser() {
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
