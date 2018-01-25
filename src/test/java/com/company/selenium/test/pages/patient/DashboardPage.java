package com.company.selenium.test.pages.patient;

import com.company.selenium.test.Consts;
import com.company.selenium.test.webtestsbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.util.logging.Logger;

/**
 * Created by dunin on 7/9/17.
 */
public class DashboardPage extends BasePage {
    private static final String PAGE_URL = Consts.DASHBOARD;
    private PatientMenu menu = new PatientMenu(getDriver());
    private static Logger log = Logger.getLogger(DashboardPage.class.getName());

    public DashboardPage(boolean openByUrl) { super(openByUrl); }

    @Override
    protected void openPage() {
        getDriver().get(PAGE_URL);
    }

    /**
     * Check that all fields and button displayed on page
     */
    @Override
    public boolean isPageOpened() {
        try {
            WebElement leftMenu = getDriver().findElement(By.xpath("//div[@class='header__menu-flexed']"));
            WebElement rightMenu = getDriver().findElement(By.xpath("//div[@class='dashboard__right-body']"));
            WebElement dashboardHead = getDriver().findElement(By.xpath("//div[@class='dashboard__head-in']"));
            return leftMenu.isDisplayed() && rightMenu.isDisplayed() && dashboardHead.isDisplayed();
        } catch (NoSuchElementException e)
        {
            log.severe(e.toString());
            return false;
        }
    }

    /**
     * Check widgets for new patient
     * @return True if widgets exist, else False
     */
    public boolean checkWidgetsForNewPatient() {
        String widgetMedHistText = "Save time on your next visit. Complete your medical history here.";

        WebElement widgetMedHist = getDriver().findElement(By.xpath("//a[@class='dashboard__item' and @href='/medical-history']/span[@class='dashboard__item-content']/span[@class='dashboard__item-text']"));
        WebElement widgetWelcomeMsg = getDriver().findElement(By.xpath("//a[@class='dashboard__item' and contains(@href, '/patient-messages/inbox')]"));
        return widgetMedHist.isDisplayed() && widgetMedHist.getText().equals(widgetMedHistText) && widgetWelcomeMsg.isDisplayed();
    }

    /**
     * getter method for Patient Menu
     * @return PatientMenu object
     */
    public PatientMenu getMenu()
    {
        return menu;
    }
}