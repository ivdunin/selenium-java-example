package com.company.selenium.test.pages.patient;

import com.company.selenium.test.Consts;
import com.company.selenium.test.webtestsbase.BasePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.logging.Logger;

public class MedicalHistoryPage extends BasePage {
    private static final String PAGE_URL = Consts.MEDICAL_HISTORY;
    private static Logger log = Logger.getLogger(MedicalHistoryPage.class.getName());

    @FindBy(xpath = "//a[@href='medical-history.html#tab-1']")
    private WebElement tabMedicalHistory;

    @FindBy(xpath = "//a[@href='medical-history.html#tab-2']")
    private WebElement tabPastSubmissions;

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
        return tabMedicalHistory.isDisplayed() && tabPastSubmissions.isDisplayed();
    }

    public MedicalHistoryPage(boolean openPageByUrl) {
        super(openPageByUrl);
    }
}
