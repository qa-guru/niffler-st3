package guru.qa.niffler.config;

import org.apache.kafka.common.protocol.types.Field;

public interface Config {

	static Config getInstance() {
		if ("docker".equals(System.getProperty("test.env"))){
			return DockerConfig.config;
		}
		return LocalConfig.config;
	}

	String databaseHost();

	String nifflerFrontUrl();

	String nifflerSpendUrl();

	String nifflerAuthUrl();

	default String databaseUser() {
		return "postgres";
	}

	default String databasePassword() {
		return "secret";
	}

	default int databasePort() {
		return 5432;
	}

}