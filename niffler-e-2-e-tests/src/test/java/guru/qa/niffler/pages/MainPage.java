package guru.qa.niffler.pages;

import static com.codeborne.selenide.Selenide.$;

public class MainPage {
   public MainPage clickButton(String nameBtn){
       $("a[href='/"+ nameBtn +"']").click();
       return this;
   }

}
