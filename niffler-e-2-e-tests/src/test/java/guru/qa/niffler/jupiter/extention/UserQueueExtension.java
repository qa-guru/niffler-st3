package guru.qa.niffler.jupiter.extention;

import guru.qa.niffler.jupiter.annotations.User;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.extension.*;

import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class UserQueueExtension implements BeforeEachCallback,BeforeTestExecutionCallback, AfterTestExecutionCallback, ParameterResolver {

    public static ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UserQueueExtension.class);
    private static Map<User.UserType, Queue<UserJson>> usersQueue = new ConcurrentHashMap<>();
    static {
        Queue<UserJson> usersWithFriends = new ConcurrentLinkedQueue<>();
        usersWithFriends.add(bindUser("UNO","12345"));
        usersWithFriends.add(bindUser("nik","12345"));
        usersQueue.put(User.UserType.WITH_FRIENDS,usersWithFriends);

        Queue<UserJson> usersWithSendInvite = new ConcurrentLinkedQueue<>();
        usersWithSendInvite.add(bindUser("alina","12345"));
        usersWithSendInvite.add(bindUser("igor","12345"));
        usersQueue.put(User.UserType.WITH_SEND_INVITE,usersWithFriends);

        Queue<UserJson> usersWithApproveInvite = new ConcurrentLinkedQueue<>();
        usersWithApproveInvite.add(bindUser("dima","12345"));
        usersWithApproveInvite.add(bindUser("leon","12345"));
        usersQueue.put(User.UserType.WITH_APPROVE_INVITE,usersWithFriends);
    }
    @Override
    public void beforeEach(ExtensionContext context) throws Exception {

        Parameter[] parameters = context.getRequiredTestMethod().getParameters();
        for (Parameter parameter: parameters){
            if(parameter.getType().isAssignableFrom(UserJson.class)) {
                User parameterAnnotation = parameter.getAnnotation(User.class);
                User.UserType userType = parameterAnnotation.userType();
                Queue<UserJson> userQueueByType = usersQueue.get(parameterAnnotation.userType());
                UserJson candidateForTest = null;
                while (candidateForTest == null){
                    candidateForTest = userQueueByType.poll();
                }
                candidateForTest.setUserType(userType);
                context.getStore(NAMESPACE).put(getAllureId(context),candidateForTest);

                break;

                }
            }
        }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        UserJson userFromTest = context.getStore(NAMESPACE).get(getAllureId(context), UserJson.class);
        usersQueue.get(userFromTest.getUserType()).add(userFromTest);
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(UserJson.class)
                && parameterContext.getParameter().isAnnotationPresent(User.class);
    }

    @Override
    public UserJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(getAllureId(extensionContext), UserJson.class);
    }



    private String getAllureId(ExtensionContext context) {
        AllureId allureId = context.getRequiredTestMethod().getAnnotation(AllureId.class);
        if (allureId == null) {
            throw  new IllegalStateException("Annotations @AllureId must be present!");
        }
        return allureId.value();
    }
    private static UserJson bindUser(String name, String password){
        UserJson userJson = new UserJson();
        userJson.setUsername(name);
        userJson.setPassword(password);
        return userJson;

    }

    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {
        Parameter[] parameters = context.getRequiredTestMethod().getParameters();
        for (Parameter parameter: parameters){
            if(parameter.getType().isAssignableFrom(UserJson.class)) {
                User parameterAnnotation = parameter.getAnnotation(User.class);
                User.UserType userType = parameterAnnotation.userType();
                Queue<UserJson> userQueueByType = usersQueue.get(parameterAnnotation.userType());
                UserJson candidateForTest = null;
                while (candidateForTest == null){
                    candidateForTest = userQueueByType.poll();
                }
                candidateForTest.setUserType(userType);
                context.getStore(NAMESPACE).put(getAllureId(context),candidateForTest);

                break;

            }
        }

    }
}
