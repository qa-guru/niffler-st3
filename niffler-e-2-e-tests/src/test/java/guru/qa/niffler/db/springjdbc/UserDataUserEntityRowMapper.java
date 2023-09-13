package guru.qa.niffler.db.springjdbc;

import guru.qa.niffler.db.model.CurrencyValues;
import guru.qa.niffler.db.model.userdata.UserDataUserEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UserDataUserEntityRowMapper implements RowMapper<UserDataUserEntity> {

    public static final UserDataUserEntityRowMapper instance = new UserDataUserEntityRowMapper();

    @Override
    public UserDataUserEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserDataUserEntity user = new UserDataUserEntity();
        user.setId(rs.getObject("id", UUID.class));
        user.setUsername(rs.getString("username"));
        user.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
        return user;
    }
}
