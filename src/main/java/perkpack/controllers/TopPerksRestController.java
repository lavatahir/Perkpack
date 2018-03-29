package perkpack.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import perkpack.models.Perk;
import perkpack.repositories.PerkRepository;

import java.util.List;

@RestController
@RequestMapping("top")
public class TopPerksRestController {
    @Autowired
    private PerkRepository perkRepository;

    @GetMapping
    public List<Perk> getPerks() {
        List<Perk> topPerks = perkRepository.findAllByOrderByScoreDesc();

        return topPerks;
    }
}
