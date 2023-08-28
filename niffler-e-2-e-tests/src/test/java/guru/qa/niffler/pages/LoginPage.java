package guru.qa.niffler.pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.model.UserJson;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class LoginPage {

    private final SelenideElement
            loginButton = $("a[href*='redirect']"),
            usernameInput = $("input[name='username']"),
            passwordInput = $("input[name='password']"),
            signInButton = $("button[type='submit']");

    public void login(UserJson userForTest) {
        internalLogin(userForTest.getUsername(), userForTest.getPassword());
    }

    public void login(String username, String password) {
        internalLogin(username, password);
    }

    private void internalLogin(String username, String password) {
        open("http://127.0.0.1:3000/main");
        loginButton.click();
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        signInButton.click();
    }
}
