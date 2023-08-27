package guru.qa.niffler.db.dao;

import java.util.UUID;

public interface UserDataUserDAO {

    int createUserInUserData(UserEntity user);

    void deleteUserByIdInUserData(UUID userId);

    void deleteUserByUsernameInUserData(String username);
}
