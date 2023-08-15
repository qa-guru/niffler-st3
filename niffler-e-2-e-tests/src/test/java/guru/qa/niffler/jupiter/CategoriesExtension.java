package guru.qa.niffler.jupiter;

import guru.qa.niffler.api.CategoriesService;
import guru.qa.niffler.model.CategoryJson;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class CategoriesExtension implements BeforeEachCallback {

    public static ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoriesExtension.class);

    private static final OkHttpClient httpClient = new OkHttpClient.Builder().build();
    private static final Retrofit retrofit = new Retrofit.Builder()
            .client(httpClient)
            .baseUrl("http://127.0.0.1:8093")
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private CategoriesService categoriesService = retrofit.create(CategoriesService.class);

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        Categories annotation = context.getRequiredTestMethod().getAnnotation(Categories.class);
        if (annotation != null) {
            CategoryJson categoryJson = new CategoryJson();
            categoryJson.setUsername(annotation.username());
            categoryJson.setCategory(annotation.category());

            CategoryJson createCategoryJson = categoriesService.addCategories(categoryJson).execute().body();
            context.getStore(NAMESPACE).put("category", createCategoryJson);
        }
    }
}
