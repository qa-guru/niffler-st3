package guru.qa.niffler.db.dao;

import java.util.UUID;

public class AuthUserDAOHibernate implements AuthUserDAO {

    @Override
    public int createUser(UserEntity user) {
        return 0;
    }

    @Override
    public UserEntity updateUser(UserEntity user) {
        return null;
    }

    @Override
    public void deleteUserById(UUID userId) {

    }

    @Override
    public UserEntity getUserById(UUID userId) {
        return null;
    }
}
