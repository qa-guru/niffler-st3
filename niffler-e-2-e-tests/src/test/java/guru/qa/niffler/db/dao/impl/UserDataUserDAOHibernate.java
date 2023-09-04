package guru.qa.niffler.db.dao.impl;

import guru.qa.niffler.db.ServiceDB;
import guru.qa.niffler.db.dao.UserDataUserDAO;
import guru.qa.niffler.db.jpa.EntityManagerFactoryProvider;
import guru.qa.niffler.db.jpa.JpaService;
import guru.qa.niffler.db.model.userdata.UserDataUserEntity;

public class UserDataUserDAOHibernate extends JpaService implements UserDataUserDAO {

    public UserDataUserDAOHibernate() {
        super(EntityManagerFactoryProvider.INSTANCE.getDataSource(ServiceDB.USERDATA).createEntityManager());
    }

    @Override
    public int createUserInUserData(UserDataUserEntity user) {
        persist(user);
        return 0;
    }

    @Override
    public void deleteUserByNameInUserData(String username) {
        remove(getUserData(username));
    }

    public UserDataUserEntity getUserData(String username) {
        return em.createQuery("select u from UserDataUserEntity u where u.username=:username",
                        UserDataUserEntity.class)
                .setParameter("username", username)
                .getSingleResult();
    }
}