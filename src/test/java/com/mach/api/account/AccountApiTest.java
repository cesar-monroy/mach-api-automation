package com.mach.api.account;

import com.mach.api.account.model.AccountAction;
import com.mach.api.account.model.AccountRequest;
import com.mach.api.test.BaseApiTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;

/**
 * Test class for Account Faker API - User creation endpoint.
 */
public class AccountApiTest extends BaseApiTest {

    private AccountApiClient accountClient;

    @Override
    @BeforeClass
    public void setUpClass() {
        super.setUpClass();
        
        // Load Bearer token from environment or use default for testing
        String bearerToken = System.getenv("ACCOUNT_API_BEARER_TOKEN") != null 
                ? System.getenv("ACCOUNT_API_BEARER_TOKEN") 
                : System.getProperty("account.api.bearer.token", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.QLhk4yMHoZgbCwvELWPcC5ll2sVj0t-Gy-eaw5vFywI");
        
        accountClient = new AccountApiClient(bearerToken);
        
        LOG.info("Account API Test setup completed");
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
}

