package guru.qa.niffler.db.dao.impl;

import guru.qa.niffler.db.ServiceDB;
import guru.qa.niffler.db.dao.AuthUserDAO;
import guru.qa.niffler.db.jpa.EntityManagerFactoryProvider;
import guru.qa.niffler.db.jpa.JpaService;
import guru.qa.niffler.db.model.auth.UserEntity;

import java.util.UUID;

public class AuthUserDAOHibernate extends JpaService implements AuthUserDAO {
    public AuthUserDAOHibernate() {
        super(EntityManagerFactoryProvider.INSTANCE.getDataSource(ServiceDB.AUTH).createEntityManager());
    }

    @Override
    public int createUser(UserEntity user) {
        user.setPassword(pe.encode(user.getPassword()));
        persist(user);
        return 0;
    }

    @Override
    public UserEntity getUser(UUID userId) {
        return em.createQuery("select u from UserEntity u where u.id=:userId", UserEntity.class)
                .setParameter("userId", userId)
                .getSingleResult();
    }

    @Override
    public void updateUser(UserEntity user) {
        merge(user);
    }

    @Override
    public void deleteUserById(UUID userId) {
        remove(getUser(userId));
    }
}
