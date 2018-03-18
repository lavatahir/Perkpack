package perkpack.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import perkpack.models.Card;
import perkpack.models.Perk;
import perkpack.repositories.CardRepository;
import perkpack.repositories.PerkRepository;

import javax.validation.Valid;

@RestController
@RequestMapping("/cards")
public class CardRestController {

    private final CardRepository cardRepository;
    private final PerkRepository perkRepository;

    @Autowired
    public CardRestController(CardRepository cardRepository, PerkRepository perkRepository){
        this.cardRepository = cardRepository;
        this.perkRepository = perkRepository;
    }


    @PostMapping
    public Card createCard(@Valid @RequestBody Card card){
        return cardRepository.save(card);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Card> getCard(@PathVariable("id") Long id){
        Card c = cardRepository.findOne(id);

        if(c == null){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().body(c);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<Card> editCard(@PathVariable("id") Long id,
                                         @RequestParam(value = "newName", required = false) String newName,
                                         @RequestParam(value = "newDescription", required = false) String newDescription){
        Card c = cardRepository.findOne(id);

        if(c == null){
            return ResponseEntity.badRequest().build();
        }

        if(!newName.isEmpty() || newName != null){
            c.setName(newName);
        }

        if(!newDescription.isEmpty() || newDescription != null){
            c.setDescription(newDescription);
        }

        Card card = cardRepository.save(c);

        return ResponseEntity.ok().body(card);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Iterable<Card>> getCards(){
        Iterable<Card> cards = cardRepository.findAll();
        if(cards == null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().body(cards);
    }

    @RequestMapping(value = "/{id}/addPerk/{perkName}", method = RequestMethod.PATCH)
    public ResponseEntity<Card> addPerkToCard(@PathVariable("id") Long id,
                                         @PathVariable(value = "perkName", required = false) String perkName){
        Card c = cardRepository.findOne(id);
        Perk perkToAdd = perkRepository.findByName(perkName);

        if(c == null || perkToAdd == null){
            return ResponseEntity.badRequest().build();
        }

        c.addPerk(perkToAdd);

        Card card = cardRepository.save(c);

        return ResponseEntity.ok().body(card);
    }

    @RequestMapping(value = "/{id}/removePerk/{perkName}", method = RequestMethod.PATCH)
    public ResponseEntity<Card> removePerkFromCard(@PathVariable("id") Long id,
                                                   @PathVariable(value = "perkName", required = false) String perkName){
        Card c = cardRepository.findOne(id);

        Perk perkToRemove = perkRepository.findByName(perkName);

        if(c == null || perkToRemove == null){
            return ResponseEntity.badRequest().build();
        }

        c.removePerk(perkToRemove);
        Card card = cardRepository.save(c);

        return ResponseEntity.ok().body(card);
    }
}
