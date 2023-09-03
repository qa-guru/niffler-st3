package guru.qa.niffler.db.dao;

import guru.qa.niffler.db.model.UserDataEntity;
import guru.qa.niffler.db.model.userdata.UserDataUserEntity;

import java.util.UUID;

public interface UserDataUserDAO {

	int createUserInUserData(UserDataUserEntity user);

	void deleteUserByIdInUserData(UUID userId);

	UserDataEntity updateUserInUserData(UserDataUserEntity user);

	UserDataEntity getUserData(String username);
}
