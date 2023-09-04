package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import guru.qa.niffler.db.dao.AuthUserDAO;
import guru.qa.niffler.db.dao.impl.AuthUserDAOHibernate;
import guru.qa.niffler.db.dao.UserDataUserDAO;
import guru.qa.niffler.db.dao.impl.UserdataUserDAOHibernate;
import guru.qa.niffler.db.model.CurrencyValues;
import guru.qa.niffler.db.model.auth.Authority;
import guru.qa.niffler.db.model.auth.AuthorityEntity;
import guru.qa.niffler.db.model.auth.AuthUserEntity;
import guru.qa.niffler.db.model.userdata.UserDataUserEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.UUID;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginTest extends BaseWebTest {

	private AuthUserDAO authUserDAO = new AuthUserDAOHibernate();
	private UserDataUserDAO userDataUserDAO = new UserdataUserDAOHibernate();
	private AuthUserEntity authUser;
	private UserDataUserEntity userdataUser;
	Faker faker = new Faker();
	String username = faker.name().username();
	String newUsername = faker.name().username();
	String password = faker.lordOfTheRings().character();

	@BeforeEach
	void createUser() {
		authUser = new AuthUserEntity();
		authUser.setUsername(username);
		authUser.setPassword(password);
		authUser.setEnabled(true);
		authUser.setAccountNonExpired(true);
		authUser.setAccountNonLocked(true);
		authUser.setCredentialsNonExpired(true);
		authUser.setAuthorities(Arrays.stream(Authority.values()).map(a -> {
			AuthorityEntity ae = new AuthorityEntity();
			ae.setAuthority(a);
			ae.setUser(authUser);
			return ae;
		}).toList());
		UUID userId = authUserDAO.createUser(authUser);
		authUser.setId(userId);

		userdataUser = new UserDataUserEntity();
		userdataUser.setUsername(username);
		userdataUser.setCurrency(CurrencyValues.RUB);
		userDataUserDAO.createUserInUserData(userdataUser);
	}

	@AfterEach
	void deleteUser() {
		userDataUserDAO.deleteUserByIdInUserData(authUser.getId());
		authUserDAO.deleteUser(authUser);
	}

	@Test
	void mainPageShouldBeVisibleAfterLogIn() {
		Selenide.open("http://127.0.0.1:3000/main");
		$("a[href*='redirect']").click();
		$("input[name='username']").setValue(authUser.getUsername());
		$("input[name='password']").setValue(password);
		$("button[type='submit']").click();
		$(".main-content .main-content__section-stats").shouldBe(visible);
	}

	@Test
	void mainPageShouldBeVisibleAfterLogInAfterUpdateUser() {
		updateUserInDB(authUser);
		updateUserData(authUser.getId());

		Selenide.open("http://127.0.0.1:3000/main");
		$("a[href*='redirect']").click();
		$("input[name='username']").setValue(authUser.getUsername());
		$("input[name='password']").setValue(authUser.getPassword());
		$("button[type='submit']").click();
		$(".main-content .main-content__section-stats").shouldBe(visible);
	}

	private void updateUserInDB(AuthUserEntity user) {
		UserDataUserEntity newUserData = new UserDataUserEntity();
		newUserData.setUsername(user.getUsername());
		userDataUserDAO.updateUserInUserData(newUserData);
		authUserDAO.updateUser(user);
	}

	private void updateUserData(UUID userId) {
		AuthUserEntity authUserEntity = authUserDAO.getUser(userId);
		authUser.setUsername(authUserEntity.getUsername());
		authUser.setEnabled(authUserEntity.getEnabled());
		authUser.setAccountNonExpired(authUserEntity.getAccountNonExpired());
		authUser.setAccountNonLocked(authUserEntity.getAccountNonLocked());
		authUser.setCredentialsNonExpired(authUserEntity.getCredentialsNonExpired());
	}
}


