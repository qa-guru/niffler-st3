package guru.qa.niffler.pages;

import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage {

    @Step("Sign in")
    public void signIn(String userName, String password) {
        $("input[name ='username']").setValue(userName);
        $("input[name = 'password']").setValue(password);
        $("button[type = 'submit']").click();
    }
}
