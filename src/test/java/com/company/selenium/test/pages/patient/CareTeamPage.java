package com.company.selenium.test.pages.patient;

import com.company.selenium.test.Consts;
import com.company.selenium.test.webtestsbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class CareTeamPage extends BasePage {
    private static final String PAGE_URL = Consts.CARE_TEAM;
    private static Logger log = Logger.getLogger(CareTeamPage.class.getName());

    public CareTeamPage(boolean openPageByUrl) {
        super(openPageByUrl);
    }

    @Override
    protected void openPage() {
        getDriver().get(PAGE_URL);
    }

    @Override
    public boolean isPageOpened() {
        List<String> headersList = Arrays.asList("Providers", "Staff", "About Our Practice", "Contact Us");

        List<WebElement> hItems = getDriver().findElements(By.xpath("//div[contains(@class, 'h1-text')]"));
        for (WebElement item: hItems) {
            if (!headersList.contains(item.getText().trim())) {
                log.severe(String.format("Cannot find header with text: %s", item.getText().trim()));
                return false;
            }
        }
        return true;
    }
}
