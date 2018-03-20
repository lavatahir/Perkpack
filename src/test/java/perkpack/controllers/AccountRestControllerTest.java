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
import perkpack.repositories.AccountRepository;

import java.nio.charset.Charset;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppBoot.class)
@WebAppConfiguration
public class AccountRestControllerTest {


    private MediaType jsonContentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;


    @Autowired
    private AccountRepository accountRepository;

    @Before
    public void setup()
    {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void createValidUserTest() throws Exception
    {
        Account account = new Account("Ali", "Farah", "a@gmail.com", "password");

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String userJson = ow.writeValueAsString(account);

        mockMvc.perform(post("/account").
                contentType(jsonContentType).
                content(userJson)).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.firstName", is(account.getFirstName()))).
                andExpect(jsonPath("$.lastName", is(account.getLastName()))).
                andExpect(jsonPath("$.email", is(account.getEmail())));
    }

    @Test
    public void createUserWithEmptyFirstNameTest() throws Exception
    {
        Account account = new Account("", "Farah", "a@gmail.com","password");

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String userJson = ow.writeValueAsString(account);

        mockMvc.perform(post("/account").
                contentType(jsonContentType).
                content(userJson)).
                andExpect(status().isBadRequest());
    }

    @Test
    public void createUserWithTooLongFirstNameTest() throws Exception
    {
        Account account = new Account("asdfsdafsadfdsafsadasdfsdafsadfdsafsadasdfsdafsadfdsafsad", "Farah", "a@gmail.com","password");

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String userJson = ow.writeValueAsString(account);

        mockMvc.perform(post("/account").
                contentType(jsonContentType).
                content(userJson)).
                andExpect(status().isBadRequest());
    }

    @Test
    public void createUserWithEmptyLastNameTest() throws Exception
    {
        Account account = new Account("ali", "", "a@gmail.com","password");

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String userJson = ow.writeValueAsString(account);

        mockMvc.perform(post("/account").
                contentType(jsonContentType).
                content(userJson)).
                andExpect(status().isBadRequest());
    }

    @Test
    public void createUserWithTooLongLastNameTest() throws Exception
    {
        Account account = new Account("ali","asdfsdafsadfdsafsadasdfsdafsadfdsafsadasdfsdafsadfdsafsad", "a@gmail.com","password");

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String userJson = ow.writeValueAsString(account);

        mockMvc.perform(post("/account").
                contentType(jsonContentType).
                content(userJson)).
                andExpect(status().isBadRequest());
    }

    @Test
    public void createUserWithEmptyEmailTest() throws Exception
    {
        Account account = new Account("ali", "farah", "","password");

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String userJson = ow.writeValueAsString(account);

        mockMvc.perform(post("/account").
                contentType(jsonContentType).
                content(userJson)).
                andExpect(status().isBadRequest());
    }

    @Test
    public void createUserWithTooLongEmailTest() throws Exception
    {
        Account account = new Account("ali","Farah", "asdfsdafsadfdsafsadasdfsdafsadfdsafsadasdfsdafsadfdsafsad@gmail.com","password");

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String userJson = ow.writeValueAsString(account);

        mockMvc.perform(post("/account").
                contentType(jsonContentType).
                content(userJson)).
                andExpect(status().isBadRequest());
    }

    @Test
    public void createUserWithInvalidEmailTest() throws Exception
    {
        Account account = new Account("ali", "farah", "alifad","password");

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String userJson = ow.writeValueAsString(account);

        mockMvc.perform(post("/account").
                contentType(jsonContentType).
                content(userJson)).
                andExpect(status().isBadRequest());
    }

    @Test
    public void getValidUserTest() throws Exception
    {
        Account account = new Account("Ali", "Farah", "a@gmail.com","password");
        Account createAccount = accountRepository.save(account);

        mockMvc.perform(get("/account/" + createAccount.getId())).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.firstName", is(account.getFirstName()))).
                andExpect(jsonPath("$.lastName", is(account.getLastName()))).
                andExpect(jsonPath("$.email", is(account.getEmail())));
    }

    @Test
    public void getInValidUserTest() throws Exception
    {
        mockMvc.perform(get("/account/" + 200)).
                andExpect(status().isBadRequest());
    }

}
