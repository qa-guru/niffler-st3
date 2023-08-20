package guru.qa.niffler.jupiter.category;

import guru.qa.niffler.api.CategoryService;
import guru.qa.niffler.model.CategoryJson;
import org.junit.jupiter.api.extension.*;

import static guru.qa.niffler.api.RetroHelper.NAMESPACE;
import static guru.qa.niffler.api.RetroHelper.RETROFIT;

public class CategoryExtension implements BeforeEachCallback, ParameterResolver {

    private final CategoryService categoryService = RETROFIT.create(CategoryService.class);

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        Category annotation = context.getRequiredTestMethod().getAnnotation(Category.class);
        if (annotation != null) {
            CategoryJson category = new CategoryJson()
                .username(annotation.username())
                .category(annotation.category());
            CategoryJson createdCategory = categoryService.addCategory(category).execute().body();
            context.getStore(NAMESPACE).put("category", createdCategory);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
        throws ParameterResolutionException {
        return parameterContext.getParameter()
            .getType()
            .isAssignableFrom(CategoryJson.class);
    }

    @Override
    public CategoryJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
        throws ParameterResolutionException {
        return extensionContext
            .getStore(NAMESPACE)
            .get("category", CategoryJson.class);
    }
}
