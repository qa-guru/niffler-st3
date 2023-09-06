package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.db.model.auth.AuthUserEntity;
import guru.qa.niffler.jupiter.extension.DaoExtension;
import guru.qa.niffler.jupiter.annotation.DBUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@ExtendWith(DaoExtension.class)
public class DBUserTest extends BaseWebTest {

	private static final String USERNAME = "Adam 5";
	private static final String PASSWORD = "12345678";

	@DBUser
	@Test
	void mainPageShouldBeVisibleAfterLogIn(AuthUserEntity user) {
		Selenide.open("http://127.0.0.1:3000/main");
		$("a[href*='redirect']").click();
		$("input[name='username']").setValue(user.getUsername());
		$("input[name='password']").setValue(user.getPassword());
		$("button[type='submit']").click();
		$(".main-content .main-content__section-stats").shouldBe(visible);
	}
}