package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.db.dao.AuthUserDAO;
import guru.qa.niffler.db.model.auth.AuthUserEntity;
import guru.qa.niffler.jupiter.annotation.DBUser;
import guru.qa.niffler.jupiter.annotation.Dao;
import guru.qa.niffler.jupiter.extension.DaoExtension;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static io.qameta.allure.Allure.step;

@ExtendWith(DaoExtension.class)
public class LoginTest extends BaseWebTest {
    private static final String defaultPassword = "12345";
    @Dao
    private AuthUserDAO authUserDAO;

    @Test
    @DBUser()
    @AllureId("121")
    void mainPageShouldBeVisibleAfterLogin(AuthUserEntity user) {
        step("Открыть страницу авторизации", () -> open("http://127.0.0.1:3000/main"));
        step("Открыть страницу авторизации", () -> $("a[href*='redirect']").click());
        step("Ввести логин", () -> $("input[name='username']").setValue(user.getUsername()));
        step("Ввести пароль", () -> $("input[name='password']").setValue(defaultPassword));
        step("Нажать кнопку 'Sign in'", () -> $("button[type='submit']").click());
    }

    @Test
    @DBUser()
    @AllureId("122")
    void getUserAfterCreateCreate(AuthUserEntity user) {
        Selenide.open("http://127.0.0.1:3000/main");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(user.getUsername());
        $("input[name='password']").setValue(defaultPassword);
        $("button[type='submit']").click();
        $(".main-content__section-stats").should(visible);

        AuthUserEntity getUser = authUserDAO.getUserById(user.getId());
        Assertions.assertEquals(user.getUsername(), getUser.getUsername());
    }
}
