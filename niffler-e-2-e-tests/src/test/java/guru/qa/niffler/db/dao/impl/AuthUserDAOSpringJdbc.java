package guru.qa.niffler.db.dao.impl;

import com.github.javafaker.Faker;
import guru.qa.niffler.db.jdbc.DataSourceProvider;
import guru.qa.niffler.db.ServiceDB;
import guru.qa.niffler.db.dao.AuthUserDAO;
import guru.qa.niffler.db.dao.UserdataUserDAO;
import guru.qa.niffler.db.springjdbc.AuthorityEntityRowMapper;
import guru.qa.niffler.db.springjdbc.UserDataEntityRowMapper;
import guru.qa.niffler.db.springjdbc.UserEntityRowMapper;
import guru.qa.niffler.db.model.auth.Authority;
import guru.qa.niffler.db.model.auth.AuthorityEntity;
import guru.qa.niffler.db.model.auth.AuthUserEntity;
import guru.qa.niffler.db.model.userdata.UserDataUserEntity;
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

public class AuthUserDAOSpringJdbc implements AuthUserDAO {

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
	public UUID createUser(AuthUserEntity user) {
		return authTtpl.execute(status -> {
			KeyHolder kh = new GeneratedKeyHolder();

			authJdbcTemplate.update(con -> {
				PreparedStatement ps = con.prepareStatement("INSERT INTO \"user\" " +
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
			authJdbcTemplate.batchUpdate("INSERT INTO authority (user_id, authority) " +
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
	public void deleteUser(AuthUserEntity user) {
		authJdbcTemplate.update("DELETE FROM authority WHERE user_id = ? ", user.getId());
		authJdbcTemplate.update("DELETE FROM \"user\" WHERE id=?", user.getId());
	}

	@Override
	public AuthUserEntity updateUser(AuthUserEntity user) {
		boolean enabled = true;
		boolean accountNonExpired = true;
		boolean accountNonLocked = true;
		boolean credentialsNonExpired = true;
		authJdbcTemplate.update("UPDATE \"user\" SET username=?, enabled=?, account_non_expired=?, " +
						"account_non_locked=?, credentials_non_expired=? WHERE id=?::uuid",
				user.getUsername(), enabled, accountNonExpired,
				accountNonLocked, credentialsNonExpired, user.getId().toString());
		return user;
	}

	@Override
	public AuthUserEntity getUser(UUID userId) {
		AuthUserEntity user = authJdbcTemplate.queryForObject(
				"SELECT * FROM \"user\" WHERE id=?::uuid",
				UserEntityRowMapper.instance,
				userId.toString());

		List<AuthorityEntity> authorities = authJdbcTemplate.query(
				"SELECT * FROM authority WHERE user_id=?::uuid",
				AuthorityEntityRowMapper.instance,
				userId.toString());

		user.setAuthorities(authorities);
		return user;
	}

	@Override
	public AuthUserEntity getUser(String username) {
		AuthUserEntity user = authJdbcTemplate.queryForObject(
				"SELECT * FROM \"user\" WHERE username=?",
				UserEntityRowMapper.instance,
				username);


		List<AuthorityEntity> authorities = authJdbcTemplate.query(
				"SELECT * FROM authority WHERE user_id=?::uuid",
				AuthorityEntityRowMapper.instance,
				user.getId());
		user.setAuthorities(authorities);
		return user;
	}
}
