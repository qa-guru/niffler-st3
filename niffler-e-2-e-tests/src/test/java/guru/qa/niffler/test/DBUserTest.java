package guru.qa.niffler.test;

import guru.qa.niffler.jupiter.dao.DaoExtension;
import guru.qa.niffler.jupiter.dbUser.DBUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@ExtendWith(DaoExtension.class)
public class DBUserTest extends BaseWebTest {

	private static final String USERNAME = "Adam Olearius";
	private static final String PASSWORD = "12345678";

	@DBUser(username = USERNAME, password = PASSWORD)
	@Test
	void mainPageShouldBeVisibleAfterLogIn() {
		$(".main-content .main-content__section-stats").shouldBe(visible);
	}
}


