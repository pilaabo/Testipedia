package me.pilaabo;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.Duration;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
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
        desiredCapabilities.setCapability("appium:app",System.getProperty("user.dir") + "/src/test/resources/apks/app-alpha-universal-release.apk");

        driver = new AndroidDriver(URL.of(URI.create("http://127.0.0.1:4723"), null), desiredCapabilities);
        driver.findElement(By.xpath("//*[contains(@text, 'Skip')]")).click(); // Skip the first screen
    }

    @Test
    public void testSearch() {
        waitForElementByXpathAndClick(
                "//*[contains(@text, 'Search Wikipedia')]",
                "Cannot find 'Search Wikipedia' input",
                5
        );
        waitForElementByXpathAndSendKeys(
                "//*[contains(@text, 'Search Wikipedia')]",
                "Java", "Cannot find search input",
                5
        );
        waitForElementPresentByXpath(
                "//*[contains(@text, 'Java (programming language)')]",
                "Cannot find 'Java (programming language)' topic",
                15
        );
    }

    @Test
    public void testCancelSearch() {
        waitForElementByXpathAndClick(
                "//*[contains(@text, 'Search Wikipedia')]",
                "Cannot find 'Search Wikipedia' input",
                5
        );
        waitForElementByXpathAndClick(
                "//*[@content-desc=\"Navigate up\"]",
                "Cannot find 'Cancel' button",
                5
        );
        waitForElementPresentByXpath(
                "//*[@resource-id=\"org.wikipedia.alpha:id/main_toolbar_wordmark\"]",
                "Cannot find 'Wikipedia' logo",
                5
        );
    }

    @AfterAll
    public static void tearDown() {
        driver.quit();
    }

    private WebElement waitForElementPresentByXpath(String xpath, String errorMessage, int timeout) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        wait.withMessage(errorMessage + "\n");
        By by = By.xpath(xpath);
        return wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }

    private WebElement waitForElementByXpathAndClick(String xpath, String errorMessage, int timeout) {
        WebElement element = waitForElementPresentByXpath(xpath, errorMessage, timeout);
        element.click();
        return element;
    }

    private WebElement waitForElementByXpathAndSendKeys(String xpath, String value, String errorMessage, int timeout) {
        WebElement element = waitForElementPresentByXpath(xpath, errorMessage, timeout);
        element.sendKeys(value);
        return element;
    }
}
