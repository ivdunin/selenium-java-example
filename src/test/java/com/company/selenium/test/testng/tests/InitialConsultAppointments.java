package com.company.selenium.test.testng.tests;

import com.company.email.NotificationsReader;
import com.company.selenium.test.Consts;
import com.company.selenium.test.configuration.TestsConfig;
import com.company.selenium.test.pages.LoginPage;
import com.company.selenium.test.pages.patient.AppointmentsPage;
import com.company.selenium.test.pages.patient.DashboardPage;
import com.company.selenium.test.testng.listeners.ScreenShotOnFailListener;
import com.company.selenium.test.utils.TimeUtils;
import com.company.selenium.test.webtestsbase.WebDriverFactory;
import com.company.selenium.test.pages.md.MDDashboardPage;
import org.joda.time.DateTime;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Listeners({ScreenShotOnFailListener.class})
public class InitialConsultAppointments {
    // Create all appointments on a next week
    private int minDays = 7;
    private int maxDays = minDays + 7;

    /**
     * Generate appointment date
     * @return date in format MM/dd/yyyy
     */
    private String getAppointmentDate()
    {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        DateTime dt = new DateTime().plusDays(ThreadLocalRandom.current().nextInt(minDays, maxDays+ 1));
        return dateFormat.format(dt.toDate());
    }

    @BeforeTest
    public void beforeTest() {
        WebDriverFactory.startBrowser(true);
    }

    @Test()
    public void createAppointmentConsultation()
    {
        String username = TestsConfig.getConfig().getDefaultAtPatientEmail();
        String password = TestsConfig.getConfig().getDefaultPassword();

        List<String> dtOption1 = Arrays.asList(getAppointmentDate(), "8:15 am", "9:00 am");
        List<String> dtOption2 = Arrays.asList(getAppointmentDate(), "9:15 am", "10:00 am");

        LoginPage page = new LoginPage();
        DashboardPage dashboard = (DashboardPage) page.doLogin(username, password, Consts.USER_TYPE_PATIENT);

        AppointmentsPage appointmentsPage = dashboard.getMenu().clickAppointment();
        appointmentsPage.newAppointments();

        boolean result = appointmentsPage.fillInAppointmentForm(
                "John Smith",
                "1040 Noel Dr., Suite 203, Menlo Park",
                dtOption1,
                dtOption2,
                "AT: Appointment description (Consultation)");
        Assert.assertTrue(result, "Cannot fill appointment form!");
        Assert.assertTrue(appointmentsPage.selectConsultation(), "Cannot choose appointment reason");
        Assert.assertTrue(appointmentsPage.submitAppoinment(), "Appointment not created or alert have wrong text!");
    }

    @Test(dependsOnMethods = {"createAppointmentConsultation"})
    public void confirmAppointmentConsultation()
    {
        String username = TestsConfig.getConfig().getProviderEmail();
        String password = TestsConfig.getConfig().getDefaultPassword();

        LoginPage page = new LoginPage();
        MDDashboardPage dashboard = (MDDashboardPage) page.doLogin(username, password, Consts.USER_TYPE_MD);
        boolean result = dashboard.confirmAppointment(Consts.APP_TYPE_INITIAL_CONSULT,
                TestsConfig.getConfig().getDefaultAtPatientName(),
                "Bellafill (Filler)",
                "1 hour");
        Assert.assertTrue(result, "Cannot confirm appointment!");
    }

    @Test(dependsOnMethods = {"confirmAppointmentConsultation"})
    public void checkPrivateLetterNotification1()
    {
        String username = TestsConfig.getConfig().getDefaultAtPatientEmail();

        NotificationsReader nr = new NotificationsReader();
        // Wait for private letter
        int tick = 0;
        while (!nr.checkPrivateMessage(username) && tick <= 15) {
            TimeUtils.waitForSeconds(1);
            tick++;
        }

        Assert.assertTrue(nr.checkPrivateMessage(username), "No new private letter about appointment!");
    }

    @AfterTest
    public void afterTest() {
        WebDriverFactory.finishBrowser();
    }
}
