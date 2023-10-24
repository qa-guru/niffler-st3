package guru.qa.niffler.page;

import guru.qa.niffler.model.UserJson;
import io.qameta.allure.Step;

import java.util.List;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;

public class PeoplePage {

	private final String PEOPLE_ROW = "//tr";
	private final String ADD_FRIEND_BUTTON = "//tr/td[2][text()='%s']//following-sibling::td//div[@data-tooltip-id='add-friend']";
	private final String AVATAR_COLUMN = "//tr/td[1]";
	private final String USERNAME_COLUMN = "//tr/td[2]";
	private final String NAME_COLUMN = "//tr/td[3]";
	private final String STATUS_COLUMN = "//tr/td[2][text()='%s']//following-sibling::td//div[@class='abstract-table__buttons']";


	@Step("Нажать на иконку 'Добавить в друзья' пользователя {0}")
	public void addFriend(String friendName) {
		$x(String.format(ADD_FRIEND_BUTTON, friendName))
				.scrollIntoView(false).click();
	}

	@Step("Выбрать пользователя для добавления в друзья")
	public String chooseFriendsForAdding() {
		int number = (int) (Math.random() * $$x(PEOPLE_ROW).size()+1);
		return $$x(USERNAME_COLUMN).get(number)
				.scrollIntoView(false).getText();
	}

	@Step("Статус пользователя {username} соответствует '{status}'")
	public void checkStatusUser(String friendName, String status) {
		$x(String.format(STATUS_COLUMN, friendName))
				.scrollIntoView(false).shouldHave(text(status));
	}

	@Step("Статус пользователей {invitations} соответствует '{status}'")
	public void checkOutcomeInvitations(List<UserJson> invitations, String status) {
		for (UserJson invitation : invitations) {
			$x(String.format(STATUS_COLUMN, invitation.getUsername()))
					.scrollIntoView(false).shouldHave(text(status));
		}
	}
}
