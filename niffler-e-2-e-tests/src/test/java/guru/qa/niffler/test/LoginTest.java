package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.db.dao.AuthUserDAO;
import guru.qa.niffler.db.dao.UserDataUserDAO;
import guru.qa.niffler.db.dao.impl.AuthUserDAOHibernate;
import guru.qa.niffler.db.dao.impl.UserdataUserDAOHibernate;
import guru.qa.niffler.db.model.CurrencyValues;
import guru.qa.niffler.db.model.auth.AuthUserEntity;
import guru.qa.niffler.db.model.auth.Authority;
import guru.qa.niffler.db.model.auth.AuthorityEntity;
import guru.qa.niffler.db.model.userdata.UserDataUserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginTest extends BaseWebTest {

    private static final String defaultPassword = "12345";

    private AuthUserDAO authUserDAO = new AuthUserDAOHibernate();
    private UserDataUserDAO userDataUserDAO = new UserdataUserDAOHibernate();

    private AuthUserEntity authUser;
    private UserDataUserEntity userdataUser;

    @BeforeEach
    void createUser() {
        authUser = new AuthUserEntity();
        authUser.setUsername("valentin_6");
        authUser.setPassword(defaultPassword);
        authUser.setEnabled(true);
        authUser.setAccountNonExpired(true);
        authUser.setAccountNonLocked(true);
        authUser.setCredentialsNonExpired(true);
        authUser.setAuthorities(Arrays.stream(Authority.values())
                .map(a -> {
                    AuthorityEntity ae = new AuthorityEntity();
                    ae.setAuthority(a);
                    ae.setUser(authUser);
                    return ae;
                }).toList());
        authUserDAO.createUser(authUser);


        userdataUser = new UserDataUserEntity();
        userdataUser.setUsername("valentin_6");
        userdataUser.setCurrency(CurrencyValues.RUB);
        userDataUserDAO.createUserInUserData(userdataUser);
    }


    @Test
    void mainPageShouldBeVisibleAfterLogin() {
        Selenide.open("http://127.0.0.1:3000/main");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(authUser.getUsername());
        $("input[name='password']").setValue(defaultPassword);
        $("button[type='submit']").click();
        $(".main-content__section-stats").should(visible);
    }
}
