package guru.qa.niffler.test.web.oldTests;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.test.web.BaseWebTest;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static guru.qa.niffler.jupiter.annotation.User.UserType.INVITATION_SEND;

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
	@AllureId("103")
	void sentInvitationShouldBeDisplayedInTable103(@User(userType = INVITATION_SEND) UserJson userForTest){
		$("[data-tooltip-id='people']").click();
		$$(".abstract-table tbody tr").findBy(text("Pending invitation")).shouldBe(visible);
	}

	@Test
	@AllureId("104")
	void sentInvitationShouldBeDisplayedInTable104(@User(userType = INVITATION_SEND) UserJson userForTest){
		$("[data-tooltip-id='people']").click();
		$$(".abstract-table tbody tr").findBy(text("Pending invitation")).shouldBe(visible);
	}
}
