package com.company.selenium.test.pages;

import com.company.selenium.test.Consts;
import com.company.selenium.test.pages.patient.DashboardPage;
import com.company.selenium.test.webtestsbase.BasePage;
import com.company.selenium.test.pages.md.MDDashboardPage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.logging.Logger;


/**
 * Created by dunin on 7/9/17.
 */
public class LoginPage extends BasePage {
    private static final String PAGE_URL = Consts.LOGIN_PAGE;
    private static Logger log = Logger.getLogger(LoginPage.class.getName());

    // Selectors
    @FindBy(id = "txtLoginEmail")
    private WebElement fieldLogin;

    @FindBy(id = "txtLoginPassword")
    private WebElement fieldPassword;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement btnLogin;

    @FindBy(xpath = "//h2[text()='Validation Error']")
    private WebElement msgValidationError;

    @FindBy(xpath = "//button[@class='confirm']")
    private WebElement btnConfirm;

    @FindBy(xpath = "//p[text()='FAILED: You already have an account, please log in with the correct password for that account']")
    private WebElement msgAccountAlreadyExist;

    @FindBy(xpath = "//p[text()='FAILED: No matching email address found']")
    private WebElement msgEmailNotExist;

    @FindBy(xpath = "//div[@class='sweet-alert showSweetAlert visible']")
    private WebElement sweetAlert;

    // Constructors and overrides
    public LoginPage() { super(true); }

    @Override
    protected void openPage() {
        getDriver().get(PAGE_URL);
    }

    /**
     * Check that all fields and button displayed on page
     */
    @Override
    public boolean isPageOpened() {
        return btnLogin.isDisplayed();
    }

    // Custom methods

    /**
     * Login to MD or Patient portal
     * @param email user email
     * @param password user password
     * @param userType user type (PATIENT_USER, MD_USER)
     * @return Dashboard Class Instance
     */
    public BasePage doLogin(String email, String password, int userType) {
        fieldLogin.sendKeys(email);
        fieldPassword.sendKeys(password);
        btnLogin.click();
        if (userType == Consts.USER_TYPE_PATIENT) {
            log.info(String.format("Patient login: %s : %s", email, password));
            return new DashboardPage(false);
        }
        else if (userType == Consts.USER_TYPE_MD) {
            log.info(String.format("MD login: %s : %s", email, password));
            if (waitForElementDisplayed(sweetAlert, 15))
                btnConfirm.click();
            else {
                log.severe("Sweet Alert Window not found!");
                return null;
            }
            return new MDDashboardPage();
        }
        else return null;
    }
}