package guru.qa.niffler.db.dao;

import guru.qa.niffler.db.model.userdata.UserDataUserEntity;

public interface UserdataUserDAO {

    int createUserInUserData(UserDataUserEntity user);

    void deleteUserInUserData(UserDataUserEntity userId);

    UserDataUserEntity getUserInUserDataByUsername(String username);
}
