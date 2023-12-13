package guru.qa.niffler.jupiter;

import guru.qa.niffler.model.UserJson;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.*;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class UserQueueExtension implements BeforeEachCallback, AfterTestExecutionCallback, ParameterResolver {

    public static ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UserQueueExtension.class);

    private static final Map<User.UserType, Queue<UserJson>> usersQueue = new ConcurrentHashMap<>();

    static {
        Queue<UserJson> usersWithFriends = new ConcurrentLinkedQueue<>();
        usersWithFriends.add(bindUser("dima", "12345"));
        usersWithFriends.add(bindUser("barsik", "12345"));
        usersQueue.put(User.UserType.WITH_FRIENDS, usersWithFriends);
        Queue<UserJson> usersInSent = new ConcurrentLinkedQueue<>();
        usersInSent.add(bindUser("bee", "12345"));
        usersInSent.add(bindUser("anna", "12345"));
        usersQueue.put(User.UserType.INVITATION_SENT, usersInSent);
        Queue<UserJson> usersInRc = new ConcurrentLinkedQueue<>();
        usersInRc.add(bindUser("valentin", "12345"));
        usersInRc.add(bindUser("pizzly", "12345"));
        usersQueue.put(User.UserType.INVITATION_RECEIVED, usersInRc);
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        putToNamespace(context.getRequiredTestMethod().getParameters(), context);
        boolean isNotPresent = Arrays.stream(context.getRequiredTestMethod().getParameters())
                .noneMatch(p -> p.isAnnotationPresent(User.class));

        if (isNotPresent) {
            Method[] declaredMethods = context.getRequiredTestClass().getDeclaredMethods();
            for (Method method : declaredMethods) {
                if (method.isAnnotationPresent(BeforeEach.class)) {
                    putToNamespace(method.getParameters(), context);
                }
            }
        }
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        for (User.UserType userType : User.UserType.values()) {
            UserJson userFromTest = context.getStore(NAMESPACE).remove(
                    getUniqKey(context, userType),
                    UserJson.class
            );
            if (userFromTest != null) {
                usersQueue.get(userFromTest.getUserType()).add(userFromTest);
            }
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(UserJson.class)
                && parameterContext.getParameter().isAnnotationPresent(User.class);
    }

    @Override
    public UserJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        User parameterAnnotation = parameterContext.getParameter().getAnnotation(User.class);
        User.UserType userType = parameterAnnotation.userType();
        return extensionContext.getStore(NAMESPACE).get(
                getUniqKey(extensionContext, userType),
                UserJson.class
        );
    }

    private String getAllureId(ExtensionContext context) {
        AllureId allureId = context.getRequiredTestMethod().getAnnotation(AllureId.class);
        if (allureId == null) {
            throw new IllegalStateException("Annotation @AllureId must be present!");
        }
        return allureId.value();
    }

    private static UserJson bindUser(String username, String password) {
        UserJson user = new UserJson();
        user.setUsername(username);
        user.setPassword(password);
        return user;
    }

    private String getUniqKey(ExtensionContext context, User.UserType userType) {
        return getAllureId(context) + "-" + userType.name();
    }

    private void putToNamespace(Parameter[] parameters, ExtensionContext context) {
        for (Parameter parameter : parameters) {
            if (parameter.getType().isAssignableFrom(UserJson.class)) {
                User parameterAnnotation = parameter.getAnnotation(User.class);
                User.UserType userType = parameterAnnotation.userType();
                Queue<UserJson> usersQueueByType = usersQueue.get(parameterAnnotation.userType());
                UserJson candidateForTest = null;
                while (candidateForTest == null) {
                    candidateForTest = usersQueueByType.poll();
                }
                candidateForTest.setUserType(userType);
                context.getStore(NAMESPACE).put(getUniqKey(context, userType), candidateForTest);
            }
        }
    }
}
