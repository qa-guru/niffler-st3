package guru.qa.niffler.test;

import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.Spend;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Test;

public class SpendingWebTest extends BaseWebTest {

    private final String USER_NAME = "dima";
    private final String CATEGORY = "Рыбалка";
    private final String DESCRIPTION = "Рыбалка на Ладоге";
    private final double AMOUNT = 14000.00;

    private static final String user = "dima";


    @Category(
            username = USER_NAME,
            category = CATEGORY
    )
    @Spend(
            username = user,
            description = DESCRIPTION,
            category = "Рыбалка",
            amount = AMOUNT,
            currency = CurrencyValues.RUB
    )
    @Test
    @AllureId("200")
    void spendingShouldBeDeletedAfterDeleteAction(SpendJson createdSpend) {
        loginPage.login(user, defaultPassword)
                .setCheckboxForSpendingWithDescription(createdSpend.getDescription())
                .clickDeleteBtn()
                .checkTableSpendingIsEmpty(createdSpend.getDescription());
    }
}
