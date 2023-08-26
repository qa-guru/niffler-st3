package guru.qa.niffler.jupiter.dbUser;

import guru.qa.niffler.db.model.UserEntity;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class DBUserExtensionParameterResolver implements ParameterResolver {

	@Override
	public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
		return parameterContext.getParameter()
				.getType()
				.isAssignableFrom(UserEntity.class);
	}

	@Override
	public UserEntity resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
		return extensionContext
				.getStore(DBUserExtension.NAMESPACE)
				.get("user", UserEntity.class);
	}
}
