package guru.qa.niffler.test;


import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.components.HeaderComponent;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.jupiter.annotation.User.UserType.*;

public class FriendsWebTest extends BaseWebTest {

    @Test
    @AllureId("301")
    void friendShouldBeDisplayedInTableAtFriendsPage(@User(userType = WITH_FRIENDS) UserJson userForTest) {
        loginPage.login(userForTest);
        new HeaderComponent()
                .goToFriendsPage()
                .checkUserHaveFriend();
    }

    @Test
    @AllureId("302")
    void friendShouldBeDisplayedInTableAtPeoplePage(@User(userType = WITH_FRIENDS) UserJson userForTest) {
        loginPage.login(userForTest);
        new HeaderComponent()
                .goToPeoplePage()
                .checkUserHaveFriend();
    }

    @Test
    @AllureId("303")
    void sentInvitationShouldBeDisplayedInTableAtPeoplePage(@User(userType = INVITATION_SENT) UserJson userForTest) {
        loginPage.login(userForTest);
        new HeaderComponent()
                .goToPeoplePage()
                .checkInvitationToFriendSent();
    }
}
