package perkpack.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import perkpack.models.Perk;
import org.springframework.beans.factory.annotation.Autowired;
import perkpack.repositories.PerkRepository;

@RestController
@RequestMapping("/perk")
public class PerkController {
    private final PerkRepository perkRepository;

    @Autowired
    public PerkController(PerkRepository perkRepository) {
        this.perkRepository = perkRepository;
    }

    @RequestMapping(method = RequestMethod.POST)
    public Perk addPerk(@RequestParam(value = "name") String name, @RequestParam(value = "description") String description) {
        Perk perkInRepository = perkRepository.findByName(name);

        if (perkInRepository != null) {
            return perkInRepository;
        }

        perkInRepository = new Perk(name, description);
        perkRepository.save(perkInRepository);
        return perkInRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Perk getPerk(@RequestParam(value="name") String name) {
        Perk perk = perkRepository.findByName(name);

        if (perk == null) {
            return null;
        }

        return perk;
    }
}