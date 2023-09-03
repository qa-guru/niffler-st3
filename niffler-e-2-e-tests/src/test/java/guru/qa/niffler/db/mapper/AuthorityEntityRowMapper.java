package guru.qa.niffler.db.mapper;

import guru.qa.niffler.db.model.auth.Authority;
import guru.qa.niffler.db.model.auth.AuthorityEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthorityEntityRowMapper implements RowMapper<AuthorityEntity> {

	public static final AuthorityEntityRowMapper instance = new AuthorityEntityRowMapper();

	@Override
	public AuthorityEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		AuthorityEntity authorityEntity = new AuthorityEntity();
		authorityEntity.setAuthority(Authority.valueOf(rs.getString("authority").toLowerCase()));
		return authorityEntity;
	}
}
