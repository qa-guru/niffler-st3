package guru.qa.niffler.db.dao;

import guru.qa.niffler.db.model.userdata.UserDataEntity;
import guru.qa.niffler.db.model.auth.UserEntity;

public interface UserDataUserDAO {

    int createUserInUserData(UserEntity user);

    UserDataEntity getUserInUserDataByUsername(String username);

    void updateUserInUserData(UserDataEntity userDe);

    void deleteUserByUsernameInUserData(String username);
}
