package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.User;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static guru.qa.niffler.jupiter.User.UserType.INVITATION_SENT;


public class InvitationWebTest extends BaseWebTest {

    @BeforeEach
    void doLogin(@User(userType = INVITATION_SENT) UserJson userForTest) {
        Selenide.open("http://127.0.0.1:3000/main");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(userForTest.getUsername());
        $("input[name='password']").setValue(userForTest.getPassword());
        $("button[type='submit']").click();
    }

    @Test
    @AllureId("301")
    void tableShouldNotHaveFriends(@User(userType = INVITATION_SENT) UserJson userForTest) {
        $("[data-tooltip-id=friends]").click();
        $(byText("There are no friends yet!")).shouldBe(visible);
    }

    @Test
    @AllureId("302")
    void tableShouldNotHaveFriends1(@User(userType = INVITATION_SENT) UserJson userForTest) {
        $("[data-tooltip-id=friends]").click();
        $(byText("There are no friends yet!")).shouldBe(visible);
    }
}
