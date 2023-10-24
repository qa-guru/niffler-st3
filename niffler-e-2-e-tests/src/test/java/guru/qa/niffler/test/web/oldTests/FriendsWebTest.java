package guru.qa.niffler.test.web.oldTests;

import guru.qa.niffler.jupiter.annotation.*;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.FriendPage;
import guru.qa.niffler.page.HeaderPage;
import guru.qa.niffler.page.PeoplePage;
import guru.qa.niffler.page.WelcomePage;
import guru.qa.niffler.test.web.BaseWebTest;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;
import static guru.qa.niffler.Constants.PENDING_INVITATION;

public class FriendsWebTest extends BaseWebTest {

	FriendPage fp = new FriendPage();
	HeaderPage hp = new HeaderPage();
	PeoplePage pp = new PeoplePage();
	WelcomePage wp = new WelcomePage();

	@AllureId("103")
	@Test
	@ApiLogin(
			user = @GenerateUser(
					outcomeInvitations = @OutcomeInvitation(count = 2)))
	void outcomeInvitationsShouldBeDisplayed(@GeneratedUser UserJson createdUser) {
		open(CFG.nifflerFrontUrl() + "/main");
		wp.clickLoginButton();
		hp.clickPeopleIcon();
		pp.checkOutcomeInvitations(createdUser.getOutcomeInvitations(), PENDING_INVITATION);

	}

	@AllureId("104")
	@Test
	@ApiLogin(
			user = @GenerateUser(
					incomeInvitations = @IncomeInvitation(count = 2)))
	void incomeInvitationsShouldBeDisplayed(@GeneratedUser UserJson createdUser) {
		open(CFG.nifflerFrontUrl() + "/main");
		wp.clickLoginButton();
		hp.clickFriendsIcon();
		fp.checkIncomeInvitationsIsVisible(createdUser.getIncomeInvitations());

	}

	@AllureId("105")
	@ApiLogin(
			user = @GenerateUser(
					friends = @Friend(count = 2)))
	@Test
	void friendsShouldBeDisplayed(@GeneratedUser UserJson createdUser) {
		open(CFG.nifflerFrontUrl() + "/main");
		wp.clickLoginButton();
		hp.clickFriendsIcon();
		fp.checkFriendsIsVisible(createdUser.getFriends());
	}
}