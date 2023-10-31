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
import static guru.qa.niffler.jupiter.annotation.User.UserType.INVITATION_SENT;
import static guru.qa.niffler.jupiter.annotation.User.UserType.WITH_FRIENDS;
import static io.qameta.allure.Allure.step;

public class TwoUsersTest extends BaseWebTest {
    @BeforeEach
    void doLogin(@User(userType = WITH_FRIENDS) UserJson user1ForTest,@User(userType = INVITATION_SENT) UserJson user2ForTest) {
        Selenide.open("http://127.0.0.1:3000/main");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(user1ForTest.getUsername());
        $("input[name='password']").setValue(user1ForTest.getPassword());
        $("button[type='submit']").click();
    }

    @Test
    @AllureId("111")
    void friendShouldBeDisplayedInTable0(@User(userType = WITH_FRIENDS) UserJson user1ForTest,
                                         @User(userType = INVITATION_SENT) UserJson user2ForTest) throws InterruptedException {

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
