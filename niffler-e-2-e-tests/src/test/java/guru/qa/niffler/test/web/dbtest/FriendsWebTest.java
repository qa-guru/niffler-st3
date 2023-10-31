package guru.qa.niffler.test.web.dbtest;


import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.test.web.BaseWebTest;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;
import static guru.qa.niffler.jupiter.annotation.User.UserType.WITH_FRIENDS;
import static io.qameta.allure.Allure.step;

public class FriendsWebTest extends BaseWebTest {

    @BeforeEach
    void doLogin(@User(userType = WITH_FRIENDS) UserJson userForTest) {
        Selenide.open("http://127.0.0.1:3000/main");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(userForTest.getUsername());
        $("input[name='password']").setValue(userForTest.getPassword());
        $("button[type='submit']").click();
    }

    @Test
    @AllureId("101")
    void friendShouldBeDisplayedInTable0(@User(userType = WITH_FRIENDS) UserJson userForTest) throws InterruptedException {

        step("Переходим на вкладку Друзья", () ->
                $x("//li[@data-tooltip-id='friends']").click());

        step("Проверяем наличие 1 друга", () ->
                $("tbody")
                        .$$("tr")
                        .shouldHave(size(1)));

        step("Проверяем наличие статуса - You are friends", () ->
                $x("//div[@class='abstract-table__buttons']//div").shouldHave(Condition.text("You are friends")));
    }

    @Test
    @AllureId("102")
    void friendShouldBeDisplayedInTable1(@User(userType = WITH_FRIENDS) UserJson userForTest) throws InterruptedException {

        step("Переходим на вкладку Друзья", () ->
                $x("//li[@data-tooltip-id='friends']").click());

        step("Проверяем наличие 1 друга", () ->
                $("tbody")
                        .$$("tr")
                        .shouldHave(size(1)));

        step("Проверяем наличие статуса - You are friends", () ->
                $x("//div[@class='abstract-table__buttons']//div").shouldHave(Condition.text("You are friends")));
    }

    @Test
    @AllureId("103")
    void friendShouldBeDisplayedInTable2(@User(userType = WITH_FRIENDS) UserJson userForTest) throws InterruptedException {

        step("Переходим на вкладку Друзья", () ->
                $x("//li[@data-tooltip-id='friends']").click());

        step("Проверяем наличие 1 друга", () ->
                $("tbody")
                        .$$("tr")
                        .shouldHave(size(1)));

        step("Проверяем наличие статуса - You are friends", () ->
                $x("//div[@class='abstract-table__buttons']//div").shouldHave(Condition.text("You are friends")));
    }
}
