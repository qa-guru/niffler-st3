package guru.qa.niffler.db.dao;

import guru.qa.niffler.db.DataSourceProvider;
import guru.qa.niffler.db.ServiceDB;
import guru.qa.niffler.db.model.Authority;
import guru.qa.niffler.db.model.AuthorityEntity;
import guru.qa.niffler.db.model.CurrencyValues;
import guru.qa.niffler.db.model.UserEntity;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class AuthUserDAOJdbc implements AuthUserDAO, UserDataUserDAO {

    private static final DataSource authDs = DataSourceProvider.INSTANCE.getDataSource(ServiceDB.AUTH);
    private static final DataSource userdataDs = DataSourceProvider.INSTANCE.getDataSource(ServiceDB.USERDATA);

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
    public UserEntity updateUser(UserEntity user) {
        try (Connection conn = authDs.getConnection();
             PreparedStatement usersPs = conn.prepareStatement(
                     "UPDATE users SET " +
                             "password = ?, " +
                             "enabled = ?, " +
                             "account_non_expired = ?, " +
                             " account_non_locked = ? , " +
                             "credentials_non_expired = ? " +
                             "WHERE id = ? ")) {

            usersPs.setString(1, pe.encode(user.getPassword()));
            usersPs.setBoolean(2, user.getEnabled());
            usersPs.setBoolean(3, user.getAccountNonExpired());
            usersPs.setBoolean(4, user.getAccountNonLocked());
            usersPs.setBoolean(5, user.getCredentialsNonExpired());
            usersPs.setObject(6, user.getId());
            usersPs.executeUpdate();
            return getUserById(user.getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteUserById(UUID userId) {
        try (Connection conn = authDs.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement usersPs = conn.prepareStatement(
                    "DELETE from users WHERE id = ?");

                 PreparedStatement authorityPs = conn.prepareStatement(
                         "DELETE from authorities WHERE user_id = ?")) {

                usersPs.setObject(1, userId);
                authorityPs.setObject(1, userId);

                authorityPs.executeUpdate();
                usersPs.executeUpdate();

                conn.commit();
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                conn.rollback();
                conn.setAutoCommit(true);
                System.out.println(e.getMessage());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserEntity getUserById(UUID userId) {
        UserEntity user = new UserEntity();

        try (Connection conn = authDs.getConnection();
             PreparedStatement usersPs = conn.prepareStatement("SELECT * FROM users WHERE id = ?");
             PreparedStatement authorityPs = conn.prepareStatement("SELECT * FROM authorities WHERE user_id = ?")) {

            usersPs.setObject(1, userId);
            usersPs.execute();
            authorityPs.setObject(1, userId);
            authorityPs.execute();

            ResultSet resultUsers = usersPs.getResultSet();

            if (resultUsers.next()) {
                user.setId(userId);
                user.setUsername(resultUsers.getString("username"));
                user.setPassword(resultUsers.getString("password"));
                user.setEnabled(resultUsers.getBoolean("enabled"));
                user.setAccountNonExpired(resultUsers.getBoolean("account_non_expired"));
                user.setAccountNonLocked(resultUsers.getBoolean("account_non_locked"));
                user.setCredentialsNonExpired(resultUsers.getBoolean("credentials_non_expired"));
            }

            ResultSet resultAuthorities = authorityPs.getResultSet();
            while (resultAuthorities.next()) {
                AuthorityEntity authorityEntity = new AuthorityEntity();
                authorityEntity.setId((UUID) resultAuthorities.getObject("id"));
                authorityEntity.setAuthority(Authority.valueOf(resultAuthorities.getString("authority")));
                authorityEntity.setUser(user);
                user.getAuthorities().add(authorityEntity);
            }
            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int createUserInUserData(UserEntity user) {
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
    public void deleteUserByNameInUserData(String username) {
        try (Connection conn = userdataDs.getConnection()) {
            try (PreparedStatement usersPs = conn.prepareStatement(
                    "DELETE FROM users WHERE username = ?")) {

                usersPs.setString(1, username);
                usersPs.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
