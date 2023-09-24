package guru.qa.niffler.jupiter.extension;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.ByteArrayInputStream;

import static com.codeborne.selenide.Selenide.closeWebDriver;

public class BrowserExtension implements BeforeEachCallback,AfterEachCallback, TestExecutionExceptionHandler {


    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        Configuration.browser = "chrome";
        Configuration.browserSize = "1980x1024";
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        if (WebDriverRunner.hasWebDriverStarted()) {
            closeWebDriver();
        }
    }

    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        if (WebDriverRunner.hasWebDriverStarted()) {
            Allure.addAttachment("Screenshot on fail", new ByteArrayInputStream(
                    ((TakesScreenshot) WebDriverRunner.getWebDriver()).getScreenshotAs(OutputType.BYTES)
            ));
        }
        throw throwable;
    }
}
