package guru.qa.niffler.pages;

import com.codeborne.selenide.ElementsCollection;
import guru.qa.niffler.model.UserJson;

import java.util.Arrays;
import java.util.List;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class AllPeoplePage {

    public boolean isStatusDisplayedForUser(UserJson userForTest, String expectedStatus) {
        $("a[href='/people']").click();

        ElementsCollection tableRows = $$(".main-content__section");
        List<String> rowTexts = tableRows.texts();

        return rowTexts.stream()
                .map(text -> text.split("\n"))
                .anyMatch(values -> Arrays.asList(values).contains(userForTest.getUsernamePeek())
                        && Arrays.asList(values).contains(expectedStatus));
    }
}
