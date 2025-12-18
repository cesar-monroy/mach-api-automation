package com.mach.api.account;

import com.mach.api.account.model.AccountAction;
import com.mach.api.account.model.AccountRequest;
import com.mach.api.client.RestClient;
import com.mach.api.test.BaseApiTest;
import io.restassured.http.Method;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;

/**
 * Test class for Account Faker API - User creation endpoint.
 */
public class AccountApiTest extends BaseApiTest {

    private static final String BEARER_TOKEN = System.getenv("ACCOUNT_API_BEARER_TOKEN") != null 
            ? System.getenv("ACCOUNT_API_BEARER_TOKEN") 
            : System.getProperty("account.api.bearer.token", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.QLhk4yMHoZgbCwvELWPcC5ll2sVj0t-Gy-eaw5vFywI");
    private static final String API_KEY = System.getenv("ACCOUNT_API_KEY") != null 
            ? System.getenv("ACCOUNT_API_KEY") 
            : System.getProperty("account.api.key", "ii92khXeY#q<t38W!8m{");
    private static final String BASE_URI = System.getenv("ACCOUNT_API_BASE_URI") != null 
            ? System.getenv("ACCOUNT_API_BASE_URI") 
            : System.getProperty("account.api.base.uri", "https://account-faker-api-staging-stg.soymach.com");

    @Override
    @org.testng.annotations.BeforeClass
    public void setUpClass() {
        restClient = new RestClient(BASE_URI, "", BEARER_TOKEN);
        
        LOG.info("Account API Test setup completed. Base URI: {}", BASE_URI);
    }

    @Test
    public void testCreateAccount() {
        AccountRequest request = new AccountRequest();
        AccountAction action = new AccountAction();
        action.setName("createAccountAction");
        action.setArgs(new HashMap<>());
        request.setActions(new AccountAction[]{action});

        Map<String, Object> headers = new HashMap<>();
        headers.put("X-Api-Key", API_KEY);
        headers.put("Content-Type", "application/json");

        runWebServiceWithBody(Method.POST, headers, "/account", request)
                .statusCode(200)
                .body(notNullValue());
    }

    @Test
    public void testCreateAccountWithCustomArgs() {
        AccountRequest request = new AccountRequest();
        AccountAction action = new AccountAction();
        action.setName("createAccountAction");
        
        Map<String, Object> args = new HashMap<>();
        args.put("email", "user@example.com");
        args.put("validateEmail", true);
        action.setArgs(args);
        
        request.setActions(new AccountAction[]{action});

        Map<String, Object> headers = new HashMap<>();
        headers.put("X-Api-Key", API_KEY);
        headers.put("Content-Type", "application/json");

        runWebServiceWithBody(Method.POST, headers, "/account", request)
                .statusCode(200)
                .body(notNullValue());
    }
}

