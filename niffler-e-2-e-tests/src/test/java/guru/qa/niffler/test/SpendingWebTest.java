package guru.qa.niffler.test;

import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.Spend;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static guru.qa.niffler.jupiter.annotation.User.UserType.WITH_FRIENDS;
import static io.qameta.allure.Allure.step;

public class SpendingWebTest extends BaseWebTest {

    private final String USER_NAME = "dima";
    private final String CATEGORY = "Рыбалка";
    private final String DESCRIPTION = "Рыбалка на Ладоге";
    private final double AMOUNT = 14000.00;

    private static final String user = "dima";

    @BeforeEach
    void doLogin(@User(userType = WITH_FRIENDS) UserJson userForTest) {
        step("Открыть страницу авторизации", () -> open("http://127.0.0.1:3000/main"));
        step("Открыть страницу авторизации", () -> $("a[href*='redirect']").click());
        step("Ввести логин", () -> $("input[name='username']").setValue(userForTest.getUsername()));
        step("Ввести пароль", () -> $("input[name='password']").setValue(userForTest.getPassword()));
        step("Нажать кнопку 'Sign in'", () -> $("button[type='submit']").click());
    }


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
    @AllureId("100")
    void spendingShouldBeDeletedAfterDeleteAction(SpendJson createdSpend,
                                                  @User(userType = WITH_FRIENDS) UserJson userForTest) {
        $(".spendings__content tbody")
                .$$("tr")
                .find(text(createdSpend.getDescription()))
                .$("td")
                .scrollTo()
                .click();

        step(
                "Delete spending",
                () -> $(byText("Delete selected")).click());

        step(
                "Check spendings",
                () -> $(".spendings__content tbody")
                        .$$("tr")
                        .shouldHave(size(0)));
    }
}
