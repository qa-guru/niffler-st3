package guru.qa.niffler.jupiter.category;

import guru.qa.niffler.api.CategoryService;
import guru.qa.niffler.model.CategoryJson;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.extension.*;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class CategoryExtension implements BeforeEachCallback {

    private static final OkHttpClient httpClient = new OkHttpClient.Builder().build();

    private static final Retrofit retrofit = new Retrofit.Builder()
            .client(httpClient)
            .baseUrl("http://127.0.0.1:8093")
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private CategoryService categoryService = retrofit.create(CategoryService.class);

    public static ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        Category annotation = context.getRequiredTestMethod().getAnnotation(Category.class);
        if (annotation != null) {
            CategoryJson category = new CategoryJson();
            category.setUsername(annotation.username());
            category.setCategory(annotation.category());
            CategoryJson createdCategory = categoryService.addCategory(category).execute().body();
            context.getStore(NAMESPACE).put("category", createdCategory);
        }
    }
}
