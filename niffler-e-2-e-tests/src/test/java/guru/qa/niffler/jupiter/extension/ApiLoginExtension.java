package guru.qa.niffler.jupiter.extension;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import guru.qa.niffler.api.AuthServiceClient;
import guru.qa.niffler.api.context.CookieContext;
import guru.qa.niffler.api.context.SessionStorageContext;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.db.model.auth.AuthUserEntity;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.Cookie;

import java.io.IOException;

import static guru.qa.niffler.jupiter.extension.DBUserExtension.getContextUser;

public class ApiLoginExtension implements BeforeEachCallback, AfterTestExecutionCallback {

	private final AuthServiceClient authServiceClient = new AuthServiceClient();

	@Override
	public void beforeEach(ExtensionContext extensionContext) {
		ApiLogin annotation = extensionContext.getRequiredTestMethod().getAnnotation(ApiLogin.class);
		if (annotation != null) {
			if (annotation.username().equals("")) {
				AuthUserEntity user = getContextUser(extensionContext);
				doLogin(user.getUsername(), user.getPassword());
			} else doLogin(annotation.username(), annotation.password());
		}
	}

	private void doLogin(String username, String password) {
		SessionStorageContext sessionStorageContext = SessionStorageContext.getInstance();
		sessionStorageContext.init();

		try {
			authServiceClient.doLogin(username, password);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		Selenide.open(Config.getInstance().nifflerFrontUrl());
		Selenide.sessionStorage().setItem("codeChallenge", sessionStorageContext.getCodeChallenge());
		Selenide.sessionStorage().setItem("id_token", sessionStorageContext.getToken());
		Selenide.sessionStorage().setItem("codeVerifier", sessionStorageContext.getCodeVerifier());
		Cookie jsessionIdCookie = new Cookie("JSESSIONID", CookieContext.getInstance().getJSessionIdCookieValue());
		WebDriverRunner.getWebDriver().manage().addCookie(jsessionIdCookie);
	}

	@Override
	public void afterTestExecution(ExtensionContext extensionCcontext) throws Exception {
		SessionStorageContext.getInstance().clearContext();
		CookieContext.getInstance().clearContext();
	}
}

