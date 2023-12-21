package guru.qa.niffler.jupiter;

import guru.qa.niffler.db.dao.AuthUserDAO;
import guru.qa.niffler.db.dao.UserDataUserDAO;
import guru.qa.niffler.db.model.Authority;
import guru.qa.niffler.db.model.AuthorityEntity;
import guru.qa.niffler.db.model.UserEntity;
import org.junit.jupiter.api.extension.*;

import java.util.Arrays;

public class DBUserExtension implements BeforeEachCallback, AfterTestExecutionCallback, ParameterResolver {

    public static ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(DBUserExtension.class);
    DaoExtension daoExtension = new DaoExtension();
    @Dao
    private AuthUserDAO authUserDAO;
    @Dao
    private UserDataUserDAO userDataUserDAO;

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        daoExtension.postProcessTestInstance(this, context);

        DBUser annotation = context.getRequiredTestMethod().getAnnotation(DBUser.class);
        if (annotation != null) {
            UserEntity createdUser = new UserEntity();
            createdUser
                    .setUsername(annotation.username())
                    .setPassword(annotation.password())
                    .setEnabled(true)
                    .setAccountNonExpired(true)
                    .setAccountNonLocked(true)
                    .setCredentialsNonExpired(true)
                    .setAuthorities(Arrays.stream(Authority.values())
                            .map(a -> {
                                AuthorityEntity ae = new AuthorityEntity();
                                ae.setAuthority(a);
                                return ae;
                            }).toList());
            authUserDAO.createUser(createdUser);
            userDataUserDAO.createUserInUserData(createdUser);

            context.getStore(NAMESPACE).put(context.getUniqueId(), createdUser);
        }
    }


    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        UserEntity user = context.getStore(NAMESPACE).get(context.getUniqueId(), UserEntity.class);
        userDataUserDAO.deleteUserByIdInUserData(user.getUsername());
        authUserDAO.deleteUserById(user.getId());
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter()
                .getType()
                .isAssignableFrom(UserEntity.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext
                .getStore(NAMESPACE)
                .get(extensionContext.getUniqueId(), UserEntity.class);
    }
}
