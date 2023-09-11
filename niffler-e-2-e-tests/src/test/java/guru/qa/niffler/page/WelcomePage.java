package guru.qa.niffler.page;

import guru.qa.niffler.db.model.auth.AuthUserEntity;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static guru.qa.niffler.Constants.SUCCESS_REGISTRATION;

public class WelcomePage {

	private final String LOGIN_BUTTON = "a[href*='redirect']";
	private final String REGISTER_BUTTON = "a[href*='http://127.0.0.1:9000/register']";

	private final String USERNAME_INPUT = "input[name='username']";
	private final String PASSWORD_INPUT = "input[name='password']";
	private final String PASSWORD_SUBMIT_INPUT = "input[name='passwordSubmit']";
	private final String SIGN_IN_BUTTON = "button[type='submit']";
	private final String SIGN_UP_BUTTON = "#register-form button";
	private final String SIGN_UP_LINK = "a[href*='register']";
	private final String ERROR_LABEL = ".form__error";
	private final String SUCCESS_REGISTER_LABEL = ".form__paragraph:first-of-type";

	@Step("Нажать на кнопку 'LOGIN'")
	public void clickLoginButton() {
		$(LOGIN_BUTTON).click();
	}

	@Step("Нажать на кнопку 'REGISTER'")
	public void clickRegisterButton() {
		$(REGISTER_BUTTON).click();
	}

	@Step("Ввести имя пользователя")
	public void setUsername(String username) {
		$(USERNAME_INPUT).setValue(username);
	}

	@Step("Ввести пароль")
	public void setPassword(String pass) {
		$(PASSWORD_INPUT).setValue(pass);
	}@Step("Ввести пароль")

	public void setSubmitPassword(String pass) {
		$(PASSWORD_SUBMIT_INPUT).setValue(pass);
	}

	@Step("Нажать на кнопку 'SIGN IN'")
	public void clickSignInButton() {
		$(SIGN_IN_BUTTON).click();
	}

	@Step("Нажать на кнопку 'SIGN UP'")
	public void clickSignUpButton() {
		$(SIGN_UP_BUTTON).click();
	}

	@Step("Нажать на кнопку 'SIGN UP'")
	public void clickSignUpLink() {
		$(SIGN_UP_LINK).click();
	}

	@Step("Отображается ошибка с текстом '{0}'")
	public void checkInvalidCredentialsErrorIsVisible(String error){
		$(ERROR_LABEL).shouldHave(text(error));
	}

	@Step("Кнопка LOGIN отображается")
	public void checkLoginButtonIsVisible(){
		$(LOGIN_BUTTON).shouldBe(visible);
	}

	@Step("Кнопка REGISTER отображается")
	public void checkRegisterButtonIsVisible(){
		$(REGISTER_BUTTON).shouldBe(visible);
	}

	@Step("Отображается текст успешной регистрации")
	public void checkSuccessRegistration(){
		$(SUCCESS_REGISTER_LABEL).shouldHave(text(SUCCESS_REGISTRATION));
	}

}
