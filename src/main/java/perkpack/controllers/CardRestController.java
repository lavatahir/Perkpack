package perkpack.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import perkpack.models.Account;
import perkpack.models.Card;
import perkpack.models.Perk;
import perkpack.repositories.CardRepository;
import perkpack.repositories.PerkRepository;
import perkpack.repositories.AccountRepository;

import javax.validation.Valid;

@RestController
@RequestMapping("/cards")
public class CardRestController {

    private final CardRepository cardRepository;
    private final PerkRepository perkRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public CardRestController(CardRepository cardRepository, PerkRepository perkRepository, AccountRepository accountRepository){
        this.cardRepository = cardRepository;
        this.perkRepository = perkRepository;
        this.accountRepository = accountRepository;
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
                                         @PathVariable(value = "perkName") String perkName){
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
                                                   @PathVariable(value = "perkName") String perkName){
        Card c = cardRepository.findOne(id);

        Perk perkToRemove = perkRepository.findByName(perkName);

        if(c == null || perkToRemove == null){
            return ResponseEntity.badRequest().build();
        }

        c.removePerk(perkToRemove);
        Card card = cardRepository.save(c);

        return ResponseEntity.ok().body(card);
    }

    @RequestMapping(value = "/{id}/addUser/{userID}", method = RequestMethod.PATCH)
    public ResponseEntity<Card> addUserToCard(@PathVariable("id") Long id,
                                              @PathVariable(value = "userID") Long userID){
        Card c = cardRepository.findOne(id);
        Account accountToAdd = accountRepository.findOne(userID);

        if(c == null || accountToAdd == null){
            return ResponseEntity.badRequest().build();
        }

        c.addUser(accountToAdd);

        Card card = cardRepository.save(c);

        return ResponseEntity.ok().body(card);
    }

    /*
    @RequestMapping(value = "/{id}/addUserToCardByEmail/{userEmail}", method = RequestMethod.PATCH)
    public ResponseEntity<Card> addUserToCardByEmail(@PathVariable("id") Long id,
                                              @PathVariable(value = "userEmail") String userEmail){
        Card c = cardRepository.findOne(id);
        Account userToAdd = accountRepository.findByEmail(userEmail);

        if(c == null || userToAdd == null){
            return ResponseEntity.badRequest().build();
        }

        c.addUser(userToAdd);

        Card card = cardRepository.save(c);

        return ResponseEntity.ok().body(card);
    }
    */

    @RequestMapping(value = "/{id}/removeUser/{userID}", method = RequestMethod.PATCH)
    public ResponseEntity<Card> removeUserFromCard(@PathVariable("id") Long id,
                                                   @PathVariable(value = "userID") Long userID){
        Card c = cardRepository.findOne(id);

        Account accountToRemove = accountRepository.findOne(userID);

        if(c == null || accountToRemove == null){
            return ResponseEntity.badRequest().build();
        }

        c.removeUser(accountToRemove);
        Card card = cardRepository.save(c);

        return ResponseEntity.ok().body(card);
    }

    /*
    @RequestMapping(value = "/{id}/removeUserFromCardByEmail/{userEmail}", method = RequestMethod.PATCH)
    public ResponseEntity<Card> removeUserFromCardByEmail(@PathVariable("id") Long id,
                                                   @PathVariable(value = "userEmail") String userEmail){
        Card c = cardRepository.findOne(id);

        Account userToRemove = accountRepository.findByEmail(userEmail);

        if(c == null || userToRemove == null){
            return ResponseEntity.badRequest().build();
        }

        c.removeUser(userToRemove);
        Card card = cardRepository.save(c);

        return ResponseEntity.ok().body(card);
    }
    */

}
