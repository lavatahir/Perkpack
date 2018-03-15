package perkpack.controllers;

import org.junit.After;
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
import perkpack.models.Category;
import perkpack.models.Perk;
import perkpack.repositories.CategoryRepository;
import perkpack.repositories.PerkRepository;

import java.nio.charset.Charset;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppBoot.class)
@WebAppConfiguration
public class PerkRestControllerTest {

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

    private Category testCategory = new Category("None");
    private Perk testPerk = new Perk("10% off Coffee", "This is a description", testCategory);

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        categoryRepository.save(testCategory);
        perkRepository.save(testPerk);
    }

    @After
    public void tearDown() {
        Perk toRemove = perkRepository.findByName("10% off Coffee");
        Category catToRemove = categoryRepository.findByName("None");
        perkRepository.delete(toRemove.getId());
        categoryRepository.delete(catToRemove.getId());
    }

    @Test
    public void getValidPerkTest() throws Exception {
        mockMvc.perform(get("/perks/" + testPerk.getId())).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.name", is(testPerk.getName()))).
                andExpect(jsonPath("$.description", is(testPerk.getDescription())));
    }

    @Test
    public void createValidPerkTest() throws Exception {
        String perkName = "Test Perk";
        String perkDescription = "This is a perk.";
        String perkJson = "{\"name\": \"" + perkName + "\", \"description\": \"" + perkDescription + "\"}";

        mockMvc.perform(post("/perks").
                contentType(jsonContentType).
                content(perkJson)).
                andExpect(status().isCreated());
    }

    @Test
    public void editPerkTest() throws Exception {
        Category startingCategory = new Category("Test Category");
        Perk startingPerk = new Perk("50% off", "Everything", startingCategory);
        categoryRepository.save(startingCategory);
        startingPerk = perkRepository.save(startingPerk);

        String newName = "25% off Coffee";
        String newDescription = "Test Description 2";

        mockMvc.perform(patch("/perkedit?id=" + startingPerk.getId() + "&name=" + newName + "&description=" + newDescription)).andExpect(status().isOk());
    }
}
