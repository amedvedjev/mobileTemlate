package com.mobileTemplate.base;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.AutomationName;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.remote.MobilePlatform;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Aleksei on 25.08.2017.
 */
public class BaseTest {
    private Process appium_Process;
    protected static AppiumDriver driver = null;
    private static DesiredCapabilities capabilities = null;
    private static String fileName = "ApiDemos-debug.apk";
    private static String userDir = "/src/test/java/resources";

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite(ITestContext iTestContext) throws Exception {
        System.out.println("BeforeSuite: ");
        String[] cmd;
        cmd = new String[]{"sh", "-c", "lsof -P | grep ':4723' | awk '{print $2}' | xargs kill -9"};
        executeShell(cmd);
        sleep(2);
        System.out.println("   start Appium server");

        List list = new ArrayList<String>();
        list.add("appium");
        list.add("--log-level");
        list.add("error");
        list.add("--port");
        list.add("4723");
        list.add("--bootstrap-port");
        list.add("5723");
        list.add("--command-timeout");
        list.add("90");
        list.add("--session-override");
        list.add("--log-timestamp");

        System.out.println(" start appium as: "+ list.toString());

        try {
            ProcessBuilder pb1 = new ProcessBuilder(list);

            //print inputStream to console
            pb1.redirectErrorStream(true);
            pb1.redirectOutput(ProcessBuilder.Redirect.INHERIT);
            appium_Process = pb1.start();
            sleep(10);
            System.out.println("  appium server started");
        } catch (Exception e) {
            System.out.println("  appium server start FAILED");
            e.printStackTrace();
        }

    }

    @BeforeMethod(alwaysRun=true)
    public void beforeMethod(Object[] testArgs, Method method, ITestContext iTestContext) throws Exception {
        System.out.println("BeforeMethod: ");
        System.out.println("  open driver");
        capabilities = DesiredCapabilities.android();
        capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, AutomationName.APPIUM);
        capabilities.setCapability("disableAndroidWatchers", "true");

        String deviceName = "Android"; // LGH8155e9892e6 Android
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
        //capabilities.setCapability(MobileCapabilityType.UDID, deviceName);

        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.ANDROID);
        //capabilities.setCapability(AndroidMobileCapabilityType.AUTO_GRANT_PERMISSIONS, "true");
        File appDir = new File(System.getProperty("user.dir") + userDir, fileName);
        capabilities.setCapability(MobileCapabilityType.APP, appDir.getAbsolutePath());
        capabilities.setCapability(AndroidMobileCapabilityType.APP_WAIT_ACTIVITY, ".ApiDemos");
        capabilities.setCapability(MobileCapabilityType.FULL_RESET, false);
        capabilities.setCapability(MobileCapabilityType.NO_RESET, true);
        capabilities.setCapability(MobileCapabilityType.CLEAR_SYSTEM_FILES, "true");

        System.out.println(capabilities);
        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
    }

    @AfterMethod(alwaysRun=true)
    public void afterMethod(ITestResult iTestResult, ITestContext iTestContext) throws Exception {
        System.out.println("AfterMethod: ");
        System.out.println("  driver quit");
        try {
            driver.quit();
        } catch (Exception e) {}
    }

    @AfterTest(alwaysRun=true)
    public void afterTest(ITestContext iTestContext) throws Exception {
        System.out.println("AfterTest: ");

        System.out.println("  ---------------------------");
        System.out.println("  passed tests execution time:");
        ArrayList<String> outList = new ArrayList<String>();
        String out;
        Integer totalPassedTestsExecutionTime = 0;
        int addBlanks;
        for (ITestResult passedTest : iTestContext.getPassedTests().getAllResults()) {
            out = "   " + passedTest.getTestClass().getName() + " " + passedTest.getName() + " [ ";
            out = out.replace("com.mobileTemplate.tests.anroid.","");
            for (Object parameter : passedTest.getParameters()) {
                out = out + parameter+", ";
            }
            out = out + "]";
            addBlanks = 100 - out.length();
            for (int i=0; i<addBlanks; i++) out = out + " ";
            outList.add(out + "- " + ((passedTest.getEndMillis() - passedTest.getStartMillis()) / 1000) + " sec");
            totalPassedTestsExecutionTime = totalPassedTestsExecutionTime +
                    (int) ((passedTest.getEndMillis() - passedTest.getStartMillis()) / 1000);
        }

        // sort results and print
        Collections.sort(outList.subList(0, outList.size()));
        for (String item : outList) System.out.println(item);
        System.out.println("\n");
        System.out.println("  Total passed tests: " + iTestContext.getPassedTests().size());
        if (iTestContext.getPassedTests().size() > 0) {
            System.out.println("  Average test execution time: " +
                    totalPassedTestsExecutionTime / iTestContext.getPassedTests().size() + " sec");
        }

        sleep(1);
    }

    @AfterSuite(alwaysRun=true)
    @Parameters({"generateReport"})
    public void tearDown(ITestContext iTestContext, @Optional String generateReport) throws Exception{
        System.out.println("AfterSuite: ");

        System.out.println("===============================================");
        System.out.println("  passed tests:  "+iTestContext.getPassedTests().size());
        System.out.println("  skipped tests: "+iTestContext.getSkippedTests().size());
        System.out.println("  failed tests:  "+iTestContext.getFailedTests().size());
        System.out.println("===============================================\n");


        if (appium_Process!=null) {
            try {
                System.out.println("closing Appium server 1");
                appium_Process.destroy();
            } catch (Exception e) {
                System.out.println("Appium server 1 FAILED to close");
                e.printStackTrace();
            }
            sleep(1);
            if (appium_Process.isAlive()) {
                System.out.println("closing Appium server 1 Forcibly");
                appium_Process.destroyForcibly();
            }
        }

        System.out.println("SUITE completed!");
    }

    static public void sleep(int sec) {
        try{Thread.sleep(sec*1000);}catch(Exception e){}
    }

    public static void executeShell(String[] cmd) {
        System.out.println("   executeShell(): " + Arrays.toString(cmd));
        ProcessBuilder pb = new ProcessBuilder(cmd);

        pb.redirectErrorStream(true);
        pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        try {
            final Process p = pb.start();
            // wait for termination.
            p.waitFor();
            //System.out.println("   executeShell(): - DONE!");
            p.destroy();
            if (p.isAlive()) {
                //System.out.println("  executeShell(): closing Forcibly");
                p.destroyForcibly();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
