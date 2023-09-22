package guru.qa.niffler.test;

import guru.qa.niffler.db.model.auth.AuthUserEntity;
import guru.qa.niffler.jupiter.annotation.DBUser;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Test;

public class ProfileWebTest extends BaseWebTest {

    @Test
    @DBUser(password = defaultPassword)
    @AllureId("400")
    void createCategory(AuthUserEntity user) {
        String categoryName = faker.animal().name();
        loginPage
                .login(user.getUsername(), defaultPassword);
        headerComponent.goToProfilePage()
                .fillCategoryName(categoryName)
                .clickCreateButton()
                .checkCategoryWithName(categoryName);
    }

    @Test
    @DBUser(password = defaultPassword)
    @AllureId("401")
    void editSurname(AuthUserEntity user) {
        String surname = faker.animal().name();
        loginPage
                .login(user.getUsername(), defaultPassword);
        headerComponent.goToProfilePage()
                .fillSurname(surname)
                .clickSubmitButton()
                .checkValueSurname(surname);
    }

    @Test
    @DBUser(password = defaultPassword)
    @AllureId("402")
    void editName(AuthUserEntity user) {
        String name = faker.animal().name();
        loginPage
                .login(user.getUsername(), defaultPassword);
        headerComponent.goToProfilePage()
                .fillName(name)
                .clickSubmitButton()
                .checkValueName(name);
    }

    @Test
    @DBUser(password = defaultPassword)
    @AllureId("403")
    void editCurrency(AuthUserEntity user) {
        String currency = "EUR";
        loginPage
                .login(user.getUsername(), defaultPassword);
        headerComponent.goToProfilePage()
                .setCurrency(currency)
                .checkCurrencyValue(currency);
    }
}
