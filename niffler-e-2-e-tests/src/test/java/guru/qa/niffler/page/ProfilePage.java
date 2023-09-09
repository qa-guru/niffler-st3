package guru.qa.niffler.page;

import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class ProfilePage {

	private final String CATEGORY_INPUT = "[name='category']";
	private final String CREATE_CATEGORY_BUTTON = "//button[text()='Create']";
	private final String CATEGORY_IN_LIST = "//li[@class='categories__item' and text()='%s']";

	private final String FIRSTNAME_INPUT = "[name='firstname']";
	private final String SURNAME_INPUT = "[name='surname']";
	private final String SUBMIT_BUTTON = "//button[text()='Submit']";

	@Step("Создать категорию '{0}'")
	public void createCategory(String category) {
		$(CATEGORY_INPUT).setValue(category);
		$x(CREATE_CATEGORY_BUTTON).click();
	}

	@Step("В списке отображается категория '{0}'")
	public void checkCategoryIsVisibleInList(String category) {
		$x(String.format(CATEGORY_IN_LIST, category)).scrollIntoView(false).shouldBe(visible);
	}

	@Step("В списке не отображается категория '{0}'")
	public void checkCategoryIsNotVisibleInList(String category) {
		$x(String.format(CATEGORY_IN_LIST, category)).shouldBe(not(exist));
	}

	@Step("Ввести имя {0}")
	public void setFirstname(String firstname) {
		$(FIRSTNAME_INPUT).setValue(firstname);
	}

	@Step("Ввести фамилию {0}")
	public void setLastname(String lastname) {
		$(SURNAME_INPUT).setValue(lastname);
	}

	@Step("Нажать кнопку подтверждения")
	public void clickSubmitButton() {
		$x(SUBMIT_BUTTON).scrollIntoView(true).click();
	}
}