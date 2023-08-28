package guru.qa.niffler.jupiter;

import guru.qa.niffler.model.UserJson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.*;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeoutException;

public class UserQueueExtension implements BeforeEachCallback, AfterTestExecutionCallback, ParameterResolver {

    public static ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UserQueueExtension.class);

    private static final Map<UserQueue.UserType, Queue<UserJson>> usersTypeQueueMap = new ConcurrentHashMap<>();

    static {
        Queue<UserJson> usersFriends = new ConcurrentLinkedQueue<>();
        usersFriends.add(bindUser("f1"));
        usersFriends.add(bindUser("f2"));
        usersTypeQueueMap.put(UserQueue.UserType.FRIEND, usersFriends);

        Queue<UserJson> usersInviteSent = new ConcurrentLinkedQueue<>();
        usersInviteSent.add(bindUser("s1"));
        usersInviteSent.add(bindUser("s2"));
        usersTypeQueueMap.put(UserQueue.UserType.INVITE_SENT, usersInviteSent);

        Queue<UserJson> usersInviteReceived = new ConcurrentLinkedQueue<>();
        usersInviteReceived.add(bindUser("r1"));
        usersInviteReceived.add(bindUser("r2"));
        usersTypeQueueMap.put(UserQueue.UserType.INVITE_RECEIVED, usersInviteReceived);
    }

    private static final Map<String, String> usernameSwapMap = new ConcurrentHashMap<>();

    static {
        usernameSwapMap.put("f1", "f2");
        usernameSwapMap.put("f2", "f1");
        usernameSwapMap.put("s1", "r1");
        usernameSwapMap.put("s2", "r2");
        usernameSwapMap.put("r1", "s1");
        usernameSwapMap.put("r2", "s2");
    }

    @Override
    public void beforeEach(ExtensionContext context) throws TimeoutException {
        Parameter[] testMethodParameters = context.getRequiredTestMethod().getParameters();
        boolean hasUserAnnotation = false;

        for (Parameter parameter : testMethodParameters) {
            if (parameter.isAnnotationPresent(UserQueue.class)) {
                hasUserAnnotation = true;
                prepareUserForTestAndStore(parameter, context);
            }
        }

        if (!hasUserAnnotation) {
            Method[] declaredMethods = context.getRequiredTestClass().getDeclaredMethods();
            for (Method method : declaredMethods) {
                if (method.isAnnotationPresent(BeforeEach.class)) {
                    Parameter[] parameters = method.getParameters();
                    for (Parameter parameter : parameters) {
                        if (parameter.getType().isAssignableFrom(UserJson.class)
                                && parameter.isAnnotationPresent(UserQueue.class)) {
                            prepareUserForTestAndStore(parameter, context);
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void afterTestExecution(ExtensionContext context) {
        UserJson userFromTest = context.getStore(NAMESPACE).get(context.getUniqueId(), UserJson.class);
        usersTypeQueueMap.get(userFromTest.getUserType()).add(userFromTest);
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws
            ParameterResolutionException {
        Parameter parameter = parameterContext.getParameter();
        return parameter == null || parameterContext.getParameter().getType().isAssignableFrom(UserJson.class);
    }

    @Override
    public UserJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws
            ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), UserJson.class);
    }

    private static UserJson bindUser(String username) {
        UserJson userJson = new UserJson();
        userJson.setUsername(username);
        userJson.setPassword("12345");
        return userJson;
    }

    private UserJson findCandidate(Queue<UserJson> userJsonQueue) {
        UserJson candidateForTest = null;
        long start = System.currentTimeMillis();
        long end = start + 50 * 1000;

        while (System.currentTimeMillis() < end && candidateForTest == null) {
            candidateForTest = userJsonQueue.poll();
        }
        return candidateForTest;
    }

    private void prepareUserForTestAndStore(Parameter parameter, ExtensionContext context) throws TimeoutException {
        UserQueue parameterAnnotations = parameter.getAnnotation(UserQueue.class);
        UserQueue.UserType userType = parameterAnnotations.userType();
        Queue<UserJson> userJsonQueue = usersTypeQueueMap.get(userType);
        UserJson candidateForTest = findCandidate(userJsonQueue);

        if (candidateForTest == null) {
            throw new TimeoutException();
        }

        String originalUsername = candidateForTest.getUsername();
        String swappedUsername = usernameSwapMap.getOrDefault(originalUsername, originalUsername);
        candidateForTest.setUsernamePeek(swappedUsername);
        candidateForTest.setUserType(userType);
        context.getStore(NAMESPACE).put(context.getUniqueId(), candidateForTest);

        System.out.println("beforeEach - " + candidateForTest.getUsername());
    }
}
