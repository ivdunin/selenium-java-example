package com.company.selenium.test.pages.md;

import com.company.selenium.test.Consts;
import com.company.selenium.test.utils.TimeUtils;
import com.company.selenium.test.webtestsbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.logging.Logger;

/**
 * Class MDDashboardPage: single page object for MD Dashboard page.
 */
public class MDDashboardPage extends BasePage {
    private static final String PAGE_URL = Consts.MD_DASHBOARD;

    private static Logger log = Logger.getLogger(MDDashboardPage.class.getName());

    private static String JS_STYLE_INLINE = "document.getElementById('sub_procedure_id').setAttribute('style', 'display: initial')";
    private static String JS_STYLE_NONE = "document.getElementById('sub_procedure_id').setAttribute('style', 'display: none')";


    private static String APPOINTMENT_ACTION = "Review Patient\nAppointment Request";

    private static String ACTION_ITEMS = "//div[@class='actionItemsText']";
    private static String ACTION_RELATED_TO = "../following-sibling::td[starts-with(@id,'item_related_to')]/div[@class='relatedToText']";
    private static String ACTION_REVIEW = "../following-sibling::td[starts-with(@id,'action')]/div[@class='form-button']";
    private static String ACTION_DISMISS = "../following-sibling::td[starts-with(@id,'dismiss')]/div[@class='dismissButton']";

    // Selectors
    @FindBy(id = "mdInviteCode")
    private WebElement inviteCode;

    @FindBy(id = "tblRequiresAttention_wrapper")
    private WebElement tableRequireAttention;

    @FindBy(xpath = "//div[@class='EMR-blue-box default']")
    private WebElement frmAppointmentRequest;

    @FindBy(id = "new-appointment")
    private WebElement frmAppointmentSave;

    @FindBy(xpath = "//button[@class='bluebutton close-ar-form']")
    private WebElement btnARCancel;

    @FindBy(xpath = "//tr[@data-position='0']/td[3]/button[contains(@class, 'greenbutton mb5 approve')]")
    private WebElement btnApprove1;

    @FindBy(xpath = "//tr[@data-position='1']/td[3]/button[contains(@class, 'greenbutton mb5 approve')]")
    private WebElement btnApprove2;

    @FindBy(xpath = "//button[contains(@class, 'redbutton mb5 reject')]")
    private WebElement btnReject;

    @FindBy(xpath = "//div[contains(@class, 'sweet-alert')]/button[@class='confirm']")
    private WebElement btnConfirm;

    @FindBy(id = "sub_procedure_id")
    private WebElement ddProcedureId;

    @FindBy(id = "duration")
    private WebElement ddDuration;

    @FindBy(id = "appointment-save")
    private WebElement btnAppointmentSave;

    @FindBy(xpath = "//div[@id='waiting']/img")
    private WebElement waitingImg;

    @FindBy(xpath = "//div[contains(@class, 'sweet-alert')]/h2[text() = 'Success']")
    private WebElement sweetAlertSuccess;

    @FindBy(id = "messagesOverlay")
    private WebElement frmMessages;

    @FindBy(id = "sendMessage")
    private WebElement btnSendMessage;

    @FindBy(xpath = "//div[@id='appointment_request']")
    private WebElement btnShowAppointments;

    @FindBy(id = "dvRequiresAttentionTop")
    private WebElement stripRequiresAttention;

    // Constructor
    public MDDashboardPage() { super(false); }

    // Overrides
    @Override
    protected void openPage() {
        getDriver().get(PAGE_URL);
    }

    /**
     * Check that all fields and button displayed on page
     */
    @Override
    public boolean isPageOpened() {
        return stripRequiresAttention.isDisplayed() && tableRequireAttention.isDisplayed();
    }

    /**
     * Check label visibility by Xpath
     * @param labelXpath element xpath
     * @return True if displayed, else False
     */
    private boolean isLabelDisplayed(String labelXpath)
    {
        // TODO: Move to BasePage class
        try {
            WebElement label = getDriver().findElement(By.xpath(labelXpath));
            return label.isDisplayed();
        } catch (NoSuchElementException e)
        {
            return false;
        }
    }

