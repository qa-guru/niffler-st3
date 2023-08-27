package guru.qa.niffler.db.dao;

import guru.qa.niffler.db.model.UserDataEntity;
import guru.qa.niffler.db.model.UserEntity;

import java.util.UUID;

public interface UserDataUserDAO {

    int createUserInUserData(UserEntity user);

    UserDataEntity getUserInUserDataByUsername(String username);

    void updateUserInUserData(UserDataEntity userDe);

    void deleteUserByUsernameInUserData(String username);
}
