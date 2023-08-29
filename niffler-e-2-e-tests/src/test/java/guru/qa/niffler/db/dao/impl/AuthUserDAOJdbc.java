package guru.qa.niffler.db.dao.impl;

import guru.qa.niffler.db.ServiceDB;
import guru.qa.niffler.db.dao.AuthUserDAO;
import guru.qa.niffler.db.dao.UserDataUserDAO;
import guru.qa.niffler.db.jdbc.DataSourceProvider;
import guru.qa.niffler.db.model.CurrencyValues;
import guru.qa.niffler.db.model.auth.AuthUserEntity;
import guru.qa.niffler.db.model.auth.Authority;
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

    @Override
    public int createUser(AuthUserEntity user) {
        int createdRows = 0;
        try (Connection conn = authDs.getConnection()) {

            conn.setAutoCommit(false);

            try (PreparedStatement usersPs = conn.prepareStatement(
                    "INSERT INTO users (username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) " +
                            "VALUES (?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);

                 PreparedStatement authorityPs = conn.prepareStatement(
                         "INSERT INTO authorities (user_id, authority) " +
                                 "VALUES (?, ?)")) {

                usersPs.setString(1, user.getUsername());
                usersPs.setString(2, pe.encode(user.getPassword()));
                usersPs.setBoolean(3, user.getEnabled());
                usersPs.setBoolean(4, user.getAccountNonExpired());
                usersPs.setBoolean(5, user.getAccountNonLocked());
                usersPs.setBoolean(6, user.getCredentialsNonExpired());

                createdRows = usersPs.executeUpdate();
                UUID generatedUserId;

                try (ResultSet generatedKeys = usersPs.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        generatedUserId = UUID.fromString(generatedKeys.getString("id"));
                    } else {
                        throw new IllegalStateException("Can`t obtain id from given ResultSet");
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
        return createdRows;
    }

    @Override
    public AuthUserEntity updateUser(AuthUserEntity user) {
        try (Connection conn = authDs.getConnection();
             PreparedStatement usersPs = conn.prepareStatement(
                     "UPDATE users SET " +
                             "password = ?, " +
                             "enabled = ?, " +
                             "account_non_expired = ?, " +
                             " account_non_locked = ? , " +
                             "credentials_non_expired = ? " +
                             "WHERE id = ? ")) {

            usersPs.setString(3, pe.encode(user.getPassword()));
            usersPs.setBoolean(4, user.getEnabled());
            usersPs.setBoolean(5, user.getAccountNonExpired());
            usersPs.setBoolean(6, user.getAccountNonLocked());
            usersPs.setBoolean(7, user.getCredentialsNonExpired());
            usersPs.setObject(8, user.getId());
            usersPs.executeUpdate();
            return getUserById(user.getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteUser(AuthUserEntity userId) {
        try (Connection conn = authDs.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement usersPs = conn.prepareStatement("DELETE FROM users WHERE id = ?");
                 PreparedStatement authorityPs = conn.prepareStatement("DELETE FROM authorities WHERE user_id = ?")) {

                authorityPs.setObject(1, userId.getId());
                usersPs.setObject(1, userId.getId());

                authorityPs.executeUpdate();
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
    public AuthUserEntity getUserById(UUID userId) {
        AuthUserEntity user = new AuthUserEntity();
        try (Connection conn = authDs.getConnection();
             PreparedStatement usersPs = conn.prepareStatement("SELECT * FROM users WHERE id = ? ")) {
            usersPs.setObject(1, userId);

            usersPs.execute();
            ResultSet resultSet = usersPs.getResultSet();

            if (resultSet.next()) {
                user.setId(resultSet.getObject("id", UUID.class));
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                user.setEnabled(resultSet.getBoolean("enabled"));
                user.setAccountNonExpired(resultSet.getBoolean("account_non_expired"));
                user.setAccountNonLocked(resultSet.getBoolean("account_non_locked"));
                user.setCredentialsNonExpired(resultSet.getBoolean("credentials_non_expired"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    @Override
    public int createUserInUserData(UserDataUserEntity user) {
        int createdRows;
        try (Connection conn = userdataDs.getConnection();
             PreparedStatement usersPs = conn.prepareStatement("INSERT INTO users (username, currency) VALUES (?, ?)")) {

            usersPs.setString(1, user.getUsername());
            usersPs.setString(2, CurrencyValues.RUB.name());

            createdRows = usersPs.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return createdRows;
    }

    @Override
    public void deleteUserByIdInUserData(UUID userId) {
        try (Connection conn = authDs.getConnection();
             PreparedStatement usersPs = conn.prepareStatement("DELETE FROM users WHERE id = ?")) {
            usersPs.setObject(1, userId);
            usersPs.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteUserByUsernameInUserData(String username) {
        try (Connection conn = authDs.getConnection();
             PreparedStatement usersPs = conn.prepareStatement("DELETE FROM users WHERE username = ?")) {
            usersPs.setString(1, username);
            usersPs.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
