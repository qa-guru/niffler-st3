package guru.qa.niffler.test;

import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.DBUser;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;

public class ApiLoginTest extends BaseWebTest{

    @Test
    @DBUser
    @ApiLogin
    @AllureId("500")
    void createCategory() {
        open(CFG.nifflerFrontUrl() + "/main");
        String categoryName = faker.animal().name();
        headerComponent.goToProfilePage()
                .fillCategoryName(categoryName)
                .clickCreateButton()
                .checkCategoryWithName(categoryName);
    }
}
