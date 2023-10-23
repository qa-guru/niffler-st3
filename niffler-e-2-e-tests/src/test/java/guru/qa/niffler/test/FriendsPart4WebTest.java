package guru.qa.niffler.test;


import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.test.pages.FriendsPage;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.jupiter.annotation.User.UserType.INVITATION_RECEIVED;
import static guru.qa.niffler.jupiter.annotation.User.UserType.INVITATION_SENT;
import static guru.qa.niffler.test.pages.LoginPage.doLogin;

public class FriendsPart4WebTest extends BaseWebTest {

//2.2) Предусмотреть возможность написания теста, использующего сразу двух пользователей разного типа.
// Например void test(@User(userType = WITH_FRIENDS) UserJson userForTest, @User(userType = INVITATION_SENT) UserJson userForTestAnother)


    @Test
    @AllureId("10221")
    void justCheckOnlyMethodHasUserParameter(@User(userType = INVITATION_SENT) UserJson userWhoSentInvitation, @User(userType = INVITATION_RECEIVED) UserJson userWhoReceivedInvitation) {

        doLogin(userWhoReceivedInvitation);
        goToTab("Friends");
        FriendsPage friendsPage = new FriendsPage();
        friendsPage.checkTableOfFriendsHasRecordWithCorrectPotentialFriendStatus(userWhoSentInvitation.getUsername());

    }

}
