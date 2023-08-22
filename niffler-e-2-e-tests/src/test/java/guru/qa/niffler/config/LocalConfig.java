package guru.qa.niffler.config;

public interface LocalConfig {

	private static final LocalConfig config = new LocalConfig() {
	}

	default String databaseHost(){
		return "localhost";
	}
}
