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
import perkpack.models.Card;
import perkpack.models.User;
import perkpack.repositories.CardRepository;
import perkpack.repositories.UserRepository;

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
    private UserRepository userRepository;


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

        //for some reason, I cant do newName or newDescription separately
        mockMvc.perform(patch("/cards/" + savedCard.getId() + "?newName=" + newName + "&newDescription=" + newDescription)).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.name", is(newName))).
                andExpect(jsonPath("$.description", is(newDescription)));

    }
}
