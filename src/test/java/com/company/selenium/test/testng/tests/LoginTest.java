package com.company.selenium.test.testng.tests;

import com.company.selenium.test.Consts;
import com.company.selenium.test.configuration.TestsConfig;
import com.company.selenium.test.webtestsbase.WebDriverFactory;
import com.company.selenium.test.pages.LoginPage;
import com.company.selenium.test.pages.md.MDDashboardPage;
import com.company.selenium.test.pages.patient.DashboardPage;
import com.company.selenium.test.testng.listeners.ScreenShotOnFailListener;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.io.IOException;

/**
 * Created by dunin on 7/9/17.
 */
@Listeners({ScreenShotOnFailListener.class})
public class LoginTest {

    @BeforeTest
    public void beforeTest() {
        WebDriverFactory.startBrowser(true);
    }

    @Test
    public void PositiveLoginMD() throws InterruptedException, IOException
    {
        LoginPage page = new LoginPage();
        MDDashboardPage dashboard = (MDDashboardPage) page.doLogin(
                TestsConfig.getConfig().getProviderEmail(),
                TestsConfig.getConfig().getProviderPassword(),
                Consts.USER_TYPE_MD);
        Assert.assertTrue(dashboard.isPageOpened(), "Dashboard not opened!");
    }

    /**
     * Test login with new created patient
     * @throws InterruptedException
     * @throws IOException
     */
    @Test
    public void PositiveLoginPatient() throws InterruptedException, IOException
    {
        String username = TestsConfig.getConfig().getDefaultAtPatientEmail();
        String password = TestsConfig.getConfig().getDefaultPassword();

        LoginPage page = new LoginPage();
        DashboardPage dashboard = (DashboardPage) page.doLogin(
                username,
                password,
                Consts.USER_TYPE_PATIENT);
        Assert.assertTrue(dashboard.isPageOpened(), "Dashboard not opened!");
    }

    @AfterTest
    public void afterTest() {
        WebDriverFactory.finishBrowser();
    }
}
