package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.jupiter.annotation.GeneratedUser;
import guru.qa.niffler.model.UserJson;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class CreateUserExtension implements BeforeEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace
            NESTED = ExtensionContext.Namespace.create(GeneratedUser.Selector.NESTED),
            OUTER = ExtensionContext.Namespace.create(GeneratedUser.Selector.OUTER);

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        Map<GeneratedUser.Selector, GenerateUser> usersForTest = usersForTest(extensionContext);
        for (Map.Entry<GeneratedUser.Selector, GenerateUser> entry : usersForTest.entrySet()) {
            UserJson user = createUserForTest(entry.getValue());
            user.setFriends(createFriendsIfPresent(entry.getValue()));
            user.setIncomeInvitations(createIncomeInvitationsIfPresent(entry.getValue()));
            user.setOutcomeInvitations(createOutcomeInvitationsIfPresent(entry.getValue()));
            extensionContext.getStore(ExtensionContext.Namespace.create(entry.getKey()))
                    .put(entry.getKey(), user);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().isAnnotationPresent(GeneratedUser.class) &&
                parameterContext.getParameter().getType().isAssignableFrom(UserJson.class);
    }

    @Override
    public UserJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        GeneratedUser generatedUser = parameterContext.getParameter().getAnnotation(GeneratedUser.class);
        return extensionContext.getStore(ExtensionContext.Namespace.create(generatedUser.selector()))
                .get(generatedUser.selector(), UserJson.class);
    }

    protected abstract UserJson createUserForTest(GenerateUser annotation);

    protected abstract List<UserJson> createFriendsIfPresent(GenerateUser annotation);

    protected abstract List<UserJson> createIncomeInvitationsIfPresent(GenerateUser annotation);

    protected abstract List<UserJson> createOutcomeInvitationsIfPresent(GenerateUser annotation);


    private Map<GeneratedUser.Selector, GenerateUser> usersForTest(ExtensionContext extensionContext) {
        Map<GeneratedUser.Selector, GenerateUser> result = new HashMap<>();
        ApiLogin apiLogin = extensionContext.getRequiredTestMethod().getAnnotation(ApiLogin.class);
        if (apiLogin != null && apiLogin.user().handleAnnotation()) {
            result.put(GeneratedUser.Selector.NESTED, apiLogin.user());
        }
        GenerateUser outerUser = extensionContext.getRequiredTestMethod().getAnnotation(GenerateUser.class);
        if (outerUser != null && outerUser.handleAnnotation()) {
            result.put(GeneratedUser.Selector.OUTER, outerUser);
        }
        return result;
    }
}
