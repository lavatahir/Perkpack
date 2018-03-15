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
import perkpack.models.User;
import perkpack.repositories.UserRepository;

import java.nio.charset.Charset;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppBoot.class)
@WebAppConfiguration
public class UserRestControllerTest {


    private MediaType jsonContentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;


    @Autowired
    private UserRepository userRepository;

    @Before
    public void setup()
    {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void createValidUserTest() throws Exception
    {
        User user = new User("Ali", "Farah", "a@gmail.com", "password");

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String userJson = ow.writeValueAsString(user);

        mockMvc.perform(post("/user").
                contentType(jsonContentType).
                content(userJson)).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.firstName", is(user.getFirstName()))).
                andExpect(jsonPath("$.lastName", is(user.getLastName()))).
                andExpect(jsonPath("$.email", is(user.getEmail())));
    }

    @Test
    public void createUserWithEmptyFirstNameTest() throws Exception
    {
        User user = new User("", "Farah", "a@gmail.com","password");

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String userJson = ow.writeValueAsString(user);

        mockMvc.perform(post("/user").
                contentType(jsonContentType).
                content(userJson)).
                andExpect(status().isBadRequest());
    }

    @Test
    public void createUserWithTooLongFirstNameTest() throws Exception
    {
        User user = new User("asdfsdafsadfdsafsadasdfsdafsadfdsafsadasdfsdafsadfdsafsad", "Farah", "a@gmail.com","password");

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String userJson = ow.writeValueAsString(user);

        mockMvc.perform(post("/user").
                contentType(jsonContentType).
                content(userJson)).
                andExpect(status().isBadRequest());
    }

    @Test
    public void createUserWithEmptyLastNameTest() throws Exception
    {
        User user = new User("ali", "", "a@gmail.com","password");

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String userJson = ow.writeValueAsString(user);

        mockMvc.perform(post("/user").
                contentType(jsonContentType).
                content(userJson)).
                andExpect(status().isBadRequest());
    }

    @Test
    public void createUserWithTooLongLastNameTest() throws Exception
    {
        User user = new User("ali","asdfsdafsadfdsafsadasdfsdafsadfdsafsadasdfsdafsadfdsafsad", "a@gmail.com","password");

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String userJson = ow.writeValueAsString(user);

        mockMvc.perform(post("/user").
                contentType(jsonContentType).
                content(userJson)).
                andExpect(status().isBadRequest());
    }

    @Test
    public void createUserWithEmptyEmailTest() throws Exception
    {
        User user = new User("ali", "farah", "","password");

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String userJson = ow.writeValueAsString(user);

        mockMvc.perform(post("/user").
                contentType(jsonContentType).
                content(userJson)).
                andExpect(status().isBadRequest());
    }

    @Test
    public void createUserWithTooLongEmailTest() throws Exception
    {
        User user = new User("ali","Farah", "asdfsdafsadfdsafsadasdfsdafsadfdsafsadasdfsdafsadfdsafsad@gmail.com","password");

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String userJson = ow.writeValueAsString(user);

        mockMvc.perform(post("/user").
                contentType(jsonContentType).
                content(userJson)).
                andExpect(status().isBadRequest());
    }

    @Test
    public void createUserWithInvalidEmailTest() throws Exception
    {
        User user = new User("ali", "farah", "alifad","password");

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String userJson = ow.writeValueAsString(user);

        mockMvc.perform(post("/user").
                contentType(jsonContentType).
                content(userJson)).
                andExpect(status().isBadRequest());
    }

    @Test
    public void getValidUserTest() throws Exception
    {
        User user = new User("Ali", "Farah", "a@gmail.com","password");
        User createUser = userRepository.save(user);

        mockMvc.perform(get("/user/" + createUser.getId())).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.firstName", is(user.getFirstName()))).
                andExpect(jsonPath("$.lastName", is(user.getLastName()))).
                andExpect(jsonPath("$.email", is(user.getEmail())));
    }

    @Test
    public void getInValidUserTest() throws Exception
    {
        mockMvc.perform(get("/user/" + 200)).
                andExpect(status().isBadRequest());
    }

}
