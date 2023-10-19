package guru.qa.niffler.api;

import com.fasterxml.jackson.databind.JsonNode;
import guru.qa.niffler.api.context.CookieContext;
import guru.qa.niffler.api.context.SessionStorageContext;
import guru.qa.niffler.api.interceptor.AddCookieInterceptor;
import guru.qa.niffler.api.interceptor.RecievedCodeInterceptor;
import guru.qa.niffler.api.interceptor.RecievedCookieInterceptor;
import io.qameta.allure.Step;

import java.io.IOException;
import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

public class AuthServiceClient extends RestService {
    public AuthServiceClient() {
        super(CFG.nifflerAuthUrl(), true,
                new RecievedCookieInterceptor(),
                new AddCookieInterceptor(),
                new RecievedCodeInterceptor()
        );
    }

    private final AuthService authService = retrofit.create(AuthService.class);

    @Step("Do api login")
    public void doLogin(String username, String password) throws IOException {
        SessionStorageContext sessionStorageContext = SessionStorageContext.getInstance();
        CookieContext cookieContext = CookieContext.getInstance();
        authService.authorize(
                "code",
                "client",
                "openid",
                CFG.nifflerFrontUrl() + "/authorized",
                sessionStorageContext.getCodeChallenge(),
                "S256"
        ).execute();

        authService.login(
                username,
                password,
                cookieContext.getXsrfTokenCookieValue()
        ).execute();

        JsonNode response = authService.token(
                "Basic " + new String(Base64.getEncoder().encode("client:secret".getBytes(UTF_8))),
                "client",
                CFG.nifflerFrontUrl() + "/authorized",
                "authorization_code",
                sessionStorageContext.getCode(),
                sessionStorageContext.getCodeVerifier()
        ).execute().body();

        sessionStorageContext.setToken(response.get("id_token").asText());
    }
}
