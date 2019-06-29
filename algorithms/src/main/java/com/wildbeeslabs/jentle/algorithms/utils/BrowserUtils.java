package com.wildbeeslabs.jentle.algorithms.utils;

import org.apache.commons.lang3.SystemUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;

import java.io.IOException;

import static java.lang.System.setProperty;

public class BrowserUtils {

    public WebDriver startFirefox() {
        if (SystemUtils.IS_OS_WINDOWS) {
            setProperty("webdriver.gecko.driver", "src/test/resources/browserBinaries/geckodriver.exe");
        }
        if (SystemUtils.IS_OS_LINUX) {
            setProperty("webdriver.gecko.driver", "src/test/resources/browserBinaries/geckodriver_linux");
        }
        if (SystemUtils.IS_OS_MAC) {
            setProperty("webdriver.gecko.driver", "src/test/resources/browserBinaries/geckodriver_mac");
        }
        FirefoxOptions capabilities = new FirefoxOptions();
        capabilities.setCapability("marionette", true);
        WebDriver driver = new FirefoxDriver();
        maximize(driver);
        System.out.println("\n---------------- FIREFOX has started! -------------------\n");
        return driver;
    }

    public WebDriver startChrome() {
        if (SystemUtils.IS_OS_WINDOWS) {
            setProperty("webdriver.chrome.driver", "src/test/resources/browserBinaries/chromedriver.exe");
        }
        if (SystemUtils.IS_OS_LINUX) {
            setProperty("webdriver.chrome.driver", "src/test/resources/browserBinaries/chromedriver_linux");
        }
        if (SystemUtils.IS_OS_MAC) {
            setProperty("webdriver.chrome.driver", "src/test/resources/browserBinaries/chromedriver_mac");
        }
        WebDriver driver = new ChromeDriver();
        maximize(driver);
        System.out.println("\n--------------- CHROME has started! ------------------\n");
        return driver;
    }

    public WebDriver startInternetExplorer() {
        //TODO!!!
        return null;
    }

    public WebDriver startBrowser() {
        switch (System.getProperty("browser").toLowerCase()) {
            case "chrome":
                return startChrome();
            case "firefox":
                return startFirefox();
            case "ie":
                return startInternetExplorer();
            default:
                System.out.println("THERE WAS AN ERROR READING THE BROWSER CONFIGURATION! CHROME WILL START BY " +
                    "DEFAULT!");
                return startChrome();
        }
    }

    public WebDriver startBrowserCustomSize(int width, int height) {
        if (SystemUtils.IS_OS_WINDOWS) {
            setProperty("webdriver.chrome.driver", "src/test/resources/browserBinaries/chromedriver.exe");
        }
        if (SystemUtils.IS_OS_LINUX) {
            setProperty("webdriver.chrome.driver", "src/test/resources/browserBinaries/chromedriver_linux");
        }
        if (SystemUtils.IS_OS_MAC) {
            setProperty("webdriver.chrome.driver", "src/test/resources/browserBinaries/chromedriver_mac");
        }
        WebDriver driver = new ChromeDriver();
        driver.manage().window().setSize(new Dimension(width, height));
        System.out.println("\n--------------- CHROME has started in CUSTOM size! ------------------\n");
        return driver;
    }

    private void maximize(WebDriver driver) {
        driver.manage().window().maximize();
    }

    public void quit(WebDriver driver) throws IOException {
        if (driver.getClass().equals(InternetExplorerDriver.class)) {
            Runtime.getRuntime().exec("taskkill /F /IM iexplore.exe");
        } else
            driver.quit();

    }
}
