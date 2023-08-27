package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotations.Category;
import guru.qa.niffler.jupiter.annotations.Spend;
import guru.qa.niffler.jupiter.annotations.User;
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
import static guru.qa.niffler.jupiter.annotations.User.UserType.WITH_FRIENDS;

public class SpendingWebTest extends BaseWebTest{

    private final String USERNAME = "dima";
    private final String PASSWORD = "12345";
    private final String CATEGORY = "Спорт";
    private final String DESCRIPTION = "Бег по Ладоге";
    private final double AMOUNT = 12000.00;


    static {
        Configuration.browser = "chrome";
        Configuration.browserSize = "1980x1024";
    }

    @BeforeEach
    void doLogin(@User(userType = WITH_FRIENDS ) UserJson userForTest) {
        Selenide.open("http://127.0.0.1:3000/main");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(userForTest.getUsername());
        $("input[name='password']").setValue(userForTest.getPassword());
        $("button[type='submit']").click();
    }
    @Category(
            username = USERNAME,
            category = CATEGORY
    )


    @Spend(
            username = USERNAME,
            description = DESCRIPTION,
            category = CATEGORY,
            amount = AMOUNT,
            currency = CurrencyValues.RUB
    )

    @Test
    @AllureId("101")
    void spendingShouldBeDeletedAfterDeleteAction(SpendJson createdSpend,@User(userType = WITH_FRIENDS ) UserJson userForTest) {

        Allure.step("Search spend", () ->
                $(".spendings__content tbody")
                .$$("tr")
                .find(text(createdSpend.getDescription()))
                .$("td")
                .scrollTo()
                .click());

        Allure.step("Delete select spend", () ->
                $(byText("Delete selected12")).click());

        Allure.step("Check clear spend", () ->
                $(".spendings__content tbody")
                .$$("tr")
                .shouldHave(size(0)));
    }
}
