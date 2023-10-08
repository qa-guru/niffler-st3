package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import guru.qa.niffler.db.model.auth.AuthUserEntity;
import guru.qa.niffler.jupiter.annotation.DBUser;
import guru.qa.niffler.jupiter.extension.DaoExtension;
import guru.qa.niffler.page.AlertPopup;
import guru.qa.niffler.page.HeaderPage;
import guru.qa.niffler.page.ProfilePage;
import guru.qa.niffler.page.WelcomePage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.Constants.*;

@ExtendWith(DaoExtension.class)
public class ProfileTest extends BaseWebTest {

	private AlertPopup alertPopup;
	private HeaderPage headerPage;
	private ProfilePage profilePage;
	private WelcomePage welcomePage;
	Faker faker = new Faker();
	String firstname = faker.name().firstName();
	String lastname = faker.name().lastName();
	String category = faker.random().hex();

	@BeforeEach
	void beforeTest(AuthUserEntity user){
		alertPopup = new AlertPopup();
		headerPage = new HeaderPage();
		profilePage = new ProfilePage();
		welcomePage = new WelcomePage();

		Selenide.open("http://127.0.0.1:3000/main");
		welcomePage.clickLoginButton();
		welcomePage.setUsername(user.getUsername());
		welcomePage.setPassword(user.getPassword());
		welcomePage.clickSignInButton();
	}

	@DBUser
	@Test
	void createCategoryTest() {
		headerPage.clickProfileIcon();
		profilePage.createCategory(category);
		alertPopup.checkAlertText(NEW_CATEGORY_ADDED);
		alertPopup.closeAlert();
		profilePage.checkCategoryIsVisibleInList(category);
	}

	@DBUser
	@Test
	void createMoreThanLimitCategoryTest() {
		headerPage.clickProfileIcon();
		for (int i=0;i<8;i++){
			profilePage.createCategory(faker.random().hex());
			alertPopup.checkAlertText(NEW_CATEGORY_ADDED);
			alertPopup.closeAlert();
		}
		String category = faker.random().hex();
		profilePage.createCategory(category);
		alertPopup.checkAlertText(CAN_NOT_ADD_NEW_CATEGORY);
		profilePage.checkCategoryIsNotVisibleInList(category);
	}

	@DBUser
	@Test
	void createExistingCategoryTest() {
		headerPage.clickProfileIcon();
		profilePage.createCategory(category);
		alertPopup.checkAlertText(NEW_CATEGORY_ADDED);
		alertPopup.closeAlert();
		profilePage.createCategory(category);
		alertPopup.checkAlertText(CAN_NOT_ADD_NEW_CATEGORY);
	}

	@DBUser
	@Test
	void updateProfileTest() {
		headerPage.clickProfileIcon();
		profilePage.setFirstname(firstname);
		profilePage.setLastname(lastname);
		profilePage.clickSubmitButton();
		alertPopup.checkAlertText(PROFILE_UPDATED);
	}
}