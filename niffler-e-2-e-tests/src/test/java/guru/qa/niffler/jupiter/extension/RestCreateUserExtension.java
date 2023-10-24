package guru.qa.niffler.jupiter.extension;

import com.github.javafaker.Faker;
import guru.qa.niffler.api.AuthServiceClient;
import guru.qa.niffler.api.FriendsServiceClient;
import guru.qa.niffler.api.RegisterServiceClient;
import guru.qa.niffler.api.context.CookieContext;
import guru.qa.niffler.api.context.SessionStorageContext;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.model.UserJson;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static guru.qa.niffler.util.FakerUtils.generateRandomUsername;

public class RestCreateUserExtension extends CreateUserExtension{

    private final RegisterServiceClient registerService = new RegisterServiceClient();

    private final AuthServiceClient authServiceClient = new AuthServiceClient();

    private final FriendsServiceClient friendsServiceClient = new FriendsServiceClient();

    public final String DEFAULT_PASSWORD = "12345678";
    @Override
    protected UserJson createUserForTest(GenerateUser annotation) {
        UserJson userJson = new UserJson();
        String username = generateRandomUsername();
        registerService.register(username, DEFAULT_PASSWORD);
        userJson.setUsername(username);
        userJson.setPassword(DEFAULT_PASSWORD);
        return userJson;
    }

    @Override
    protected List<UserJson> createFriendsIfPresent(GenerateUser annotation, UserJson currentUser) throws IOException {
        if (annotation.friends().handleAnnotation()) {
            List<UserJson> friendsList = new ArrayList<>();
            for (int i = 0; i < annotation.friends().count(); i++) {
                UserJson friend = createUserJson(null, null);
                registerService.register(friend.getUsername(), friend.getPassword());
                addInvitation(currentUser.getUsername(), friend);
                acceptInvitation(currentUser, friend.getUsername());
                friendsList.add(friend);
            }
            return friendsList;
        } else return Collections.emptyList();
    }

    @Override
    protected List<UserJson> createIncomeInvitationsIfPresent(GenerateUser annotation, UserJson currentUser) throws IOException {
        if (annotation.incomeInvitations().handleAnnotation()) {
            List<UserJson> friendsList = new ArrayList<>();
            for (int i = 0; i < annotation.incomeInvitations().count(); i++) {
                UserJson friend = createUserJson(null, null);
                registerService.register(friend.getUsername(), friend.getPassword());
                addInvitation(currentUser.getUsername(), friend);
                friendsList.add(friend);
            }
            return friendsList;
        } else return Collections.emptyList();
    }

    @Override
    protected List<UserJson> createOutcomeInvitationsIfPresent(GenerateUser annotation, UserJson currentUser) throws IOException {
        if (annotation.outcomeInvitations().handleAnnotation()) {
            List<UserJson> friendsList = new ArrayList<>();
            for (int i = 0; i < annotation.outcomeInvitations().count(); i++) {
                UserJson friend = createUserJson(null, null);
                registerService.register(friend.getUsername(), friend.getPassword());
                addInvitation(friend.getUsername(), currentUser);
                friendsList.add(friend);
            }
            return friendsList;
        } else return Collections.emptyList();
    }

    private UserJson createUserJson(@Nullable String desiredUsername, @Nullable String desiredPassword) {
        Faker faker = new Faker();
        final String username = desiredUsername == null ? faker.name().username() : desiredUsername;
        final String password = desiredPassword == null ? faker.internet().password(3, 12) : desiredPassword;
        UserJson user = new UserJson();
        user.setUsername(username);
        user.setPassword(password);
        return user;
    }

    private void addInvitation(String userWhoReceivedInvitation, UserJson userWhoSentInvitation) throws IOException, IOException {
        UserJson userWhoReceivedInvitationJson = new UserJson();
        userWhoReceivedInvitationJson.setUsername(userWhoReceivedInvitation);

        SessionStorageContext sessionStorageContext = SessionStorageContext.getInstance();
        CookieContext cookieContext = CookieContext.getInstance();
        authServiceClient.doLogin(userWhoSentInvitation.getUsername(), userWhoSentInvitation.getPassword());


        friendsServiceClient.addFriend(sessionStorageContext.getToken(), userWhoReceivedInvitationJson);
    }

    private void acceptInvitation(UserJson friendshipTo, String friendshipFrom) throws IOException {
        UserJson userWhoSentInvitationJson = new UserJson();
        userWhoSentInvitationJson.setUsername(friendshipFrom);

        SessionStorageContext sessionStorageContext = SessionStorageContext.getInstance();
        CookieContext cookieContext = CookieContext.getInstance();
        authServiceClient.doLogin(friendshipTo.getUsername(), friendshipTo.getPassword());

        friendsServiceClient.acceptInvitation(sessionStorageContext.getToken(), userWhoSentInvitationJson);
    }
}
