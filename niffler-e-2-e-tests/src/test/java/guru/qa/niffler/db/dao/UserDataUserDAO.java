package guru.qa.niffler.db.dao;

import guru.qa.niffler.db.model.UserEntity;

public interface UserDataUserDAO {

    int createUserInUserData(UserEntity user);

    void deleteUserByNameInUserData(String username);
}
