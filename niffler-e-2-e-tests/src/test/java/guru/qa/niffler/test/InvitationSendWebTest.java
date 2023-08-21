package guru.qa.niffler.test;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.jupiter.user.User;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static guru.qa.niffler.jupiter.user.User.UserType.INVITATION_SEND;

public class InvitationSendWebTest extends BaseWebTest {

	@BeforeEach
	void doLogin(@User(userType = INVITATION_SEND) UserJson userForTest) {
		Selenide.open("http://127.0.0.1:3000/main");
		$("a[href*='redirect']").click();
		$("input[name='username']").setValue(userForTest.getUsername());
		$("input[name='password']").setValue(userForTest.getPassword());
		$("button[type='submit']").click();
	}

	@Test
	@AllureId("101")
	void sentInvitationShouldBeDisplayedInTable(@User(userType = INVITATION_SEND) UserJson userForTest){
		$("[data-tooltip-id='people']").click();
		ElementsCollection friends = $$(".abstract-table tbody tr");
		friends.shouldHave(sizeGreaterThan(0));
		for (SelenideElement friend : friends) {
			friend.$(".abstract-table__buttons").$("div:first-child").shouldHave(text("Pending invitation"));
		}
	}
}
