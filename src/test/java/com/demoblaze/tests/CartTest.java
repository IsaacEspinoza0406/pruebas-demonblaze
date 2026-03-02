package com.demoblaze.tests;

import com.demoblaze.pages.CartPage;
import com.demoblaze.pages.HomePage;
import com.demoblaze.pages.ProductPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class CartTest {
    private WebDriver driver;
    private HomePage homePage;
    private ProductPage productPage;
    private CartPage cartPage;

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "false"));
        if (headless) {
            options.addArguments("--headless=new");
        }
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.get("https://www.demoblaze.com/");
        
        homePage = new HomePage(driver);
        productPage = new ProductPage(driver);
        cartPage = new CartPage(driver);
    }

    @Test(description = "TC-CART-01: Agregar producto")
    public void testAddProduct() {
        homePage.clickProduct("Samsung galaxy s6");
        productPage.addToCart();
        String alertText = productPage.getAlertTextAndAccept();
        Assert.assertTrue(alertText.contains("Product added"), "Alert text mismatch!");
        
        homePage.goToCart();
        Assert.assertTrue(cartPage.isProductInCart("Samsung galaxy s6"), "Product not in cart!");
    }

    @Test(description = "TC-CART-02: Agregar múltiples productos")
    public void testAddMultipleProducts() {
        homePage.clickProduct("Samsung galaxy s6");
        productPage.addToCart();
        Assert.assertTrue(productPage.getAlertTextAndAccept().contains("Product added"), "Alert text mismatch for first product");
        homePage.goToHome();
        
        homePage.clickProduct("Nokia lumia 1520");
        productPage.addToCart();
        Assert.assertTrue(productPage.getAlertTextAndAccept().contains("Product added"), "Alert text mismatch for second product");
        
        homePage.goToCart();
        Assert.assertTrue(cartPage.isProductInCart("Samsung galaxy s6"), "S6 not in cart!");
        Assert.assertTrue(cartPage.isProductInCart("Nokia lumia 1520"), "Lumia not in cart!");
    }

    @Test(description = "TC-CART-03: Eliminar producto")
    public void testDeleteProduct() {
        homePage.clickProduct("Samsung galaxy s6");
        productPage.addToCart();
        Assert.assertTrue(productPage.getAlertTextAndAccept().contains("Product added"), "Alert text mismatch for second product");
        
        homePage.goToCart();
        Assert.assertTrue(cartPage.isProductInCart("Samsung galaxy s6"), "Product should be in cart before deletion");
        
        cartPage.deleteProduct("Samsung galaxy s6");
        Assert.assertFalse(cartPage.isProductInCart("Samsung galaxy s6"), "Product should be removed from cart");
    }

    @Test(description = "TC-CART-04: Persistencia del carrito")
    public void testCartPersistence() {
        homePage.clickProduct("Samsung galaxy s6");
        productPage.addToCart();
        Assert.assertTrue(productPage.getAlertTextAndAccept().contains("Product added"), "Alert text mismatch for second product");
        
        homePage.goToCart();
        Assert.assertTrue(cartPage.isProductInCart("Samsung galaxy s6"), "Product should be in cart");
        
        driver.navigate().refresh();
        Assert.assertTrue(cartPage.isProductInCart("Samsung galaxy s6"), "Product should persist in cart after refresh");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            long pauseBeforeQuitMs = Long.parseLong(System.getProperty("pauseBeforeQuitMs", "0"));
            if (pauseBeforeQuitMs > 0) {
                try {
                    Thread.sleep(pauseBeforeQuitMs);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            driver.quit();
        }
    }
}
