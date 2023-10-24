package guru.qa.niffler.db.dao;

import guru.qa.niffler.db.model.userdata.UserDataUserEntity;

public interface UserdataUserDAO {

	int createUserInUserData(UserDataUserEntity user);

	void deleteUser(UserDataUserEntity user);

	UserDataUserEntity updateUserInUserData(UserDataUserEntity user);

	UserDataUserEntity getUserData(String username);

	void addFriendForUser(UserDataUserEntity user, UserDataUserEntity friend);

	void addInvitationForFriend(UserDataUserEntity user, UserDataUserEntity friend);
}
