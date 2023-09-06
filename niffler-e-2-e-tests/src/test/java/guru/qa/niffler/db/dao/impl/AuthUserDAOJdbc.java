package guru.qa.niffler.db.dao.impl;

import com.github.javafaker.Faker;
import guru.qa.niffler.db.jdbc.DataSourceProvider;
import guru.qa.niffler.db.ServiceDB;
import guru.qa.niffler.db.dao.AuthUserDAO;
import guru.qa.niffler.db.dao.UserDataUserDAO;
import guru.qa.niffler.db.model.CurrencyValues;
import guru.qa.niffler.db.model.auth.Authority;
import guru.qa.niffler.db.model.auth.AuthorityEntity;
import guru.qa.niffler.db.model.auth.AuthUserEntity;
import guru.qa.niffler.db.model.userdata.UserDataUserEntity;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class AuthUserDAOJdbc implements AuthUserDAO, UserDataUserDAO {

	private static DataSource authDs = DataSourceProvider.INSTANCE.getDataSource(ServiceDB.AUTH);
	private static DataSource userdataDs = DataSourceProvider.INSTANCE.getDataSource(ServiceDB.USERDATA);

	Faker faker = new Faker();

	@Override
	public UUID createUser(AuthUserEntity user) {
		try (Connection conn = authDs.getConnection()) {

			conn.setAutoCommit(false);

			try (PreparedStatement usersPs = conn.prepareStatement(
					"INSERT INTO users (username, password, enabled, " +
							"account_non_expired, account_non_locked, credentials_non_expired) " +
							"VALUES (?, ?, ?, ?, ?, ?)",
					PreparedStatement.RETURN_GENERATED_KEYS);
			     PreparedStatement authorityPs = conn.prepareStatement(
					     "INSERT INTO authorities (user_id, authority) VALUES (?, ?)")) {
				usersPs.setString(1, user.getUsername());
				usersPs.setString(2, pe.encode(user.getPassword()));
				usersPs.setBoolean(3, user.getEnabled());
				usersPs.setBoolean(4, user.getAccountNonExpired());
				usersPs.setBoolean(5, user.getAccountNonLocked());
				usersPs.setBoolean(6, user.getCredentialsNonExpired());

				usersPs.executeUpdate();
				UUID generatedUserId;
				try (ResultSet generatedKeys = usersPs.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						generatedUserId = UUID.fromString(generatedKeys.getString("id"));
					} else {
						throw new IllegalStateException("Не можем получить ID из ResultSet");
					}
				}

				for (Authority authority : Authority.values()) {
					authorityPs.setObject(1, generatedUserId);
					authorityPs.setString(2, authority.name());
					authorityPs.addBatch();
					authorityPs.clearParameters();
				}

				authorityPs.executeBatch();
				user.setId(generatedUserId);
				conn.commit();
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				conn.rollback();
				conn.setAutoCommit(true);
			}

		} catch (SQLException e) {

			throw new RuntimeException(e);
		}
		return user.getId();
	}

	@Override
	public void deleteUser(AuthUserEntity user) {
		String deleteAuthoritySQL = "DELETE FROM authorities WHERE user_id=?::uuid";
		String deleteUserSQL = "DELETE FROM users WHERE id=?::uuid";
		try (Connection conn = authDs.getConnection()) {

			conn.setAutoCommit(false);
			try (PreparedStatement authorityPs = conn.prepareStatement(deleteAuthoritySQL);
			     PreparedStatement usersPs = conn.prepareStatement(deleteUserSQL)) {

				authorityPs.setString(1, user.getId().toString());
				authorityPs.executeUpdate();

				usersPs.setString(1, user.getId().toString());
				usersPs.executeUpdate();

				conn.commit();
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				conn.rollback();
				conn.setAutoCommit(true);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public int createUserInUserData(UserDataUserEntity user) {
		int createdRows = 0;
		try (Connection conn = userdataDs.getConnection()) {
			try (PreparedStatement usersPs = conn.prepareStatement(
					"INSERT INTO users (username, currency) " +
							"VALUES (?, ?)",
					PreparedStatement.RETURN_GENERATED_KEYS)) {
				usersPs.setString(1, user.getUsername());
				usersPs.setString(2, CurrencyValues.RUB.name());
				createdRows = usersPs.executeUpdate();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return createdRows;
	}

	@Override
	public void deleteUserByIdInUserData(UUID userId) {
		String username = getUser(userId).getUsername();
		String deleteSql = "DELETE FROM users WHERE username=?";
		try (Connection conn = userdataDs.getConnection()) {
			try (PreparedStatement usersPs = conn.prepareStatement(deleteSql)) {
				usersPs.setString(1, username);
				usersPs.executeUpdate();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public AuthUserEntity getUser(UUID userId) {
		AuthUserEntity user = new AuthUserEntity();
		String usersSql = "SELECT * FROM users WHERE id=?::uuid";
		String authoritiesSql = "SELECT * FROM authorities WHERE user_id=?::uuid";
		try (Connection conn = authDs.getConnection()) {
			try (PreparedStatement usersPs = conn.prepareStatement(usersSql)) {
				usersPs.setString(1, userId.toString());
				ResultSet resultSet = usersPs.executeQuery();
				while (resultSet.next()) {
					user.setId(userId);
					user.setUsername(resultSet.getString("username"));
					user.setPassword(pe.encode(resultSet.getString("password")));
					user.setEnabled(resultSet.getBoolean("enabled"));
					user.setAccountNonExpired(resultSet.getBoolean("account_non_expired"));
					user.setAccountNonLocked(resultSet.getBoolean("account_non_locked"));
					user.setCredentialsNonExpired(resultSet.getBoolean("credentials_non_expired"));
				}
			}
			try (PreparedStatement usersPs = conn.prepareStatement(authoritiesSql)) {
				usersPs.setString(1, userId.toString());
				ResultSet resultSet = usersPs.executeQuery();
				while (resultSet.next()) {
					AuthorityEntity authorityEntity = new AuthorityEntity();
					Authority authority = Authority.valueOf(resultSet.getString("authority"));
					authorityEntity.setAuthority(authority);
					user.addAuthorities(authorityEntity);
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return user;
	}

	@Override
	public AuthUserEntity getUser(String username) {
		AuthUserEntity user = new AuthUserEntity();
		String usersSql = "SELECT * FROM users WHERE username=?";
		String authoritiesSql = "SELECT * FROM authorities WHERE user_id=?::uuid";
		try (Connection conn = authDs.getConnection()) {
			try (PreparedStatement usersPs = conn.prepareStatement(usersSql)) {
				usersPs.setString(1, username);
				ResultSet rs = usersPs.executeQuery();
				while (rs.next()) {
					user.setId(rs.getObject("id", UUID.class));
					user.setUsername(rs.getString("username"));
					user.setPassword(pe.encode(rs.getString("password")));
					user.setEnabled(rs.getBoolean("enabled"));
					user.setAccountNonExpired(rs.getBoolean("account_non_expired"));
					user.setAccountNonLocked(rs.getBoolean("account_non_locked"));
					user.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));
				}
			}
			try (PreparedStatement usersPs = conn.prepareStatement(authoritiesSql)) {
				usersPs.setString(1, user.getId().toString());
				ResultSet resultSet = usersPs.executeQuery();
				while (resultSet.next()) {
					AuthorityEntity authorityEntity = new AuthorityEntity();
					Authority authority = Authority.valueOf(resultSet.getString("authority"));
					authorityEntity.setAuthority(authority);
					user.addAuthorities(authorityEntity);
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return user;
	}

	@Override
	public void deleteUserByUsernameInUserData(String username) {
		String deleteSql = "DELETE FROM users WHERE username=?";
		try (Connection conn = userdataDs.getConnection()) {
			try (PreparedStatement usersPs = conn.prepareStatement(deleteSql)) {
				usersPs.setString(1, username);
				usersPs.executeUpdate();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public AuthUserEntity updateUser(AuthUserEntity user) {
		boolean enabled = true;
		boolean accountNonExpired = true;
		boolean accountNonLocked = true;
		boolean credentialsNonExpired = true;
		String updateSql = "UPDATE users SET username=?, enabled=?, account_non_expired=?, " +
				"account_non_locked=?, credentials_non_expired=? WHERE id=?::uuid";
		try (Connection conn = authDs.getConnection()) {
			try (PreparedStatement usersPs = conn.prepareStatement(updateSql)) {
				usersPs.setString(1, user.getUsername());
				usersPs.setBoolean(2, enabled);
				usersPs.setBoolean(3, accountNonExpired);
				usersPs.setBoolean(4, accountNonLocked);
				usersPs.setBoolean(5, credentialsNonExpired);
				usersPs.setString(6, user.getId().toString());
				usersPs.executeUpdate();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return user;
	}

	@Override
	public UserDataUserEntity updateUserInUserData(UserDataUserEntity user) {
		UserDataUserEntity userDataEntity = new UserDataUserEntity();
		String newUsername = faker.name().username();
		String newFirstname = faker.name().firstName();
		String newSurname = faker.name().lastName();
		String updateSql = "UPDATE users SET username=?, firstname=?, surname=? WHERE username=?";
		try (Connection conn = userdataDs.getConnection()) {
			try (PreparedStatement usersPs = conn.prepareStatement(updateSql)) {
				usersPs.setString(1, newUsername);
				usersPs.setString(2, newFirstname);
				usersPs.setString(3, newSurname);
				usersPs.setString(4, user.getUsername());
				usersPs.executeUpdate();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		userDataEntity.setUsername(newUsername);
		userDataEntity.setFirstname(newFirstname);
		userDataEntity.setSurname(newSurname);
		return userDataEntity;
	}

	@Override
	public UserDataUserEntity getUserData(String username) {
		UserDataUserEntity user = new UserDataUserEntity();
		String getUsernameSql = "SELECT * FROM users WHERE username=?";
		try (Connection conn = userdataDs.getConnection()) {
			try (PreparedStatement usersPs = conn.prepareStatement(getUsernameSql)) {
				usersPs.setString(1, username);
				ResultSet rs = usersPs.executeQuery();
				while (rs.next()) {
					user.setId(rs.getObject("id", UUID.class));
					user.setUsername(rs.getString("username"));
					user.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
					user.setFirstname(rs.getString("firstname"));
					user.setSurname(rs.getString("surname"));
					user.setPhoto(rs.getString("photo").getBytes());
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return user;
	}
}
