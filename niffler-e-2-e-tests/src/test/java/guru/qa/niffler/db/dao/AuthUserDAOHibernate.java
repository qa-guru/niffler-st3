package guru.qa.niffler.db.dao;

import guru.qa.niffler.db.ServiceDB;
import guru.qa.niffler.db.jpa.EntityManagerFactoryProvider;
import guru.qa.niffler.db.jpa.JpaService;
import guru.qa.niffler.db.model.UserDataEntity;
import guru.qa.niffler.db.model.auth.AuthUserEntity;

import java.util.UUID;

public class AuthUserDAOHibernate extends JpaService implements AuthUserDAO {
	public AuthUserDAOHibernate() {
		super(EntityManagerFactoryProvider.INSTANCE.getDataSource(ServiceDB.AUTH)
				.createEntityManager());
	}

	@Override
	public UUID createUser(AuthUserEntity user) {
		user.setPassword(pe.encode(user.getPassword()));
		persist(user);
		return user.getId();
	}

	@Override
	public void deleteUser(AuthUserEntity user) {
		remove(user);
	}

	@Override
	public AuthUserEntity updateUser(AuthUserEntity user) {
		return merge(user);
	}

	@Override
	public AuthUserEntity getUser(UUID userId) {
		return em.createQuery("select u from AuthUserEntity u where u.id=:userId", AuthUserEntity.class)
				.setParameter("id", userId)
				.getSingleResult();
	}

	@Override
	public AuthUserEntity getUser(String username) {
		return em.createQuery("select u from AuthUserEntity u where u.username=:username", AuthUserEntity.class)
				.setParameter("username", username)
				.getSingleResult();
	}

	@Override
	public void deleteUserByUsernameInUserData(UUID userId, String username) {

	}

	@Override
	public UserDataEntity getUserData(String username) {
		return null;
	}
}
