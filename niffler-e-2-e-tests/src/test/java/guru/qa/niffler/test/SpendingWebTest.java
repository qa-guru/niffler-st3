package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.category.Category;
import guru.qa.niffler.jupiter.spend.Spend;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class SpendingWebTest {

    static {
        Configuration.browser = "chrome";
        Configuration.browserSize = "1980x1024";
    }

    @BeforeEach
    void doLogin() {
        Selenide.open("http://127.0.0.1:3000/main");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue("admin");
        $("input[name='password']").setValue("admin");
        $("button[type='submit']").click();
    }

    @Category(
            username = "admin",
            category = "Охота"
    )
    @Spend(
            username = "admin",
            description = "Рыбалка на Ладоге",
            category = "Охота",
            amount = 14000.00,
            currency = CurrencyValues.RUB
    )
    @Test
    void spendingShouldBeDeletedAfterDeleteAction(SpendJson createdSpend) {
        $(".spendings__content tbody")
                .$$("tr")
                .find(text(createdSpend.getDescription()))
                .$$("td")
                .first()
                .scrollTo()
                .click();

        $(byText("Delete selected")).click();

        $(".spendings__content tbody")
                .$$("tr")
                .shouldHave(size(0));
    }
}
