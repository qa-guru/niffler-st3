package guru.qa.niffler.api;

import guru.qa.niffler.model.SpendJson;
import io.qameta.allure.Step;

import java.io.IOException;

public class SpendServiceClient extends RestService {
    public SpendServiceClient() {
        super(config.nifflerSpendUrl());
    }

    private final SpendService spendService = retrofit.create(SpendService.class);

    @Step("Create spend")
    public SpendJson addSpend(SpendJson spend) throws IOException {
        return spendService.addSpend(spend)
                .execute()
                .body();
    }
}
