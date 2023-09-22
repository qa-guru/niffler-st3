package guru.qa.niffler.config;

public class DockerConfig implements Config {

	static final DockerConfig config = new DockerConfig();

	private DockerConfig(){

	}

	@Override
	public String databaseHost() {
		return "niffler-all-db";
	}

	@Override
	public String nifflerFrontUrl() {
		return "http://frontend.niffler.dc";
	}

	@Override
	public String nifflerSpendUrl() {
		return "niffler-spend:8093";
	}

	@Override
	public String nifflerAuthUrl() {
		return "http://auth.niffler.dc";
	}
}
