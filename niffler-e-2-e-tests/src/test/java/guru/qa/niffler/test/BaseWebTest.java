package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Page;
import guru.qa.niffler.jupiter.annotation.WebTest;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;

@WebTest
public abstract class BaseWebTest {

    static {
        Configuration.browser = "chrome";
        Configuration.browserSize = "1980x1024";
    }

    @Step("Открыть страницу {0}")
    public static synchronized <T> T openPage(Class<T> pageClass) {
        Selenide.open(Config.getInstance().baseUrl() + pageClass.getAnnotation(Page.class).url());
        try {
            return pageClass.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Something goes wrong. Error:" + e);
        }
    }

    @Step("Перейти на вкладку: \"{0}\"")
    public static synchronized void goToTab(String tabName) {
        $x(".//li[@data-tooltip-content='" + tabName + "']").shouldBe(visible).click();
    }
}
