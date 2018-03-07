package perkpack.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import perkpack.models.Perk;
import org.springframework.beans.factory.annotation.Autowired;
import perkpack.repositories.PerkRepository;

@Controller
public class PerkController {
    private final PerkRepository perkRepository;

    @Autowired
    public PerkController(PerkRepository perkRepository) {
        this.perkRepository = perkRepository;
    }

    @GetMapping("/perk")
    private String perkForm(Model model) {
        Iterable<Perk> perkList = perkRepository.findAll();

        model.addAttribute("perks", perkList);
        model.addAttribute("createdPerk", new Perk());

        return "perk";
    }

    @PostMapping("/perk")
    public String perkCreation(@ModelAttribute Perk createdPerk, Model model) {
        if (perkRepository.findByName(createdPerk.getName()) == null) {
            System.out.println(perkRepository.save(createdPerk));
        }

        return perkForm(model);
    }
}