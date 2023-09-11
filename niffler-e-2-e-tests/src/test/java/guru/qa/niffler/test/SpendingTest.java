package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import guru.qa.niffler.db.model.auth.AuthUserEntity;
import guru.qa.niffler.jupiter.annotation.DBUser;
import guru.qa.niffler.jupiter.extension.DaoExtension;
import guru.qa.niffler.page.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.Constants.*;

@ExtendWith(DaoExtension.class)
public class SpendingTest extends BaseWebTest {

	private AlertPopup alertPopup;
	private HeaderPage headerPage;
	private MainPage mainPage;
	private ProfilePage profilePage;
	private WelcomePage welcomePage;
	Faker faker = new Faker();
	String category = faker.animal().name();
	String amount = faker.number().digits(3);
	String description = faker.beer().name();

	@BeforeEach
	void beforeTest(AuthUserEntity user){
		alertPopup = new AlertPopup();
		headerPage = new HeaderPage();
		mainPage = new MainPage();
		profilePage = new ProfilePage();
		welcomePage = new WelcomePage();

		Selenide.open("http://127.0.0.1:3000/main");
		welcomePage.clickLoginButton();
		welcomePage.setUsername(user.getUsername());
		welcomePage.setPassword(user.getPassword());
		welcomePage.clickSignInButton();

		headerPage.clickProfileIcon();
		profilePage.createCategory(category);
		alertPopup.closeAlert();
		headerPage.clickMainPageIcon();
	}

	@DBUser
	@Test
	void createSpendingTest() {
		mainPage.clickSelectCategory();
		mainPage.selectCategory(category);
		mainPage.setAmount(amount);
		mainPage.setDescription(description);
		mainPage.clickAddSpendingButton();
		alertPopup.checkAlertText(SPENDING_SUCCESSFULLY_ADDED);
	}


}