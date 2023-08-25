package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.db.dao.AuthUserDAO;
import guru.qa.niffler.db.dao.UserDataUserDAO;
import guru.qa.niffler.db.model.Authority;
import guru.qa.niffler.db.model.AuthorityEntity;
import guru.qa.niffler.db.model.UserEntity;
import guru.qa.niffler.jupiter.Dao;
import guru.qa.niffler.jupiter.DaoExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.UUID;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@ExtendWith(DaoExtension.class)
public class LoginTest extends BaseWebTest {

	@Dao
	private AuthUserDAO authUserDAO;
	@Dao
	private UserDataUserDAO userDataUserDAO;
	private UserEntity user;


	@BeforeEach
	void createUser() {
		user = new UserEntity();
		user.setUsername("Anton2");
		user.setPassword("12345678");
		user.setEnabled(true);
		user.setAccountNonExpired(true);
		user.setAccountNonLocked(true);
		user.setCredentialsNonExpired(true);
		user.setAuthorities(Arrays.stream(Authority.values())
				.map(a -> {
					AuthorityEntity ae = new AuthorityEntity();
					ae.setAuthority(a);
					return ae;
				}).toList());
		authUserDAO.createUser(user);
		userDataUserDAO.createUserInUserData(user);
	}

	@AfterEach
	void deleteUser() {
		userDataUserDAO.deleteUserByIdInUserData(user.getId());
		authUserDAO.deleteUserById(user.getId());
	}

	@Test
	void mainPageShouldBeVisibleAfterLogIn() {
		Selenide.open("http://127.0.0.1:3000/main");
		$("a[href*='redirect']").click();
		$("input[name='username']").setValue(user.getUsername());
		$("input[name='password']").setValue(user.getPassword());
		$("button[type='submit']").click();
		$(".main-content .main-content__section-stats").shouldBe(visible);
	}

	@Test
	void mainPageShouldBeVisibleAfterLogInAfterUpdateUser() {
		updateUserInDB(user.getId());
		updateUserData(user.getId());

		Selenide.open("http://127.0.0.1:3000/main");
		$("a[href*='redirect']").click();
		$("input[name='username']").setValue(user.getUsername());
		$("input[name='password']").setValue(user.getPassword());
		$("button[type='submit']").click();
		$(".main-content .main-content__section-stats").shouldBe(visible);
	}

	private void updateUserInDB(UUID userId){
		userDataUserDAO.updateUserByIdInUserData(userId);
		authUserDAO.updateUserById(userId);
	}

	private void updateUserData(UUID userId){
		UserEntity userEntity = authUserDAO.getUserById(userId);
		user.setUsername(userEntity.getUsername());
		user.setEnabled(userEntity.getEnabled());
		user.setAccountNonExpired(userEntity.getAccountNonExpired());
		user.setAccountNonLocked(userEntity.getAccountNonLocked());
		user.setCredentialsNonExpired(userEntity.getCredentialsNonExpired());
	}
}


