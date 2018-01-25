package com.company.selenium.test.pages.patient;

import com.company.selenium.test.Consts;
import com.company.selenium.test.webtestsbase.BasePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class OnlineInitialConsultsPage extends BasePage {
    private static final String PAGE_URL = Consts.ONLINE_INITIAL_CONSULTS;

    @FindBy(xpath = "//div[@class='modal-body']")
    private WebElement frmInitialConsult;

    @FindBy(linkText = "Start Initial Consult")
    private WebElement btnStartInitialConsult;

    public OnlineInitialConsultsPage(boolean openPageByUrl) {
        super(openPageByUrl);
    }

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
        return frmInitialConsult.isDisplayed() && btnStartInitialConsult.isDisplayed();
    }
}
