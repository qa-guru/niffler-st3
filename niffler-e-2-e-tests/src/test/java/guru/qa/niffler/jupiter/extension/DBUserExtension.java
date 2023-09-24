package guru.qa.niffler.jupiter.extension;

import com.github.javafaker.Faker;
import guru.qa.niffler.db.model.auth.AuthUserEntity;
import guru.qa.niffler.db.model.auth.Authority;
import guru.qa.niffler.db.model.auth.AuthorityEntity;
import guru.qa.niffler.db.model.userdata.UserDataUserEntity;
import guru.qa.niffler.db.repository.UserRepository;
import guru.qa.niffler.db.repository.UserRepositorySpringJdbc;
import guru.qa.niffler.jupiter.annotation.DBUser;
import guru.qa.niffler.model.CurrencyValues;
import org.junit.jupiter.api.extension.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class DBUserExtension implements BeforeEachCallback, ParameterResolver, AfterTestExecutionCallback  {

    public static ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(DBUserExtension.class);
    private final UserRepository userRepository = new UserRepositorySpringJdbc();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        DBUser annotation = context.getRequiredTestMethod().getAnnotation(DBUser.class);
        if (annotation != null) {
            Faker faker = new Faker();
            AuthUserEntity user = new AuthUserEntity();
            user.setUsername(annotation.login().isEmpty() ? faker.name().username() : annotation.login());
            user.setPassword(annotation.password().isEmpty() ? faker.internet().password() : annotation.password());
            user.setEnabled(true);
            user.setAccountNonExpired(true);
            user.setAccountNonLocked(true);
            user.setCredentialsNonExpired(true);
            user.setAuthorities(new ArrayList<>(Arrays.stream(Authority.values())
                    .map(a -> {
                        AuthorityEntity ae = new AuthorityEntity();
                        ae.setAuthority(a);
                        ae.setUser(user);
                        return ae;
                    }).collect(Collectors.toList())));
            userRepository.createUserForTest(user);
            UserDataUserEntity userData = new UserDataUserEntity();
            userData.setUsername(user.getUsername());
            userData.setCurrency(CurrencyValues.RUB);

            context.getStore(NAMESPACE).put(context.getUniqueId(), user);
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
                .get(extensionContext.getUniqueId(), AuthUserEntity.class);
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        AuthUserEntity userFromTest = context.getStore(NAMESPACE).get(context.getUniqueId(), AuthUserEntity.class);
        userRepository.removeAfterTest(userFromTest);
    }
}
