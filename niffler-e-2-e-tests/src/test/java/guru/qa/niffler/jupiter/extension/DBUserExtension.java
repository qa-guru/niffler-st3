package guru.qa.niffler.jupiter.extension;

import com.github.javafaker.Faker;
import guru.qa.niffler.db.dao.AuthUserDAO;
import guru.qa.niffler.db.dao.UserDataUserDAO;
import guru.qa.niffler.db.dao.impl.AuthUserDAOHibernate;
import guru.qa.niffler.db.dao.impl.UserDataUserDAOHibernate;
import guru.qa.niffler.db.model.auth.AuthUserEntity;
import guru.qa.niffler.db.model.auth.Authority;
import guru.qa.niffler.db.model.auth.AuthorityEntity;
import guru.qa.niffler.db.model.userdata.UserDataUserEntity;
import guru.qa.niffler.jupiter.annotation.DBUser;
import guru.qa.niffler.model.CurrencyValues;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.extension.*;

import java.util.Arrays;

public class DBUserExtension implements BeforeEachCallback, ParameterResolver, AfterTestExecutionCallback  {

    public static ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(DBUserExtension.class);
    private static final AuthUserDAO authUserDAO = new AuthUserDAOHibernate();
    private static final UserDataUserDAO userDataUserDAO = new UserDataUserDAOHibernate();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        DBUser annotation = context.getRequiredTestMethod().getAnnotation(DBUser.class);
        if (annotation != null) {
            Faker faker = new Faker();
            AuthUserEntity user = new AuthUserEntity();
            user.setUsername(faker.name().username());
            user.setPassword("12345");
            user.setEnabled(true);
            user.setAccountNonExpired(true);
            user.setAccountNonLocked(true);
            user.setCredentialsNonExpired(true);
            user.setAuthorities(Arrays.stream(Authority.values())
                    .map(a -> {
                        AuthorityEntity ae = new AuthorityEntity();
                        ae.setAuthority(a);
                        ae.setUser(user);
                        return ae;
                    }).toList());
            authUserDAO.createUser(user);
            UserDataUserEntity userData = new UserDataUserEntity();
            userData.setUsername(user.getUsername());
            userData.setCurrency(CurrencyValues.RUB);

            userDataUserDAO.createUserInUserData(userData);
            context.getStore(NAMESPACE).put(getAllureId(context), user);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter()
                .getType()
                .isAssignableFrom(AuthUserEntity.class);
    }

    @Override
    public AuthUserEntity resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext
                .getStore(NAMESPACE)
                .get(getAllureId(extensionContext), AuthUserEntity.class);
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        AuthUserEntity userFromTest = context.getStore(NAMESPACE).get(getAllureId(context), AuthUserEntity.class);
        authUserDAO.deleteUser(userFromTest);
        userDataUserDAO.deleteUserByNameInUserData(userFromTest.getUsername());
    }

    private String getAllureId(ExtensionContext context) {
        AllureId allureId = context.getRequiredTestMethod().getAnnotation(AllureId.class);
        if (allureId == null) {
            throw new IllegalStateException("Annotation @AllureId must be present!");
        }
        return allureId.value();
    }
}
