package perkpack.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import perkpack.models.Category;
import perkpack.models.Perk;
import perkpack.models.PerkScoreChange;
import perkpack.repositories.CategoryRepository;
import perkpack.repositories.PerkRepository;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

@RestController
public class PerkRestController {
    private final PerkRepository perkRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public PerkRestController(PerkRepository perkRepository, CategoryRepository categoryRepository) {
        this.perkRepository = perkRepository;
        this.categoryRepository = categoryRepository;
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

    @RequestMapping(value = "/score", method = RequestMethod.POST)
    @ResponseBody
    public void changeScore(@RequestBody PerkScoreChange scoreChange) {
        Perk perkInRepository = perkRepository.findByName(scoreChange.getName());

        if (perkInRepository == null) {
            return;
        }

        perkInRepository.setScore(scoreChange.getScore());

        perkRepository.save(perkInRepository);
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
}
