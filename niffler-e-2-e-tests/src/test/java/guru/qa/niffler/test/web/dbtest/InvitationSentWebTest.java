package guru.qa.niffler.test.web.dbtest;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.test.web.BaseWebTest;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;
import static guru.qa.niffler.jupiter.annotation.User.UserType.INVITATION_SENT;
import static io.qameta.allure.Allure.step;

public class InvitationSentWebTest extends BaseWebTest {
    @BeforeEach
    void doLogin(@User(userType = INVITATION_SENT) UserJson userForTest) {
        Selenide.open("http://127.0.0.1:3000/main");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(userForTest.getUsername());
        $("input[name='password']").setValue(userForTest.getPassword());
        $("button[type='submit']").click();
    }

    @Test
    @AllureId("104")
    void invitationSentShouldBeDisplayedInTable0(@User(userType = INVITATION_SENT) UserJson userForTest) throws InterruptedException {

        step("Переходим на вкладку Люди", () ->
                $x("//li[@data-tooltip-id='people']").click());

        step("Проверяем, что отправлено 1 приглашение", () ->
                $("tbody")
                        .$$("tr")
                        .filter(text("Pending invitation"))
                        .shouldHave(size(1)));
    }

    @Test
    @AllureId("105")
    void invitationSentShouldBeDisplayedInTable1(@User(userType = INVITATION_SENT) UserJson userForTest) throws InterruptedException {

        step("Переходим на вкладку Люди", () ->
                $x("//li[@data-tooltip-id='people']").click());

        step("Проверяем, что отправлено 1 приглашение", () ->
                $("tbody")
                        .$$("tr")
                        .filter(text("Pending invitation"))
                        .shouldHave(size(1)));
    }

    @Test
    @AllureId("106")
    void invitationSentShouldBeDisplayedInTable2(@User(userType = INVITATION_SENT) UserJson userForTest) throws InterruptedException {

        step("Переходим на вкладку Люди", () ->
                $x("//li[@data-tooltip-id='people']").click());

        step("Проверяем, что отправлено 1 приглашение", () ->
                $("tbody")
                        .$$("tr")
                        .filter(text("Pending invitation"))
                        .shouldHave(size(1)));
    }
}
