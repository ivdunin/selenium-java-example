package com.company.selenium.test.pages;


import com.company.selenium.test.Consts;
import com.company.selenium.test.webtestsbase.BasePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;


/**
 * Created by dunin on 7/8/17.
 */
public class InsertVerificationCodePage extends BasePage {
    private static final String PAGE_URL = Consts.INSERT_VERIFICATION_CODE_PAGE;

    @FindBy(id = "verify_code")
    private WebElement verifyCode;

    @FindBy(xpath = "//div[contains(@class, 'btn-verify-patient')]")
    private WebElement submitBtn;

    @FindBy(xpath = "//div[text()='Invalid verification code']")
    private WebElement invalidCode;

    public InsertVerificationCodePage() { super(false); }

    @Override
    protected void openPage() {
        getDriver().get(PAGE_URL);
    }

    /**
     * Check that all fields and button displayed on page
     */
    @Override
    public boolean isPageOpened() {
        return  submitBtn.isDisplayed();
    }

    public void insertVerificationCode(String code) {
        verifyCode.sendKeys(code);
    }

    public ConfirmationPage submitClick() {
        submitBtn.click();
        return new ConfirmationPage();
    }

    public boolean isInvalidCode() {
        return invalidCode.isDisplayed();
    }
}
