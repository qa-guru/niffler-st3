package guru.qa.niffler.db.dao.impl;

import guru.qa.niffler.db.ServiceDB;
import guru.qa.niffler.db.dao.UserDataUserDAO;
import guru.qa.niffler.db.jpa.EntityManagerFactoryProvider;
import guru.qa.niffler.db.jpa.JpaService;
import guru.qa.niffler.db.model.auth.UserEntity;
import guru.qa.niffler.db.model.userdata.UserDataEntity;
import guru.qa.niffler.db.model.userdata.UserDataUserEntity;

import java.util.UUID;

public class UserdataUserDAOHibernate extends JpaService implements UserDataUserDAO {

    @Override
    public int createUserInUserData(UserEntity user) {
        return 0;
    }

    @Override
    public UserDataEntity getUserInUserDataByUsername(String username) {
        return null;
    }

    @Override
    public void updateUserInUserData(UserDataEntity userDe) {

    }

    @Override
    public void deleteUserByUsernameInUserData(String username) {

    }
}
