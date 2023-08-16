package guru.qa.niffler.jupiter;

import guru.qa.niffler.api.CategoryService;
import guru.qa.niffler.model.CategoryJson;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class CategoryExtension implements BeforeEachCallback {

    private static final OkHttpClient httpClient = new OkHttpClient.Builder().build();
    private static final Retrofit retrofit = new Retrofit.Builder()
            .client(httpClient)
            .baseUrl("http://127.0.0.1:8093")
            .addConverterFactory(JacksonConverterFactory.create())
            .build();
    public static ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);
    private final CategoryService categoryService = retrofit.create(CategoryService.class);

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        var annotation = context.getRequiredTestMethod().getAnnotation(Category.class);
        if (annotation != null) {
            var category = new CategoryJson();
            category.setCategory(annotation.category());
            category.setUsername(annotation.username());
            var createdCategory = categoryService.addCategory(category).execute().body();
            context.getStore(NAMESPACE).put("category", createdCategory);
        }
    }
}
