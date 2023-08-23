package guru.qa.niffler.test;

import guru.qa.niffler.jupiter.WebTest;
import guru.qa.niffler.page.LoginPage;

@WebTest
public abstract class BaseWebTest {

    protected LoginPage loginPage = new LoginPage();
}