    /**
     * Check if appointment request form reason Initial Consult
     * @return True if Appointment Reason == Initial Consult
     */
    private boolean isInitialConsult()
    {
        return isLabelDisplayed(String.format("//div[@class='option-row-body' and contains(text(), '%s')]", Consts.APP_TYPE_INITIAL_CONSULT));
    }

    /**
     * Check if appointment request form reason Procedure Treatment
     * @return True if Appointment Reason == Procedure Treatment
     */
    private boolean isTreatment()
    {
        return isLabelDisplayed(String.format("//div[@class='option-row-body' and contains(text(), '%s')]", Consts.APP_TYPE_TREATMENT));
    }

    /**
     * Check if appointment request form reason Follow UP
     * @return True if Appointment Reason == Follow up
     */
    private boolean isFollowUp()
    {
        return isLabelDisplayed(String.format("//div[@class='option-row-body' and contains(text(), '%s')]", Consts.APP_TYPE_FOLLOW_UP));
    }

    /**
     * Confirm Initial Consult or Follow Up Appointment
     * @param patientName patient name
     * @param procedure select procedure
     * @param duration select duration
     * @return
     */
    public boolean confirmAppointment(String appointmentType, String patientName, String procedure, String duration)
    {
        // Wait for page loading
        int tick = 0;
        while (waitingImg.isDisplayed() && tick <= (WAIT_FOR_PAGE_LOAD_IN_SECONDS * 2))
        {
            TimeUtils.waitForSeconds(0.5);
            tick++;
        }

        // Show only appointments
        btnShowAppointments.click();
        // FIXME: Very bad decision!!1 Need to fix. Remember items before btnShowAppointments.click
        // FIXME: and check after click (if not change, wait more).
        TimeUtils.waitForSeconds(2);


        // TODO: refactoring add classes for frmAppointmentRequest and frmAppointmentSave with own methods
        // TODO: e.g. example new FormAppointmentRequest.CancelAppointment(String patient, date)

        List<WebElement> tableItems = getDriver().findElements(By.xpath(ACTION_ITEMS));
        for (WebElement item: tableItems) {
            if (item.getText().equals(APPOINTMENT_ACTION)) {
                // TODO: Will find only first matching entry!!! Need additional checks
                WebElement relatedTo = item.findElement(By.xpath(ACTION_RELATED_TO));
                if (relatedTo.getText().equals(patientName)) {
                    WebElement btnActionReview = item.findElement(By.xpath(ACTION_REVIEW));

                    if (!waitForElementDisplayed(btnActionReview, WAIT_FOR_PAGE_LOAD_IN_SECONDS))
                    {
                        log.severe("Button Review not available for appointment!");
                        return false;
                    }
                    btnActionReview.click();

                    if (!waitForElementDisplayed(frmAppointmentRequest, 15))
                    {
                        log.severe("Appointment Request form not found!");
                        return false;
                    }

                    if (appointmentType.equals(Consts.APP_TYPE_INITIAL_CONSULT))
                    {
                        if (isInitialConsult())
                            btnApprove1.click();
                        else
                        {
                            log.severe("Not Initial Form!");
                            return false;
                        }
                    }
                    else if (appointmentType.equals(Consts.APP_TYPE_FOLLOW_UP))
                    {
                        if (isFollowUp())
                            btnApprove1.click();
                        else
                        {
                            log.severe("Not Follow Up Form!");
                            return false;
                        }
                    }
                    else {
                        log.severe(String.format("Unknown appointment type: %s", appointmentType));
                        return false;
                    }


                    if (!waitForElementDisplayed(frmAppointmentSave, 15))
                    {
                        log.severe("Appointment Save form not found!");
                        return  false;
                    }

                    // Select element not visible, so we need to do it visible before select items
                    JavascriptExecutor js = (JavascriptExecutor) getDriver();
                    js.executeScript(JS_STYLE_INLINE);

                    new Select(ddProcedureId).selectByVisibleText(procedure);
                    // Return previous attribute value
                    js.executeScript(JS_STYLE_NONE);


                    new Select(ddDuration).selectByVisibleText(duration);
                    btnAppointmentSave.click();
                    if (!waitForElementDisplayed(sweetAlertSuccess, 15))
                    {
                        log.severe("Alert Window with appointment confirmation not found or it is not success!");
                        return false;
                    }

                    btnConfirm.click();

                    if (!waitForElementDisplayed(frmMessages, 15))
                    {
                        log.severe("Message for not found!");
                        return false;
                    }

                    if (isInteractable(btnSendMessage))
                        btnSendMessage.click();
                    else
                    {
                        log.severe("Cannot send confirmation message! Button not available!");
                        return false;
                    }

                    if (!waitForElementDisplayed(sweetAlertSuccess, 15))
                    {
                        log.severe("Alert Window with message confirmation not found or it is not success!");
                        return false;
                    }

                    btnConfirm.click();

                    return true;
                }
            }
        }

        log.severe("Something goes wrong when confirm appointment!");
        return false;
    }

