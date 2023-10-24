package guru.qa.niffler.db.repository;

import guru.qa.niffler.db.model.auth.AuthUserEntity;
import guru.qa.niffler.db.model.userdata.UserDataUserEntity;
import guru.qa.niffler.model.UserJson;

import java.util.List;

public interface UserRepository {
    void createUserForTest(AuthUserEntity user);

    void removeAfterTest(AuthUserEntity user);

    void setFriendsForUser(List<UserJson> friends);
    void updateUserForTest(UserDataUserEntity user);

    UserDataUserEntity getUserData(AuthUserEntity user);

    void makeUsersFriends(UserDataUserEntity firstUser, UserDataUserEntity secondUser);

    void addInvitation(UserDataUserEntity user, UserDataUserEntity friend);

    void addFriendForUser(UserDataUserEntity user, UserDataUserEntity friend);
}