package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CandidateInfoForFriendsTestDto;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.*;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import static java.util.Arrays.asList;

public class UserQueueExtension implements BeforeEachCallback, AfterTestExecutionCallback, ParameterResolver {

    public static ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UserQueueExtension.class);

    private static Map<User.UserType, Queue<UserJson>> usersQueue = new ConcurrentHashMap<>();

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
        List<Parameter> fullParametersList = new ArrayList<>(asList(context.getRequiredTestMethod().getParameters()));

        Optional<Method> beforeEachMethod = Arrays.stream(context.getRequiredTestClass().getDeclaredMethods())
                .filter(
                        method -> method.isAnnotationPresent(BeforeEach.class)).findFirst();
        beforeEachMethod.ifPresent(method -> fullParametersList.addAll(asList(method.getParameters())));

        Map<CandidateInfoForFriendsTestDto, UserJson> candidatesForTest = new ConcurrentHashMap<>();

        for (Parameter parameter : fullParametersList) {
            if (parameter.getType().isAssignableFrom(UserJson.class) && parameter.isAnnotationPresent(User.class)) {
                User parameterAnnotation = parameter.getAnnotation(User.class);
                User.UserType userType = parameterAnnotation.userType();
                Queue<UserJson> usersQueueByType = usersQueue.get(userType);
                UserJson candidateForTest = null;
                while (candidateForTest == null) {
                    candidateForTest = usersQueueByType.poll();
                }
                candidateForTest.setUserType(userType);

                CandidateInfoForFriendsTestDto candidateInfo = new CandidateInfoForFriendsTestDto();
                candidateInfo.setParameterName(parameter.getName());
                candidateInfo.setUserType(userType);

                candidatesForTest.put(
                        candidateInfo,
                        candidateForTest);

                context.getStore(NAMESPACE).put(getAllureId(context), candidatesForTest);
            }
        }
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        Map<CandidateInfoForFriendsTestDto, UserJson> usersFromTest = context.getStore(NAMESPACE).get(getAllureId(context), Map.class);
        for (CandidateInfoForFriendsTestDto userType : usersFromTest.keySet()) {
            usersQueue.get(userType.getUserType()).add(usersFromTest.get(userType));
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(UserJson.class)
                && parameterContext.getParameter().isAnnotationPresent(User.class);
    }

    @Override
    public UserJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        CandidateInfoForFriendsTestDto key = new CandidateInfoForFriendsTestDto();
        key.setUserType(parameterContext.getParameter().getAnnotation(User.class).userType());
        key.setParameterName(parameterContext.getParameter().getName());

        return (UserJson) extensionContext.getStore(NAMESPACE).get(getAllureId(extensionContext), Map.class).get(key);


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

    //////////////

  /*  @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        List<Parameter> fullParametersList = asList(context.getRequiredTestMethod().getParameters());
        if (fullParametersList.isEmpty()) {
            Optional<Method> beforeEachMethod = Arrays.stream(context.getRequiredTestClass().getDeclaredMethods())
                    .filter(
                            method -> method.isAnnotationPresent(BeforeEach.class)).findFirst();
            beforeEachMethod.ifPresent(method -> fullParametersList.addAll(asList(method.getParameters())));
        }
        ///  Map<User.UserType, UserJson> candidatesForTest = new ConcurrentHashMap<>();
        Map<Pair<User.UserType, String>, UserJson> candidatesForTest = new ConcurrentHashMap<>();

        for (Parameter parameter : fullParametersList) {
            if (parameter.getType().isAssignableFrom(UserJson.class) && parameter.isAnnotationPresent(User.class)) {
                UserJson candidateForTest = setCandidateForTest(parameter);
                ///    context.getStore(NAMESPACE).put(getAllureId(context), candidateForTest);
                //// break;
                candidatesForTest.put(Pair.of(parameter.getAnnotation(User.class).userType(), parameter.getName()), candidateForTest);

                context.getStore(NAMESPACE).put(getAllureId(context), candidatesForTest);
            }
        }
    }

    private static UserJson setCandidateForTest(Parameter parameter) {
        User parameterAnnotation = parameter.getAnnotation(User.class);
        User.UserType userType = parameterAnnotation.userType();
        Queue<UserJson> usersQueueByType = usersQueue.get(userType);
        UserJson candidateForTest = null;
        while (candidateForTest == null) {
            candidateForTest = usersQueueByType.poll();
        }
        candidateForTest.setUserType(userType);
        return candidateForTest;
    }*/


}
