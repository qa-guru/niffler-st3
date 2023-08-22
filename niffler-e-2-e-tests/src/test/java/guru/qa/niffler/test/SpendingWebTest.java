package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotations.Category;
import guru.qa.niffler.jupiter.annotations.Spend;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class SpendingWebTest {
    private final String
            USER_NAME = "Kirill",
            PASSWORD = "12345",
            DESCRIPTION = "Ужин в ресторане",
            CATEGORY = "Ресторан";
    private final double AMOUNT = 7000.00;


    static {
        Configuration.browserSize = "1980x1024";
    }

    @BeforeEach
    void doLogin() {
        Selenide.open("http://127.0.0.1:3000/main");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(USER_NAME);
        $("input[name='password']").setValue(PASSWORD);
        $("button[type='submit']").click();
    }

    @Category(
            username = USER_NAME,
            category = CATEGORY
    )
    @Spend(username = USER_NAME,
            description = DESCRIPTION,
            category = CATEGORY,
            amount = AMOUNT,
            currency = CurrencyValues.RUB)
    @Test
    void spendingShouldBeDeletedAfterDeleteAction(SpendJson createdSpend) {
        $(".spendings__content tbody")
                .$$("tr")
                .find(text(createdSpend.getDescription()))
                .$("td")
                .scrollTo()
                .click();

        $(byText("Delete selected")).click();

        $(".spendings__content tbody")
                .$$("tr")
                .shouldHave(size(0));
    }
}
