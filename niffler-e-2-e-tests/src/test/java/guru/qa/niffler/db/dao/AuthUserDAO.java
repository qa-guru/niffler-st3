package guru.qa.niffler.db.dao;

import guru.qa.niffler.db.model.UserEntity;

import java.util.UUID;

public interface AuthUserDAO {

	int createUser(UserEntity user);

	void deleteUserById(UUID userId);
}
