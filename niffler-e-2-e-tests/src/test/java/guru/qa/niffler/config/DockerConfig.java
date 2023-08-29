package guru.qa.niffler.config;

import com.codeborne.selenide.Configuration;

public class DockerConfig implements Config {

    static final DockerConfig config = new DockerConfig();

    static {
        Configuration.remote = "http://localhost:4444/wd";
    }

    private DockerConfig() {
    }

    @Override
    public String databaseHost() {
        return "niffler-all-db";
    }

    @Override
    public String nifflerSpendUrl() {
        return "niffler-spend:8093";
    }
}
