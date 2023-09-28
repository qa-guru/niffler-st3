package guru.qa.niffler.db.dao.impl;

import guru.qa.niffler.db.ServiceDB;
import guru.qa.niffler.db.dao.UserDataUserDAO;
import guru.qa.niffler.db.jpa.EntityManagerFactoryProvider;
import guru.qa.niffler.db.jpa.JpaService;
import guru.qa.niffler.db.model.auth.UserEntity;
import guru.qa.niffler.db.model.userdata.UserDataEntity;

public class UserdataUserDAOHibernate extends JpaService implements UserDataUserDAO {

    public UserdataUserDAOHibernate() {
        super(EntityManagerFactoryProvider.INSTANCE.getDataSource(ServiceDB.USERDATA).createEntityManager());
    }

    @Override
    public int createUserInUserData(UserDataEntity user) {
        persist(user);
        return 0;
    }

    @Override
    public UserDataEntity getUserInUserDataByUsername(String username) {
        return em.createQuery("select u from UserDataEntity u where u.username=:username",
                        UserDataEntity.class)
                .setParameter("username", username)
                .getSingleResult();
    }

    @Override
    public void updateUserInUserData(UserDataEntity userDe) {
        merge(userDe);
    }

    @Override
    public void deleteUserByUsernameInUserData(String username) {
        remove(getUserInUserDataByUsername(username));
    }
}
