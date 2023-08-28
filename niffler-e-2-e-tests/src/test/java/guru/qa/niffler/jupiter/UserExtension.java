package guru.qa.niffler.jupiter;

import com.github.javafaker.Faker;
import guru.qa.niffler.db.dao.AuthUserDAO;
import guru.qa.niffler.db.dao.AuthUserDAOJdbc;
import guru.qa.niffler.db.dao.UserDataUserDAO;
import guru.qa.niffler.db.model.Authority;
import guru.qa.niffler.db.model.AuthorityEntity;
import guru.qa.niffler.db.model.UserEntity;
import org.junit.jupiter.api.extension.*;

import java.util.Arrays;

public class UserExtension implements BeforeEachCallback, ParameterResolver, AfterTestExecutionCallback  {

    public static ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UserExtension.class);
    private static final AuthUserDAO authUserDAO = new AuthUserDAOJdbc();

    private static final UserDataUserDAO userDataUserDAO = new AuthUserDAOJdbc();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        DBUser annotation = context.getRequiredTestMethod().getAnnotation(DBUser.class);
        if (annotation != null) {
            Faker faker = new Faker();
            UserEntity user = new UserEntity();
            user.setUsername(faker.name().username());
            user.setPassword(faker.internet().password());
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
            context.getStore(NAMESPACE).put("createUser", user);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter()
                .getType()
                .isAssignableFrom(UserEntity.class);
    }

    @Override
    public UserEntity resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext
                .getStore(NAMESPACE)
                .get("createUser", UserEntity.class);
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        UserEntity userFromTest = context.getStore(NAMESPACE).get("createUser", UserEntity.class);
        authUserDAO.deleteUserById(userFromTest.getId());
        userDataUserDAO.deleteUserByNameInUserData(userFromTest.getUsername());
    }
}
