package com.demoblaze.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.List;

public class CartPage extends BasePage {
    private By cartItems = By.cssSelector("#tbodyid tr");
    private By deleteLink = By.xpath("//a[text()='Delete']");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public boolean isProductInCart(String productName) {
        By productLocator = By.xpath("//td[text()='" + productName + "']");
        return isDisplayed(productLocator);
    }

    public void deleteProduct(String productName) {
        By productLocator = By.xpath("//td[text()='" + productName + "']");
        By deleteButton = By.xpath("//td[text()='" + productName + "']/following-sibling::td/a[text()='Delete']");
        click(deleteButton);
        wait.until(org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated(productLocator));
    }
    
    // Alternative delete if only one product is expected
    public void deleteFirstProduct() {
        click(deleteLink);
    }
    
    public int getCartItemCount() {
        List<WebElement> items = driver.findElements(cartItems);
        return items.size();
    }
}
