package com.demoblaze.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage extends BasePage {
    private By cartLink = By.id("cartur");
    private By homeLink = By.cssSelector("li.nav-item.active a");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public void clickProduct(String productName) {
        click(By.linkText(productName));
    }

    public void goToCart() {
        click(cartLink);
    }
    
    public void goToHome() {
        click(homeLink);
    }
}
