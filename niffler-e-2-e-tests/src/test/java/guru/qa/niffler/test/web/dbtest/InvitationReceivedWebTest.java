package guru.qa.niffler.test.web.dbtest;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.test.web.BaseWebTest;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;
import static guru.qa.niffler.jupiter.annotation.User.UserType.INVITATION_RECEIVED;
import static io.qameta.allure.Allure.step;

public class InvitationReceivedWebTest extends BaseWebTest {
    @BeforeEach
    void doLogin(@User(userType = INVITATION_RECEIVED) UserJson userForTest) {
        Selenide.open("http://127.0.0.1:3000/main");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(userForTest.getUsername());
        $("input[name='password']").setValue(userForTest.getPassword());
        $("button[type='submit']").click();
    }

    @Test
    @AllureId("107")
    void invitationReceivedShouldBeDisplayedInTable0(@User(userType = INVITATION_RECEIVED) UserJson userForTest) throws InterruptedException {

        step("Переходим на вкладку Друзья", () ->
                $x("//li[@data-tooltip-id='friends']").click());

        step("Проверяем, что присутствует кнопка Submit-invitation", () ->
                $x("//div[@data-tooltip-id='submit-invitation']").shouldBe(visible));
    }

    @Test
    @AllureId("108")
    void invitationReceivedShouldBeDisplayedInTable1(@User(userType = INVITATION_RECEIVED) UserJson userForTest) throws InterruptedException {

        step("Переходим на вкладку Друзья", () ->
                $x("//li[@data-tooltip-id='friends']").click());

        step("Проверяем, что присутствует кнопка Submit-invitation", () ->
                $x("//div[@data-tooltip-id='submit-invitation']").shouldBe(visible));
    }

    @Test
    @AllureId("109")
    void invitationReceivedShouldBeDisplayedInTable2(@User(userType = INVITATION_RECEIVED) UserJson userForTest) throws InterruptedException {

        step("Переходим на вкладку Друзья", () ->
                $x("//li[@data-tooltip-id='friends']").click());

        step("Проверяем, что присутствует кнопка Submit-invitation", () ->
                $x("//div[@data-tooltip-id='submit-invitation']").shouldBe(visible));
    }
}
