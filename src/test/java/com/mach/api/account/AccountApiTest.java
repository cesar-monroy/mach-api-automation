package com.mach.api.account;

import com.mach.api.account.model.AccountAction;
import com.mach.api.account.model.AccountRequest;
import com.mach.api.account.model.AccountResponse;
import com.mach.api.test.BaseApiTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.assertEquals;

/**
 * Test class for Account Faker API - User creation endpoint.
 */
public class AccountApiTest extends BaseApiTest {

    private AccountApiClient accountClient;

    @Override
    @BeforeClass
    public void setUpClass() {
        super.setUpClass();
       
        String bearerToken = System.getenv("FAKER_API_TOKEN_STG");
        if (bearerToken == null || bearerToken.isEmpty()) {
            throw new IllegalStateException(
                "FAKER_API_TOKEN_STG environment variable is required. " +
                "Please set it before running tests: export FAKER_API_TOKEN_STG=your-token-here"
            );
        }
        
        accountClient = new AccountApiClient(bearerToken);
    }

    @Test
    public void testCreateAccount() {
        accountClient.createAccount()
                .statusCode(200)
                .body(notNullValue());
    }

    @Test
    public void testCreateAccountWithCustomArgs() {
        Map<String, Object> args = new HashMap<>();
        args.put("email", "user@example.com");
        args.put("validateEmail", true);
        
        accountClient.createAccount(args)
                .statusCode(200)
                .body(notNullValue());
    }

    @Test
    public void testCreateAccountWithBuilder() {
        AccountAction action = AccountAction.builder()
                .name("createAccountAction")
                .args(new HashMap<>())
                .build();

        AccountRequest request = AccountRequest.builder()
                .actions(new AccountAction[]{action})
                .build();

        accountClient.createAccount(request)
                .statusCode(200)
                .body(notNullValue());
    }

    @Test
    public void testCreateAccountWithBuilderAndCustomArgs() {
        Map<String, Object> customArgs = new HashMap<>();
        customArgs.put("email", "builder@example.com");
        customArgs.put("validateEmail", true);
        customArgs.put("sendWelcomeEmail", false);

        AccountAction action = AccountAction.builder()
                .name("createAccountAction")
                .args(customArgs)
                .build();

        AccountRequest request = AccountRequest.builder()
                .actions(new AccountAction[]{action})
                .build();

        accountClient.createAccount(request)
                .statusCode(200)
                .body(notNullValue());
    }

    @Test
    public void testCreateAccountWithBuilderFluentStyle() {
        AccountRequest request = AccountRequest.builder()
                .actions(new AccountAction[]{
                        AccountAction.builder()
                                .name("createAccountAction")
                                .args(Map.of("email", "fluent@example.com", "validateEmail", true))
                                .build()
                })
                .build();

        accountClient.createAccount(request)
                .statusCode(200)
                .body(notNullValue());
    }

    @Test
    public void testCreateAccountEmailValidation() {
        // Arrange - Define the email to send in the request
        String expectedEmail = "test.validation@example.com";
        
        Map<String, Object> customArgs = new HashMap<>();
        customArgs.put("email", expectedEmail);
        customArgs.put("validateEmail", true);

        AccountAction action = AccountAction.builder()
                .name("createAccountAction")
                .args(customArgs)
                .build();

        AccountRequest request = AccountRequest.builder()
                .actions(new AccountAction[]{action})
                .build();

        AccountResponse response = accountClient.createAccount(request)
                .statusCode(200)
                .body(notNullValue())
                .extract()
                .body()
                .as(AccountResponse.class);

        assertEquals(response.getEmail(), expectedEmail, 
                "El email en la respuesta debe coincidir con el email enviado en el parámetro");
        
        assertThat("MachId should not be null", response.getMachId(), notNullValue());
        assertThat("DocumentNumber should not be null", response.getDocumentNumber(), notNullValue());
        assertThat("Phone should not be null", response.getPhone(), notNullValue());
    }
    
}

