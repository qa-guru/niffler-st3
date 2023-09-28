package guru.qa.niffler.db.dao;

import guru.qa.niffler.db.model.auth.UserEntity;
import guru.qa.niffler.db.model.userdata.UserDataEntity;

public interface UserDataUserDAO {

    int createUserInUserData(UserDataEntity user);

    UserDataEntity getUserInUserDataByUsername(String username);

    void updateUserInUserData(UserDataEntity userDe);

    void deleteUserByUsernameInUserData(String username);
}
