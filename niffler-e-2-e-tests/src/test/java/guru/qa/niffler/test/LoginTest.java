package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import guru.qa.niffler.db.model.auth.AuthUserEntity;
import guru.qa.niffler.jupiter.annotation.DBUser;
import guru.qa.niffler.jupiter.extension.DaoExtension;
import guru.qa.niffler.page.HeaderPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.WelcomePage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.Constants.INVALID_CREDENTIALS;

@ExtendWith(DaoExtension.class)
public class LoginTest extends BaseWebTest {
	private WelcomePage welcomePage;
	private HeaderPage headerPage;
	private MainPage mainPage;
	Faker faker = new Faker();

	@BeforeEach
	void beforeTest(){
		mainPage = new MainPage();
		welcomePage = new WelcomePage();
		headerPage = new HeaderPage();
	}

	@DBUser
	@Test
	void mainPageShouldBeVisibleAfterLogInWithCorrectCredsTest(AuthUserEntity user) {
		Selenide.open("http://127.0.0.1:3000/main");
		welcomePage.clickLoginButton();
		welcomePage.setUsername(user.getUsername());
		welcomePage.setPassword(user.getPassword());
		welcomePage.clickSignInButton();
		mainPage.checkMainPageIsVisible();
		headerPage.checkLogoutIconIsVisible();
	}

	@DBUser
	@Test
	void errorShouldBeVisibleAfterLogInWithNonCorrectCredsTest(AuthUserEntity user) {
		Selenide.open("http://127.0.0.1:3000/main");
		welcomePage.clickLoginButton();
		welcomePage.setUsername(user.getUsername());
		welcomePage.setPassword(faker.animal().name());
		welcomePage.clickSignInButton();
		welcomePage.checkInvalidCredentialsErrorIsVisible(INVALID_CREDENTIALS);
		mainPage.checkMainPageIsNotVisible();
	}

	@DBUser
	@Test
	void logoutTest(AuthUserEntity user) {
		Selenide.open("http://127.0.0.1:3000/main");
		welcomePage.clickLoginButton();
		welcomePage.setUsername(user.getUsername());
		welcomePage.setPassword(user.getPassword());
		welcomePage.clickSignInButton();
		headerPage.clickLogoutIcon();
		welcomePage.checkLoginButtonIsVisible();
		welcomePage.checkRegisterButtonIsVisible();
	}

	@Test
	void registerTest() {
		String username = faker.name().username();
		String password = faker.animal().name();
		Selenide.open("http://127.0.0.1:3000/main");
		welcomePage.clickRegisterButton();
		welcomePage.setUsername(username);
		welcomePage.setPassword(password);
		welcomePage.setSubmitPassword(password);
		welcomePage.clickSignUpButton();
		welcomePage.checkSuccessRegistration();
	}
}