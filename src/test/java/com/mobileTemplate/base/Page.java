package com.mobileTemplate.base;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import java.text.Normalizer;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
/**
 * Created by Aleksei on 25.08.2017.
 */
public abstract class Page {

    protected WebDriver driver;
    private int defaultLook = 10; //default look for elements
    private int fastLook = 5; // wait for 7 sec

    private static Point point = null;
    private static Dimension size = null;

    public Page(WebDriver driver) {
        this.driver = driver;
        setDefaultTiming();
    }

    public boolean tapElement(MobileElement element) {
        try {
            new TouchAction((MobileDriver) driver).press(element).waitAction(Duration.ofMillis(70)).release().perform();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void tapElement(int x, int y) {
        ((AppiumDriver) driver).tap(1, x, y, 200); //200ms touch
    }

    public String getOrientation() {
        return ((AppiumDriver) driver).getOrientation().toString();
    }


    public void setLookTiming(int time) {
        PageFactory.initElements(new AppiumFieldDecorator(driver, time, TimeUnit.SECONDS), this);
    }
    public void setFastLookTiming() {
        //driver.manage().timeouts().implicitlyWait(fastLook, TimeUnit.SECONDS);
        PageFactory.initElements(new AppiumFieldDecorator(driver, fastLook, TimeUnit.SECONDS), this);
    }

    public void setDefaultTiming() {
        PageFactory.initElements(new AppiumFieldDecorator(driver, defaultLook, TimeUnit.SECONDS), this);
    }

    public Boolean testStringsEquals(String result, String expected) {
        return testStringsEquals(result, expected, true);
    }

    public Boolean testStringsEquals(String result, String expected, Boolean showFalse) {
        //do normalization first before compare
        result = Normalizer.normalize(result, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        expected = Normalizer.normalize(expected, Normalizer.Form.NFD);

        if (expected.equals(result)) {
            return true;
        } else {
            if (showFalse) System.out.println("    Expected: " + expected + ", result: " + result);
            return false;
        }
    }

    public void scrollToDirection(MobileElement elemContainer, String direction) {
        int duration = 1000; // = 1sec
        //get location and size
        point = elemContainer.getLocation();
        size = elemContainer.getSize();

        // l - left, r - right, u - up, d - down

        if (direction.equals("l")) {
            ((AppiumDriver) driver).swipe(
                    point.x + size.getWidth() - 5,  point.y + size.getHeight()/2,
                    point.x + 5,                    point.y + size.getHeight()/2,
                    duration);
        } else if (direction.equals("r")) {
            ((AppiumDriver) driver).swipe(
                    point.x + 5,                    point.y + size.getHeight()/2,
                    point.x + size.getWidth() - 5,  point.y + size.getHeight()/2,
                    duration);
        } else if (direction.equals("u")) {
            ((AppiumDriver) driver).swipe(
                    point.x + size.getWidth()/2,    point.y + size.getHeight() - 5,
                    point.x + size.getWidth()/2,    point.y - 5,
                    duration);
        } else if (direction.equals("d")) {
            ((AppiumDriver) driver).swipe(
                    point.x + size.getWidth()/2,    point.y + 5,
                    point.x + size.getWidth()/2,    point.y + size.getHeight() - 5,
                    duration);
        }
    }
}
