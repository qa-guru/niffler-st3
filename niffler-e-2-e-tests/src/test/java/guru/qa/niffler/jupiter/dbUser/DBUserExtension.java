package guru.qa.niffler.jupiter.dbUser;

import guru.qa.niffler.db.dao.AuthUserDAO;
import guru.qa.niffler.db.dao.AuthUserDAOJdbc;
import guru.qa.niffler.db.dao.UserDataUserDAO;
import guru.qa.niffler.db.model.Authority;
import guru.qa.niffler.db.model.AuthorityEntity;
import guru.qa.niffler.db.model.UserEntity;
import org.junit.jupiter.api.extension.*;

import java.util.Arrays;
import java.util.UUID;

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
			UUID uuid = authUserDAO.createUser(user);
			user.setId(uuid);
			userDataUserDAO.createUserInUserData(user);
			context.getStore(NAMESPACE).put(context.getUniqueId(), user);
		}
	}

	@Override
	public void afterTestExecution(ExtensionContext context) throws Exception {
		UserEntity user = context.getStore(NAMESPACE).get(context.getUniqueId(), UserEntity.class);
		userDataUserDAO.deleteUserByIdInUserData(user.getId());
		authUserDAO.deleteUserById(user.getId());
	}

	@Override
	public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
		return parameterContext.getParameter()
				.getType()
				.isAssignableFrom(UserEntity.class);
	}

	@Override
	public UserEntity resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
		return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), UserEntity.class);
	}
}
