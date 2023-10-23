package guru.qa.niffler.test.pages;


import guru.qa.niffler.jupiter.annotation.Page;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;
import static guru.qa.niffler.test.BaseWebTest.openPage;

@Page(url = "/")
public class LoginPage {

    @Step("Авторизоваться пользователем в системе")
    public static void doLogin(UserJson userForTest) {
        openPage(LoginPage.class);
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(userForTest.getUsername());
        $("input[name='password']").setValue(userForTest.getPassword());
        $("button[type='submit']").click();
    }

}
