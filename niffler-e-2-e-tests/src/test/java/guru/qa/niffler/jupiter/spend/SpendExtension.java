package guru.qa.niffler.jupiter.spend;

import guru.qa.niffler.api.SpendService;
import guru.qa.niffler.model.SpendJson;
import org.junit.jupiter.api.extension.*;

import java.util.Date;

import static guru.qa.niffler.api.RetroHelper.NAMESPACE;
import static guru.qa.niffler.api.RetroHelper.RETROFIT;

public class SpendExtension implements BeforeEachCallback, ParameterResolver {

    private final SpendService spendService = RETROFIT.create(SpendService.class);

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        Spend annotation = context.getRequiredTestMethod().getAnnotation(Spend.class);
        if (annotation != null) {
            SpendJson spend = new SpendJson()
                .username(annotation.username())
                .description(annotation.description())
                .amount(annotation.amount())
                .category(annotation.category())
                .spendDate(new Date())
                .currency(annotation.currency());
            SpendJson createdSpend = spendService.addSpend(spend).execute().body();
            context.getStore(NAMESPACE).put("spend", createdSpend);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter()
            .getType()
            .isAssignableFrom(SpendJson.class);
    }

    @Override
    public SpendJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext
            .getStore(NAMESPACE)
            .get("spend", SpendJson.class);
    }
}
