package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.db.dao.AuthUserDAO;
import guru.qa.niffler.db.dao.UserDataUserDAO;
import guru.qa.niffler.db.model.UserEntity;
import guru.qa.niffler.jupiter.dao.Dao;
import guru.qa.niffler.jupiter.dao.DaoExtension;
import guru.qa.niffler.jupiter.dbUser.DBUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@ExtendWith(DaoExtension.class)
public class DBUserTest extends BaseWebTest {

	@Dao
	private AuthUserDAO authUserDAO;
	@Dao
	private UserDataUserDAO userDataUserDAO;
	private UserEntity user;
	private static final String USERNAME = "Adam Olearius";
	private static final String PASSWORD = "12345678";

	@BeforeEach
	void createUser(UserEntity user) {
		Selenide.open("http://127.0.0.1:3000/main");
		$("a[href*='redirect']").click();
		$("input[name='username']").setValue(user.getUsername());
		$("input[name='password']").setValue(user.getPassword());
		$("button[type='submit']").click();
	}

	@AfterEach
	void deleteUser(UserEntity user) {
		userDataUserDAO.deleteUserByIdInUserData(user.getId());
		authUserDAO.deleteUserById(user.getId());
	}

	@DBUser(username = USERNAME, password = PASSWORD)
	@Test
	void mainPageShouldBeVisibleAfterLogIn() {
		$(".main-content .main-content__section-stats").shouldBe(visible);
	}
}


