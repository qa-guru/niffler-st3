package guru.qa.niffler.db.dao;

import guru.qa.niffler.db.model.userdata.UserDataUserEntity;

import guru.qa.niffler.db.model.UserEntity;

import java.util.UUID;

public interface UserDataUserDAO {

    int createUserInUserData(UserDataUserEntity user);

    void deleteUserByNameInUserData(String username);
}
