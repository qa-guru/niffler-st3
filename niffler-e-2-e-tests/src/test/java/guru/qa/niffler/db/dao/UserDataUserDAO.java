package guru.qa.niffler.db.dao;

import guru.qa.niffler.db.model.UserDataEntity;
import guru.qa.niffler.db.model.UserEntity;

import java.util.UUID;

public interface UserDataUserDAO {

    UserEntity getUserByUsername(String username);

    void updateUser(UserEntity user);

    int createUserInUserData(UserEntity user);

    void deleteUserByIdInUserData(UUID userId);

    UserDataEntity getUserInUserDataByUsername(String username);

    void updateUserInUserData(UserDataEntity user);
}
