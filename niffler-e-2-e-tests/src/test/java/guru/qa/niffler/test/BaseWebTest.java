package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import guru.qa.niffler.jupiter.annotation.WebTest;
import guru.qa.niffler.page.LoginPage;

@WebTest
public abstract class BaseWebTest {

    protected LoginPage loginPage = new LoginPage();

    static {
        Configuration.browser = "chrome";
        Configuration.browserSize = "1980x1024";
    }
}