    /**
     * Confirm Initial Consult Appointment
     * @param patientName patient name
     * @param duration select duration
     * @return
     */
    public boolean confirmTreatmentAppointment(String patientName, String duration)
    {
        // Wait for page loading
        int tick = 0;
        while (waitingImg.isDisplayed() && tick <= (WAIT_FOR_PAGE_LOAD_IN_SECONDS * 2))
        {
            TimeUtils.waitForSeconds(0.5);
            tick++;
        }

        // Show only appointments
        btnShowAppointments.click();
        waitForPageToLoad(getDriver());


        // TODO: refactoring add classes for frmAppointmentRequest and frmAppointmentSave with own methods
        // TODO: e.g. example new FormAppointmentRequest.CancelAppointment(String patient, date)

        List<WebElement> tableItems = getDriver().findElements(By.xpath(ACTION_ITEMS));
        for (WebElement item: tableItems) {
            if (item.getText().equals(APPOINTMENT_ACTION)) {
                // TODO: Will find only first matching entry!!! Need additional checks
                WebElement relatedTo = item.findElement(By.xpath(ACTION_RELATED_TO));
                if (relatedTo.getText().equals(patientName)) {
                    WebElement btnActionReview = item.findElement(By.xpath(ACTION_REVIEW));

                    if (!waitForElementDisplayed(btnActionReview, WAIT_FOR_PAGE_LOAD_IN_SECONDS))
                    {
                        log.severe("Button Review not available for appointment!");
                        return false;
                    }
                    btnActionReview.click();

                    if (!waitForElementDisplayed(frmAppointmentRequest, 15))
                    {
                        log.severe("Appointment form not found!");
                        return false;
                    }

                    if (isTreatment())
                        btnApprove1.click();
                    else
                    {
                        log.severe("Not Procedure Treatment Form!");
                        return false;
                    }


                    if (!waitForElementDisplayed(frmAppointmentSave, 15))
                    {
                        log.severe("Appointment Save form not found!");
                        return  false;
                    }

                    new Select(ddDuration).selectByVisibleText(duration);
                    btnAppointmentSave.click();
                    if (!waitForElementDisplayed(sweetAlertSuccess, 15))
                    {
                        log.severe("Alert Window with appointment confirmation not found or it is not success!");
                        return false;
                    }

                    btnConfirm.click();

                    if (!waitForElementDisplayed(frmMessages, 15))
                    {
                        log.severe("Message for not found!");
                        return false;
                    }

                    if (isInteractable(btnSendMessage))
                        btnSendMessage.click();
                    else
                    {
                        log.severe("Cannot send confirmation message! Button not available!");
                        return false;
                    }

                    if (!waitForElementDisplayed(sweetAlertSuccess, 15))
                    {
                        log.severe("Alert Window with message confirmation not found or it is not success!");
                        return false;
                    }

                    btnConfirm.click();

                    return true;
                }
            }
        }

        log.severe("Something goes wrong when confirm appointment!");
        return false;
    }

}
