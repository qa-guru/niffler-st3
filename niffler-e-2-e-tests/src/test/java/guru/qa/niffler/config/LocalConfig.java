package guru.qa.niffler.config;

public class LocalConfig implements Config {

    static final LocalConfig config = new LocalConfig();

    private LocalConfig() {
    }

    @Override
    public String databaseHost() {
        return "localhost";
    }
}
