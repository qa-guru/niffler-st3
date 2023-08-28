package guru.qa.niffler.test;

import guru.qa.niffler.jupiter.UserQueue;
import guru.qa.niffler.jupiter.WebTest;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.pages.AllPeoplePage;
import guru.qa.niffler.pages.FriendsPage;
import guru.qa.niffler.pages.LoginPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@WebTest
public class UserWebTest {

    private static final int NUMBER_OF_REPEATS = 3;
    private static final String
            YOU_ARE_FRIENDS_STATUS = "You are friends",
            PENDING_INVITATION_STATUS = "Pending invitation",
            SUBMIT_INVITATION_BUTTON_NOT_FOUND = "Submit invitation button not found for the user in the table",
            FRIEND_STATUS_NOT_DISPLAYED = "The status '" + YOU_ARE_FRIENDS_STATUS + "' not displayed for the user in the table",
            PENDING_INVITATION_NOT_DISPLAYED = "The status '" + PENDING_INVITATION_STATUS + "' not displayed for the user in the table";

    private LoginPage loginPage = new LoginPage();
    private FriendsPage friendsPage = new FriendsPage();
    private AllPeoplePage peoplePage = new AllPeoplePage();

    @BeforeEach
    void doLogin(@UserQueue(userType = UserQueue.UserType.FRIEND) UserJson userForTest) {
        loginPage.login(userForTest);
    }

    @RepeatedTest(NUMBER_OF_REPEATS)
    void shouldDisplayFriendStatusInFriendList(UserJson userForTest) {
        assertTrue(friendsPage.isFriendDisplayed(userForTest, YOU_ARE_FRIENDS_STATUS),
                FRIEND_STATUS_NOT_DISPLAYED);
    }

    @RepeatedTest(NUMBER_OF_REPEATS)
    void shouldDisplayPendingInvitationInPeopleList(@UserQueue(userType = UserQueue.UserType.INVITE_SENT) UserJson userForTest) {
        assertTrue(peoplePage.isStatusDisplayedForUser(userForTest, PENDING_INVITATION_STATUS),
                PENDING_INVITATION_NOT_DISPLAYED);
    }

    @RepeatedTest(NUMBER_OF_REPEATS)
    void shouldDisplaySubmitInvitationButton(@UserQueue(userType = UserQueue.UserType.INVITE_RECEIVED) UserJson userForTest) {
        assertTrue(friendsPage.isSubmitInvitationButtonDisplayed(userForTest),
                SUBMIT_INVITATION_BUTTON_NOT_FOUND);
    }
}
