package guru.qa.niffler.jupiter.extension;

import com.github.javafaker.Faker;
import guru.qa.niffler.db.dao.AuthUserDAO;
import guru.qa.niffler.db.dao.UserDataUserDAO;
import guru.qa.niffler.db.dao.impl.AuthUserDAOHibernate;
import guru.qa.niffler.db.dao.impl.UserdataUserDAOHibernate;
import guru.qa.niffler.db.model.auth.AuthUserEntity;
import guru.qa.niffler.db.model.auth.Authority;
import guru.qa.niffler.db.model.auth.AuthorityEntity;
import guru.qa.niffler.db.model.userdata.UserDataUserEntity;
import guru.qa.niffler.jupiter.annotation.DBUser;
import org.junit.jupiter.api.extension.*;

import java.util.Arrays;
import static guru.qa.niffler.db.model.CurrencyValues.RUB;

public class DBUserExtension implements BeforeEachCallback, ParameterResolver, AfterTestExecutionCallback {

	public static ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(DBUserExtension.class);

	Faker faker = new Faker();

	@Override
	public void beforeEach(ExtensionContext context) throws Exception {
		DBUser annotation = context.getRequiredTestMethod().getAnnotation(DBUser.class);
		AuthUserDAO authUserDAO = new AuthUserDAOHibernate();
		UserDataUserDAO userDataUserDAO = new UserdataUserDAOHibernate();
		if (annotation != null) {
			AuthUserEntity authUser = createAuthUserEntity(annotation);
			authUserDAO.createUser(authUser);
			authUser.setId(authUserDAO.getUser(authUser.getUsername()).getId());
			UserDataUserEntity userdataUser = convertUser(authUser);
			userDataUserDAO.createUserInUserData(userdataUser);

			context.getStore(NAMESPACE).put(getUserKey(context.getUniqueId()), authUser);
			context.getStore(NAMESPACE).put(getUserDataKey(context.getUniqueId()), userdataUser);
		}
	}

	@Override
	public void afterTestExecution(ExtensionContext context) throws Exception {
		AuthUserDAO authUserDAO = new AuthUserDAOHibernate();
		UserDataUserDAO userDataUserDAO = new UserdataUserDAOHibernate();
		AuthUserEntity user = context.getStore(NAMESPACE).get(getUserKey(context.getUniqueId()), AuthUserEntity.class);
		userDataUserDAO.deleteUser(convertUser(user));
		authUserDAO.deleteUser(user);
	}

	@Override
	public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
		return parameterContext.getParameter().getType().isAssignableFrom(AuthUserEntity.class) &&
				extensionContext.getTestMethod().isPresent() &&
				extensionContext.getTestMethod().get().isAnnotationPresent(DBUser.class);
	}
	@Override
	public AuthUserEntity resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
		return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId() + "user", AuthUserEntity.class);
	}

	private AuthUserEntity createAuthUserEntity (DBUser annotation){
		AuthUserEntity authUser = new AuthUserEntity();
		String username = annotation.username().isEmpty() ? faker.name().username() : annotation.username();
		String password = annotation.password().isEmpty() ? faker.funnyName().name() : annotation.password();
		authUser.setUsername(username);
		authUser.setPassword(password);
		authUser.setEnabled(true);
		authUser.setAccountNonExpired(true);
		authUser.setAccountNonLocked(true);
		authUser.setCredentialsNonExpired(true);
		authUser.setAuthorities(Arrays.stream(Authority.values())
				.map(a -> {
					AuthorityEntity ae = new AuthorityEntity();
					ae.setAuthority(a);
					ae.setUser(authUser);
					return ae;
				}).toList());
		return authUser;
	}

	private UserDataUserEntity convertUser(AuthUserEntity authUser){
		UserDataUserEntity user = new UserDataUserEntity();
		user.setUsername(authUser.getUsername());
		user.setCurrency(RUB);
		return user;
	}

	private String getUserKey(String uniqueId) {
		return uniqueId + "user";
	}

	private String getUserDataKey(String uniqueId) {
		return uniqueId + "userdata";
	}
}
