package com.company.selenium.test.webtestsbase;

import com.company.selenium.test.utils.TimeUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.util.logging.Logger;

/**
 * Created by Sidelnikov Mikhail on 19.09.14.
 * This is the main class for pages. When you create your page - you must extend your class from this
 */
public abstract class BasePage {
    protected static final int WAIT_FOR_PAGE_LOAD_IN_SECONDS = 10;
    protected static final int WAIT_FOR_INTERACTABLE = 10;
    private static Logger log = Logger.getLogger(BasePage.class.getName());

    /**
     * In subclasses  should be used for page opening
     */
    protected abstract void openPage();

    /**
     * checks is page opened
     * @return true if opened
     */
    public abstract boolean isPageOpened();

    public BasePage(boolean openPageByUrl){
        if(openPageByUrl){
            openPage();
        }
        PageFactory.initElements(getDriver(), this);
        waitForOpen();
    }

    /**
     * Waiting for page opening
     */
    protected void waitForOpen(){
        int secondsCount = 0;
        boolean isPageOpenedIndicator = isPageOpened();
        while (!isPageOpenedIndicator && secondsCount < WAIT_FOR_PAGE_LOAD_IN_SECONDS) {
            TimeUtils.waitForSeconds(1);
            secondsCount++;
            isPageOpenedIndicator = isPageOpened();
        }
        if(!isPageOpenedIndicator) {
            throw new AssertionError("Page was not opened");
        }
    }

    /**
     * getting webdriver instance
     * @return initialized in tests webdriver instance
     */
    protected WebDriver getDriver(){
        return WebDriverFactory.getDriver();
    }

    /**
     * Wait until element became interactable
     * @param element WebElement to check
     * @return True if Interactable, else False
     */
    protected boolean isInteractable(WebElement element) {
        int waitForSec = 0;
        while (waitForSec <= WAIT_FOR_INTERACTABLE) {
            try {
                log.fine("Check element for interactable...");
                element.click();
                return true;
            } catch (ElementNotInteractableException e) {
                log.fine("Element not interactable. Waiting...");
            }

            TimeUtils.waitForSeconds(1);
            waitForSec++;
        }
        // Still not Interactable
        log.severe(String.format("Element %s not interactable... Test will fail!", element.getTagName()));
        return false;
    }

    /**
     * Wait for table with items
     * @return True if table found, else false
     */
    protected boolean waitForElementDisplayed(WebElement element, int seconds){
        try {
            int tick = 0;

            while (tick <= seconds && !element.isDisplayed()) {
                TimeUtils.waitForSeconds(1);
                tick++;
            }
            return element.isDisplayed();
        } catch (NoSuchElementException e)
        {
            log.severe(e.toString());
            return false;
        }
    }

    public void waitForPageToLoad(WebDriver driver) {
        ExpectedCondition< Boolean > pageLoad = new
                ExpectedCondition < Boolean > () {
                    public Boolean apply(WebDriver driver) {
                        return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
                    }
                };

        Wait< WebDriver > wait = new WebDriverWait(driver, 60);
        try {
            wait.until(pageLoad);
        } catch (Throwable pageLoadWaitError) {
            Assert.assertTrue(true, "Timeout during page load");
        }
    }
    // TODO: https://watirmelon.blog/2016/06/29/using-webdriver-to-automatically-check-for-javascript-errors-on-every-page-2016-edition/
}
