package com.company.selenium.test.pages.patient;

import com.company.selenium.test.Consts;
import com.company.selenium.test.utils.TimeUtils;
import com.company.selenium.test.webtestsbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

public class AppointmentsPage extends BasePage {
    private static final String PAGE_URL = Consts.APPOINTMENTS;

    // Appointment reasons
    public static final String CONSULTAION = "Consultation only";
    public static final String TREATMENT_PROCEDURE = "Treatment and/or procedure";
    public static final String POST_TREATMENT = "Post-treatment follow-up";
    public static final int DT_OPTION_1 = 1;
    public static final int DT_OPTION_2 = 2;

    private static Logger log = Logger.getLogger(AppointmentsPage.class.getName());

    // Locators
    @FindBy(xpath = "//a[@aria-controls='Upcoming Appointments']")
    private WebElement tabUpcomingAppointments;

    @FindBy(xpath = "//a[contains(@class, 'request-new-appointment')]")
    private WebElement requestAppointment;

    @FindBy(xpath = "//textarea[@name='description']")
    private WebElement fieldDescription;

    @FindBy(xpath = "//button[@type='submit' and contains(text(), 'Submit Appointment Request')]")
    private WebElement btnSubmit;

    @FindBy(xpath = "//div[contains(@class, 'alert__head')]")
    private WebElement alertWnd;

    @FindBy(name = "procedures[]")
    private WebElement selectProcedure;

    @FindBy(name = "previous_appointment")
    private WebElement selectPrevAppointment;

    // Constructors and overrides
    public AppointmentsPage(boolean openPageByUrl) { super(openPageByUrl); }

    @Override
    protected void openPage() {
        getDriver().get(PAGE_URL);
    }

    /**
     * Check that all fields and button displayed on page
     */
    @Override
    public boolean isPageOpened() {
        return tabUpcomingAppointments.isDisplayed() && requestAppointment.isDisplayed();
    }

    // Custom page methods

    /**
     * Request new appointment
     */
    public void newAppointments()
    {
        requestAppointment.click();
    }

    /**
     * Click on radio button
     * @param rbXpathSpan radio button element xpath (span element)
     * @param rbXpathInput radio button element xpath (input)
     * @return True if can click on radio button
     */
    private boolean rbClick(String rbXpathSpan, String rbXpathInput) {
        WebElement rbElement;
        try {
            log.info(String.format("Search element by xpath: %s", rbXpathSpan));
            rbElement = getDriver().findElement(By.xpath(rbXpathSpan));
        } catch (NoSuchElementException e)
        {
            log.severe(String.format("Cannot found element with xpath: %s", rbXpathSpan));
            return false;
        }

        try {
            String enabled;
            if (!rbXpathInput.isEmpty()) {
                enabled = getDriver().findElement(By.xpath(rbXpathInput)).getAttribute("disabled");
                if (enabled != null && enabled.equals("true")) {
                    log.warning(String.format("Element disabled: %s", rbXpathInput));
                    return false;
                }
            }
        } catch (NoSuchElementException e)
        {
            log.severe(String.format("Cannot found element with xpath: %s", rbXpathInput));
            return false;
        }

        if (isInteractable(rbElement))
        {
            if (rbElement.isEnabled()) {
                rbElement.click();
                return true;
            }
            else
                return false;
        } else
        {
            log.severe(String.format("Cannot interact with element xpath: %s", rbXpathSpan));
            return false;
        }
    }

    /**
     * Select Doctor radio button
     * @param doctorName doctor name
     * @return True if click on Doctor
     */
    private boolean selectDoctorByName(String doctorName) {
        String xpath = String.format("//label/span[@class='check__text']/b[contains(text(),'%s')]", doctorName);
        if (!rbClick(xpath, "")) {
            log.severe("Cannot check doctor radio button");
            return false;
        }
        return true;
    }

    /**
     * Select location by text
     * @param locationText location text
     * @return True if click on location
     */
    private boolean selectLocationByText(String locationText) {
        String xpath = String.format("//label[contains(@for, 'location')]/span[@class='check__text' and contains(text(), '%s')]/preceding-sibling::span[@class='check__radio']", locationText);
        String xpathInput = String.format("//label[contains(@for, 'location')]/span[@class='check__text' and contains(text(), '%s')]/preceding-sibling::input", locationText);
        if (!rbClick(xpath, xpathInput)) {
            log.severe("Cannot check location radio button!");
            return false;
        }
        return true;
    }

    /**
     * Select appointment reason
     * @param reason appointment reason
     * @return True if click on reason
     */
    private boolean selectAppointmentReason(String reason) {
        String xpath = String.format("//label/span[@class='check__text' and contains(text(), '%s')]", reason);
        if (!rbClick(xpath, "")) {
            log.severe("Cannot check appointment radio button!");
            return false;
        }
        return true;
    }

