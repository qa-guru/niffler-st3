package guru.qa.niffler.db.dao.impl;

import guru.qa.niffler.db.jdbc.DataSourceProvider;
import guru.qa.niffler.db.ServiceDB;
import guru.qa.niffler.db.dao.AuthUserDAO;
import guru.qa.niffler.db.dao.UserDataUserDAO;
import guru.qa.niffler.db.model.*;
import guru.qa.niffler.db.model.auth.Authority;
import guru.qa.niffler.db.model.auth.AuthorityEntity;
import guru.qa.niffler.db.model.auth.UserEntity;
import guru.qa.niffler.db.model.userdata.UserDataEntity;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AuthUserDAOJdbc implements AuthUserDAO, UserDataUserDAO {

    private static DataSource authDs = DataSourceProvider.INSTANCE.getDataSource(ServiceDB.AUTH);
    private static DataSource userdataDs = DataSourceProvider.INSTANCE.getDataSource(ServiceDB.USERDATA);

    @Override
    public int createUser(UserEntity user) {
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
    public UserEntity getUser(UUID userId) {
        UserEntity user = new UserEntity();
        try (Connection conn = authDs.getConnection();
             PreparedStatement usersPs = conn.prepareStatement("SELECT * FROM users u " +
                     "JOIN authorities a ON u.id = a.user_id " +
                     "WHERE u.id = ?")) {
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

                List<AuthorityEntity> authorities = new ArrayList<AuthorityEntity>();
                AuthorityEntity authority = new AuthorityEntity();
                authority.setAuthority(Authority.valueOf(resultSet.getString("authority")));
                authorities.add(authority);
                while (resultSet.next()) {
                    AuthorityEntity ae = new AuthorityEntity();
                    ae.setAuthority(Authority.valueOf(resultSet.getString("authority")));
                    authorities.add(ae);
                }
                user.setAuthorities(authorities);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    @Override
    public void updateUser(UserEntity user) {
        try (Connection conn = authDs.getConnection();
             PreparedStatement usersPs = conn.prepareStatement(
                     "UPDATE users SET " +
                             "password = ?," +
                             "enabled = ?," +
                             "account_non_expired = ?," +
                             "account_non_locked = ?," +
                             "credentials_non_expired = ? " +
                             "WHERE id = ? ")) {
            usersPs.setString(1, pe.encode(user.getPassword()));
            usersPs.setBoolean(2, user.getEnabled());
            usersPs.setBoolean(3, user.getAccountNonExpired());
            usersPs.setBoolean(4, user.getAccountNonLocked());
            usersPs.setBoolean(5, user.getCredentialsNonExpired());
            usersPs.setObject(6, user.getId());
            usersPs.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteUserById(UUID userId) {
        try (Connection conn = authDs.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement authorityPs = conn.prepareStatement(
                    "DELETE FROM authorities WHERE user_id = ?");
                 PreparedStatement usersPs = conn.prepareStatement(
                    "DELETE FROM users WHERE id = ?")) {

                usersPs.setObject(1, userId);
                authorityPs.setObject(1, userId);

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
    public int createUserInUserData(UserEntity user) {
        int createdRows = 0;
        try (Connection conn = userdataDs.getConnection()) {
            try (PreparedStatement usersPs = conn.prepareStatement(
                    "INSERT INTO users (username, currency) " +
                            "VALUES (?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {

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
    public UserDataEntity getUserInUserDataByUsername(String username) {
        UserDataEntity userData = new UserDataEntity();
        try (Connection conn = userdataDs.getConnection();
             PreparedStatement usersPs = conn.prepareStatement("SELECT * FROM users WHERE username = ?")) {
            usersPs.setObject(1, username);
            usersPs.execute();
            ResultSet resultSet = usersPs.getResultSet();

            if (resultSet.next()) {
                userData.setId(resultSet.getObject("id", UUID.class));
                userData.setUsername(resultSet.getString("username"));
                userData.setCurrency((CurrencyValues) resultSet.getObject("currency"));
                userData.setFirstname(resultSet.getString("firstname"));
                userData.setSurname(resultSet.getString("surname"));
                userData.setPhoto(resultSet.getBytes("photo"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userData;
    }

    @Override
    public void updateUserInUserData(UserDataEntity userDe) {
        try (Connection conn = userdataDs.getConnection();
             PreparedStatement usersPs = conn.prepareStatement(
                     "UPDATE users SET " +
                             "currency = ?," +
                             "firstname = ?," +
                             "surname = ?," +
                             "photo = ?," +
                             "WHERE id = ? ")) {
            usersPs.setString(1, userDe.getCurrency().name());
            usersPs.setString(2, userDe.getFirstname());
            usersPs.setString(3, userDe.getSurname());
            usersPs.setBytes(4, userDe.getPhoto());
            usersPs.setObject(5, userDe.getId());
            usersPs.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteUserByUsernameInUserData(String username) {
        try (Connection conn = userdataDs.getConnection()) {
            try (PreparedStatement usersPs = conn.prepareStatement(
                    "DELETE FROM users WHERE username = ? ")) {
                usersPs.setString(1, username);
                usersPs.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
