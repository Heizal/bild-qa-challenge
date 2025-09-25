package com.bild.qa.tests;

import com.bild.qa.base.BaseTest;
import io.appium.java_client.AppiumBy;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class NegativeTest extends BaseTest {

    @Test
    public void validateExpiredSessionForcesReLogin(){
        //Accept CMP and Onboarding
        driver.findElement(AppiumBy.androidUIAutomator("new UiSelector().text(\"Alle akzeptieren\")")).click();
        driver.findElement(AppiumBy.androidUIAutomator("new UiSelector().text(\"WEITER\")")).click();
        driver.findElement(AppiumBy.androidUIAutomator("new UiSelector().resourceId(\"js-submit-button\")")).click();
        driver.findElement(AppiumBy.androidUIAutomator("new UiSelector().resourceId(\"push_activate\")")).click();
        //Allow push notifications
        try {
            driver.findElement(By.id("com.android.permissioncontroller:id/permission_allow_button")).click();
        } catch (Exception ignored) {
            System.out.println("Push permission dialog not shown, continuing test.");
        }

        driver.findElement(AppiumBy.androidUIAutomator("new UiSelector().text(\"ANMELDEN\")")).click();

        //Login with valid credentials
        driver.findElement(AppiumBy.androidUIAutomator("new UiSelector().resourceId(\"identifier\")")).sendKeys("testuserwelt@gmail.com");
        driver.findElement(AppiumBy.androidUIAutomator("new UiSelector().resourceId(\"password\")")).sendKeys("12345678");

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(
                        AppiumBy.androidUIAutomator("new UiSelector().text(\"JETZT ANMELDEN\")")
                )).click();

        //Verify that user is logged in
        new WebDriverWait(driver, Duration.ofSeconds(20))
                .until(ExpectedConditions.visibilityOfElementLocated(
                        AppiumBy.androidUIAutomator("new UiSelector().textContains(\"Mehr\")")
                )).click();

        driver.findElement(AppiumBy.androidUIAutomator("new UiSelector().text(\"Mein Konto\")")).click();

        assertTrue(
                driver.findElement(AppiumBy.androidUIAutomator("new UiSelector().text(\"testuserwelt@gmail.com\")")).isDisplayed(),
                "Username should be displayed"
        );

        //Simulate session expiration by clearing app data
        String appPackage = "com.netbiscuits.bild.android";
        try {
            Runtime.getRuntime().exec("adb shell pm clear " + appPackage).waitFor();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // --- Step 3: Relaunch app ---
        driver.terminateApp(appPackage);
        driver.activateApp(appPackage);

        //User will get bacjk to onboarding screen after app data is cleared
        driver.findElement(AppiumBy.androidUIAutomator("new UiSelector().text(\"Alle akzeptieren\")")).click();
        driver.findElement(AppiumBy.androidUIAutomator("new UiSelector().text(\"WEITER\")")).click();
        driver.findElement(AppiumBy.androidUIAutomator("new UiSelector().resourceId(\"js-submit-button\")")).click();
        driver.findElement(AppiumBy.androidUIAutomator("new UiSelector().resourceId(\"push_activate\")")).click();
        //Allow push notifications
        try {
            driver.findElement(By.id("com.android.permissioncontroller:id/permission_allow_button")).click();
        } catch (Exception ignored) {
            System.out.println("Push permission dialog not shown, continuing test.");
        }
        driver.findElement(AppiumBy.androidUIAutomator("new UiSelector().text(\"NEIN, DANKE\")")).click();

        //Verify that user is logged out -> Check Meine Konto has no credentials
        driver.findElement(AppiumBy.androidUIAutomator("new UiSelector().text(\"Mehr\")")).click();
        driver.findElement(AppiumBy.androidUIAutomator("new UiSelector().text(\"Mein Konto\")")).click();
        assertTrue(
                driver.findElement(AppiumBy.androidUIAutomator("new UiSelector().text(\"LOGIN\")")).isDisplayed(),
                "Login button should be displayed"
        );
    }
}
