package guru.qa.niffler.db.dao;

import guru.qa.niffler.db.model.UserDataEntity;
import guru.qa.niffler.db.model.UserEntity;

public interface UserDataUserDAO {

    UserDataEntity getUserInUserData(String username);

    int createUserInUserData(UserEntity user);

    void updateUserInUserData(UserDataEntity user);

    void deleteUserByIdInUserData(String username);
}
