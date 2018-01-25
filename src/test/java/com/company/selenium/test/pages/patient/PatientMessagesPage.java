package com.company.selenium.test.pages.patient;

import com.company.selenium.test.Consts;
import com.company.selenium.test.webtestsbase.BasePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class PatientMessagesPage extends BasePage {
    private static final String PAGE_URL = Consts.MESSAGES;

    @FindBy(xpath = "//a[@href='/patient-messages/inbox']")
    private WebElement folderInbox;

    @FindBy(xpath = "//a[@href='/patient-messages/deleted']")
    private WebElement folderDeleted;

    @FindBy(xpath = "//a[@data-target='#js-modal-compose']")
    private WebElement btnCompose;

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
        return folderInbox.isDisplayed() && folderDeleted.isDisplayed() && btnCompose.isDisplayed();
    }

    public PatientMessagesPage(boolean openPageByUrl) {
        super(openPageByUrl);
    }
}
