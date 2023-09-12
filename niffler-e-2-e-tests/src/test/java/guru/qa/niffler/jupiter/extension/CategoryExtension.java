package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.SpendServiceClient;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.model.CategoryJson;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class CategoryExtension implements BeforeEachCallback {

    public static ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);

    private SpendServiceClient spendServiceClient = new SpendServiceClient();

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        Category annotation = extensionContext.getRequiredTestMethod().getAnnotation(Category.class);
        if (annotation != null) {
            CategoryJson category = new CategoryJson();
            category.setCategory(annotation.category());
            category.setUsername(annotation.username());

            CategoryJson createdCategory = spendServiceClient.addCategory(category);
            extensionContext.getStore(NAMESPACE).put("category", createdCategory);
        }
    }
}
