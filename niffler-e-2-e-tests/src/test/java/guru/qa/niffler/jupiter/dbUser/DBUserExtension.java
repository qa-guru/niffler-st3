package guru.qa.niffler.jupiter.dbUser;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.db.dao.AuthUserDAO;
import guru.qa.niffler.db.dao.AuthUserDAOJdbc;
import guru.qa.niffler.db.dao.UserDataUserDAO;
import guru.qa.niffler.db.model.Authority;
import guru.qa.niffler.db.model.AuthorityEntity;
import guru.qa.niffler.db.model.UserEntity;
import guru.qa.niffler.jupiter.dao.DaoExtension;
import org.junit.jupiter.api.extension.*;

import java.util.Arrays;

import static com.codeborne.selenide.Selenide.$;

@ExtendWith(DaoExtension.class)
public class DBUserExtension implements BeforeEachCallback, ParameterResolver, AfterTestExecutionCallback {

	public static ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(DBUserExtension.class);

	private AuthUserDAO authUserDAO = new AuthUserDAOJdbc();

	private UserDataUserDAO userDataUserDAO = new AuthUserDAOJdbc();

	@Override
	public void beforeEach(ExtensionContext context) throws Exception {
		DBUser annotation = context.getRequiredTestMethod().getAnnotation(DBUser.class);
		if (annotation != null) {
			UserEntity user = new UserEntity();
			user.setUsername(annotation.username());
			user.setPassword(annotation.password());
			user.setEnabled(true);
			user.setAccountNonExpired(true);
			user.setAccountNonLocked(true);
			user.setCredentialsNonExpired(true);
			user.setAuthorities(Arrays.stream(Authority.values())
					.map(authority -> {
						var ae = new AuthorityEntity();
						ae.setAuthority(authority);
						return ae;
					}).toList()
			);
			context.getStore(NAMESPACE).put("user", user);
			authUserDAO.createUser(user);
			userDataUserDAO.createUserInUserData(user);

			Selenide.open("http://127.0.0.1:3000/main");
			$("a[href*='redirect']").click();
			$("input[name='username']").setValue(user.getUsername());
			$("input[name='password']").setValue(user.getPassword());
			$("button[type='submit']").click();
		}
	}

	@Override
	public void afterTestExecution(ExtensionContext context) throws Exception {
		var user = context.getStore(NAMESPACE).get("user", UserEntity.class);
		userDataUserDAO.deleteUserByIdInUserData(user.getId());
		authUserDAO.deleteUserById(user.getId());
	}

	@Override
	public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
//		return parameterContext
//				.getParameter()
//				.getType()
//				.isAssignableFrom(UserEntity.class);

		return parameterContext.getParameter().getType().equals(ExtensionContext.class);

	}

	@Override
	public UserEntity resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
		return extensionContext
				.getStore(DBUserExtension.NAMESPACE)
				.get("user", UserEntity.class);
	}
}
