package guru.qa.niffler.test;


import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.test.pages.FriendsPage;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.jupiter.annotation.User.UserType.WITH_FRIENDS;
import static guru.qa.niffler.test.pages.LoginPage.doLogin;

public class FriendsPart2WebTest extends BaseWebTest {

    //- параметр @User есть только в beforeEach методе;

    @BeforeEach
    void login(@User(userType = WITH_FRIENDS) UserJson userForTest) {
        doLogin(userForTest);
    }

    @Test
    @AllureId("11132")
    void justCheckOnlyBeforeEachMethodHasUserParameter() {
        goToTab("Friends");
        FriendsPage friendsPage = new FriendsPage();
        friendsPage.checkTableOfFriendsHasRecordWithCorrectFriendStatus();
    }
}
