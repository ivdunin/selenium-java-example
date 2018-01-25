package com.company.selenium.test.pages.patient;

import com.company.selenium.test.Consts;
import com.company.selenium.test.webtestsbase.BasePage;

public class FinancialHistoryPage extends BasePage {
    public static final String PAGE_URL = Consts.FINANCIAL_HISTORY;

    /**
     * In subclasses  should be used for page opening
     */
    @Override
    protected void openPage() {

    }

    /**
     * checks is page opened
     *
     * @return true if opened
     */
    @Override
    public boolean isPageOpened() {
        // TODO: Add checks
        return true;
    }

    public FinancialHistoryPage(boolean openPageByUrl) {
        super(openPageByUrl);
    }
}
