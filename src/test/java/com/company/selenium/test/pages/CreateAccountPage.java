package com.company.selenium.test.pages;

import com.company.selenium.test.Consts;
import com.company.selenium.test.utils.TimeUtils;
import com.company.selenium.test.webtestsbase.BasePage;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.logging.Logger;

//TODO: Need refactoring. Create parent class CreateAccountPageLocators to Store only locators

/**
 * Created by dunin on 7/7/17.
 */
public class CreateAccountPage extends BasePage {
    private static final String PAGE_URL = Consts.CREATE_ACCOUNT_PAGE;
    private static Logger log = Logger.getLogger(CreateAccountPage.class.getName());

    @FindBy(id = "full_name")
    private WebElement fullName;

    @FindBy(id = "email")
    private WebElement email;

    @FindBy(id = "phone")
    private WebElement phoneNumber;

    @FindBy(id = "password")
    private WebElement password;

    @FindBy(id = "subscribe")
    private WebElement subscribe;

    @FindBy(xpath = "//div[contains(@class, 'js-btn-create-patient')]")
    private WebElement createAccountBtn;

    @FindBy(xpath = "//button[@class='confirm']")
    private WebElement confirmBtn;

    @FindBy(xpath = "//a[@href='/user-login']")
    private WebElement loginLink;

    @FindBy(id = "reset-password")
    private WebElement resetPasswordLink;

    @FindBy(xpath = "//div[@class='red-error' and starts-with(text(),'Account exists for this email address.')]")
    private WebElement accountExistMessage;

    @FindBy(xpath = "//div[@class='red-error' and text()='Please provide first and last name']")
    private WebElement incorrectFullName;

    @FindBy(xpath = "//div[@class='red-error' and text()='Please provide a valid email address']")
    private WebElement incorrectEmail;

    @FindBy(xpath = "//div[@class='red-error' and text()='Your password must be 6 characters or longer with at least 1 letter and 1 number']")
    private WebElement incorrectPassword;


    public CreateAccountPage() {
        // We don't need to open this page on creating CreateAccountPage instance
        super(false);
    }

    @Override
    protected void openPage() {
        getDriver().get(PAGE_URL);
    }

    /**
     * Check that all fields and button displayed on page
     */
    @Override
    public boolean isPageOpened() {
        return  createAccountBtn.isDisplayed();
    }

    /**
     * Fill account info form before submit
     * @param fullname Patient full name
     * @param email Patient email
     * @param phone Patient phone
     * @param password Patient password
     * @param subscribe Subscribe patient to news
     */
    public void fillAccountInfo(String fullname, String email, String phone, String password, boolean subscribe)
    {
        this.fullName.sendKeys(fullname);
        this.email.sendKeys(email);
        this.phoneNumber.sendKeys(phone);
        this.password.sendKeys(password);
        if (!this.subscribe.isSelected() && subscribe) { this.subscribe.click(); }
        if (this.subscribe.isSelected() && !subscribe) { this.subscribe.click(); }
    }

    public void createAccount()
    {
        createAccountBtn.click();
        //FIXME: remove wait and add check element <div id='waiting'>.isDisplayed()
        TimeUtils.waitForSeconds(3);  // wait after click
    }

    public InsertVerificationCodePage confirmClick()
    {
        confirmBtn.click();
        return new InsertVerificationCodePage();
    }

    public boolean isIncorrectFullName() {
        return incorrectFullName.isDisplayed();
    }

    public boolean isIncorrectEmail() {
        return incorrectEmail.isDisplayed();
    }

    public boolean isIncorrectPassword() {
        return incorrectPassword.isDisplayed();
    }

    public boolean isAccountExist()
    {
        try {
            return accountExistMessage.isDisplayed();
        } catch (NoSuchElementException e)
        {
            log.severe("Element with mwssage 'Account exists for this email address.' not found!");
            return false;
        }
    }
}
