package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.Category;
import guru.qa.niffler.jupiter.Spend;
import guru.qa.niffler.jupiter.User;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static guru.qa.niffler.jupiter.User.UserType.WITH_FRIENDS;

public class SpendingWebTest extends BaseWebTest {

    static {
        Configuration.browser = "chrome";
        Configuration.browserSize = "1920x1080";
    }

    private static final String USERNAME = "dima";
    private static final String PASSWORD = "12345";
    private static final String CATEGORY = "Рыбалка";

    @BeforeEach
    void doLogin() {
        Selenide.open("http://127.0.0.1:3000/main");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(USERNAME);
        $("input[name='password']").setValue(PASSWORD);
        $("button[type='submit']").click();
    }

    @Category(
            username = USERNAME,
            category = CATEGORY
    )

    @Spend(
            username = USERNAME,
            description = "Рыбалка на Ладоге",
            category = CATEGORY,
            amount = 14000.00,
            currency = CurrencyValues.RUB
    )
    @Test
    @AllureId("100")
    void spendingShouldBeDeletedAfterDeleteAction(SpendJson createdSpend) {
        $(".spendings__content tbody")
                .$$("tr")
                .find(text(createdSpend.getDescription()))
                .$$("td")
                .first()
                .scrollTo()
                .click();

        Allure.step(
                "Delete spending",
                () -> $(byText("Delete selected")).click())
        ;

        Allure.step(
                "Check spendings",
                () -> $(".spendings__content tbody")
                        .$$("tr")
                        .shouldHave(size(0))
        );
    }
}
