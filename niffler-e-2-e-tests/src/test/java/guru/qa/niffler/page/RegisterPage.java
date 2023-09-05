package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$x;

public class RegisterPage {

    private final SelenideElement userNameFld = $x("//input[@name = 'username']");
    private final SelenideElement passwordFld = $x("//input[@name = 'password']");
    private final SelenideElement confirmPasswordFld = $x("//input[@name = 'passwordSubmit']");
    private final SelenideElement signUpBtn = $x("//button[@type = 'submit']");
    private final SelenideElement formAfterRegister = $x("//p[@class = 'form__paragraph']");
    private final SelenideElement formError = $x("//span[@class = 'form__error']");

    @Step("Заполнить поле \"UserName\"")
    public RegisterPage fillUserName(String userName) {
        userNameFld.setValue(userName);
        return this;
    }

    @Step("Заполнить поле \"Password\"")
    public RegisterPage fillPassword(String password) {
        passwordFld.setValue(password);
        return this;
    }

    @Step("Заполнить поле \"ConfirmPassword\"")
    public RegisterPage fillConfirmPassword(String password) {
        confirmPasswordFld.setValue(password);
        return this;
    }

    @Step("Нажать кнопку \"Sign up\"")
    public RegisterPage clickSignUp() {
        signUpBtn.click();
        return this;
    }

    @Step("Проверяет, что отображается подтверждение регистрации")
    public RegisterPage checkAlertAfterRegistration() {
        formAfterRegister.shouldBe(text("Congratulations! You've registered!"));
        return this;
    }

    @Step("Проверяет, что отображается сообщение об ошибке")
    public RegisterPage checkTextFromError() {
        formError.shouldBe(text("Passwords should be equal"));
        return this;
    }
}
