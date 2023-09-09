package guru.qa.niffler.page;

import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class MainPage {

	private final String MAIN_PAGE = ".main-content";
	private final String SELECT_CATEGORY = ".add-spending__form .select-wrapper";
	private final String AMOUNT_INPUT = "[name='amount']";
	private final String DESCRIPTION_INPUT = "[name='description']";
	private final String ADD_SPENDING_BUTTON = "//button[text()='Add new spending']";


	@Step("Главная страница отображается")
	public void checkMainPageIsVisible() {
		$(MAIN_PAGE).shouldBe(visible);
	}

	@Step("Главная страница не отображается")
	public void checkMainPageIsNotVisible() {
		$(MAIN_PAGE).shouldBe(not(visible));
	}

	@Step("Кликнуть по селекту категории")
	public void clickSelectCategory() {
		$(SELECT_CATEGORY).click();
	}

	@Step("Кликнуть по категории")
	public void selectCategory(String category) {
		$x(String.format("//*[text()='%s']", category)).click();
	}

	@Step("Кликнуть по категории")
	public void setAmount(String amount) {
		$(AMOUNT_INPUT).setValue(amount);
	}

	@Step("Кликнуть по категории")
	public void setDescription(String description) {
		$(DESCRIPTION_INPUT).setValue(description);
	}

	@Step("Кликнуть по категории")
	public void clickAddSpendingButton() {
		$x(ADD_SPENDING_BUTTON).click();
	}

	@Step("Расходы на {0} отображены")
	public void checkSpendingIsVisible(String spending) {
		$(".spendings__content tbody")
				.$$("tr")
				.find(text(spending))
				.$("td")
				.scrollTo()
				.click();
	}

}
