import java.io.File;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * LocalLoginTest - Automated UI and Functional Test Suite for the Local Login Web Application.
 * Configured for resilient cross-platform and headless CI/CD execution.
 */
public class LocalLoginTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeMethod
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--remote-allow-origins=*");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        String pagePath = new File("webpage/login.html")
                .getAbsoluteFile()
                .toURI()
                .toString();

        Reporter.log("Navigating to test page: " + pagePath, true);
        driver.get(pagePath);
    }

    @Test(description = "Verify valid student credentials authenticate successfully and display confirmation message")
    public void validLoginTest() {
        Reporter.log("Executing validLoginTest...", true);

        WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
        usernameField.clear();
        usernameField.sendKeys("student");

        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.clear();
        passwordField.sendKeys("12345");

        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("loginButton")));
        loginButton.click();

        WebElement messageElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("message")));
        String message = messageElement.getText();

        Reporter.log("Received login response message: " + message, true);
        Assert.assertEquals(message, "Login Successful", "Expected valid login confirmation message to match");
    }

    @Test(description = "Verify invalid student credentials are rejected and display an error warning")
    public void invalidLoginTest() {
        Reporter.log("Executing invalidLoginTest...", true);

        WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
        usernameField.clear();
        usernameField.sendKeys("wrong");

        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.clear();
        passwordField.sendKeys("wrong");

        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("loginButton")));
        loginButton.click();

        WebElement messageElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("message")));
        String message = messageElement.getText();

        Reporter.log("Received rejection message: " + message, true);
        Assert.assertEquals(message, "Invalid Username or Password", "Expected invalid login error warning to match");
    }

    @Test(description = "Verify login box CSS styling and background color match specifications")
    public void checkBackgroundColor() {
        Reporter.log("Executing checkBackgroundColor test...", true);

        WebElement loginBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginBox")));
        String bgColor = loginBox.getCssValue("background-color");

        Reporter.log("Retrieved login box background color: " + bgColor, true);
        Assert.assertEquals(bgColor, "rgba(173, 216, 230, 1)", "Expected login box background color to match lightblue");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            Reporter.log("WebDriver session closed cleanly.", true);
        }
    }
}
