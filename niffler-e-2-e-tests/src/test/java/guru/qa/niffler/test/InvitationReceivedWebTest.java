package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.User;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static guru.qa.niffler.jupiter.User.UserType.INVITATION_RECEIVED;
import static guru.qa.niffler.jupiter.User.UserType.WITH_FRIENDS;

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
    @AllureId("201")
    void friendShouldBeDisplayedInTable0(@User(userType = WITH_FRIENDS) UserJson userForTest, @User(userType = INVITATION_RECEIVED) UserJson userForTest1) {
        $("[data-tooltip-id=friends]").click();
        $$("[data-tooltip-id=submit-invitation]").shouldHave(size(1));
    }

    @Test
    @AllureId("202")
    void friendShouldBeDisplayedInTable1() {
        $("[data-tooltip-id=friends]").click();
        $$("[data-tooltip-id=submit-invitation]").shouldHave(size(1));
        Selenide.sleep(3000);
    }

    @Test
    @AllureId("203")
    void friendShouldBeDisplayedInTable2(@User(userType = INVITATION_RECEIVED) UserJson userForTest) {
        $("[data-tooltip-id=friends]").click();
        $$("[data-tooltip-id=submit-invitation]").shouldHave(size(1));
    }

    @Test
    @AllureId("204")
    void friendShouldBeDisplayedInTable3(@User(userType = INVITATION_RECEIVED) UserJson userForTest) {
        $("[data-tooltip-id=friends]").click();
        $$("[data-tooltip-id=submit-invitation]").shouldHave(size(1));
    }
}
