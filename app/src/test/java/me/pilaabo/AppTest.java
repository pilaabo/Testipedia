package me.pilaabo;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;

class AppTest {
    private static AppiumDriver driver;

    @BeforeAll
    public static void setUp() throws MalformedURLException {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability("platformName", "Android");
        desiredCapabilities.setCapability("appium:platformVersion", "15");
        desiredCapabilities.setCapability("appium:deviceName", "Android Emulator");
        desiredCapabilities.setCapability("appium:automationName", "uiautomator2");
        desiredCapabilities.setCapability("appium:appPackage", "org.wikipedia.alpha");
        desiredCapabilities.setCapability("appium:appActivity", "org.wikipedia.main.MainActivity");
        desiredCapabilities.setCapability("appium:app",
                System.getProperty("user.dir") + "/src/test/resources/apks/app-alpha-universal-release.apk");

        driver = new AndroidDriver(new URL("http://192.168.1.3:4723"), desiredCapabilities);
    }

    @Test
    public void firstTest() {
        System.out.println("First test run");
    }

    @AfterAll
    public static void tearDown() {
        driver.quit();
    }
}
