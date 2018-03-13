package perkpack.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import perkpack.models.Perk;
import perkpack.models.PerkScoreChange;
import perkpack.repositories.PerkRepository;

@RestController
public class PerkRestController {
    private final PerkRepository perkRepository;

    @Autowired
    public PerkRestController(PerkRepository perkRepository) {
        this.perkRepository = perkRepository;
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
        System.out.println(id + " " + name + " " + description);

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
