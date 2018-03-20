package perkpack.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import perkpack.AppBoot;
import perkpack.models.Account;
import perkpack.models.Card;
import perkpack.models.Category;
import perkpack.models.Perk;
import perkpack.repositories.CardRepository;
import perkpack.repositories.CategoryRepository;
import perkpack.repositories.PerkRepository;
import perkpack.repositories.AccountRepository;

import java.nio.charset.Charset;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppBoot.class)
@WebAppConfiguration
public class CardRestControllerTest {

    private MediaType jsonContentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PerkRepository perkRepository;

    @Autowired
    private CategoryRepository categoryRepository;
    private Account account = new Account("Lava", "Tahir", "lavatahir@gmail.com","password");


    private Card validCard = new Card("American Express", "Credit Card");

    @Before
    public void setup()
    {

        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void createValidCardTest() throws Exception
    {

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String userJson = ow.writeValueAsString(validCard);

        mockMvc.perform(post("/cards").
                contentType(jsonContentType).
                content(userJson)).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.name", is(validCard.getName()))).
                andExpect(jsonPath("$.description", is(validCard.getDescription())));
    }

    @Test
    public void getValidCardTest() throws Exception
    {
        Card savedCard = cardRepository.save(validCard);

        mockMvc.perform(get("/cards/" + savedCard.getId())).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.name", is(savedCard.getName()))).
                andExpect(jsonPath("$.description", is(savedCard.getDescription())));
    }

    @Test
    public void editValidCardTest() throws Exception
    {
        Card savedCard = cardRepository.save(validCard);
        String newName = "Visa";
        String newDescription = "A Better Way";

        mockMvc.perform(patch("/cards/" + savedCard.getId() + "?newName=" + newName + "&newDescription=" + newDescription)).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.name", is(newName))).
                andExpect(jsonPath("$.description", is(newDescription)));

    }

  /*  These tests fail with Postgres. Need to figure out why
    @Test
    public void addPerkToCardTest() throws Exception
    {
        Card savedCard = cardRepository.save(validCard);
        Category games = new Category("Games");
        categoryRepository.save(games);
        Perk p = new Perk("Perk1", "Description for perk", games);
        perkRepository.save(p);

        mockMvc.perform(patch("/cards/"+savedCard.getId() + "/addPerk/" + p.getName())).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.perks[0].name", is(p.getName())));
    }

    @Test
    public void removePerkFromCardTest() throws Exception
    {
        Card savedCard = cardRepository.save(validCard);
        Category games = new Category("Games1");
        categoryRepository.save(games);
        Perk p = new Perk("Perk2", "Description for perk2", games);
        perkRepository.save(p);

        mockMvc.perform(patch("/cards/"+savedCard.getId() + "/addPerk/" + p.getName()));

        mockMvc.perform(patch("/cards/"+savedCard.getId() + "/removePerk/" + p.getName())).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.perks", hasSize(0)));
    }
    */

    @Test
    public void addUserToCardTest() throws Exception
    {
        Card savedCard = cardRepository.save(validCard);
        Category games = new Category("Games2");
        categoryRepository.save(games);
        Account u = new Account("Lava", "Tahir", "lava@gmail.com", "password");
        accountRepository.save(u);

        mockMvc.perform(patch("/cards/"+savedCard.getId() + "/addUser/" + u.getId())).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.accounts[0].firstName", is(u.getFirstName())));
    }

    @Test
    public void removeUserFromCardTest() throws Exception
    {
        Card savedCard = cardRepository.save(validCard);
        Category games = new Category("Games3");
        categoryRepository.save(games);
        Account u = new Account("Lava", "Tahir", "lava@gmail.com", "password");
        accountRepository.save(u);

        mockMvc.perform(patch("/cards/"+savedCard.getId() + "/addUser/" + u.getId()));

        mockMvc.perform(patch("/cards/"+savedCard.getId() + "/removeUser/" + u.getId())).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.accounts", hasSize(0)));
    }
    /*
    @Test
    public void addUserToCardByEmailTest() throws Exception
    {
        Card savedCard = cardRepository.save(validCard);
        Category games = new Category("Games4");
        categoryRepository.save(games);
        Account u = new Account("Lava", "Tahir", "lava@gmail.com", "password");
        accountRepository.save(u);

        mockMvc.perform(patch("/cards/"+savedCard.getId() + "/addUserToCardByEmail/" + u.getEmail())).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.users[0].firstName", is(u.getFirstName())));
    }

    @Test
    public void removeUserFromCardByEmailTest() throws Exception
    {
        Card savedCard = cardRepository.save(validCard);
        Category games = new Category("Games5");
        categoryRepository.save(games);
        Account u = new Account("Lava", "Tahir", "lava@gmail.com", "password");
        accountRepository.save(u);

        mockMvc.perform(patch("/cards/"+savedCard.getId() + "/addUser/" + u.getId()));

        mockMvc.perform(patch("/cards/"+savedCard.getId() + "/removeUserByEmail/" + u.getEmail())).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.users", hasSize(0)));
    }
    */
}
