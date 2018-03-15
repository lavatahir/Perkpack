package perkpack.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import perkpack.models.Category;
import perkpack.models.Perk;
import org.springframework.beans.factory.annotation.Autowired;
import perkpack.repositories.CategoryRepository;
import perkpack.repositories.PerkRepository;

@Controller
public class PerkController {
    private final PerkRepository perkRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public PerkController(PerkRepository perkRepository, CategoryRepository categoryRepository) {
        this.perkRepository = perkRepository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/perk")
    private String perkForm(Model model) {
        Iterable<Perk> perkList = perkRepository.findAll();
        Iterable<Category> categoryList = categoryRepository.findAll();

        model.addAttribute("perks", perkList);
        model.addAttribute("categories", categoryList);
        model.addAttribute("createdPerk", new Perk());

        return "perk";
    }

    @PostMapping("/perk")
    public String perkCreation(@ModelAttribute Perk createdPerk, Model model) {
        if (perkRepository.findByName(createdPerk.getName()) == null) {
            perkRepository.save(createdPerk);
        }

        return perkForm(model);
    }
}