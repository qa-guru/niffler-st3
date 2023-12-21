package guru.qa.niffler.db.dao;

import guru.qa.niffler.db.model.UserEntity;

import java.util.UUID;

public class AuthUserDAOHibernate implements AuthUserDAO {
    @Override
    public UserEntity getUser(String username) {
        return null;
    }

    @Override
    public int createUser(UserEntity user) {
        return 0;
    }

    @Override
    public void updateUser(UserEntity user) {

    }

    @Override
    public void deleteUserById(UUID userId) {

    }
}
