package guru.qa.niffler.test;

import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.Spend;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static guru.qa.niffler.jupiter.annotation.User.UserType.WITH_FRIENDS;
import static guru.qa.niffler.test.pages.LoginPage.doLogin;

@Disabled
class SpendingWebTest extends BaseWebTest {

    private static final String user = "dima";

    @BeforeEach
    void beforeEach(@User(userType = WITH_FRIENDS) UserJson userForTest) {
        doLogin(userForTest);
    }

    @Category(
            username = user,
            category = "Рыбалка"
    )
    @Spend(
            username = user,
            description = "Рыбалка на Ладоге",
            category = "Рыбалка",
            amount = 14000.00,
            currency = CurrencyValues.RUB
    )
    @Test
    @AllureId("100")
    void spendingShouldBeDeletedAfterDeleteAction(SpendJson createdSpend,
                                                  @User(userType = WITH_FRIENDS) UserJson userForTest) {
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
