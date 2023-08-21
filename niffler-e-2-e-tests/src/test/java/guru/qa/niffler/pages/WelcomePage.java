package guru.qa.niffler.pages;

import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class WelcomePage {

    @Step("open Welcome page")
    public WelcomePage openPage(){
        open("http://127.0.0.1:3000/");

        return this;
    }

    @Step("Click button Login")
    public LoginPage goToLogin(){
        $("a[href*='redirect']").click();

        return new LoginPage();
    }
}
