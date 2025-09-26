package com.bild.qa.base;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class BaseTest {
    protected AndroidDriver driver;

    protected static Dotenv dotenv = Dotenv.configure()
            .ignoreIfMissing()
            .load();
    protected String testEmail = getEnv("TEST_EMAIL");
    protected String testPassword = getEnv("TEST_PASSWORD");

    private static String getEnv(String key) {
        String value = System.getenv(key);
        if (value == null && dotenv != null) {
            value = dotenv.get(key);
        }
        if (value == null) {
            throw new RuntimeException("Missing required env var: " + key);
        }
        return value;
    }

    @BeforeEach
    public void setUp() throws MalformedURLException {
        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformName("Android")
                .setDeviceName("Pixel_9_Pro")
                .setAppPackage("com.netbiscuits.bild.android")
                .setAppActivity("de.bild.android.app.MainActivity")
                .setAutoGrantPermissions(true);

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/"), options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
