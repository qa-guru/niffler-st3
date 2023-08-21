package guru.qa.niffler.test;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.jupiter.user.User;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static guru.qa.niffler.jupiter.user.User.UserType.INVITATION_RECEIVED;
import static guru.qa.niffler.jupiter.user.User.UserType.INVITATION_SEND;

public class InvitationReceivedWebTest extends BaseWebTest {

	@BeforeEach
	void doLogin(@User(userType = INVITATION_RECEIVED) UserJson userForTest) {
		Selenide.open("http://127.0.0.1:3000/main");
		$("a[href*='redirect']").click();
		$("input[name='username']").setValue(userForTest.getUsername());
		$("input[name='password']").setValue(userForTest.getPassword());
		$("button[type='submit']").click();
	}

	@Test
	@AllureId("101")
	void receivedInvitationShouldBeDisplayedInTable(@User(userType = INVITATION_RECEIVED) UserJson userForTest){
		$("[data-tooltip-id='friends']").$(".header__sign").shouldBe(visible);
		$("[data-tooltip-id='friends']").click();
		$("[data-tooltip-id='submit-invitation']").shouldBe(visible);
		$("[data-tooltip-id='decline-invitation']").shouldBe(visible);
		ElementsCollection friends = $$(".abstract-table tbody tr");
		ArrayList<String> newFriendsUsername = new ArrayList<>();
		for (SelenideElement friend : friends) {
			newFriendsUsername.add(friend.$x("td[2]").getText());
		}
		$("[data-tooltip-id='people']").click();
		for (String name : newFriendsUsername) {
			$x("//td[text()='"+name+"']/following-sibling::*/div/div[@data-tooltip-id='submit-invitation']").shouldBe(visible);
			$x("//td[text()='"+name+"']/following-sibling::*/div/div[@data-tooltip-id='decline-invitation']").shouldBe(visible);
		}
	}
}
