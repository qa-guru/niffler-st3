package guru.qa.niffler.page;

import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class HeaderPage {

	private final String MAIN_ELEMENT = ".header";
	private final String MAIN_PAGE_ICON = "[data-tooltip-id='main']";
	private final String FRIENDS_PAGE_ICON = "[data-tooltip-id='friends']";
	private final String PEOPLE_PAGE_ICON = "[data-tooltip-id='people']";
	private final String PROFILE_PAGE_ICON = "[data-tooltip-id='profile']";
	private final String LOGOUT_ICON = "[data-tooltip-id='logout']";

	@Step("Нажать на иконку 'Главная страница'")
	public void clickMainPageIcon() {
		$(MAIN_PAGE_ICON).click();
	}

	@Step("Нажать на иконку 'Друзья'")
	public void clickFriendsIcon() {
		$(FRIENDS_PAGE_ICON).click();
	}

	@Step("Нажать на иконку 'Люди'")
	public void clickPeopleIcon() {
		$(PEOPLE_PAGE_ICON).click();
	}

	@Step("Нажать на иконку 'Профиль'")
	public void clickProfileIcon() {
		$(PROFILE_PAGE_ICON).click();
	}

	@Step("Нажать на иконку 'Выйти'")
	public void clickLogoutIcon() {
		$(LOGOUT_ICON).click();
	}

	@Step("Иконка 'Выйти' отображается")
	public void checkLogoutIconIsVisible() {
		$(LOGOUT_ICON).shouldBe(visible);
	}
}
