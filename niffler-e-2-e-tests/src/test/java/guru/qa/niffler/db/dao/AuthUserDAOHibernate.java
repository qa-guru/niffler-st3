package guru.qa.niffler.db.dao;

import guru.qa.niffler.db.model.UserEntity;

import java.util.UUID;

public class AuthUserDAOHibernate implements AuthUserDAO {
	@Override
	public int createUser(UserEntity user) {
		return 0;
	}

	@Override
	public void deleteUserById(UUID userId) {
	}

	@Override
	public void updateUserById(UUID userId, String username) {
	}

	@Override
	public UserEntity getUser(UUID userId) {
		return null;
	}

	@Override
	public UserEntity getUser(String username) {
		return null;
	}

	@Override
	public void deleteUserByUsernameInUserData(UUID userId, String username) {

	}
}
