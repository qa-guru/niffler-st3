package guru.qa.niffler.db.dao;

import guru.qa.niffler.db.model.auth.UserEntity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

public interface AuthUserDAO {

    PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    int createUser(UserEntity user);

    UserEntity getUser(UUID userId);

    void updateUser(UserEntity user);

    void deleteUserById(UUID userId);
}
