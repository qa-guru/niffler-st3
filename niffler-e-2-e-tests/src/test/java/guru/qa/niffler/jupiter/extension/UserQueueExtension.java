package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.*;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;


public class UserQueueExtension implements BeforeEachCallback, AfterTestExecutionCallback, ParameterResolver {

	public static ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UserQueueExtension.class);

	private static final Map<User.UserType, Queue<UserJson>> usersQueue = new ConcurrentHashMap<>();

	static {
		Queue<UserJson> usersWithFriends = new ConcurrentLinkedQueue<>();
		usersWithFriends.add(bindUser("ivanov", "12345678"));
		usersWithFriends.add(bindUser("irina", "12345678"));
		usersQueue.put(User.UserType.WITH_FRIENDS, usersWithFriends);
		Queue<UserJson> usersInSend = new ConcurrentLinkedQueue<>();
		usersInSend.add(bindUser("ivanov146", "12345678"));
		usersInSend.add(bindUser("marina", "12345678"));
		usersQueue.put(User.UserType.INVITATION_SEND, usersInSend);
		Queue<UserJson> usersInRc = new ConcurrentLinkedQueue<>();
		usersInRc.add(bindUser("sofia", "12345678"));
		usersInRc.add(bindUser("Mihail", "12345678"));
		usersQueue.put(User.UserType.INVITATION_RECEIVED, usersInRc);
	}

	@Override
	public void beforeEach(ExtensionContext extensionContext) {
		Parameter[] parameters;
		Optional<Method> beforeEachMethod =  Arrays.stream(extensionContext.getRequiredTestClass().getDeclaredMethods())
				.filter(method -> method.isAnnotationPresent(BeforeEach.class)).findFirst();
		if(beforeEachMethod.isPresent()) {
			parameters = beforeEachMethod.get().getParameters();
		} else {
			parameters = extensionContext.getRequiredTestMethod().getParameters();
		}

		Map<User.UserType, UserJson> candidatesForTest = new HashMap<>();
		for (Parameter parameter : parameters) {
			if (parameter.getType().isAssignableFrom(UserJson.class) && parameter.isAnnotationPresent(User.class)) {
				User parameterAnnotation = parameter.getAnnotation(User.class);
				User.UserType userType = parameterAnnotation.userType();
				Queue<UserJson> userQueueByType = usersQueue.get(userType);
				UserJson candidateForTest = null;
				while (candidateForTest == null) {
					candidateForTest = userQueueByType.poll();
				}
				candidateForTest.setUserType(userType);
				candidatesForTest.put(userType, candidateForTest);
			}
		}
		extensionContext.getStore(NAMESPACE).put(extensionContext.getUniqueId(), candidatesForTest);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void afterTestExecution(ExtensionContext extensionContext) {
		Map<User.UserType, UserJson> usersFromTest = extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), Map.class);
		for (User.UserType userType : usersFromTest.keySet()) {
			usersQueue.get(userType).add(usersFromTest.get(userType));
		}
	}

	@Override
	public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
		return parameterContext.getParameter().getType().isAssignableFrom(UserJson.class)
				&& parameterContext.getParameter().isAnnotationPresent(User.class);
	}

	@Override
	public UserJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
		User.UserType userType = parameterContext.getParameter().getAnnotation(User.class).userType();
		return (UserJson) extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), Map.class).get(userType);
	}

	private static UserJson bindUser(String userName, String password) {
		UserJson user = new UserJson();
		user.setUsername(userName);
		user.setPassword(password);
		return user;
	}
}
