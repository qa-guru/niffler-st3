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
    public String nifflerFrontUrl() {
        return "http://127.0.0.1:3000";
    }

    @Override
    public String nifflerSpendUrl() {
        return "http://127.0.0.1:8093";
    }

    @Override
    public String nifflerAuthUrl() {
        return "http://127.0.0.1:9000";
    }

    @Override
    public String getCurrencyGrpcAddress() {
        return "localhost";
    }

    @Override
    public int getCurrencyGrpcPort() {
        return 8092;
    }
}
