package perkpack.controllers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
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
import perkpack.repositories.AccountRepository;

import javax.transaction.Transactional;
import java.nio.charset.Charset;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppBoot.class)
@WebAppConfiguration
@Transactional
public class AccountRestControllerTest {


    private MediaType jsonContentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    private static final String firstName = "Ali";
    private static final String lastName = "farah";
    private static final String email = "a@gmail.com";
    private static final String password = "test123";

    @Autowired
    private WebApplicationContext webApplicationContext;


    @Autowired
    private AccountRepository accountRepository;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void createValidUserTest() throws Exception {
        Account account = new Account(firstName, lastName, email, password);

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
    public void createUserWithEmptyFirstNameTest() throws Exception {
        Account account = new Account("", lastName, email, password);

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String userJson = ow.writeValueAsString(account);

        mockMvc.perform(post("/account").
                contentType(jsonContentType).
                content(userJson)).
                andExpect(status().isBadRequest());
    }

    @Test
    public void createUserWithTooLongFirstNameTest() throws Exception {
        Account account = new Account("asdfsdafsadfdsafsadasdfsdafsadfdsafsadasdfsdafsadfdsafsad", lastName, email, password);

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String userJson = ow.writeValueAsString(account);

        mockMvc.perform(post("/account").
                contentType(jsonContentType).
                content(userJson)).
                andExpect(status().isBadRequest());
    }

    @Test
    public void createUserWithEmptyLastNameTest() throws Exception {
        Account account = new Account(firstName, "", email, password);

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String userJson = ow.writeValueAsString(account);

        mockMvc.perform(post("/account").
                contentType(jsonContentType).
                content(userJson)).
                andExpect(status().isBadRequest());
    }

    @Test
    public void createUserWithTooLongLastNameTest() throws Exception {
        Account account = new Account(firstName, "fdsfffffffffffffffffffffffffffffffffffffffffffffffffffdsfdsfsdfsdfsdfsdfsdfsdfsdfdsfsdfdsfdsfsdfsdfdsfds", email, password);

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String userJson = ow.writeValueAsString(account);

        mockMvc.perform(post("/account").
                contentType(jsonContentType).
                content(userJson)).
                andExpect(status().isBadRequest());
    }

    @Test
    public void createUserWithEmptyEmailTest() throws Exception {
        Account account = new Account(firstName, lastName, "", password);

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String userJson = ow.writeValueAsString(account);

        mockMvc.perform(post("/account").
                contentType(jsonContentType).
                content(userJson)).
                andExpect(status().isBadRequest());
    }

    @Test
    public void createUserWithTooLongEmailTest() throws Exception {
        Account account = new Account(firstName, lastName, "afdsfdfdsafffffffffffffffffffjksdfkdjslfjdsljfklsdjflkjsdlfkjdslkfjlsdkjflsdkjflskdjflksdjlfjsdkfjlsjflsdjflsdjflksjdflks@gmail.com", password);

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String userJson = ow.writeValueAsString(account);

        mockMvc.perform(post("/account").
                contentType(jsonContentType).
                content(userJson)).
                andExpect(status().isBadRequest());
    }

    @Test
    public void createUserWithInvalidEmailTest() throws Exception {
        Account account = new Account(firstName, lastName, "fsdfsdfsd", password);

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String userJson = ow.writeValueAsString(account);

        mockMvc.perform(post("/account").
                contentType(jsonContentType).
                content(userJson)).
                andExpect(status().isBadRequest());
    }

    @Test
    public void getValidUserTest() throws Exception {
        Account account = new Account(firstName, lastName, email, password);
        Account createAccount = accountRepository.save(account);

        mockMvc.perform(get("/account/" + createAccount.getId())).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.firstName", is(account.getFirstName()))).
                andExpect(jsonPath("$.lastName", is(account.getLastName()))).
                andExpect(jsonPath("$.email", is(account.getEmail())));
    }

    @Test
    public void getInValidUserTest() throws Exception {
        mockMvc.perform(get("/account/" + 200)).
                andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = email, password = password)
    public void getLoggedInAccountWithSomeoneLoggedInTest() throws Exception {
        Account account = new Account(firstName, lastName, email, password);
        accountRepository.save(account);

        mockMvc.perform(get("/account/authenticate")).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.firstName", is(account.getFirstName()))).
                andExpect(jsonPath("$.lastName", is(account.getLastName()))).
                andExpect(jsonPath("$.email", is(account.getEmail())));
    }

    @Test
    public void getLoggedInAccountWithNoOneLoggedInTest() throws Exception {
        mockMvc.perform(get("/account/authenticate")).
                andExpect(status().isUnauthorized());
    }

}
