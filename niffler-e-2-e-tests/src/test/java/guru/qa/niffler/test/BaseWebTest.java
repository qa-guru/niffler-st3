package guru.qa.niffler.test;

import com.github.javafaker.Faker;
import guru.qa.niffler.jupiter.annotation.WebTest;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.components.HeaderComponent;

@WebTest
public abstract class BaseWebTest {

    protected LoginPage loginPage = new LoginPage();
    protected HeaderComponent headerComponent = new HeaderComponent();
    protected Faker faker = new Faker();

    protected static final String defaultPassword = "12345";
}
