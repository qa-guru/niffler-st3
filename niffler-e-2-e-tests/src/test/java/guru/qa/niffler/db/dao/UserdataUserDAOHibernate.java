package guru.qa.niffler.db.dao;

import guru.qa.niffler.db.ServiceDB;
import guru.qa.niffler.db.jpa.EntityManagerFactoryProvider;
import guru.qa.niffler.db.jpa.JpaService;
import guru.qa.niffler.db.model.UserDataEntity;
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

	}

	@Override
	public UserDataEntity updateUserInUserData(UserDataUserEntity user) {
		return null;
	}

	@Override
	public UserDataEntity getUserData(String username) {
		return null;
	}
}
