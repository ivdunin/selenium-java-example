package com.company.selenium.test.testng.tests;

import com.company.selenium.test.Consts;
import com.company.selenium.test.configuration.TestsConfig;
import com.company.selenium.test.webtestsbase.BasePage;
import com.company.selenium.test.webtestsbase.WebDriverFactory;
import com.company.selenium.test.pages.LoginPage;
import com.company.selenium.test.pages.patient.DashboardPage;
import com.company.selenium.test.testng.listeners.ScreenShotOnFailListener;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.logging.Logger;

@Listeners({ScreenShotOnFailListener.class})
public class PatientSmokeTest {
    private BasePage currentPage;
    private Logger log = Logger.getLogger(PatientSmokeTest.class.getName());


    @BeforeTest
    public void beforeTest() {
        WebDriverFactory.startBrowser(true);

        String username = TestsConfig.getConfig().getDefaultAtPatientEmail();
        String password = TestsConfig.getConfig().getDefaultPassword();

        LoginPage page = new LoginPage();
        currentPage = page.doLogin(username, password, Consts.USER_TYPE_PATIENT);
    }

    @Test
    public void SmokeTest()
    {
        boolean isErrors = false;

        if (null == ((DashboardPage)currentPage).getMenu().clickDashboard()) {
            log.severe("Cannot open Dashboard Page");
            isErrors = true;
        }

        if (null == ((DashboardPage)currentPage).getMenu().clickCareTeam()) {
            log.severe("Cannot open Care Team Page");
            isErrors = true;
        }

        if (null == ((DashboardPage)currentPage).getMenu().clickMessages()) {
            log.severe("Cannot open Messages Page");
            isErrors = true;
        }

        if (null == ((DashboardPage)currentPage).getMenu().clickOnlineInitialConsults()) {
            log.severe("Cannot open Online Initial Consults Page");
            isErrors = true;
        }

        if (null == ((DashboardPage)currentPage).getMenu().clickOnlineFollowUps()) {
            log.severe("Cannot open Online Follow Ups Page");
            isErrors = true;
        }

        if (null == ((DashboardPage)currentPage).getMenu().clickMedicalHistory()) {
            log.severe("Cannot open Medical History Page");
            isErrors = true;
        }

        if (null == ((DashboardPage)currentPage).getMenu().clickAppointment()) {
            log.severe("Cannot open Appointment Page");
            isErrors = true;
        }

        if (null == ((DashboardPage)currentPage).getMenu().clickFormsPage()) {
            log.severe("Cannot open Forms Page");
            isErrors = true;
        }

        if (null == ((DashboardPage)currentPage).getMenu().clickPhotosPage()) {
            log.severe("Cannot open Photos Page");
            isErrors = true;
        }

        if (null == ((DashboardPage)currentPage).getMenu().clickFinancialHistory()) {
            log.severe("Cannot open Financial History Page");
            isErrors = true;
        }

        if (null == ((DashboardPage)currentPage).getMenu().clickOnlineStore()) {
            log.severe("Cannot open Online Store Page");
            isErrors = true;
        }

        Assert.assertFalse(isErrors, "Something wrong with patient portal!");
    }

    @AfterTest
    public void afterTest() {
        WebDriverFactory.finishBrowser();
    }

}
