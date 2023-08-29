package guru.qa.niffler.config;

import com.codeborne.selenide.Configuration;

public class LocalConfig implements Config {

    static final LocalConfig config = new LocalConfig();

    static {
        Configuration.browserSize = "1980x1024";
    }

    private LocalConfig() {
    }

    @Override
    public String databaseHost() {
        return "localhost";
    }

    @Override
    public String nifflerSpendUrl() {
        return "http://127.0.0.1:8093";
    }
}
