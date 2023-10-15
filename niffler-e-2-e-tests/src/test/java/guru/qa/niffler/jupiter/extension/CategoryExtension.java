package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.CategoryService;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.model.CategoryJson;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;

public class CategoryExtension implements BeforeEachCallback {

    private static final OkHttpClient httpClient = new OkHttpClient.Builder().build();
    private static final Retrofit retrofit =
            new Retrofit.Builder()
                    .client(httpClient)
                    .baseUrl("http://127.0.0.1:8093")
                    .addConverterFactory(JacksonConverterFactory.create())
                    .build();

    private final CategoryService categoryService = retrofit.create(CategoryService.class);

    @Override
    public void beforeEach(ExtensionContext context) throws IOException {
        Category annotation = context.getRequiredTestMethod().getAnnotation(Category.class);
        if (annotation != null) {
            CategoryJson category = new CategoryJson();
            category.setUsername(annotation.username());
            category.setCategory(annotation.category());

            categoryService.addCategory(category).execute().body();
        }
    }
}
