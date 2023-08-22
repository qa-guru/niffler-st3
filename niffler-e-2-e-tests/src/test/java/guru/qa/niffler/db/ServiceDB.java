package guru.qa.niffler.db;

public enum ServiceDB {
	AUTH("jdbc:postgresql://niffler-all-db:5432/niffler-auth"),
	USERDATA("jdbc:postgresql://niffler-all-db:5432/niffler-userdata"),
	CURRENCY("jdbc:postgresql://niffler-all-db:5432/niffler-currency"),
	SPEND("jdbc:postgresql://niffler-all-db:5432/niffler-spend");

	private final String url;

	String ServiceDB(String url) {
		return url;
	}
}