package perkpack.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import perkpack.models.Card;
import perkpack.repositories.CardRepository;

@Controller
public class CardController {

    private final CardRepository cardRepository;
    @Autowired
    public CardController(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @GetMapping("/card")
    public String cardForm(Model model) {
        model.addAttribute("card", new Card());
        Iterable<Card> cards = cardRepository.findAll();
        model.addAttribute("cards", cards);
        return "create-card-form";
    }

    @PostMapping("/card")
    public String cardFormSubmit(@ModelAttribute Card card, Model model) {
        if (cardRepository.findByName(card.getName()) == null){
            cardRepository.save(card);
        }
        return cardForm(model);
    }
}
