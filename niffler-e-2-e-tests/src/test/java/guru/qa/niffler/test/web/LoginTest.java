package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import guru.qa.niffler.db.model.auth.AuthUserEntity;
import guru.qa.niffler.db.model.auth.Authority;
import guru.qa.niffler.db.model.auth.AuthorityEntity;
import guru.qa.niffler.db.repository.UserRepository;
import guru.qa.niffler.db.repository.UserRepositorySpringJdbc;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;

@WireMockTest(httpPort = 8089)
public class LoginTest extends BaseWebTest {

    private static final String defaultPassword = "12345";
    private static String userdataUrl;

    private UserRepository userRepository = new UserRepositorySpringJdbc();
    private AuthUserEntity authUser;

    private WireMockServer wireMockServer = new WireMockServer(new WireMockConfiguration()
            .port(8089));

    static {
        String[] urlParts = CFG.nifflerUserdataUrl().split(":");
        userdataUrl = urlParts[0];
    }

    private WireMock wiremock = new WireMock(
            userdataUrl,
            8089
    );

    @BeforeEach
    void createUser() {
        wiremock.register(get(urlPathEqualTo("/currentUser"))
                .withQueryParam("username", equalTo("valentin_30"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-type", "Application/json")
                        .withBody("{\n" +
                                "      \"id\": \"229fc371-2821-4795-81a5-0b26d3cd417e\",\n" +
                                "      \"username\": \"valentin_30\",\n" +
                                "      \"firstname\": \"Valentin\",\n" +
                                "      \"surname\": null,\n" +
                                "      \"currency\": \"RUB\",\n" +
                                "      \"photo\": null\n" +
                                "    }")
                ));

        authUser = new AuthUserEntity();
        authUser.setUsername("valentin_30");
        authUser.setPassword(defaultPassword);
        authUser.setEnabled(true);
        authUser.setAccountNonExpired(true);
        authUser.setAccountNonLocked(true);
        authUser.setCredentialsNonExpired(true);
        authUser.setAuthorities(new ArrayList<>(Arrays.stream(Authority.values())
                .map(a -> {
                    AuthorityEntity ae = new AuthorityEntity();
                    ae.setAuthority(a);
                    ae.setUser(authUser);
                    return ae;
                }).toList()));

        userRepository.createUserForTest(authUser);
    }

    @AfterEach
    void deleteUser() {
        userRepository.removeAfterTest(authUser);
    }

    @Test
    void mainPageShouldBeVisibleAfterLogin() throws IOException {
        Selenide.open(CFG.nifflerFrontUrl() + "/main");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(authUser.getUsername());
        $("input[name='password']").setValue(defaultPassword);
        $("button[type='submit']").click();
        $(".main-content__section-stats").should(visible);
    }
}
