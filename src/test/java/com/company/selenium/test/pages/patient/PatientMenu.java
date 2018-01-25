package com.company.selenium.test.pages.patient;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

import java.util.logging.Logger;

public class PatientMenu {
    private WebDriver webDriver = null;
    private Logger log = Logger.getLogger(PatientMenu.class.getName());

    public PatientMenu(WebDriver driver)
    {
        webDriver = driver;
    }

    /**
     * Click on a menu item specified by xpath
      * @param dataMenuID value of div[@data-menu-id]
     * @param link value of corresponding a[@href]
     * @return True if element found and can click, False if not
     */
    private boolean menuItemClick(String dataMenuID, String link)
    {
        if (webDriver.getCurrentUrl().contains(link))
        {
            log.warning(String.format("Cannot open page: %s; because we already on a page: %s", link, webDriver.getCurrentUrl()));
            return true;
        }

        String XPATH_MENU_ITEM = "//div[@data-menu-id='%s']/a[@href='%s']";

        try {
            String xpath = String.format(XPATH_MENU_ITEM, dataMenuID, link);
            webDriver.findElement(By.xpath(xpath)).click();
            return true;
        } catch (NoSuchElementException e)
        {
            log.severe(String.format("[CURRENT_URL:%s] Cannot find menu item with attributes: [%s : %s]", webDriver.getCurrentUrl(), dataMenuID, link));
            log.severe(e.getMessage());
            return false;
        }
    }

    /**
     * Click on a Dashboard menu
     * @return DashboardPage page object
     */
    public DashboardPage clickDashboard()
    {
        try {
            if (menuItemClick("dashboard", "/patient-dashboard"))
                return new DashboardPage(false);
            else
                return null;
        } catch (AssertionError e)
        {
            log.severe(e.getMessage());
            return null;
        }
    }


    /**
     * Click on a CareTeam menu
     * @return CareTeamPage page object
     */
    public CareTeamPage clickCareTeam()
    {
        try {
            if (menuItemClick("your-care-team", "/patient-care-team"))
                return new CareTeamPage(false);
            else
                return null;
        } catch (AssertionError e)
        {
            log.severe(e.getMessage());
            return null;
        }
    }

    /**
     * Click on a Messages menu
     * @return PatientMessagesPage page object
     */
    public PatientMessagesPage clickMessages()
    {
        try {
            if (menuItemClick("messages", "/patient-messages/inbox"))
                return new PatientMessagesPage(false);
            else
                return null;
        } catch (AssertionError e)
        {
            log.severe(e.getMessage());
            return null;
        }
    }

    /**
     * Click on a Online Initial Consults
     * @return PatientMessagesPage page object
     */
    public OnlineInitialConsultsPage clickOnlineInitialConsults()
    {
        try {
            if (menuItemClick("online-initial-consult", "/patient-consults/history"))
                return new OnlineInitialConsultsPage(false);
            else
                return null;
        } catch (AssertionError e)
        {
            log.severe(e.getMessage());
            return null;
        }
    }

    /**
     * Click on a Online Follow Ups
     * @return OnlineFollowUpPage page object
     */
    public OnlineFollowUpPage clickOnlineFollowUps()
    {
        try {
            if (menuItemClick("follow-ups", "/patient-follow-ups/history"))
                return new OnlineFollowUpPage(false);
            else
                return null;
        } catch (AssertionError e)
        {
            log.severe(e.getMessage());
            return null;
        }
    }

    /**
     * Click on a Medical History
     * @return MedicalHistoryPage page object
     */
    public MedicalHistoryPage clickMedicalHistory()
    {
        try {
            if (menuItemClick("medical-history", "/medical-history"))
                return new MedicalHistoryPage(false);
            else
                return null;
        } catch (AssertionError e)
        {
            log.severe(e.getMessage());
            return null;
        }
    }

    /**
     * Click on appointment menu
     * @return AppointmentsPage page object
     */
    public AppointmentsPage clickAppointment()
    {
        try {
            if (menuItemClick("appointment", "/appointments"))
                return new AppointmentsPage(false);
            else
                return null;
        } catch (AssertionError e)
        {
            log.severe(e.getMessage());
            return null;
        }
    }

    /**
     * Click on a Forms
     * @return FormsPage page object
     */
    public FormsPage clickFormsPage()
    {
        try {
            if (menuItemClick("forms", "/patient-forms/index"))
                return new FormsPage(false);
            else
                return null;
        } catch (AssertionError e)
        {
            log.severe(e.getMessage());
            return null;
        }
    }

    /**
     * Click on a Photos
     * @return PhotosPage page object
     */
    public PhotosPage clickPhotosPage()
    {
        try {
            if (menuItemClick("photos", "/patient-photos"))
                return new PhotosPage(false);
            else
                return null;
        } catch (AssertionError e)
        {
            log.severe(e.getMessage());
            return null;
        }
    }

    /**
     * Click on a Financial History
     * @return FinancialHistoryPage page object
     */
    public FinancialHistoryPage clickFinancialHistory()
    {
        try {
            if (menuItemClick("payments", "/financial-history"))
                return new FinancialHistoryPage(false);
            else
                return null;
        } catch (AssertionError e)
        {
            log.severe(e.getMessage());
            return null;
        }
    }

    /**
     * Click on a Online Store
     * @return OnlineStorePage page object
     */
    public OnlineStorePage clickOnlineStore()
    {
        try {
            if (menuItemClick("online-store", "/store"))
                return new OnlineStorePage(false);
            else
                return null;
        } catch (AssertionError e)
        {
            log.severe(e.getMessage());
            return null;
        }
    }
}
