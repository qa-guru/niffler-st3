package guru.qa.niffler.page;

import guru.qa.niffler.model.UserJson;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static io.qameta.allure.Allure.step;

public class LoginPage {

    @Step("Логин")
    public void login(String username, String password) {
        step("Открыть страницу авторизации", () -> open("http://127.0.0.1:3000/main"));
        step("Открыть страницу авторизации", () -> $("a[href*='redirect']").click());
        step("Ввести логин", () -> $("input[name='username']").setValue(username));
        step("Ввести пароль", () -> $("input[name='password']").setValue(password));
        step("Нажать кнопку 'Sign in'", () -> $("button[type='submit']").click());
    }

    public void login(UserJson userForTest) {
        login(userForTest.getUsername(), userForTest.getPassword());
    }
}
