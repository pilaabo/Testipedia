package me.pilaabo;

import java.net.MalformedURLException;
import java.net.URI;
import java.time.Duration;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
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

        driver = new AndroidDriver(URI.create("http://127.0.0.1:4723").toURL(), desiredCapabilities);
        driver.findElement(By.xpath("//*[contains(@text, 'Skip')]")).click(); // Skip the first screen
    }

    @Test
    public void testSearch() {
        waitForElementAndClick(
                By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
                "Cannot find 'Search Wikipedia' input",
                5
        );
        waitForElementAndSendKeys(
                By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
                "Java", "Cannot find search input",
                5
        );
        waitForElementPresent(
                By.xpath("//*[contains(@text, 'Java (programming language)')]"),
                "Cannot find 'Java (programming language)' topic",
                15
        );
    }

    @Test
    public void testCancelSearch() {
        waitForElementAndClick(
                By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
                "Cannot find 'Search Wikipedia' input",
                5
        );
        waitForElementAndSendKeys(
            By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
            "Java", "Cannot find search input",
            5
        );
        waitForElementAndClear(
            By.xpath("//*[contains(@text, 'Java')]"),
            "Cannot find search input",
            5
        );
        waitForElementAndClick(
                By.xpath("//*[@content-desc=\"Navigate up\"]"),
                "Cannot find 'Cancel' button",
                5
        );
        waitForElementPresent(
                By.xpath("//*[@resource-id=\"org.wikipedia.alpha:id/main_toolbar_wordmark\"]"),
                "Cannot find 'Wikipedia' logo",
                5
        );
    }

    @Test
    public void testCompareArticleTitle() {
        waitForElementAndClick(
                By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
                "Cannot find 'Search Wikipedia' input",
                5
        );
        waitForElementAndSendKeys(
                By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
                "Java", "Cannot find search input",
                5
        );
        waitForElementAndClick(
                By.xpath("//*[contains(@text, 'Java (programming language)')]"),
                "Cannot find 'Java (programming language)' topic",
                15
        );
        WebElement titleElement = waitForElementPresent(
                By.xpath("//*[@text=\"Java (programming language)\"]"),
                "Cannot find article title",
                15
        );
        assertElementHasText(titleElement, "Java (programming language)", "Article title is not 'Java (programming language)");
    }

    @AfterAll
    public static void tearDown() {
        driver.quit();
    }

    private WebElement waitForElementPresent(By by, String errorMessage, int timeout) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        wait.withMessage(errorMessage + "\n");
        return wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }

    private WebElement waitForElementAndClick(By by, String errorMessage, int timeout) {
        WebElement element = waitForElementPresent(by, errorMessage, timeout);
        element.click();
        return element;
    }

    private WebElement waitForElementAndSendKeys(By by, String value, String errorMessage, int timeout) {
        WebElement element = waitForElementPresent(by, errorMessage, timeout);
        element.sendKeys(value);
        return element;
    }

    private WebElement waitForElementAndClear(By by, String errorMessage, int timeout) {
        WebElement element = waitForElementPresent(by, errorMessage, timeout);
        element.clear();
        return element;
    }

    private void assertElementHasText(WebElement element, String expectedValue, String errorMessage) {
        Assertions.assertEquals(expectedValue, element.getText(), errorMessage);
    }
}
