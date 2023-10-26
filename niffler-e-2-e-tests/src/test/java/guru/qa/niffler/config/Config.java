package guru.qa.niffler.config;

import java.util.List;

public interface Config {

    static Config getInstance() {
        if ("docker".equals(System.getProperty("test.env"))) {
            return DockerConfig.config;
        }
        return LocalConfig.config;
    }

    String databaseHost();

    String nifflerFrontUrl();

    String nifflerSpendUrl();

    default String databaseUser() {
        return "postgres";
    }

    default String databasePassword() {
        return "secret";
    }

    default int databasePort() {
        return 5432;
    }

    String nifflerAuthUrl();

    String nifflerUserdataUrl();

    String nifflerCurrencyUrl();

    default int nifflerCurrencyPort() {
        return 8092;
    }

    String nifflerGatewayUrl();

    String kafkaAddress();

    default List<String> kafkaTopics() {
        return List.of("users");
    }
}
