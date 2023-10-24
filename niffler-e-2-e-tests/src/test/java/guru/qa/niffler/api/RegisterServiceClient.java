package guru.qa.niffler.api;

import io.qameta.allure.Step;

import java.io.IOException;
import java.util.UUID;

public class RegisterServiceClient extends RestService{
    public RegisterServiceClient() {
        super(CFG.nifflerAuthUrl());
    }

    private final RegisterService registerService = retrofit.create(RegisterService.class);

    @Step("Register")
    public void register(String username,
                         String password) {
        try {
            String csrfToken = UUID.randomUUID().toString();
            registerService.register("XSRF-TOKEN=" + csrfToken, csrfToken, username, password, password)
                    .execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
