package guru.qa.niffler.db.dao.impl;

import guru.qa.niffler.db.ServiceDB;
import guru.qa.niffler.db.dao.AuthUserDAO;
import guru.qa.niffler.db.jpa.EntityManagerFactoryProvider;
import guru.qa.niffler.db.jpa.JpaService;
import guru.qa.niffler.db.model.auth.AuthUserEntity;

import java.util.UUID;

public class AuthUserDAOHibernate extends JpaService implements AuthUserDAO {
	public AuthUserDAOHibernate() {
		super(EntityManagerFactoryProvider.INSTANCE.getDataSource(ServiceDB.AUTH)
				.createEntityManager());
	}

	@Override
	public UUID createUser(AuthUserEntity user) {
		AuthUserEntity authUser = new AuthUserEntity(user);
		authUser.setPassword(pe.encode(user.getPassword()));
		persist(authUser);
		user.setId(getUser(user.getUsername()).getId());
		return user.getId();
	}

	@Override
	public void deleteUser(AuthUserEntity user) {
		AuthUserEntity authUser = em.find(AuthUserEntity.class, user.getId());
		remove(authUser);
	}

	@Override
	public AuthUserEntity updateUser(AuthUserEntity user) {
		return merge(user);
	}

	@Override
	public AuthUserEntity getUser(UUID userId) {
		return em.createQuery("select u from AuthUserEntity u where u.id=:userId", AuthUserEntity.class)
				.setParameter("id", userId.toString())
				.getSingleResult();
	}

	@Override
	public AuthUserEntity getUser(String username) {
		return em.createQuery("select u from AuthUserEntity u where u.username=:username", AuthUserEntity.class)
				.setParameter("username", username)
				.getSingleResult();
	}
}
