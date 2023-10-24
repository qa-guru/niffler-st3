package guru.qa.niffler.db.dao.impl;

import com.github.javafaker.Faker;
import guru.qa.niffler.db.ServiceDB;
import guru.qa.niffler.db.dao.UserdataUserDAO;
import guru.qa.niffler.db.jdbc.DataSourceProvider;
import guru.qa.niffler.db.model.userdata.UserDataUserEntity;
import guru.qa.niffler.db.springjdbc.UserDataEntityRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import static guru.qa.niffler.model.CurrencyValues.RUB;

public class UserDataUserDAOSpringJdbc implements UserdataUserDAO {

	private final TransactionTemplate authTtpl;
	private final TransactionTemplate userdataTtpl;
	private final JdbcTemplate authJdbcTemplate;

	private final JdbcTemplate userdataJdbcTemplate;
	Faker faker = new Faker();

	public UserDataUserDAOSpringJdbc() {
		JdbcTransactionManager authTm = new JdbcTransactionManager(
				DataSourceProvider.INSTANCE.getDataSource(ServiceDB.AUTH));
		JdbcTransactionManager userdataTm = new JdbcTransactionManager(
				DataSourceProvider.INSTANCE.getDataSource(ServiceDB.USERDATA));

		this.authTtpl = new TransactionTemplate(authTm);
		this.userdataTtpl = new TransactionTemplate(userdataTm);
		this.authJdbcTemplate = new JdbcTemplate(authTm.getDataSource());
		this.userdataJdbcTemplate = new JdbcTemplate(userdataTm.getDataSource());
	}

	@Override
	public int createUserInUserData(UserDataUserEntity user) {
		return userdataJdbcTemplate.update(
				"INSERT INTO \"user\" (username, currency) VALUES (?, ?)",
				user.getUsername(), RUB.name());
	}

	@Override
	public void deleteUser(UserDataUserEntity user) {
		userdataJdbcTemplate.update("DELETE FROM \"user\" WHERE username=?", user.getUsername());
	}

	@Override
	public UserDataUserEntity updateUserInUserData(UserDataUserEntity user) {
		String newFirstname = faker.name().firstName();
		String newSurname = faker.name().lastName();
		user.setId(getUserData(user.getUsername()).getId());
		user.setFirstname(newFirstname);
		user.setSurname(newSurname);
		userdataJdbcTemplate.update("UPDATE \"user\" SET firstname=?, surname=? WHERE username=?",
				newFirstname, newSurname, user.getUsername());
		return user;
	}

	@Override
	public UserDataUserEntity getUserData(String username) {
		return userdataJdbcTemplate.queryForObject(
				"SELECT * FROM \"user\" WHERE username=?",
				UserDataEntityRowMapper.instance, username);
	}

	@Override
	public void addFriendForUser(UserDataUserEntity user, UserDataUserEntity friend) {
		UserDataUserEntity userDataEntity = getUserData(user.getUsername());
		UserDataUserEntity friendDataEntity = getUserData(friend.getUsername());
		userdataJdbcTemplate.update(
				"INSERT INTO \"friendship\" (user_id, friend_id, pending) VALUES (?, ?, ?)",
				userDataEntity.getId(), friendDataEntity.getId(), false);
		userdataJdbcTemplate.update(
				"INSERT INTO \"friendship\" (user_id, friend_id, pending) VALUES (?, ?, ?)",
				friendDataEntity.getId(), userDataEntity.getId(), false);
	}

	@Override
	public void addInvitationForFriend(UserDataUserEntity user, UserDataUserEntity friend) {
		UserDataUserEntity userDataEntity = getUserData(user.getUsername());
		UserDataUserEntity friendDataEntity = getUserData(friend.getUsername());
		userdataJdbcTemplate.update(
				"INSERT INTO \"friendship\" (user_id, friend_id, pending) VALUES (?, ?, ?)",
				userDataEntity.getId(), friendDataEntity.getId(), true);
	}
}
