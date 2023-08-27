package guru.qa.niffler.jupiter.extention;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import guru.qa.niffler.jupiter.annotations.WebTest;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.ByteArrayInputStream;
public class BrowserExtension implements AfterAllCallback, TestExecutionExceptionHandler {
    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        if(WebDriverRunner.hasWebDriverStarted()){
            Selenide.closeWebDriver();
        }
    }

    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        if(WebDriverRunner.hasWebDriverStarted()){
            Allure.addAttachment("Screenshot fail test!", new ByteArrayInputStream(
                    ((TakesScreenshot)WebDriverRunner.getWebDriver()).getScreenshotAs(OutputType.BYTES)));
            throw throwable;
        }
    }
}
