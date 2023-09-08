package guru.qa.niffler.db.dao;

import guru.qa.niffler.db.model.userdata.UserDataUserEntity;

public interface UserDataUserDAO {

	int createUserInUserData(UserDataUserEntity user);

	void deleteUser(UserDataUserEntity user);

	UserDataUserEntity updateUserInUserData(UserDataUserEntity user);

	UserDataUserEntity getUserData(String username);
}
