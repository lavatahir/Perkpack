package perkpack.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import perkpack.models.Card;
import perkpack.repositories.CardRepository;
import javax.validation.Valid;

@RestController
@RequestMapping("cards")
public class CardController {

    private final CardRepository cardRepository;

    @Autowired
    public CardController(CardRepository cardRepository){
        this.cardRepository = cardRepository;
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
}
