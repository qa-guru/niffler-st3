package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class BaseWebTest {

    static {
        Configuration.browser = "chrome";
        Configuration.browserSize = "1980x1024";
    }

    @Step("Выполнить логин в портал используя логин \"{0}\" и пароль \"{1}\"")
    void doLoginStep(String login, String password) {
        Selenide.open("http://127.0.0.1:3000/main");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(login);
        $("input[name='password']").setValue(password);
        $("button[type='submit']").click();
    }
}
