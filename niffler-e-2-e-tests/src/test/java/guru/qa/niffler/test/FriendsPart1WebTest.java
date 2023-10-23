package guru.qa.niffler.test;


import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.test.pages.AllPeoplePage;
import guru.qa.niffler.test.pages.FriendsPage;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.jupiter.annotation.User.UserType.*;
import static guru.qa.niffler.test.pages.LoginPage.doLogin;

public class FriendsPart1WebTest extends BaseWebTest {

    //- параметр @User есть и в beforeEach, и в тестовом методах;

    @BeforeEach
    void login(@User(userType = WITH_FRIENDS) UserJson userForTest) {
        doLogin(userForTest);
    }

    @Test
    @AllureId("101")
    void statusFriendsShouldBeDisplayedInTableTest(@User(userType = WITH_FRIENDS) UserJson userForTest) {
        goToTab("Friends");
        FriendsPage friendsPage = new FriendsPage();
        friendsPage.checkTableOfFriendsHasRecordWithCorrectFriendStatus();
    }

    @Test
    @AllureId("102")
    void statusInvitationSentShouldBeDisplayedInTableTest(@User(userType = INVITATION_SENT) UserJson userForTest) {
        Selenide.sleep(2000);
        goToTab("All people");
        AllPeoplePage allPeoplePage = new AllPeoplePage();
        allPeoplePage.checkTableOfFriendsHasRecordWithCorrectPotentialFriendStatus();
    }

    @Test
    @AllureId("103")
    void statusInvitationReceivedShouldBeDisplayedInTableTest(@User(userType = INVITATION_RECEIVED) UserJson userForTest) {
        Selenide.sleep(3000);

        goToTab("Friends");
        FriendsPage friendsPage = new FriendsPage();
        friendsPage.checkTableOfFriendsHasRecordWithCorrectPotentialFriendStatus();

    }
}
