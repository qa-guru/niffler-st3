package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.db.model.auth.AuthUserEntity;
import guru.qa.niffler.jupiter.annotation.DBUser;
import guru.qa.niffler.jupiter.extension.DaoExtension;
import guru.qa.niffler.page.HeaderPage;
import guru.qa.niffler.page.PeoplePage;
import guru.qa.niffler.page.WelcomePage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.Constants.PENDING_INVITATION;

@ExtendWith(DaoExtension.class)
public class FriendsTest extends BaseWebTest {

	private HeaderPage headerPage;
	private PeoplePage peoplePage;
	private WelcomePage welcomePage;
	private String friendsName;

	@BeforeEach
	void beforeTest(AuthUserEntity user){
		headerPage = new HeaderPage();
		peoplePage = new PeoplePage();
		welcomePage = new WelcomePage();

		Selenide.open("http://127.0.0.1:3000/main");
		welcomePage.clickLoginButton();
		welcomePage.setUsername(user.getUsername());
		welcomePage.setPassword(user.getPassword());
		welcomePage.clickSignInButton();
	}


	@DBUser
	@Test
	void addFriendsTest() {
		headerPage.clickPeopleIcon();
		friendsName = peoplePage.chooseFriendsForAdding();
		peoplePage.addFriend(friendsName);
		peoplePage.checkStatusUser(friendsName, PENDING_INVITATION);
	}
}