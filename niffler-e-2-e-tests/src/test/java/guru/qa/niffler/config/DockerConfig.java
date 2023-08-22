package guru.qa.niffler.config;

public interface DockerConfig {

	default String databaseHost(){
		return "niffler-all-db";
	}
}
