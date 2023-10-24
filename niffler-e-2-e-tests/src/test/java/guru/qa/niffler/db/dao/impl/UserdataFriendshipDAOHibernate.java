package guru.qa.niffler.db.dao.impl;

import guru.qa.niffler.db.ServiceDB;
import guru.qa.niffler.db.dao.UserdataFriendshipDAO;
import guru.qa.niffler.db.dao.UserdataUserDAO;
import guru.qa.niffler.db.jpa.EntityManagerFactoryProvider;
import guru.qa.niffler.db.jpa.JpaService;
import guru.qa.niffler.db.model.userdata.UserDataUserEntity;

public class UserdataFriendshipDAOHibernate extends JpaService implements UserdataFriendshipDAO {
	public UserdataFriendshipDAOHibernate() {
		super(EntityManagerFactoryProvider.INSTANCE.getDataSource(ServiceDB.USERDATA)
				.createEntityManager());
	}


	@Override
	public UserDataUserEntity addIncomeInvitation(UserDataUserEntity user, UserDataUserEntity incomeFriend) {
		return null;
	}

	@Override
	public UserDataUserEntity addOutcomeInvitation(UserDataUserEntity user, UserDataUserEntity outcomeFriend) {
		return null;
	}
}
