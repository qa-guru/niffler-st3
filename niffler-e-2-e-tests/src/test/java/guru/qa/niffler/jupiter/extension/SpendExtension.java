package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.SpendServiceClient;
import guru.qa.niffler.jupiter.annotation.Spend;
import guru.qa.niffler.model.SpendJson;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.Date;

public class SpendExtension implements BeforeEachCallback {

    public static ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(SpendExtension.class);


    private SpendServiceClient spendService = new SpendServiceClient();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        Spend annotation = context.getRequiredTestMethod().getAnnotation(Spend.class);
        if (annotation != null) {
            SpendJson spend = new SpendJson();
            spend.setUsername(annotation.username());
            spend.setDescription(annotation.description());
            spend.setAmount(annotation.amount());
            spend.setCategory(annotation.category());
            spend.setSpendDate(new Date());
            spend.setCurrency(annotation.currency());
            SpendJson createdSpend = spendService.addSpend(spend);
            context.getStore(NAMESPACE).put("spend", createdSpend);
        }
    }
}
