package guru.qa.niffler.db.dao;

import guru.qa.niffler.db.model.UserDataEntity;
import guru.qa.niffler.db.model.UserEntity;

import java.util.UUID;

public interface UserDataUserDAO {

	int createUserInUserData(UserEntity user);

	void deleteUserByIdInUserData(UUID userId);

	void updateUserInUserData(String oldUsername, String newUsername);

	UserDataEntity getUserData(String username);
}
