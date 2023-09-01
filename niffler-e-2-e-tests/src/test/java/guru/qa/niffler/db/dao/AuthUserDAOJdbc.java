package guru.qa.niffler.db.dao;

import com.github.javafaker.Faker;
import guru.qa.niffler.db.DataSourceProvider;
import guru.qa.niffler.db.ServiceDB;
import guru.qa.niffler.db.model.*;

import javax.sql.DataSource;
import java.sql.*;
import java.util.UUID;

public class AuthUserDAOJdbc implements AuthUserDAO, UserDataUserDAO {

	private static DataSource authDs = DataSourceProvider.INSTANCE.getDataSource(ServiceDB.AUTH);
	private static DataSource userdataDs = DataSourceProvider.INSTANCE.getDataSource(ServiceDB.USERDATA);

	Faker faker = new Faker();

	@Override
	public UUID createUser(UserEntity user) {
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
	public void deleteUserById(UUID userId) {
		String deleteAuthoritySQL = "DELETE FROM authorities WHERE user_id=?::uuid";
		String deleteUserSQL = "DELETE FROM users WHERE id=?::uuid";
		try (Connection conn = authDs.getConnection()) {

			conn.setAutoCommit(false);
			try (PreparedStatement authorityPs = conn.prepareStatement(deleteAuthoritySQL);
			     PreparedStatement usersPs = conn.prepareStatement(deleteUserSQL)) {

				authorityPs.setString(1, userId.toString());
				authorityPs.executeUpdate();

				usersPs.setString(1, userId.toString());
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
	public int createUserInUserData(UserEntity user) {
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
	public UserEntity getUser(UUID userId) {
		UserEntity user = new UserEntity();
		String getUsernameSql = "SELECT * FROM users WHERE id=?::uuid";
		try (Connection conn = authDs.getConnection()) {
			try (PreparedStatement usersPs = conn.prepareStatement(getUsernameSql)) {
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
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return user;
	}

	@Override
	public UserEntity getUser(String username) {
		UserEntity user = new UserEntity();
		String getUsernameSql = "SELECT * FROM users WHERE username=?";
		try (Connection conn = authDs.getConnection()) {
			try (PreparedStatement usersPs = conn.prepareStatement(getUsernameSql)) {
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
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return user;
	}

	@Override
	public void deleteUserByUsernameInUserData(UUID userId, String username) {

	}

	@Override
	public void updateUserById(UUID userId, String username) {
		boolean enabled = true;
		boolean accountNonExpired = true;
		boolean accountNonLocked = true;
		boolean credentialsNonExpired = true;
		String updateSql = "UPDATE users SET username=?, enabled=?, account_non_expired=?, " +
				"account_non_locked=?, credentials_non_expired=? WHERE id=?::uuid";
		try (Connection conn = authDs.getConnection()) {
			try (PreparedStatement usersPs = conn.prepareStatement(updateSql)) {
				usersPs.setString(1, username);
				usersPs.setBoolean(2, enabled);
				usersPs.setBoolean(3, accountNonExpired);
				usersPs.setBoolean(4, accountNonLocked);
				usersPs.setBoolean(5, credentialsNonExpired);
				usersPs.setString(6, userId.toString());
				usersPs.executeUpdate();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void updateUserInUserData(String oldUsername, String newUsername) {
		String newFirstname = faker.name().firstName();
		String newSurname = faker.name().lastName();
		String updateSql = "UPDATE users SET username=?, firstname=?, surname=? WHERE username=?";
		try (Connection conn = userdataDs.getConnection()) {
			try (PreparedStatement usersPs = conn.prepareStatement(updateSql)) {
				usersPs.setString(1, newUsername);
				usersPs.setString(2, newFirstname);
				usersPs.setString(3, newSurname);
				usersPs.setString(4, oldUsername);
				usersPs.executeUpdate();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public UserDataEntity getUserData(String username) {
		UserDataEntity user = new UserDataEntity();
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
