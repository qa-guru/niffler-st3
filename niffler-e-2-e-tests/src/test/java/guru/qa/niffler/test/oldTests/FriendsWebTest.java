package guru.qa.niffler.test.oldTests;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.test.BaseWebTest;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static guru.qa.niffler.jupiter.annotation.User.UserType.INVITATION_SEND;
import static guru.qa.niffler.jupiter.annotation.User.UserType.WITH_FRIENDS;

public class FriendsWebTest extends BaseWebTest {

	@BeforeEach
	void doLogin(@User(userType = WITH_FRIENDS) UserJson userForTest) {
		Selenide.open("http://127.0.0.1:3000/main");
		$("a[href*='redirect']").click();
		$("input[name='username']").setValue(userForTest.getUsername());
		$("input[name='password']").setValue(userForTest.getPassword());
		$("button[type='submit']").click();
	}

	@Test
	@AllureId("100")
	void friendsShouldBeDisplayedInTable100(@User(userType = WITH_FRIENDS) UserJson user1, @User(userType = INVITATION_SEND) UserJson user2){
		$("[data-tooltip-id='friends']").click();
		$$(".abstract-table tbody tr").findBy(text("You are friends")).shouldBe(visible);
		$("[data-tooltip-id='people']").click();
		$$(".abstract-table tbody tr").findBy(text("You are friends")).shouldBe(visible);
	}

	@Test
	@AllureId("101")
	void friendsShouldBeDisplayedInTable101(@User(userType = WITH_FRIENDS) UserJson userForTest){
		$("[data-tooltip-id='friends']").click();
		$$(".abstract-table tbody tr").findBy(text("You are friends")).shouldBe(visible);
		$("[data-tooltip-id='people']").click();
		$$(".abstract-table tbody tr").findBy(text("You are friends")).shouldBe(visible);
	}

	@Test
	@AllureId("102")
	void friendsShouldBeDisplayedInTable102(){
		$("[data-tooltip-id='friends']").click();
		$$(".abstract-table tbody tr").findBy(text("You are friends")).shouldBe(visible);
		$("[data-tooltip-id='people']").click();
		$$(".abstract-table tbody tr").findBy(text("You are friends")).shouldBe(visible);
	}
}