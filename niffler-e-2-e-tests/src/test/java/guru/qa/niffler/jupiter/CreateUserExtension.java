package guru.qa.niffler.jupiter;

import guru.qa.niffler.db.dao.AuthUserDAO;
import guru.qa.niffler.db.dao.UserDataUserDAO;
import guru.qa.niffler.db.model.Authority;
import guru.qa.niffler.db.model.AuthorityEntity;
import guru.qa.niffler.db.model.UserEntity;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.lang.reflect.Field;
import java.util.Arrays;

public class CreateUserExtension implements BeforeEachCallback, ParameterResolver {

    private static final ExtensionContext.Namespace USER_NAMESPACE = ExtensionContext.Namespace.create(CreateUserExtension.class);

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        Object testInstance = context.getRequiredTestInstance();
        AuthUserDAO authUserDAO = getFieldValue(testInstance, AuthUserDAO.class);
        UserDataUserDAO userDataUserDAO = getFieldValue(testInstance, UserDataUserDAO.class);

        DBUser dbUserAnnotation = context.getRequiredTestMethod().getAnnotation(DBUser.class);
        if (dbUserAnnotation != null) {
            UserEntity user = createUserEntityFromAnnotation(dbUserAnnotation);
            authUserDAO.createUser(user);
            userDataUserDAO.createUserInUserData(user);

            context.getStore(USER_NAMESPACE).put(context.getUniqueId(), user);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return parameterContext.getParameter().getType().isAssignableFrom(UserEntity.class) &&
                extensionContext.getTestMethod().isPresent() &&
                extensionContext.getTestMethod().get().isAnnotationPresent(DBUser.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return extensionContext.getStore(USER_NAMESPACE).get(extensionContext.getUniqueId());
    }

    private UserEntity createUserEntityFromAnnotation(DBUser annotation) {
        UserEntity user = new UserEntity();
        user.setUsername(annotation.username());
        user.setPassword(annotation.password());
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
        return user;
    }

    private <T> T getFieldValue(Object object, Class<T> clazz) {
        for (Field field : object.getClass().getDeclaredFields()) {
            if (clazz.isAssignableFrom(field.getType())) {
                field.setAccessible(true);
                try {
                    return clazz.cast(field.get(object));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return null;
    }
}



