package guru.qa.niffler.db.dao;

import guru.qa.niffler.db.model.auth.AuthUserEntity;
import guru.qa.niffler.db.model.userdata.UserDataUserEntity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

public interface AuthUserDAO {

	PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

	UUID createUser(AuthUserEntity user);

	void deleteUser(AuthUserEntity user);

	AuthUserEntity updateUser(AuthUserEntity user);

	AuthUserEntity getUser(UUID userId);

	AuthUserEntity getUser(String username);

	void deleteUserByUsernameInUserData(UUID userId, String username);

	UserDataUserEntity getUserData(String username);
}
