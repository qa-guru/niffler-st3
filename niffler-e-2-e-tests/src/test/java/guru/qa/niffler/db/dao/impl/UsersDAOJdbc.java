package guru.qa.niffler.db.dao.impl;

import guru.qa.niffler.db.ServiceDB;
import guru.qa.niffler.db.dao.AuthUserDAO;
import guru.qa.niffler.db.dao.UserdataUserDAO;
import guru.qa.niffler.db.jdbc.DataSourceProvider;
import guru.qa.niffler.db.model.CurrencyValues;
import guru.qa.niffler.db.model.auth.AuthUserEntity;
import guru.qa.niffler.db.model.auth.Authority;
import guru.qa.niffler.db.model.auth.AuthorityEntity;
import guru.qa.niffler.db.model.userdata.UserDataUserEntity;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UsersDAOJdbc implements AuthUserDAO, UserdataUserDAO {

    private static DataSource authDs = DataSourceProvider.INSTANCE.getDataSource(ServiceDB.AUTH);
    private static DataSource userdataDs = DataSourceProvider.INSTANCE.getDataSource(ServiceDB.USERDATA);

    @Override
    public void createUser(AuthUserEntity user) {
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

                usersPs.executeUpdate();
                UUID generatedUserId;
                try (ResultSet generatedKeys = usersPs.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        generatedUserId = UUID.fromString(generatedKeys.getString("id"));
                    } else {
                        throw new IllegalStateException("Can`t obtain id from given ResultSet");
                    }
                }

                for (AuthorityEntity authority : user.getAuthorities()) {
                    authorityPs.setObject(1, generatedUserId);
                    authorityPs.setString(2, authority.getAuthority().name());
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
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public AuthUserEntity updateUser(AuthUserEntity user) {
        try (Connection conn = authDs.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement usersPs = conn.prepareStatement(
                    "UPDATE users SET " +
                            "password = ?, " +
                            "enabled = ?, " +
                            "account_non_expired = ?, " +
                            "account_non_locked = ?, " +
                            "credentials_non_expired = ? " +
                            "WHERE id = ? ");

                 PreparedStatement clearAuthorityPs = conn.prepareStatement("DELETE FROM authorities WHERE user_id = ?");

                 PreparedStatement authorityPs = conn.prepareStatement(
                         "INSERT INTO authorities (user_id, authority) " +
                                 "VALUES (?, ?)")) {

                clearAuthorityPs.setObject(1, user.getId());
                clearAuthorityPs.executeUpdate();

                for (AuthorityEntity authority : user.getAuthorities()) {
                    authorityPs.setObject(1, user.getId());
                    authorityPs.setString(2, authority.getAuthority().name());
                    authorityPs.addBatch();
                    authorityPs.clearParameters();
                }
                authorityPs.executeBatch();

                usersPs.setString(1, pe.encode(user.getPassword()));
                usersPs.setBoolean(2, user.getEnabled());
                usersPs.setBoolean(3, user.getAccountNonExpired());
                usersPs.setBoolean(4, user.getAccountNonLocked());
                usersPs.setBoolean(5, user.getCredentialsNonExpired());
                usersPs.setObject(6, user.getId());
                usersPs.executeUpdate();

                conn.commit();
                conn.setAutoCommit(true);

                return getUserById(user.getId());
            } catch (SQLException e) {
                conn.rollback();
                conn.setAutoCommit(true);
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteUser(AuthUserEntity user) {
        try (Connection conn = authDs.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement usersPs = conn.prepareStatement("DELETE FROM users WHERE id = ?");
                 PreparedStatement authorityPs = conn.prepareStatement("DELETE FROM authorities WHERE user_id = ?")) {

                authorityPs.setObject(1, user.getId());
                usersPs.setObject(1, user.getId());

                authorityPs.executeUpdate();
                usersPs.executeUpdate();

                conn.commit();
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                conn.rollback();
                conn.setAutoCommit(true);
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public AuthUserEntity getUserById(UUID userId) {
        AuthUserEntity user = new AuthUserEntity();
        try (Connection conn = authDs.getConnection();
             PreparedStatement usersPs = conn.prepareStatement("SELECT * " +
                     "FROM public.users u " +
                     "JOIN authorities a ON u.id = a.user_id " +
                     "where u.id = ?")) {
            usersPs.setObject(1, userId);

            usersPs.execute();
            ResultSet resultSet = usersPs.getResultSet();

            boolean userNotSet = true;
            while (resultSet.next()) {
                if (userNotSet) {
                    user.setId(resultSet.getObject(1, UUID.class));
                    user.setUsername(resultSet.getString(2));
                    user.setPassword(resultSet.getString(3));
                    user.setEnabled(resultSet.getBoolean(4));
                    user.setAccountNonExpired(resultSet.getBoolean(5));
                    user.setAccountNonLocked(resultSet.getBoolean(6));
                    user.setCredentialsNonExpired(resultSet.getBoolean(7));
                    userNotSet = false;
                }

                AuthorityEntity authority = new AuthorityEntity();
                authority.setId(resultSet.getObject(8, UUID.class));
                authority.setAuthority(Authority.valueOf(resultSet.getString(10)));

                user.getAuthorities().add(authority);
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
    public void deleteUserInUserData(UserDataUserEntity user) {
        try (Connection conn = userdataDs.getConnection();
             PreparedStatement usersPs = conn.prepareStatement("DELETE FROM users WHERE id = ?")) {
            usersPs.setObject(1, user.getId());
            usersPs.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserDataUserEntity getUserInUserDataByUsername(String username) {
        UserDataUserEntity user = new UserDataUserEntity();
        try (Connection conn = userdataDs.getConnection();
             PreparedStatement usersPs = conn.prepareStatement("SELECT * FROM users WHERE username = ? ")) {
            usersPs.setObject(1, username);

            usersPs.execute();
            ResultSet resultSet = usersPs.getResultSet();

            if (resultSet.next()) {
                user.setId(resultSet.getObject("id", UUID.class));
                user.setUsername(resultSet.getString("username"));
                user.setCurrency(CurrencyValues.valueOf(resultSet.getString("currency")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }
}
