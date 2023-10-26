package guru.qa.niffler.config;

import com.codeborne.selenide.Configuration;
import org.openqa.selenium.chrome.ChromeOptions;

public class DockerConfig implements Config {

    static final DockerConfig config = new DockerConfig();

    static {
        Configuration.remote = "http://selenoid:4444/wd/hub";
        Configuration.browserSize = "1980x1024";
        Configuration.browser = "chrome";
        Configuration.browserVersion = "110.0";
        Configuration.browserCapabilities = new ChromeOptions()
                .addArguments("--no-sandbox");
    }

    private DockerConfig() {
    }

    @Override
    public String databaseHost() {
        return "niffler-all-db";
    }

    @Override
    public String nifflerSpendUrl() {
        return "http://niffler-spend:8093";
    }

    @Override
    public String nifflerFrontUrl() {
        return "http://frontend.niffler.dc";
    }

    @Override
    public String nifflerAuthUrl() {
        return "http://auth.niffler.dc:9000";
    }

    @Override
    public String nifflerUserdataUrl() {
        return "userdata.niffler.dc:8089";
    }

    @Override
    public String nifflerCurrencyUrl() {
        return "http://currency.niffler.dc";
    }

    @Override
    public String nifflerGatewayUrl() {
        return "http://gateway.niffler.dc:8090";
    }

    @Override
    public String kafkaAddress() {
        return "kafka:9092";
    }
}
