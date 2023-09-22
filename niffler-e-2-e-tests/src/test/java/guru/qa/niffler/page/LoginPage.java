package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;

public class LoginPage {

    private final SelenideElement loginBtn = $x("//a[@href = '/redirect']");
    private final SelenideElement loginFld = $("input[name='username']");
    private final SelenideElement passwordFld = $("input[name='password']");
    private final SelenideElement submitBtn = $("button[type='submit']");
    private final SelenideElement formError = $x("//p[@class = 'form__error']");
    private final SelenideElement registerBtn = $x("//a[contains (@href, '/register')]");

    @Step("Логин")
    public MainPage login(String username, String password) {
        clickLoginBtn()
                .fillLogin(username)
                .fillPassword(password)
                .clickSubmitBtn();
        return new MainPage();
    }

    public void login(UserJson userForTest) {
        login(userForTest.getUsername(), userForTest.getPassword());
    }

    @Step("Нажать кнопку \"Login\"")
    public LoginPage clickLoginBtn() {
        loginBtn.click();
        return this;
    }

    @Step("Заполнить поле \"Логин\"")
    public LoginPage fillLogin(String username) {
        loginFld.setValue(username);
        return this;
    }

    @Step("Заполнить поле \"Пароль\"")
    public LoginPage fillPassword(String password) {
        passwordFld.setValue(password);
        return this;
    }

    @Step("Нажать кнопку \"Submit\"")
    public MainPage clickSubmitBtn() {
        submitBtn.click();
        return new MainPage();
    }

    @Step("Нажать кнопку \"Register\"")
    public RegisterPage clickRegisterBtn() {
        registerBtn.click();
        return new RegisterPage();
    }

    @Step("Проверяет, что отображается сообщение об ошибке")
    public LoginPage checkTextFromError() {
        formError.shouldBe(text("Неверные учетные данные пользователя"));
        return this;
    }
}
