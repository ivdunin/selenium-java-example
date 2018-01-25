package com.company.selenium.test.pages;

import com.company.selenium.test.Consts;
import com.company.selenium.test.webtestsbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;



/**
 * Created by dunin on 7/8/17.
 */
public class ConfirmationPage extends BasePage {
    private static final String PAGE_URL = Consts.CONFIRM_ACCOUNT_PAGE;

    @FindBy(xpath = "//div[contains(text(),'Thank you, your email has been verified.')]")
    private WebElement succesfullVerifyMsg;

    @FindBy(xpath = "//a[@class='btn btn-success btn-block' and @href='/patient-dashboard']")
    private WebElement nextButton;

    public ConfirmationPage() { super(false); }

    @Override
    protected void openPage() {
        getDriver().get(PAGE_URL);
    }

    /**
     * Check that all fields and button displayed on page
     */
    @Override
    public boolean isPageOpened() {
        return  succesfullVerifyMsg.isDisplayed();
    }

    public boolean checkNameOnPage(String name) {
        String searchString = "//div[contains(text(),'"+ name + "')]";
        return getDriver().findElement(By.xpath(searchString)).isDisplayed();
    }

    public void nextClick() {
        // Should return new page
        nextButton.click();
    }
}

