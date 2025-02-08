package me.pilaabo;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.Duration;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

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
        desiredCapabilities.setCapability("appium:app", System.getProperty("user.dir") + "/src/test/resources/apks/app-alpha-universal-release.apk");

        driver = new AndroidDriver(URL.of(URI.create("http://127.0.0.1:4723"), null), desiredCapabilities);
    }

    @BeforeEach
    public void skipOnboarding() {
        driver.findElement(By.id("org.wikipedia.alpha:id/fragment_onboarding_skip_button")).click();
    }

    @Test
    public void firstTest() {
        WebElement initSeach = waitForElementPresentById("org.wikipedia.alpha:id/search_container", "Cannot find init search field");
        initSeach.click();
        WebElement search = waitForElementPresentById("org.wikipedia.alpha:id/search_src_text", "Cannot find search field");
        search.sendKeys("Java");
        waitForElementPresentByXpath("//*[contains(@text, 'Java (programming language)')]", "Cannot find 'Java (programming language)' search result");
    }

    @AfterAll
    public static void tearDown() {
        driver.quit();
    }

    private WebElement waitForElementPresentByXpath(String xpath, String errorMessage) {
        return waitForElementPresentByXpath(xpath, errorMessage, 5);
    }

    private WebElement waitForElementPresentById(String id, String errorMessage) {
        return waitForElementPresentById(id, errorMessage, 5);
    }

    private WebElement waitForElementPresentByXpath(String xpath, String errorMessage, int timeout) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        wait.withMessage(errorMessage + "\n");
        By by = By.xpath(xpath);
        return wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }

    private WebElement waitForElementPresentById(String id, String errorMessage, int timeout) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        wait.withMessage(errorMessage + "\n");
        By by = By.id(id);
        return wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }
}
