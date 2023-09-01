package guru.qa.niffler.db.dao;

import guru.qa.niffler.db.model.UserEntity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

public interface AuthUserDAO {

	PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

	UUID createUser(UserEntity user);

	void deleteUserById(UUID userId);

	void updateUserById(UUID userId, String username);

	UserEntity getUser(UUID userId);

	UserEntity getUser(String username);

	void deleteUserByUsernameInUserData(UUID userId, String username);
}
