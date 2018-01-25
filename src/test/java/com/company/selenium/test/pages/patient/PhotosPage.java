package com.company.selenium.test.pages.patient;

import com.company.selenium.test.Consts;
import com.company.selenium.test.webtestsbase.BasePage;

public class PhotosPage extends BasePage {
    public static final String PAGE_URL = Consts.PHOTOS;
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
        // TODO: add checks
        return true;
    }

    public PhotosPage(boolean openPageByUrl) {
        super(openPageByUrl);
    }
}
