package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.db.dao.AuthUserDAO;
import guru.qa.niffler.db.model.Authority;
import guru.qa.niffler.db.model.AuthorityEntity;
import guru.qa.niffler.db.model.UserEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginTest extends BaseWebTest{

	private UserEntity user;
	private AuthUserDAO authUserDAO;

	@BeforeEach
	void createUser(){
		user = new UserEntity();
		user.setUsername("Vadim");
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
	}

	@Test
	void mainPageShouldBeVisibleAfterLogIn(){
		Selenide.open("http://127.0.0.1:3000/main");
		$("a[href*='redirect']").click();
		$("input[name='username']").setValue(user.getUsername());
		$("input[name='password']").setValue(user.getPassword());
		$("button[type='submit']").click();
		$(".main-content .main-content__section-stats").shouldBe(visible);
	}

	@AfterEach
	void deleteUser(){
		authUserDAO.deleteUserById(user.getId());
	}
}


