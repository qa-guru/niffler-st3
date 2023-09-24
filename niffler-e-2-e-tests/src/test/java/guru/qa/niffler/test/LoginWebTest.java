package guru.qa.niffler.test;

import guru.qa.niffler.db.model.auth.AuthUserEntity;
import guru.qa.niffler.jupiter.annotation.DBUser;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;

public class LoginWebTest extends BaseWebTest {

    @Test
    @DBUser(password = defaultPassword)
    @AllureId("100")
    void statisticsShouldBeVisibleAfterLogin(AuthUserEntity user) {
        open(CFG.nifflerFrontUrl());
        loginPage
                .login(user.getUsername(), defaultPassword)
                .checkVisibleStatistics();
    }

    @Test
    @AllureId("101")
    void loginWithBadCredential() {
        open(CFG.nifflerFrontUrl());
        loginPage
                .login("qwe", "123123");
        loginPage.checkTextFromError();
    }

    @Test
    @AllureId("102")
    void registerNewUser() {
        open(CFG.nifflerFrontUrl());
        loginPage
                .clickRegisterBtn()
                .fillUserName(faker.name().username())
                .fillPassword(defaultPassword)
                .fillConfirmPassword(defaultPassword)
                .clickSignUp()
                .checkAlertAfterRegistration();
    }

    @Test
    @AllureId("103")
    void passwordShouldBeEquals() {
        open(CFG.nifflerFrontUrl());
        loginPage
                .clickRegisterBtn()
                .fillUserName(faker.name().username())
                .fillPassword(defaultPassword)
                .fillConfirmPassword("defaultPassword")
                .clickSignUp()
                .checkTextFromError();
    }
}
