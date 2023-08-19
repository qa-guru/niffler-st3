package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.User;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.$;
import static guru.qa.niffler.jupiter.User.UserType.WITH_FRIENDS;

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
	@AllureId("101")
	void friendsShouldBeDisplayedInTable(@User(userType = WITH_FRIENDS) UserJson userForTest){
		System.out.println();
	}

	@Test
	@AllureId("102")
	void friendsShouldBeDisplayedInTable3(@User(userType = WITH_FRIENDS) UserJson userForTest){
		System.out.println();
	}

	@Test
	@AllureId("103")
	void friendsShouldBeDisplayedInTable2(@User(userType = WITH_FRIENDS) UserJson userForTest){
		System.out.println();
	}
}
