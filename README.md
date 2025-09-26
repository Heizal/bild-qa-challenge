# Bild qa challenge

## Overview
I wrote two testcases. One checking the user credentials of a logged in User and the second verifying force log out when a user's credentials expire

## Test cases

### Postive test: Check user credentials of a logged in user
**Scenario:** User logs in and wants to verify their credentials

**Steps to reproduce**
- User accepts CMP and goes through onboarding flow
- User logs in with valid credentials
- User navigates to Meine Konto page and their credentials are displayed correctly

**Expected result:** User's credentials are displayed correctly on the Meine Konto page

### Negative test: Verify force log out when user credentials expire
**Scenario:** User's credentials expire and they should be logged out automatically

**Steps to reproduce**
- User accepts CMP and goes through onboarding flow
- User logs in with valid credentials
- User's credentials are set to expire. (In real-time this would be incase a user doesn't open the application for a long time. For this test case, I cleared the app data to simulate this)
- When user opens the app again, they'll go through the full onboarding flow again
- User credentials should not be displayed on the Meine Konto page
- User should be prompted to log in again

**Expected result:** User is logged out automatically when credentials expire and prompted to log in again

## Architecture and choices
Project structure:
```
bild-qa-challenge
├── .github
│   └── workflows
│       └── ci.yml
└── artifacts
    └── PositiveTest.mp4
    └── NegativeTest.mp4
src
└── test
    ├── java
    │   └── com
    │       └── bild
    │           └── qa
    │               ├── BaseTest.java
    │               ├── PositiveTest.java
    │               └── NegativeTest.java
target
└── surefire-reports
    ├── PositiveTest.txt
    └── NegativeTest.txt
├── pom.xml
├── README.md


``` 
- I used Appium and Java to write the test cases
- Emulator: Pixel 9 Pro API 36
- I mostly used UiSelector to locate elements as the application did not have consistent resource ids (In the future, using resource ids would be more reliable to avoid flaky tests)

## Setup and Installation

Make sure you have the following installed:

### Java JDK 21 (or compatible with your `pom.xml`)

````
java -version
````

If missing, install via [Adoptium Temurin](https://adoptium.net/en-GB)

### Apache Maven 3.9+
````
mvn -version
````
If missing, install with Homebrew:
````
brew install maven
````
### Android Studio + SDKs

- Install Android Studio. 
- Open SDK Manager → install:
  - Android 16 (or the version you’re targeting)
  - SDK Tools: 
    - Android SDK Platform-Tools, 
    - Emulator, 
    - Command-line tools.

### Appium Server v2.x
Install Appium globally with Node.js:
````
npm install -g appium
appium -v
````
Add the UIAutomator2 driver:

````
appium driver install uiautomator2
````
ADB (Android Debug Bridge)
Included with `platform-tools`. Verify:
````
adb devices
````
### Emulator / Device Setup

- Recommended device: Any device with (API 34/35/36). 
- Create emulator in Android Studio:
  - AVD Manager → “Create Virtual Device” → Phone → Pixel 9 Pro → Android 16.
- Start emulator:
````
emulator -avd Pixel_9_Pro
````

### Environment Variables
Set the following environment variables:
````
TEST_EMAIL=testexample@email.com
TEST_PASSWORD=yourpassword
DEVICE_NAME=your_emulator_or_device_name
PLATFORM_NAME=Android
PLATFORM_VERSION=16
APP_PACKAGE=com.netbiscuits.bild.android
APP_ACTIVITY=de.bild.android.app.MainActivity
````
### Running the tests
Start Appium server:
````
appium
````
In another terminal, navigate to the project directory and run:
````
mvn test
````
To run a specific test class:
````
mvn -Dtest=ClassName test
````
Replace `ClassName` with the desired test class name.

### Appium Inspector (Optional)
To inspect elements, use Appium Inspector:
- Open Appium Desktop → Start New Session.
- Set Desired Capabilities:
````
{
  "platformName": "Android",
  "deviceName": "Pixel_9_Pro",
  "app": "/path/to/your/app.apk",
  "automationName": "UiAutomator2"
}
````
- Click “Start Session” to inspect elements.  
- Use the Inspector to locate elements and generate selectors.

## Notes
- Ensure the emulator is running before starting Appium server.
- Adjust paths and versions as necessary for your environment.
- For real devices, enable Developer Options and USB Debugging.
- The app used in this project is assumed to be pre-installed on the emulator/device.