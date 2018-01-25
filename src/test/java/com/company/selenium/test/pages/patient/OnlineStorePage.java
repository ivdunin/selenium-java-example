package com.company.selenium.test.pages.patient;

import com.company.selenium.test.Consts;
import com.company.selenium.test.webtestsbase.BasePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class OnlineStorePage extends BasePage {
    public static final String PAGE_URL = Consts.ONLINE_STORE;

    @FindBy(xpath = "//a[@href='#catalog-tab']")
    private WebElement tabBuyProducts;

    @FindBy(xpath = "//a[@href='#shopping-cart-tab']")
    private WebElement tabShopingCart;

    @FindBy(xpath = "//a[@href='#purchase-history-tab']")
    private WebElement tabPurchaseHistory;
    /**
     * In subclasses  should be used for page opening
     */
    @Override
    protected void openPage() {
        getDriver().get(PAGE_URL);
    }

    /**
     * checks is page opened
     *
     * @return true if opened
     */
    @Override
    public boolean isPageOpened() {
        return tabBuyProducts.isDisplayed() && tabShopingCart.isDisplayed() && tabPurchaseHistory.isDisplayed();
    }

    public OnlineStorePage(boolean openPageByUrl) {
        super(openPageByUrl);
    }
}
