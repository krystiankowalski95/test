package pl.lodz.p.it.ssbd2020.ssbd05.mok;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AccessLevelsTestUI {

    private WebDriver driver;
    JavascriptExecutor js;

    @Before
    public void setUp() {
        WebDriverManager.getInstance(ChromeDriver.class).setup();
        ChromeOptions options = new ChromeOptions();
        options.setHeadless(true);
        options.setAcceptInsecureCerts(true);
        options.addArguments("--lang=en");
        driver = new ChromeDriver(options);
        js = (JavascriptExecutor) driver;
    }

    @After
    public void tearDown() {
        driver.close();
    }

    @Test
    public void accessLevelTest() throws InterruptedException {
        driver.get("https://localhost:8181/ssbd05/index.xhtml");
        driver.manage().window().fullscreen();
        driver.findElement(By.id("loginButton")).click();
        driver.findElement(By.id("login:username")).click();
        driver.findElement(By.id("login:username")).sendKeys("admin");
        driver.findElement(By.id("login:password")).sendKeys("admin123");
        driver.findElement(By.xpath("//*[contains(@id, 'submit')]")).click();
        Thread.sleep(500);
        driver.findElement(By.xpath("//*[contains(@id, 'dynaButton')]")).click();
        Thread.sleep(500);
        driver.findElement(By.xpath("//*[contains(@id, 'changeRoleButton')]")).click();
        Thread.sleep(500);
        driver.findElement(By.xpath("//*[contains(@id, 'changeAdmin')]")).click();
        Thread.sleep(500);
        driver.findElement(By.cssSelector(".pi-bars")).click();
        Thread.sleep(500);
        driver.findElement(By.cssSelector("#listAccountsButton > .ui-button-text")).click();
        Thread.sleep(500);
        driver.findElement(By.xpath("//*[contains(@id, 'filterAccountsTextBox')]")).sendKeys("Poziomydostepu");
        driver.findElement(By.xpath("//*[contains(@id, 'filterbutton')]")).click();
        Thread.sleep(500);
        driver.findElement(By.xpath("//td[7]/button/span")).click();
        Thread.sleep(500);

        String first = driver.findElements(By.tagName("tr")).get(7).getText();

        driver.findElement(By.xpath("//*[contains(@id, 'addAccessLevelButton')]")).click();
        driver.findElement(By.xpath("//*[contains(@id, 'addManagerRole')]")).click();
        driver.findElement(By.xpath("//*[contains(@id, 'addRole')]")).click();

        String second = driver.findElements(By.tagName("tr")).get(7).getText();

        driver.findElement(By.xpath("//*[contains(@id, 'removeAccessLevelButton')]")).click();
        driver.findElement(By.xpath("//*[contains(@id, 'removeManagerRole')]")).click();
        driver.findElement(By.xpath("//*[contains(@id, 'removeRole')]")).click();

        String third = driver.findElements(By.tagName("tr")).get(7).getText();

        assertFalse(first.contains("Manager"));
        assertTrue(second.contains("Manager"));
        assertFalse(third.contains("Manager"));

        driver.findElement(By.xpath("//*[contains(@id, 'dynaButton')]")).click();
        driver.findElement(By.xpath("//*[contains(@id, 'logoutButton')]")).click();
    }
}
