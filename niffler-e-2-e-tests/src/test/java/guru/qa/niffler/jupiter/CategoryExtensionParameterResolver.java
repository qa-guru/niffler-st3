package guru.qa.niffler.jupiter;

import guru.qa.niffler.model.CategoryJson;
import org.junit.jupiter.api.extension.*;

public class CategoryExtensionParameterResolver implements ParameterResolver {
    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter()
                .getType()
                .isAssignableFrom(CategoryJson.class);
    }

    @Override
    public CategoryJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext
                .getStore(CategoryExtension.NAMESPACE)
                .get("category", CategoryJson.class);
    }
}
