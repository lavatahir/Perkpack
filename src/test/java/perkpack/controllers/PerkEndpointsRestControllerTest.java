package perkpack.controllers;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.JsonPath;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import perkpack.AppBoot;
import perkpack.models.Account;
import perkpack.models.Category;
import perkpack.models.Perk;
import perkpack.repositories.AccountRepository;
import perkpack.repositories.CategoryRepository;
import perkpack.repositories.PerkRepository;

import java.nio.charset.Charset;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppBoot.class)
@WebAppConfiguration
public class PerkEndpointsRestControllerTest {
    private MediaType jsonContentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private PerkRepository perkRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AccountRepository accountRepository;

    private Category testCategory = new Category("None");
    private Category testCategory2 = new Category("TestCategory");

    private Perk testPerk = new Perk("10% off Coffee", "This is a description", testCategory);
    private Perk testPerk2 = new Perk("20% off Coffee", "This is another description", testCategory2);
    private Perk testPerk3 = new Perk("30% off Coffee", "This is ANOTHER description", testCategory2);

    private Account testAccount = new Account("Cyrus", "Sadeghi", "cyrus@perkpack.com", "pw");
    private Account testAccount2 = new Account("Cyyrus", "Sadeghi", "cyrus2@perkpack.com", "pw");
    private Account testAccount3 = new Account("Cyyyrus", "Sadeghi", "cyrus3@perkpack.com", "pw");

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        categoryRepository.save(testCategory);
        categoryRepository.save(testCategory2);

        perkRepository.save(testPerk);
        perkRepository.save(testPerk2);
        perkRepository.save(testPerk3);

        accountRepository.save(testAccount);
        accountRepository.save(testAccount2);
        accountRepository.save(testAccount3);
    }

    @After
    public void tearDown() {
        accountRepository.delete(testAccount3);
        accountRepository.delete(testAccount2);
        accountRepository.delete(testAccount);

        perkRepository.delete(testPerk3);
        perkRepository.delete(testPerk2);
        perkRepository.delete(testPerk);

        categoryRepository.delete(testCategory2);
        categoryRepository.delete(testCategory);
    }

    @Test
    @WithMockUser(username = "cyrus@perkpack.com", password = "pw")
    public void testTopPerks() throws Exception {
        String perkUpvoteJson = "{\"name\": \"" + testPerk.getName() + "\", \"vote\": \"" + 1 + "\"}";
        String perk2UpvoteJson = "{\"name\": \"" + testPerk2.getName() + "\", \"vote\": \"" + 1 + "\"}";
        String perk3UpvoteJson = "{\"name\": \"" + testPerk3.getName() + "\", \"vote\": \"" + 1 + "\"}";

        mockMvc.perform(get("/account/authenticate")).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.firstName", is(testAccount.getFirstName()))).
                andExpect(jsonPath("$.lastName", is(testAccount.getLastName()))).
                andExpect(jsonPath("$.email", is(testAccount.getEmail())));

        mockMvc.perform(post("/perks/vote").
                contentType(jsonContentType).
                content(perkUpvoteJson)).
                andExpect(status().isOk());

        mockMvc.perform(get("/top")).
                andExpect(status().isOk()).
                andExpect(jsonPath("$[0].name", is(testPerk.getName())));

        mockMvc.perform(post("/perks/vote").
                contentType(jsonContentType).
                content(perkUpvoteJson)).
                andExpect(status().isOk());
    }
}
