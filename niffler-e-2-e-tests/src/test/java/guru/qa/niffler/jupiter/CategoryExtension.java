package guru.qa.niffler.jupiter;

import guru.qa.niffler.api.CategoriesService;
import guru.qa.niffler.model.CategoryJson;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.extension.*;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class CategoryExtension implements BeforeEachCallback, ParameterResolver {
    public static ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);

    private static final OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
    private static final Retrofit retrofit = new Retrofit.Builder().
            client(okHttpClient)
            .baseUrl("http://127.0.0.1:8093")
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private CategoriesService categoriesService = retrofit.create(CategoriesService.class);

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        Category annotation = context.getRequiredTestMethod().getAnnotation(Category.class);
        if (annotation != null) {
            CategoryJson categoryJson = new CategoryJson();
            categoryJson.setCategory(annotation.username());
            categoryJson.setUsername(annotation.username());
            CategoryJson createdCategory = categoriesService.addCategory(categoryJson).execute().body();
            context.getStore(NAMESPACE).put("category", createdCategory);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(CategoryJson.class);
    }

    @Override
    public CategoryJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get("category", CategoryJson.class);
    }
}
