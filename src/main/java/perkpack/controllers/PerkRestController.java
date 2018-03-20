package perkpack.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import perkpack.models.Category;
import perkpack.models.Perk;
import perkpack.models.PerkVote;
import perkpack.models.User;
import perkpack.repositories.CategoryRepository;
import perkpack.repositories.PerkRepository;
import perkpack.repositories.PerkVoteRepository;
import perkpack.repositories.UserRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class PerkRestController {
    private final PerkRepository perkRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final PerkVoteRepository perkVoteRepository;

    @Autowired
    public PerkRestController(PerkRepository perkRepository, CategoryRepository categoryRepository, UserRepository userRepository, PerkVoteRepository perkVoteRepository) {
        this.perkRepository = perkRepository;
        this.userRepository = userRepository;
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
        User user = getUser();

        if (perkInRepository == null) {
            return ResponseEntity.badRequest().build();
        }

        if(perkInRepository.vote(perkVote, user))
        {
            perkVoteRepository.save(perkVote);
            userRepository.save(user);
            return ResponseEntity.ok().body(perkRepository.save(perkInRepository));

        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("User already voted");
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

    private User getUser()
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = ((org.springframework.security.core.userdetails.User)auth.getPrincipal()).getUsername();
        return userRepository.findByEmail(email);
    }
}
