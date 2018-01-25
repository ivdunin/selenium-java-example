package com.company.selenium.test.pages.patient;

import com.company.selenium.test.Consts;
import com.company.selenium.test.webtestsbase.BasePage;

import java.util.logging.Logger;

public class FormsPage extends BasePage {
    private static final String PAGE_URL = Consts.FORMS;
    private static Logger log = Logger.getLogger(FormsPage.class.getName());

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
        // TODO: Add conditions to check if opened
        return true;
    }

    public FormsPage(boolean openPageByUrl) {
        super(openPageByUrl);
    }
}
