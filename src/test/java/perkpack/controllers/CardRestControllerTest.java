package perkpack.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
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
    private static Account user;
    private static Account user2;
    private static Account user3;
    private static Perk perk;
    private static Category games;
    private static Card card;

    @Before
    public void setup()
    {
        user = new Account("Lava", "Tahir", "lava@gmail.com", "password");
        user = accountRepository.save(user);

        games = new Category("Games1");
        games = categoryRepository.save(games);

        perk = new Perk("Perk1", "Description for perk", games);
        perk = perkRepository.save(perk);

        card = new Card("American Express", "Credit Card");
        card = cardRepository.save(card);

        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @After
    public void tearDown()
    {
        accountRepository.delete(user);
        perkRepository.delete(perk);
        categoryRepository.delete(games);
        cardRepository.delete(card);
    }

    @Test
    public void createValidCardTest() throws Exception
    {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String userJson = ow.writeValueAsString(card);

        mockMvc.perform(post("/cards").
                contentType(jsonContentType).
                content(userJson)).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.name", is(card.getName()))).
                andExpect(jsonPath("$.description", is(card.getDescription())));
    }
    @Test
    public void getValidCardTest() throws Exception
    {
        mockMvc.perform(get("/cards/" + card.getId())).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.name", is(card.getName()))).
                andExpect(jsonPath("$.description", is(card.getDescription())));
    }
    @Test
    public void editValidCardTest() throws Exception
    {
        String newName = "Visa";
        String newDescription = "A Better Way";

        mockMvc.perform(patch("/cards/" + card.getId() + "?newName=" + newName + "&newDescription=" + newDescription)).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.name", is(newName))).
                andExpect(jsonPath("$.description", is(newDescription)));
    }

    @Test
    @WithMockUser(username = "lava@gmail.com", password = "password")
    public void addPerkToCardTest() throws Exception
    {
        mockMvc.perform(patch("/cards/"+card.getId() + "/addPerk/" + perk.getName())).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.perks[0].name", is(perk.getName())));
        Card c = cardRepository.findOne(card.getId());
        Perk p = perkRepository.findOne(perk.getId());
    }

    @Test
    @WithMockUser(username = "lava@gmail.com", password = "password")
    public void removePerkFromCardTest() throws Exception
    {
        mockMvc.perform(patch("/cards/"+card.getId() + "/addPerk/" + perk.getName()));

        mockMvc.perform(patch("/cards/"+card.getId() + "/removePerk/" + perk.getName())).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.perks", hasSize(0)));
    }

    @Test
    @WithMockUser(username = "ali@gmail.com", password = "password")
    public void addUserToCardTest() throws Exception
    {
        user2 = new Account("Ali", "Farah", "ali@gmail.com", "password");
        user2 = accountRepository.save(user2);
        mockMvc.perform(patch("/cards/" + card.getId() + "/addUser")).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.accounts[0].firstName", is(user2.getFirstName())));

        mockMvc.perform(patch("/cards/" + card.getId() + "/removeUser/")).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.accounts", hasSize(0)));

        accountRepository.delete(user2);
    }

    @Test
    @WithMockUser(username = "guy@gmail.com", password = "password")
    public void removeUserFromCardTest() throws Exception
    {
        user3 = new Account("Some", "Guy", "guy@gmail.com", "password");
        user3 = accountRepository.save(user3);
        mockMvc.perform(patch("/cards/"+card.getId() + "/addUser"));

        mockMvc.perform(patch("/cards/"+card.getId() + "/removeUser/")).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.accounts", hasSize(0)));

        accountRepository.delete(user3);
    }
}
