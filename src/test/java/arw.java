import org.junit.Assert;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.testng.annotations.*;
import io.github.bonigarcia.wdm.WebDriverManager;

//import static javax.management.MBeanServerFactory.builder;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class arw {
    protected static WebDriver driver;
    protected static WebDriverWait wait;

    protected static Actions action;
    @BeforeMethod
    public void webSetup(){
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get("https://seleniumbase.io/realworld");
        wait = new WebDriverWait(driver, 15);
        action = new Actions(driver);
    }

    public void switchTab(int tabNumber) throws InterruptedException {
        Thread.sleep(500);
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
        switchTab(0);
        fillUserInfomation(MfaCode);
        logIn();
        clickOnDemoPage();
        switchTab(2);
        fillDemoPage();
    }


    public void ifMfaCodeIsValid() throws InterruptedException {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//span[@id='ttl'])")));
        String MfaCodeTime = driver.findElement(By.xpath("//span[@id='ttl']")).getText();
        int time=Integer.parseInt(MfaCodeTime);
//        System.out.println("time reaming : "+time);
        if(time<5){
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

    public void clickOnDemoPage() throws InterruptedException {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[normalize-space()='Demo Page']")));

        driver.findElement(By.xpath("//a[normalize-space()='Demo Page']")).click();
        Thread.sleep(2000);
    }

    public void checkTextFieldIsEmpty(String txt)
    {
        Assert.assertTrue(txt.isEmpty());
    }
    public void checkTextFieldIsNotEmpty(String txt)
    {
        Assert.assertFalse(txt.isEmpty());
    }

    public void verifyTextIsCorrect(String s1,String s2)
    {
//        System.out.println(s1);
//        System.out.println(s2);
        Assert.assertEquals(s1,s2);
    }

    public void fillDemoPage() throws InterruptedException {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[normalize-space()='Demo Page']")));
        fillFirstTextInputField();
        fillSecondTextInputField();
        fillPrefilledTextField();
        clickButton("Purple");
        clickButton("Green");
        placeHolderText();
        clickOnSVG();
        scrollBarFull(+100);
        verifyProgressBar(100);

        dropDown("75%");
        verifyHTMLMeter("75%");
        verifyIframeSrc();
        verifyIframeText();
        clickOnRadioButton();
        checkBox();
        dragAndDropImage();
    }




    public void fillFirstTextInputField(){
        String text= driver.findElement(By.xpath("//input[@id='myTextInput']")).getText();
        checkTextFieldIsEmpty(text);
        driver.findElement(By.xpath("//input[@id='myTextInput']")).sendKeys("abc");
    }

    public void fillSecondTextInputField()
    {
        String secondText=driver.findElement(By.xpath("//textarea[@id='myTextarea']")).getText();
        checkTextFieldIsEmpty(secondText);
        driver.findElement(By.xpath("//textarea[@id='myTextarea']")).sendKeys("kbc");
    }

    public void fillPrefilledTextField()
    {
        WebElement preFilledTextFieldXpath= driver.findElement(By.xpath("//input[@id='myTextInput2']"));
        String prefilledText=preFilledTextFieldXpath.getAttribute("value");
        checkTextFieldIsNotEmpty(prefilledText);
        preFilledTextFieldXpath.clear();
        preFilledTextFieldXpath.sendKeys("Hello");
    }

    public void clickButton(String color) throws InterruptedException {
        WebElement clickMeButton=driver.findElement(By.xpath("//button[@id='myButton']"));
        action.moveToElement(clickMeButton).click().perform();
        WebElement readOnlyText=driver.findElement(By.xpath("//input[@type='text' and @id='readOnlyText']"));
        WebElement paragraphText=driver.findElement(By.xpath("//td//p[@id='pText']"));
        verifyTextIsCorrect(clickMeButton.getText(),"Click Me ("+color+")");
        verifyTextIsCorrect(readOnlyText.getAttribute("value"),"The Color is "+color+"");
        verifyTextIsCorrect(paragraphText.getText(),"This Text is "+color+"");
        Thread.sleep(1000);
    }

    public void placeHolderText(){
        WebElement placeHolderTextXpath=driver.findElement(By.xpath("//input[@id='placeholderText']"));
        verifyTextIsCorrect(placeHolderTextXpath.getAttribute("placeholder"),"Placeholder Text Field");
        placeHolderTextXpath.sendKeys("sample text");
    }

    public void clickOnSVG(){
        WebElement svgWithRect=driver.findElement(By.xpath("//*[name()='rect' and @id='svgRect']"));
        action.moveToElement(svgWithRect).click().perform();
    }


    private void scrollBarFull(int numberOfRangeToDragTheSlider) throws InterruptedException {
        WebElement scrollBar=driver.findElement(By.xpath("//input[@id='mySlider']"));
        Dimension sliderWidth = scrollBar.getSize();
//        System.out.println(sliderWidth);
//        action.moveToElement(scrollBar).clickAndHold().moveByOffset(0,150).release().perform();
        action.moveToElement(scrollBar).clickAndHold().moveByOffset(numberOfRangeToDragTheSlider, 0)
                .release().perform();
        Thread.sleep(700);
    }

    public void verifyProgressBar(int numberOfRangeToDragTheSlider){
        WebElement progressBarXpath=driver.findElement(By.xpath("//label[@id='progressLabel']"));
        verifyTextIsCorrect(progressBarXpath.getText(),"Progress Bar: ("+numberOfRangeToDragTheSlider+"%)");
    }


    public void dropDown(String percentage) throws InterruptedException {
        Select dropDownXpath= new Select(driver.findElement(By.xpath("//select[@id='mySelect']")));
        dropDownXpath.selectByValue(percentage);
    }

    public void verifyHTMLMeter(String percentage){
        String HTMLMeter=driver.findElement(By.xpath("//label[@id='meterLabel']")).getText();
        verifyTextIsCorrect(HTMLMeter,"HTML Meter: ("+percentage+")");
    }

    public void verifyIframeSrc(){
        driver.switchTo().frame("myFrame1");
        String src=driver.findElement(By.tagName("img")).getAttribute("src");
        System.out.println(src);
        checkTextFieldIsNotEmpty(src);
    }

    public void verifyIframeText(){
        driver.switchTo().defaultContent();
//        WebElement iFrame=driver.findElement(By.xpath("//iframe[@id='myFrame2']"));
        driver.switchTo().frame("myFrame2");
        String iFrameText=driver.findElement(By.tagName("h4")).getText();
        verifyTextIsCorrect(iFrameText,"iFrame Text");
    }

    public void clickOnRadioButton() throws InterruptedException {
        driver.switchTo().defaultContent();
        WebElement radioButton1=driver.findElement(By.xpath("//input[@id='radioButton1']"));
        WebElement radioButton2=driver.findElement(By.xpath("//input[@id='radioButton2']"));
        radioButton2.click();
        Thread.sleep(500);
        radioButton1.click();
    }

    public void checkBox() {
        WebElement checkBox1=driver.findElement(By.xpath("//input[@id='checkBox1']"));
        WebElement checkBox2=driver.findElement(By.xpath("//input[@id='checkBox2']"));
        WebElement checkBox3=driver.findElement(By.xpath("//input[@id='checkBox3']"));
        WebElement checkBox4=driver.findElement(By.xpath("//input[@id='checkBox4']"));
        WebElement checkBox5=driver.findElement(By.xpath("//input[@id='checkBox5']"));
        clickOnCheckBox(checkBox1);
        clickOnCheckBox(checkBox2);
        clickOnCheckBox(checkBox3);
        clickOnCheckBox(checkBox4);
        clickOnCheckBox(checkBox5);
        clickOnIFrameCheckBox();

    }

   public void clickOnCheckBox(WebElement element){
       action.moveToElement(element).click().perform();
   }

   public void clickOnIFrameCheckBox() {
        driver.switchTo().frame("myFrame3");
        WebElement checkBox6=driver.findElement(By.xpath("//input[@id='checkBox6']"));
        clickOnCheckBox(checkBox6);

   }


   public void dragAndDropImage() throws InterruptedException {
       driver.switchTo().defaultContent();

       WebElement From=driver.findElement(By.xpath("//img[@id='logo']"));

       WebElement To=driver.findElement(By.xpath("//div[@id='drop2']"));

       Action dragAndDrop = action.clickAndHold(From)
               .moveToElement(To)
               .release(To)
               .build();

//       action.dragAndDrop(From, To).build().perform();
       dragAndDrop.perform();
//       Thread.sleep(3000);
   }

    @AfterMethod
    public void closeWeb(){
        driver.quit();
    }

}
