package guru.qa.niffler.jupiter.extension;

import com.github.javafaker.Faker;
import guru.qa.niffler.db.dao.AuthUserDAO;
import guru.qa.niffler.db.dao.AuthUserDAOJdbc;
import guru.qa.niffler.db.dao.UserDataUserDAO;
import guru.qa.niffler.db.model.Authority;
import guru.qa.niffler.db.model.AuthorityEntity;
import guru.qa.niffler.db.model.UserEntity;
import guru.qa.niffler.jupiter.annotation.DBUser;
import org.junit.jupiter.api.extension.*;

import java.util.Arrays;

public class DbUserExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

    private static final AuthUserDAO authUserDAO = new AuthUserDAOJdbc();
    private static final UserDataUserDAO userDataUserDAO = new AuthUserDAOJdbc();
    public static ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(DbUserExtension.class);

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        DBUser annotation = context.getRequiredTestMethod().getAnnotation(DBUser.class);
        if (annotation != null) {
            UserEntity user = new UserEntity();
            Faker faker = new Faker();
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

            context.getStore(NAMESPACE).put("user", user);
            authUserDAO.createUser(user);
            userDataUserDAO.createUserInUserData(user);
        }
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        UserEntity user = context.getStore(NAMESPACE).get("user", UserEntity.class);
        userDataUserDAO.deleteUserByUsernameInUserData(user.getUsername());
        authUserDAO.deleteUserById(user.getId());

    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws
            ParameterResolutionException {
        return parameterContext
                .getParameter()
                .getType()
                .isAssignableFrom(UserEntity.class);
    }

    @Override
    public UserEntity resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws
            ParameterResolutionException {
        return extensionContext.getStore(DbUserExtension.NAMESPACE).get("user", UserEntity.class);
    }
}
