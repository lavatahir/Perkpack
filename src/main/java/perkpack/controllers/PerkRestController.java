package perkpack.controllers;

import org.springframework.beans.factory.annotation.Autowired;
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
}
