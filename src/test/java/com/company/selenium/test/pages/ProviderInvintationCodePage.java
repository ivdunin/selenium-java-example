package com.company.selenium.test.pages;

import com.company.selenium.test.Consts;
import com.company.selenium.test.webtestsbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;


/**
 * Created by dunin on 7/7/17.
 */
public class ProviderInvintationCodePage extends BasePage {
    private static final String PAGE_URL = Consts.INSERT_PROVIDER_CODE_PAGE;

    @FindBy(id = "invite_code")
    private WebElement providerInvintationCode;

    @FindBy(xpath = "//div[contains(@class, 'js-btn-submit-patient')]")
    private WebElement submitButton;

    @FindBy(xpath = "//div[contains(@class, 'js-frm-submit')]")
    private WebElement nextButton;

    @FindBy(xpath = "//div[text()='Dmitriy Shust, MD']")
    private WebElement providerName;

    @FindBy(xpath = "//div[@class='red-error' and text()='Please provide a valid invitation code']")
    private WebElement incorrectProviderMessage;

    public ProviderInvintationCodePage() {
        super(true);
    }

    @Override
    public void openPage() {
        getDriver().get(PAGE_URL);
    }

    @Override
    public boolean isPageOpened() {
        return providerInvintationCode.isDisplayed() && submitButton.isDisplayed();
    }

    public void insertProviderCode(String text) {
        providerInvintationCode.sendKeys(text);
    }

    public void clearProviderCode() {
        providerInvintationCode.clear();
    }

    /**
     * Click on Submit button
     */
    public void clickSubmit() {
        submitButton.click();
    }

    /**
     * Click on next button and return CreateAccountPage instance.
     * @return new CreateAccountPage instance
     */
    public CreateAccountPage clickNext() {
        nextButton.click();
        return new CreateAccountPage();
    }

    public boolean isNextBtnFound() {
        return nextButton.isDisplayed();
    }

    /**
     * Check provide name after insert provider invitation code
     * @param name provide name
     * @return True if found div element with name, else False
     */
    public boolean checkProvideName(String name) {
        String searchString = ".//div[text()='" + name + "']";
        return getDriver().findElement(By.xpath(searchString)).isDisplayed();
    }

    public boolean isIncorrectProviderCode() {
        return incorrectProviderMessage.isDisplayed();
    }

}


