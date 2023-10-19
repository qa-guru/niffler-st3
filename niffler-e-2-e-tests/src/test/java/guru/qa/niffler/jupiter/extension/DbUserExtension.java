package guru.qa.niffler.jupiter.extension;

import com.github.javafaker.Faker;
import guru.qa.niffler.db.dao.AuthUserDAO;
import guru.qa.niffler.db.dao.UserDataUserDAO;
import guru.qa.niffler.db.dao.impl.AuthUserDAOHibernate;
import guru.qa.niffler.db.dao.impl.UserdataUserDAOHibernate;
import guru.qa.niffler.db.model.CurrencyValues;
import guru.qa.niffler.db.model.auth.Authority;
import guru.qa.niffler.db.model.auth.AuthorityEntity;
import guru.qa.niffler.db.model.auth.UserEntity;
import guru.qa.niffler.db.model.userdata.UserDataEntity;
import guru.qa.niffler.jupiter.annotation.DBUser;
import org.junit.jupiter.api.extension.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class DbUserExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

    public static ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(DbUserExtension.class);
    Faker faker = new Faker();
    public static final String defaultPassword = "12345";

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AuthUserDAO authUserDAO = new AuthUserDAOHibernate();
        UserDataUserDAO userDataUserDAO = new UserdataUserDAOHibernate();
        DBUser dbUser = context.getRequiredTestMethod().getAnnotation(DBUser.class);
        if (dbUser != null) {
            UserEntity authUser = new UserEntity();
            authUser.setUsername(faker.name().username());
            authUser.setPassword(defaultPassword);
            authUser.setEnabled(true);
            authUser.setAccountNonExpired(true);
            authUser.setAccountNonLocked(true);
            authUser.setCredentialsNonExpired(true);
            authUser.setAuthorities(new ArrayList<>(Arrays.stream(Authority.values())
                    .map(a -> {
                        AuthorityEntity ae = new AuthorityEntity();
                        ae.setAuthority(a);
                        ae.setUser(authUser);
                        return ae;
                    }).collect(Collectors.toList())));
            authUserDAO.createUser(authUser);

            UserDataEntity userdataUser = new UserDataEntity();
            userdataUser.setUsername(authUser.getUsername());
            userdataUser.setCurrency(CurrencyValues.RUB);
            userDataUserDAO.createUserInUserData(userdataUser);

            context.getStore(NAMESPACE).put(context.getUniqueId(), authUser);
        }
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        AuthUserDAO authUserDAO = new AuthUserDAOHibernate();
        UserDataUserDAO userDataUserDAO = new UserdataUserDAOHibernate();
        UserEntity user = context.getStore(NAMESPACE).get(context.getUniqueId(), UserEntity.class);
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
        return extensionContext.getStore(DbUserExtension.NAMESPACE).get(extensionContext.getUniqueId(), UserEntity.class);
    }
}
