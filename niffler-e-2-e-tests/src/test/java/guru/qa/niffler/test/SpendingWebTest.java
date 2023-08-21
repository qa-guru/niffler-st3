package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.Category;
import guru.qa.niffler.jupiter.Spend;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import org.junit.jupiter.api.AfterEach;
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
        Configuration.pageLoadStrategy = "eager";
    }

    @BeforeEach
    void doLogin() {
        Selenide.open("http://127.0.0.1:3000/main");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue("SLomako");
        $("input[name='password']").setValue("12345");
        $("button[type='submit']").click();
    }

    @AfterEach
    void tearDown() {
        Selenide.closeWebDriver();
    }

    @Category(
            username = "SLomako",
            category = "Стрельба"
    )
    @Spend(
            username = "SLomako",
            description = "Соревнования",
            category = "Стрельба",
            amount = 10000.00,
            currency = CurrencyValues.RUB
    )
    @Test
    void spendingShouldBeDeletedAfterDeleteAction(SpendJson createdSpend) {
        $(".spendings__content tbody")
                .$$("tr")
                .findBy(text(createdSpend.getDescription()))
                .$("td", 0)
                .scrollIntoView(true)
                .click();

        $(byText("Delete selected")).click();

        $(".spendings__content tbody")
                .$$("tr")
                .shouldHave(size(0));
    }
}