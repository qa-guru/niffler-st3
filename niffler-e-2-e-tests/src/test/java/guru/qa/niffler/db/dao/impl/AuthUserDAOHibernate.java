package guru.qa.niffler.db.dao.impl;

import guru.qa.niffler.db.dao.AuthUserDAO;
import guru.qa.niffler.db.model.auth.UserEntity;

import java.util.UUID;

public class AuthUserDAOHibernate implements AuthUserDAO {
    @Override
    public int createUser(UserEntity user) {
        return 0;
    }

    @Override
    public UserEntity getUser(UUID userId) {
        return null;
    }

    @Override
    public void updateUser(UserEntity user) {

    }

    @Override
    public void deleteUserById(UUID userId) {

    }
}
