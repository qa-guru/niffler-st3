package guru.qa.niffler.db.dao.impl;

import guru.qa.niffler.db.ServiceDB;
import guru.qa.niffler.db.dao.UserDataUserDAO;
import guru.qa.niffler.db.jpa.EntityManagerFactoryProvider;
import guru.qa.niffler.db.jpa.JpaService;
import guru.qa.niffler.db.model.auth.AuthUserEntity;
import guru.qa.niffler.db.model.userdata.UserDataUserEntity;

import java.util.UUID;

public class UserdataUserDAOHibernate extends JpaService implements UserDataUserDAO {
	public UserdataUserDAOHibernate() {
		super(EntityManagerFactoryProvider.INSTANCE.getDataSource(ServiceDB.USERDATA)
				.createEntityManager());
	}

	@Override
	public int createUserInUserData(UserDataUserEntity user) {
		persist(user);
		return 0;
	}

	@Override
	public void deleteUserByIdInUserData(UUID userId) {
		AuthUserEntity user = new AuthUserDAOHibernate().getUser(userId);
		remove(getUserData(user.getUsername()));
	}

	@Override
	public UserDataUserEntity updateUserInUserData(UserDataUserEntity user) {
		return merge(user);
	}

	@Override
	public UserDataUserEntity getUserData(String username) {
		return em.createQuery("select u from Users u where u.username=:username", UserDataUserEntity.class)
				.setParameter("id", username)
				.getSingleResult();
	}
}