    /**
     * Select Date option 1
     * @param option option number
     * @param datetime date
     * @param fromTime from time
     * @param toTime to tome
     */
    private void selectDateOption(int option, String datetime, String fromTime, String toTime) {
        log.info(String.format("Create appointment (option #%s) on date: %s from %s to %s", option, datetime, fromTime, toTime));

        try {
            WebElement dateTimePicker = getDriver().findElement(By.xpath(String.format("//div[@id='datetimepicker%s']/input", option)));
            // Scroll to datetimepickerelement
            ((JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollIntoView(true);", dateTimePicker);
            TimeUtils.waitForSeconds(1);

            dateTimePicker.click();
            dateTimePicker.clear();
            dateTimePicker.sendKeys(datetime);

            selectTimeOptions(option, fromTime, toTime);
        } catch (NoSuchElementException e)
        {
            log.severe("DateTimePicker element not found!");
        }
    }

    /**
     * Select from and to time for chosen option
     * @param option date time option
     * @param fromTime from time
     * @param toTime to time
     */
    private void selectTimeOptions(int option, String fromTime, String toTime) {
        String xpathFromTime = String.format("//div[@id='datetimepicker%s']/../following-sibling::div[1]/div/select", option);
        String xpathToTime = String.format("//div[@id='datetimepicker%s']/../following-sibling::div[2]/div/select", option);
        WebElement fromTimeDD = getDriver().findElement(By.xpath(xpathFromTime));
        WebElement toTimeDD = getDriver().findElement(By.xpath(xpathToTime));

        new Select(fromTimeDD).selectByVisibleText(fromTime);
        new Select(toTimeDD).selectByVisibleText(toTime);
    }

    /**
     * Request appointment for patient
     * @param aProvider provider
     * @param aLocation location for appointment
     * @param aDateTime1 first date time interval
     * @param aDateTime2 second date time interval
     * @param desc appointment description
     * @return True if appointment create and alert window shown, else False
     */
    public boolean fillInAppointmentForm(String aProvider, String aLocation, List<String> aDateTime1, List<String> aDateTime2, String desc) {
        try {
            if (!selectDoctorByName(aProvider))
                return false;

            if (!selectLocationByText(aLocation))
                return false;

            selectDateOption(DT_OPTION_1, aDateTime1.get(0), aDateTime1.get(1), aDateTime1.get(2));
            selectDateOption(DT_OPTION_2, aDateTime2.get(0), aDateTime2.get(1), aDateTime2.get(2));

            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy - HH:mm:ss");
            Date date = new Date();

            String appointmentDesc = String.format("Description: %s; Created at: %s\n" +
                    "First option: %s %s - %s\n" +
                    "Second option: %s %s - %s", desc, dateFormat.format(date),
                    aDateTime1.get(0), aDateTime1.get(1), aDateTime1.get(2),
                    aDateTime2.get(0), aDateTime2.get(1), aDateTime2.get(2));

            fieldDescription.sendKeys(appointmentDesc);

            return true;
        } catch (RuntimeException e)
        {
            return false;
        }
    }

    public boolean selectConsultation() {
        return selectAppointmentReason(CONSULTAION);
    }

    public boolean selectTreatmentProcedure(List<String> procedures)
    {
        if (!selectAppointmentReason(TREATMENT_PROCEDURE))
            return false;

        // TODO: error, waiting for this element too long!!!
        // FIXME: need to fix this behavior
        waitForElementDisplayed(selectProcedure, 15);

        for (String procedure: procedures) {
            new Select(selectProcedure).selectByVisibleText(procedure);
        }

        return true;
    }

    public boolean selectPostTreatment(int index)
    {
        if (!selectAppointmentReason(POST_TREATMENT))
            return false;

        // TODO: error, waiting for this element too long!!!
        // FIXME: need to fix this behavior
        waitForElementDisplayed(selectPrevAppointment, 15);
        List<WebElement> options = selectPrevAppointment.findElements(By.tagName("option"));

        if (index  < options.size())
            new Select(selectPrevAppointment).selectByIndex(index);
        else
            log.warning(String.format("Wrong index: %s! Elements in list: %s", index, options.size()));

        return true;
    }

    public boolean submitAppoinment()
    {
        if (btnSubmit.isEnabled())
            btnSubmit.click();
        else {
            log.severe("Button Submit not enabled!");
            return false;
        }
        // Wait for alert window until timeout
        int waitForSec = 0;
        while (!alertWnd.isDisplayed() && waitForSec <= WAIT_FOR_PAGE_LOAD_IN_SECONDS)
        {
            TimeUtils.waitForSeconds(1);
            waitForSec++;
        }

        if (alertWnd.isDisplayed()){
            String alertText = alertWnd.getText();
            log.info(String.format("Alert text: '%s'", alertText));
            return alertText.contains("Thank you for requesting appointment.");
        } else
        {
            log.info("Alert window not visible!");
            return false;
        }
    }

}
