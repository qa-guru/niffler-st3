package guru.qa.niffler.jupiter.extensions;

import guru.qa.niffler.api.CategoryService;
import guru.qa.niffler.jupiter.annotations.Spend;
import guru.qa.niffler.model.CategoryJson;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.extension.*;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class CategoryExtension implements BeforeEachCallback, ParameterResolver {
    public static ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);
    private static final OkHttpClient httpClient = new OkHttpClient.Builder().build();
    private static final Retrofit retrofit = new Retrofit.Builder()
            .client(httpClient)
            .baseUrl("http://127.0.0.1:8093")
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private CategoryService categoryService = retrofit.create(CategoryService.class);

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        Spend annotation = context.getRequiredTestMethod().getAnnotation(Spend.class);
        if (annotation != null) {
            CategoryJson category = new CategoryJson();
            category.setUsername(annotation.username());
            category.setCategory(annotation.category());
            CategoryJson createdCategory = categoryService.addCategory(category).execute().body();
            context.getStore(NAMESPACE).put("category", createdCategory);
        }

    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter()
                .getType()
                .isAssignableFrom(CategoryJson.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext
                .getStore(CategoryExtension.NAMESPACE)
                .get("category", CategoryJson.class);
    }
}
