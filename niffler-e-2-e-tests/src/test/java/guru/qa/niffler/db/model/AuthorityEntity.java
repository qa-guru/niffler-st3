package guru.qa.niffler.db.model;

import java.util.UUID;

public class AuthorityEntity {

	private UUID id;
	private Authority authority;
	private UserEntity user;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Authority getAuthority() {
		return authority;
	}

	public void setAuthority(Authority authority) {
		this.authority = authority;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public static class UserDataEntity {
		private UUID id;

		private String username;

		private CurrencyValues currency;

		private String firstname;

		private String surname;

		private byte[] photo;

		public UUID getId() {
			return id;
		}

		public void setId(UUID id) {
			this.id = id;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public CurrencyValues getCurrency() {
			return currency;
		}

		public void setCurrency(CurrencyValues currency) {
			this.currency = currency;
		}

		public String getFirstname() {
			return firstname;
		}

		public void setFirstname(String firstname) {
			this.firstname = firstname;
		}

		public String getSurname() {
			return surname;
		}

		public void setSurname(String surname) {
			this.surname = surname;
		}

		public byte[] getPhoto() {
			return photo;
		}

		public void setPhoto(byte[] photo) {
			this.photo = photo;
		}
	}
}
