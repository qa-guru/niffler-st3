package guru.qa.niffler.db.springjdbc;

import guru.qa.niffler.db.model.CurrencyValues;
import guru.qa.niffler.db.model.userdata.UserDataEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UserDataEntityRowMapper implements RowMapper<UserDataEntity> {

    public static final UserDataEntityRowMapper instance = new UserDataEntityRowMapper();

    @Override
    public UserDataEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserDataEntity userData = new UserDataEntity();
        userData.setId(rs.getObject("id", UUID.class));
        userData.setUsername(rs.getString("username"));
        userData.setCurrency((CurrencyValues) rs.getObject("currency"));
        userData.setFirstname(rs.getString("firstname"));
        userData.setSurname(rs.getString("surname"));
        userData.setPhoto(rs.getBytes("photo"));
        return userData;
    }
}
