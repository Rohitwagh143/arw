import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.testng.annotations.*;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class arw {
    protected static WebDriver driver;
    protected static WebDriverWait wait;
    @BeforeMethod
    public void webSetup(){
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get("https://seleniumbase.io/realworld");
        wait = new WebDriverWait(driver, 15);
    }

    public void switchTab(int tabNumber) throws InterruptedException {
        ArrayList<String> newTb = new ArrayList<String>(driver.getWindowHandles());
        //switch to new tab
        driver.switchTo().window(newTb.get(tabNumber));
        Thread.sleep(1000);
    }

    public void clickOnGetMfeGetDetailLink()
    {
        driver.findElement(By.xpath("//a[normalize-space()='seleniumbase.io/realworld/signup']")).click();
    }

    @Test
    public void logInPage() throws InterruptedException{
        clickOnGetMfeGetDetailLink();
        switchTab(1);
        ifMfaCodeIsValid();
        String MfaCode=GetMfaCode();
//        System.out.println(MfaCode);
        switchTab(0);
        fillUserInfomation(MfaCode);
        logIn();
        clickOnDemoPage();
    }


    public void ifMfaCodeIsValid() throws InterruptedException {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//span[@id='ttl'])")));
        String MfaCodeTime = driver.findElement(By.xpath("//span[@id='ttl']")).getText();
        int time=Integer.parseInt(MfaCodeTime);
//        System.out.println("time reaming : "+time);
        if(time<8){
            Thread.sleep((time*1000)+1000);
            ifMfaCodeIsValid();
        }
    }

    public String GetMfaCode(){
        String MfaCode = driver.findElement(By.xpath("//span[@id='totp']")).getText();
        return MfaCode;
    }

    public void fillUserInfomation(String MfaCode){
        driver.findElement(By.xpath("//input[@id='username']")).sendKeys("demo_user");
        driver.findElement(By.xpath("//input[@id='password']")).sendKeys("secret_pass");
        driver.findElement(By.xpath("//input[@id='totpcode']")).sendKeys(MfaCode);
    }
    public void logIn(){
        driver.findElement(By.xpath("//a[@id='log-in']")).click();
    }

    public void clickOnDemoPage()
    {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[normalize-space()='Demo Page']")));
        wait.until(ExpectedConditions.elementToBeSelected(By.xpath("//a[normalize-space()='Demo Page']")));

        driver.findElement(By.xpath("//a[normalize-space()='Demo Page']")).click();
    }

    public void fillDemoPage()
    {

    }


    @AfterMethod
    public void closeWeb(){
        driver.quit();
    }

}
