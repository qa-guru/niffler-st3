package guru.qa.niffler.db.dao;

import com.github.javafaker.Faker;
import guru.qa.niffler.db.DataSourceProvider;
import guru.qa.niffler.db.ServiceDB;
import guru.qa.niffler.db.mapper.AuthorityEntityRowMapper;
import guru.qa.niffler.db.mapper.UserDataEntityRowMapper;
import guru.qa.niffler.db.mapper.UserEntityRowMapper;
import guru.qa.niffler.db.model.Authority;
import guru.qa.niffler.db.model.AuthorityEntity;
import guru.qa.niffler.db.model.UserDataEntity;
import guru.qa.niffler.db.model.UserEntity;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.UUID;

import static guru.qa.niffler.model.CurrencyValues.RUB;

public class AuthUserDAOSpringJdbc implements AuthUserDAO, UserDataUserDAO {

	private final TransactionTemplate authTtpl;
	private final TransactionTemplate userdataTtpl;
	private final JdbcTemplate authJdbcTemplate;

	private final JdbcTemplate userdataJdbcTemplate;
	Faker faker = new Faker();

	public AuthUserDAOSpringJdbc() {
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
	@SuppressWarnings("unchecked")
	public UUID createUser(UserEntity user) {
		return authTtpl.execute(status -> {
			KeyHolder kh = new GeneratedKeyHolder();

			authJdbcTemplate.update(con -> {
				PreparedStatement ps = con.prepareStatement("INSERT INTO users " +
						"(username, password, enabled, account_non_expired, " +
						"account_non_locked, credentials_non_expired) " +
						"VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, user.getUsername());
				ps.setString(2, pe.encode(user.getPassword()));
				ps.setBoolean(3, user.getEnabled());
				ps.setBoolean(4, user.getAccountNonExpired());
				ps.setBoolean(5, user.getAccountNonLocked());
				ps.setBoolean(6, user.getCredentialsNonExpired());
				return ps;
			}, kh);
			final UUID userId = (UUID) kh.getKeyList().get(0).get("id");
			authJdbcTemplate.batchUpdate("INSERT INTO authorities (user_id, authority) " +
					"VALUES (?, ?)", new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					ps.setObject(1, userId);
					ps.setObject(2, Authority.values()[i].name());
				}

				@Override
				public int getBatchSize() {
					return Authority.values().length;
				}
			});
			return userId;
		});
	}

	@Override
	public void deleteUserById(UUID userId) {
		authJdbcTemplate.update("DELETE FROM authorities WHERE user_id = ? ", userId);
		authJdbcTemplate.update("DELETE FROM users WHERE id=?", userId);
	}

	@Override
	public void updateUserById(UUID userId, String username) {
		boolean enabled = true;
		boolean accountNonExpired = true;
		boolean accountNonLocked = true;
		boolean credentialsNonExpired = true;
		authJdbcTemplate.update("UPDATE users SET username=?, enabled=?, account_non_expired=?, " +
						"account_non_locked=?, credentials_non_expired=? WHERE id=?::uuid",
				username, enabled, accountNonExpired,
				accountNonLocked, credentialsNonExpired, userId);
	}

	@Override
	public UserEntity getUser(UUID userId) {
		UserEntity user = authJdbcTemplate.queryForObject(
				"SELECT * FROM users WHERE id=?::uuid",
				UserEntityRowMapper.instance,
				userId.toString());

		List<AuthorityEntity> authorities = authJdbcTemplate.query(
				"SELECT * FROM authorities WHERE user_id=?::uuid",
				AuthorityEntityRowMapper.instance,
				userId.toString());

		user.setAuthorities(authorities);
		return user;
	}

	@Override
	public UserEntity getUser(String username) {
		UserEntity user = authJdbcTemplate.queryForObject(
				"SELECT * FROM users WHERE username=?",
				UserEntityRowMapper.instance,
				username);


		List<AuthorityEntity> authorities = authJdbcTemplate.query(
				"SELECT * FROM authorities WHERE user_id=?::uuid",
				AuthorityEntityRowMapper.instance,
				user.getId());
		user.setAuthorities(authorities);
		return user;
	}

	@Override
	public int createUserInUserData(UserEntity user) {
		return userdataJdbcTemplate.update(
				"INSERT INTO users (username, currency) VALUES (?, ?)",
				user.getUsername(), RUB.name());
	}

	@Override
	public void deleteUserByIdInUserData(UUID userId) {
		userdataJdbcTemplate.update("DELETE FROM users WHERE id=?", userId);
	}

	@Override
	public void updateUserInUserData(String oldUsername, String newUsername) {
		String newFirstname = faker.name().firstName();
		String newSurname = faker.name().lastName();
		userdataJdbcTemplate.update("UPDATE users SET username=?, firstname=?, surname=? WHERE username=?",
				newUsername, newFirstname, newSurname, oldUsername);
	}

	@Override
	public void deleteUserByUsernameInUserData(UUID userId, String username) {
		userdataJdbcTemplate.update("DELETE FROM users WHERE username=?", username);
	}

	@Override
	public UserDataEntity getUserData(String username) {
		return userdataJdbcTemplate.queryForObject(
				"SELECT * FROM users WHERE username=?",
				UserDataEntityRowMapper.instance, username);
	}
}
