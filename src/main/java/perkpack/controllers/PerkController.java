package perkpack.controllers;

import perkpack.models.Perk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import perkpack.repositories.PerkRepository;

@RestController
public class PerkController {
    private final PerkRepository perkRepository;

    @Autowired
    public PerkController(PerkRepository perkRepository) {
        this.perkRepository = perkRepository;
    }

    @RequestMapping(method = RequestMethod.POST)
    public Perk addPerk(@PathVariable String perkName, @PathVariable String description) {
        Perk perkInRepository = perkRepository.findByName(perkName);

        if (perkInRepository != null) {
            return perkInRepository;
        }

        perkInRepository = new Perk(perkName, description);
        perkRepository.save(perkInRepository);
        return perkInRepository;
    }
}