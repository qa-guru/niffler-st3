package guru.qa.niffler.page;

import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class AlertPopup {

	private final String ALERT_TEXT = "//*[@role='alert']//div[2]";
	private final String ALERT_CLOSE_ICON = ".Toastify__close-button";


	@Step("Отображается алерт с текстом '{0}'")
	public void checkAlertText(String alert) {
		$x(ALERT_TEXT).shouldHave(text(alert));
	}

	@Step("Закрыть алерт")
	public void closeAlert() {
		$(ALERT_CLOSE_ICON).click();
	}

}
