package przyklad1;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.fail;

public class WarsztatyZadanie2 {

    private WebDriver driver;

    @Before
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/drivers/chromedriver");
        // Uruchom nowy egzemplarz przeglądarki Chrome
        driver = new ChromeDriver();

        // Zmaksymalizuj okno przeglądarki
        driver.manage().window().maximize();


    }

    @Test
    public void loginToShopAndBuyTest() throws InterruptedException, IOException {
        driver.get("https://prod-kurs.coderslab.pl/index.php?controller=authentication&back=my-account");

        WebElement emailWebElement = driver.findElement(By.name("email"));
        WebElement passwordWebElement = driver.findElement(By.name("password"));
        WebElement loginButton = driver.findElement(By.id("submit-login"));
        System.out.println(emailWebElement);
        System.out.println(passwordWebElement);

        emailWebElement.sendKeys("fiqaicnnbzhfwzbeoh@awdrt.com");
        passwordWebElement.sendKeys("januszkowalski");
        loginButton.click();
        WebElement clothesButton = driver.findElement(By.id("category-3"));
        clothesButton.click();

        List<WebElement> articles = driver.findElements(By.tagName("article"));
        Optional<WebElement> webElementOptional = articles.stream()
                .filter(webElement -> webElement.getText().contains("Hummingbird Printed Sweater"))
                .findFirst();
        if (!webElementOptional.isPresent()){
            fail();
        }

        WebElement sweater = webElementOptional.get();
        sweater.click();

        Select sizeSelect = new Select(driver.findElement(By.id("group_1")));

        Optional<WebElement> webElementSizeOptional = sizeSelect.getOptions().stream()
                .filter(webElement -> webElement.getText().contains("M"))
                .findFirst();
        if (!webElementSizeOptional.isPresent()){
            fail();
        }else{
            webElementSizeOptional.get().click();
        }

        WebElement quantity = driver.findElement(By.id("quantity_wanted"));
        quantity.clear();
        quantity.sendKeys("5");

        WebElement addToCartButton = driver.findElement(By.className("add-to-cart"));
        addToCartButton.click();

        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        WebElement cartViewButton = driver.findElement(By.className("cart-content-btn"))
                .findElement(By.className("btn-primary"));

        cartViewButton.click();

        WebElement proceedToCheckoutButton = driver.findElement(By.className("btn-primary"));
        proceedToCheckoutButton.click();

        WebElement continueButton = driver.findElement(By.className("continue"));
        continueButton.click();

        WebElement deliveryOption = driver.findElement(By.id("delivery_option_1"));
        if (!deliveryOption.isSelected()){
            deliveryOption.click();
        }
        WebElement continueButton2 = driver.findElement(By.name("confirmDeliveryOption")); 
        continueButton2.click();

        WebElement paymentOption = driver.findElement(By.id("payment-option-1"));
        if (!paymentOption.isSelected()) {
            paymentOption.click();
        }

        WebElement terms = driver.findElement(By.id("conditions_to_approve[terms-and-conditions]"));

        if (!terms.isSelected()){
            terms.click();
        }

        WebElement confirm = driver.findElement(By.id("payment-confirmation"));
        confirm.click();

        JavascriptExecutor jsE = (JavascriptExecutor) driver;
        jsE.executeScript("window.scrollBy(0,125)");

        File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshotFile, new File("screenshot.png"));


        

    }
    @After
    public void tearDown() throws Exception {
//         Zamknij przeglądarkę
        driver.quit();
    }
}
