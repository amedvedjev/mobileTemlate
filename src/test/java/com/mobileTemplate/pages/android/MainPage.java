package com.mobileTemplate.pages.android;

import com.mobileTemplate.base.Page;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.HowToUseLocators;
import io.appium.java_client.pagefactory.LocatorGroupStrategy;
import org.openqa.selenium.WebDriver;

import java.util.List;

/**
 * Created by Aleksei on 25.08.2017.
 */
public class MainPage extends Page {

    // top
    @HowToUseLocators(androidAutomation = LocatorGroupStrategy.CHAIN)
    @AndroidFindBy(id = "action_bar")
    @AndroidFindBy(className = "android.widget.TextView")
    private AndroidElement headerTitle;

    // body
    @AndroidFindBy(id = "list")
    private List<AndroidElement> topLayout;


    public MainPage(WebDriver driver) {
        super(driver);
    }

    public boolean isMainPageLoaded() {
        System.out.println("  isMainPageLoaded()");
        boolean bool;
        setDefaultTiming();
        bool = !topLayout.isEmpty();
        return bool;
    }

    public Boolean tapCellByTitle(String title) {
        System.out.println("  tapCellByTitle(): " + title);
        List<AndroidElement> elementList = driver.findElements(MobileBy.AndroidUIAutomator(
                "new UiScrollable(new UiSelector().resourceIdMatches(\".*id/list\")).setMaxSearchSwipes(5).scrollIntoView("
                        + "new UiSelector().text(\"" + title + "\"))"));
        if (elementList.isEmpty())
            return false;
        else
            return tapElement(elementList.get(0));
    }


    public String getHeaderText() {
        System.out.println("  getHeaderText()");
        try {
            return headerTitle.getText();
        } catch (Exception e) {
            return "";
        }
    }
}
