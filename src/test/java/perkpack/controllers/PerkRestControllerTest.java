package perkpack.controllers;

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
import perkpack.models.Perk;
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
public class PerkRestControllerTest {

    private MediaType jsonContentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private PerkRepository perkRepository;

    private Perk testPerk = new Perk("10% off Coffee", "This is a description");

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        perkRepository.save(testPerk);
    }

    @Test
    public void getValidPerkTest() throws Exception {
        Perk savedPerk = perkRepository.save(testPerk);

        mockMvc.perform(get("/perks/" + savedPerk.getId())).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.name", is(savedPerk.getName()))).
                andExpect(jsonPath("$.description", is(savedPerk.getDescription())));
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
}
