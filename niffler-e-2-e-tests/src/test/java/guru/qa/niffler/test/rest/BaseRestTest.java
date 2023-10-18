package guru.qa.niffler.test.rest;

import guru.qa.niffler.api.AuthServiceClient;
import guru.qa.niffler.jupiter.annotation.RestTest;

import java.io.IOException;

@RestTest
public abstract class BaseRestTest {

    private final AuthServiceClient authClient = new AuthServiceClient();

    protected void doLogin(String username, String password) throws IOException {
        authClient.doLogin(username, password);
    }
}
