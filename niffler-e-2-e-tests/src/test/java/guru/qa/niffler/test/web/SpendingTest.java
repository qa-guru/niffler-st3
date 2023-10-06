package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.DBUser;
import guru.qa.niffler.jupiter.extension.DaoExtension;
import guru.qa.niffler.page.AlertPopup;
import guru.qa.niffler.page.HeaderPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.ProfilePage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Isolated;

import static guru.qa.niffler.Constants.SPENDING_SUCCESSFULLY_ADDED;

@Isolated
@ExtendWith(DaoExtension.class)
public class SpendingTest extends BaseWebTest {

	private AlertPopup alertPopup;
	private HeaderPage headerPage;
	private MainPage mainPage;
	private ProfilePage profilePage;
	Faker faker = new Faker();
	String category = faker.animal().name();
	String amount = faker.number().digits(3);
	String description = faker.beer().name();

	@DBUser
	@ApiLogin
	@Test
	void createSpendingTest() {
		alertPopup = new AlertPopup();
		headerPage = new HeaderPage();
		mainPage = new MainPage();
		profilePage = new ProfilePage();

		Selenide.open(CFG.nifflerFrontUrl() + "/main");

		headerPage.clickProfileIcon();
		profilePage.createCategory(category);
		alertPopup.closeAlert();
		headerPage.clickMainPageIcon();

		mainPage.clickSelectCategory();
		mainPage.selectCategory(category);
		mainPage.setAmount(amount);
		mainPage.setDescription(description);
		mainPage.clickAddSpendingButton();
		alertPopup.checkAlertText(SPENDING_SUCCESSFULLY_ADDED);
	}
}