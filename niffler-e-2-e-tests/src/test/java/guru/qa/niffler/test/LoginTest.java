package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.db.model.auth.AuthUserEntity;
import guru.qa.niffler.db.model.auth.Authority;
import guru.qa.niffler.db.model.auth.AuthorityEntity;
import guru.qa.niffler.db.model.userdata.UserDataUserEntity;
import guru.qa.niffler.db.repository.UserRepository;
import guru.qa.niffler.db.repository.UserRepositorySpringJdbc;
import guru.qa.niffler.page.WelcomePage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginTest extends BaseWebTest {

    private static final String defaultPassword = "12345";

    private UserRepository userRepository = new UserRepositorySpringJdbc();

    private AuthUserEntity authUser;
    private UserDataUserEntity userdataUser;

    @BeforeEach
    void createUser() {
        authUser = new AuthUserEntity();
        authUser.setUsername("valentin_26");
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
        Selenide.open("http://127.0.0.1:3000/main");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(authUser.getUsername());
        $("input[name='password']").setValue(defaultPassword);
        $("button[type='submit']").click();
        $(".main-content__section-stats").should(visible);
    }
}
