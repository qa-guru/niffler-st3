package guru.qa.niffler.page;

import guru.qa.niffler.model.UserJson;
import io.qameta.allure.Step;

import java.util.List;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class FriendPage {
	private final String USERNAME_COLUMN = "//tr/td[2]";
	private final String ACCEPT_BUTTON_BY_USERNAME = "//tr/td[2][text()='%s']//following-sibling::td//*[@data-tooltip-id='submit-invitation']";
	private final String DECLINE_BUTTON_BY_USERNAME = "//tr/td[2][text()='%s']//following-sibling::td//*[@data-tooltip-id='decline-invitation']";

	@Step("Друзья отображаются на странице друзей")
	public void checkFriendsIsVisible(List<UserJson> friends) {
		for (UserJson friend : friends) {
			$$x(USERNAME_COLUMN).findBy(text(friend.getUsername())).shouldBe(visible);
		}
	}

	@Step("Предложения дружбы отображаются на странице друзей")
	public void checkIncomeInvitationsIsVisible(List<UserJson> invitations) {
		for (UserJson invitation : invitations) {
			$x(String.format(ACCEPT_BUTTON_BY_USERNAME, invitation.getUsername()))
					.shouldBe(visible);
			$x(String.format(DECLINE_BUTTON_BY_USERNAME, invitation.getUsername()))
					.shouldBe(visible);
		}
	}
}
