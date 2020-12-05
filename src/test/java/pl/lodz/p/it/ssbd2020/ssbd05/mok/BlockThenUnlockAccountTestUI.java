package pl.lodz.p.it.ssbd2020.ssbd05.mok;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class BlockThenUnlockAccountTestUI {
    private WebDriver driver;
    JavascriptExecutor js;


    @Before
    public void setUp() {
        WebDriverManager.getInstance(ChromeDriver.class).setup();
        ChromeOptions options = new ChromeOptions();
        options.setHeadless(true);
        options.setAcceptInsecureCerts(true);
        options.addArguments("--lang=pl");
        driver = new ChromeDriver(options);
        js = (JavascriptExecutor) driver;

    }
    @After
    public void tearDown() {
        driver.close();
    }
    @Test
    public void blockThenUnlockAccount() throws InterruptedException {
        driver.get("https://localhost:8181/ssbd05/index.xhtml");
        driver.findElement(By.id("loginButton")).click();
        driver.findElement(By.id("login:username")).click();
        driver.findElement(By.id("login:username")).sendKeys("admin");
        driver.findElement(By.id("login:password")).sendKeys("admin123");
        driver.findElement(By.xpath("//*[contains(@id, 'submit')]")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("//*[contains(@id, 'dynaButton')]")).click();
        driver.findElement(By.xpath("//*[contains(@id, 'changeRoleButton')]")).click();
        driver.findElement(By.xpath("//*[contains(@id, 'changeAdmin')]")).click();
        driver.findElement(By.cssSelector(".pi-bars")).click();
        Thread.sleep(500);
        driver.findElement(By.xpath("//*[contains(@id, 'listAccountsButton')]")).click();
        driver.findElement(By.xpath("//*[contains(@id, 'filterAccountsTextBox')]")).sendKeys("Testujeblokowanie");
        driver.findElement(By.xpath("//*[contains(@id, 'filterbutton')]")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("//*[contains(@id, 'goDetailsButton')]")).click();
        String active = driver.findElement(By.xpath("//*[contains(@id, 'active')]")).getText();
        Assert.assertEquals("Tak",active);
        driver.findElement(By.xpath("//*[contains(@id, 'blockaccount')]")).click();
        Thread.sleep(500);
        String inactive = driver.findElement(By.xpath("//*[contains(@id, 'inactive')]")).getText();
        Assert.assertEquals("Nie",inactive);
        driver.findElement(By.xpath("//*[contains(@id, 'unlockaccount')]")).click();
        Thread.sleep(500);
        driver.findElement(By.xpath("//*[contains(@id, 'dynaButton')]")).click();
        driver.findElement(By.xpath("//*[contains(@id, 'logoutButton')]")).click();
    }
}
