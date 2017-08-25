package com.mobileTemplate.tests.android;

import com.mobileTemplate.base.BaseTest;
import com.mobileTemplate.pages.android.MainPage;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by Aleksei on 25.08.2017.
 */
public class test_1 extends BaseTest {
    private MainPage mainPage;

    @Test
    public void foundGraphics_Sweep() {
        mainPage = new MainPage(driver);
        assertTrue("Main screen NOT loaded", mainPage.isMainPageLoaded());
        assertTrue("'Graphics' cell NOT found", mainPage.tapCellByTitle("Graphics"));
        assertTrue("Header title NOT Correct", mainPage.getHeaderText().equals("API Demos"));
        assertTrue("'Sweep", mainPage.tapCellByTitle("Sweep"));
        assertTrue("Header title NOT Correct", mainPage.getHeaderText().contains("Sweep"));
    }

    @Test
    public void foundViews_Sweep() {
        mainPage = new MainPage(driver);
        assertTrue("Main screen NOT loaded", mainPage.isMainPageLoaded());
        assertTrue("'Views' cell NOT found", mainPage.tapCellByTitle("Views"));
        assertTrue("Header title NOT Correct", mainPage.getHeaderText().equals("API Demos"));
        // Sweep does not exist
        assertTrue("'Sweep' cell NOT found", mainPage.tapCellByTitle("Sweep"));
        assertTrue("Header title NOT Correct", mainPage.getHeaderText().contains("Sweep"));
    }

    @Test
    public void foundViews_WebView() {
        mainPage = new MainPage(driver);
        assertTrue("Main screen NOT loaded", mainPage.isMainPageLoaded());
        assertTrue("'Views' cell NOT found", mainPage.tapCellByTitle("Views"));
        assertTrue("Header title NOT Correct", mainPage.getHeaderText().equals("API Demos"));
        assertTrue("'WebView' cell NOT found", mainPage.tapCellByTitle("WebView"));
        assertTrue("Header title NOT Correct", mainPage.getHeaderText().contains("WebView"));
    }
}
