package guru.qa.niffler.db.dao.impl;

import guru.qa.niffler.db.ServiceDB;
import guru.qa.niffler.db.dao.UserdataUserDAO;
import guru.qa.niffler.db.jpa.EntityManagerFactoryProvider;
import guru.qa.niffler.db.jpa.JpaService;
import guru.qa.niffler.db.model.userdata.UserDataUserEntity;

public class UserdataUserDAOHibernate extends JpaService implements UserdataUserDAO {

    public UserdataUserDAOHibernate() {
        super(EntityManagerFactoryProvider.INSTANCE.getDataSource(ServiceDB.USERDATA).createEntityManager());
    }

    @Override
    public int createUserInUserData(UserDataUserEntity user) {
        persist(user);
        return 0;
    }

    @Override
    public void deleteUserInUserData(UserDataUserEntity user) {
        remove(user);
    }

    @Override
    public UserDataUserEntity getUserInUserDataByUsername(String username) {
        return em.createQuery("select u from UserDataUserEntity u where u.username=:username", UserDataUserEntity.class)
                .setParameter("username", username)
                .getSingleResult();
    }
}
