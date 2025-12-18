package com.mach.api.account;

import com.mach.api.test.BaseApiTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;

/**
 * Test class for Account Faker API - User creation endpoint.
 * Uses AccountApiClient to avoid code duplication.
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
}

