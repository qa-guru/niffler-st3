package guru.qa.niffler.test;


import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.User;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.components.HeaderComponent;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.jupiter.User.UserType.*;
import static com.codeborne.selenide.Selenide.$;
import static guru.qa.niffler.jupiter.annotation.User.UserType.WITH_FRIENDS;

public class FriendsWebTest extends BaseWebTest {

    @Test
    @AllureId("101")
    void friendShouldBeDisplayedInTableAtFriendsPage(@User(userType = WITH_FRIENDS) UserJson userForTest) {
        loginPage.login(userForTest);
        new HeaderComponent()
                .goToFriendsPage()
                .checkUserHaveFriend();
    }

    @Test
    @AllureId("102")
    void friendShouldBeDisplayedInTableAtPeoplePage(@User(userType = WITH_FRIENDS) UserJson userForTest) {
        loginPage.login(userForTest);
        new HeaderComponent()
                .goToPeoplePage()
                .checkUserHaveFriend();
    }

    @Test
    @AllureId("103")
    void sentInvitationShouldBeDisplayedInTableAtPeoplePage(@User(userType = INVITATION_SENT) UserJson userForTest) {
        loginPage.login(userForTest);
        new HeaderComponent()
                .goToPeoplePage()
                .checkInvitationToFriendSent();
    }

    @Test
    @AllureId("104")
    void invitationShouldBeDisplayedInTableAtFriendsPage(@User(userType = INVITATION_SENT) UserJson userSent,
                                                         @User(userType = INVITATION_RECEIVED) UserJson userReceived) {
        loginPage.login(userReceived);
        new HeaderComponent()
                .goToFriendsPage()
                .checkUserHaveFriendInvitation(userSent.getUsername());
    }

    @Test
    @AllureId("105")
    void invitationShouldBeDisplayedInTableAtPeoplePage(@User(userType = INVITATION_SENT) UserJson userSent,
                                                         @User(userType = INVITATION_RECEIVED) UserJson userReceived) {
        loginPage.login(userReceived);
        new HeaderComponent()
                .goToPeoplePage()
                .checkInvitationToFriendSent(userSent.getUsername());
    }
}
