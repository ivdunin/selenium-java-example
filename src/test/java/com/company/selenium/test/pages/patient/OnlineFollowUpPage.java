package com.company.selenium.test.pages.patient;

import com.company.selenium.test.Consts;
import com.company.selenium.test.webtestsbase.BasePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class OnlineFollowUpPage extends BasePage {
    private static final String PAGE_URL = Consts.ONLINE_FOLLOW_UPS;

    @FindBy(xpath = "//div[@class='modal-body']")
    private WebElement frmFollowUp;

    @FindBy(linkText = "Start Follow-Up")
    private WebElement btnStartFollowUp;

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
        return frmFollowUp.isDisplayed() && btnStartFollowUp.isDisplayed();
    }

    public OnlineFollowUpPage(boolean openPageByUrl) {
        super(openPageByUrl);
    }
}
