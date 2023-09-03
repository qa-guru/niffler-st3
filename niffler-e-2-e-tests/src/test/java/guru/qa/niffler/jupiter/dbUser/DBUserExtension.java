package guru.qa.niffler.jupiter.dbUser;

import guru.qa.niffler.db.dao.AuthUserDAO;
import guru.qa.niffler.db.dao.AuthUserDAOJdbc;
import guru.qa.niffler.db.dao.UserDataUserDAO;
import guru.qa.niffler.db.model.CurrencyValues;
import guru.qa.niffler.db.model.auth.Authority;
import guru.qa.niffler.db.model.auth.AuthorityEntity;
import guru.qa.niffler.db.model.auth.AuthUserEntity;
import guru.qa.niffler.db.model.userdata.UserDataUserEntity;
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
			AuthUserEntity authUser = new AuthUserEntity();
			authUser.setUsername(annotation.username());
			authUser.setPassword(annotation.password());
			authUser.setEnabled(true);
			authUser.setAccountNonExpired(true);
			authUser.setAccountNonLocked(true);
			authUser.setCredentialsNonExpired(true);
			authUser.setAuthorities(Arrays.stream(Authority.values())
					.map(authority -> {
						var ae = new AuthorityEntity();
						ae.setAuthority(authority);
						return ae;
					}).toList()
			);
			UUID uuid = authUserDAO.createUser(authUser);
			authUser.setId(uuid);

			UserDataUserEntity userdataUser = new UserDataUserEntity();
			userdataUser.setUsername(authUser.getUsername());
			userdataUser.setCurrency(CurrencyValues.RUB);
			userDataUserDAO.createUserInUserData(userdataUser);

			context.getStore(NAMESPACE).put(context.getUniqueId(), authUser);
		}
	}

	@Override
	public void afterTestExecution(ExtensionContext context) throws Exception {
		AuthUserEntity user = context.getStore(NAMESPACE).get(context.getUniqueId(), AuthUserEntity.class);
		userDataUserDAO.deleteUserByIdInUserData(user.getId());
		authUserDAO.deleteUser(user);
	}

	@Override
	public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
		return parameterContext.getParameter()
				.getType()
				.isAssignableFrom(AuthUserEntity.class);
	}

	@Override
	public AuthUserEntity resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
		return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), AuthUserEntity.class);
	}
}
