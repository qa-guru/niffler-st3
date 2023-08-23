package guru.qa.niffler.db.dao;

import guru.qa.niffler.db.model.UserEntity;

import java.util.UUID;

public interface UserDataUserDAO {

    int createUserInUserData(UserEntity user);

    void deleteUserByIdInUserData(UUID userId);
}
