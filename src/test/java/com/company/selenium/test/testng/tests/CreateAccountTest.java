package com.company.selenium.test.testng.tests;

import com.company.email.NotificationsReader;
import com.company.selenium.test.Consts;
import com.company.selenium.test.configuration.TestsConfig;
import com.company.selenium.test.pages.*;
import com.company.selenium.test.pages.patient.DashboardPage;
import com.company.selenium.test.testng.listeners.ScreenShotOnFailListener;
import com.company.selenium.test.utils.TimeUtils;
import com.company.selenium.test.webtestsbase.WebDriverFactory;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by dunin on 7/7/17.
 */
@Listeners({ScreenShotOnFailListener.class})
public class CreateAccountTest {
    private String providerInvintationCode = TestsConfig.getConfig().getProviderCode();
    private String providerName = TestsConfig.getConfig().getProviderName();
    private String defaultPassword = TestsConfig.getConfig().getDefaultPassword();
    private String defaultPatientName = TestsConfig.getConfig().getDefaultPatientName();
    private String defaultPhone = TestsConfig.getConfig().getDefaultPatientPhone();

    private String testMailAccount = "";

    @BeforeTest
    public void beforeTest() {
        WebDriverFactory.startBrowser(true);
    }

    /**
     * Open create account page for given provider and enter account data
     * @param test_email account email
     * @param full_name patient name
     * @param phone_number patient phone number
     * @param password patient password
     * @param subscribe subscribe to news
     * @return CreateAccountPage object
     */
    private CreateAccountPage checkAccountPage(String test_email, String full_name, String phone_number, String password, boolean subscribe) {
        ProviderInvintationCodePage ProviderPage = new ProviderInvintationCodePage();
        ProviderPage.clearProviderCode();
        ProviderPage.insertProviderCode(providerInvintationCode);
        ProviderPage.clickSubmit();
        Assert.assertTrue(ProviderPage.isNextBtnFound(), "Next Button not found!");
        Assert.assertTrue(ProviderPage.checkProvideName(providerName), "Incorrect provider name");
        CreateAccountPage AccountPage = ProviderPage.clickNext();
        AccountPage.fillAccountInfo(full_name, test_email,phone_number, password, subscribe);
        AccountPage.createAccount();
        return AccountPage;
    }

    /**
     * Create account with correct data
     * @throws InterruptedException
     * @throws IOException
     */
    @Test
    public void CreateAccount() throws InterruptedException, IOException
    {
        DateFormat dateFormat = new SimpleDateFormat("MMdd-HHmmss");
        Date date = new Date();

        String uniqueTimestamp = dateFormat.format(date);

        testMailAccount = String.format("at_patient_%s@company.com", uniqueTimestamp);

        InsertVerificationCodePage VerificationCodePage = checkAccountPage(
                testMailAccount,
                String.format("%s_%s", defaultPatientName, uniqueTimestamp),
                defaultPhone,
                defaultPassword,
                true).confirmClick();

        int tick = 0;
        int RECEIVE_TIMEOUT = 3;

        NotificationsReader nr = new NotificationsReader();
        String v_code = nr.getVerificationCode(testMailAccount);

        while (v_code.equals(Consts.NO_VERIFICATION_CODE_FOUND)) {
            TimeUtils.waitForSeconds(1);
            v_code = nr.getVerificationCode(testMailAccount);
            tick++;
            Assert.assertTrue(tick < RECEIVE_TIMEOUT, "No verification code received in " + RECEIVE_TIMEOUT + " seconds!");
        }

        VerificationCodePage.insertVerificationCode(v_code.trim());
        ConfirmationPage confirmationPage = VerificationCodePage.submitClick();
        Assert.assertTrue(confirmationPage.checkNameOnPage(defaultPatientName), "Incorrect name on page or name not found!");
        confirmationPage.nextClick();

        // Wait for welcome letter
        tick = 0;
        while (!nr.checkWelcomeLetter(testMailAccount) && tick <= 15) {
            TimeUtils.waitForSeconds(1);
            tick++;
        }

        Assert.assertTrue(nr.checkWelcomeLetter(testMailAccount), "No welcome letter for new patient!");
    }


    /**
     * Test login with new created patient
     * @throws InterruptedException
     * @throws IOException
     */
    @Test(dependsOnMethods = {"CreateAccount"})
    public void LoginNewPatient() throws InterruptedException, IOException
    {
        LoginPage page = new LoginPage();
        DashboardPage dashboard = (DashboardPage) page.doLogin(testMailAccount, defaultPassword, Consts.USER_TYPE_PATIENT);
        Assert.assertTrue(dashboard.isPageOpened(), "Dashboard not opened!");
        Assert.assertTrue(dashboard.checkWidgetsForNewPatient(), "Widgets not found!");
    }

    /**
     * Test that system not allows input incorrect provider code
     */
    @Test
    public void IncorrectProviderCode() {
        ProviderInvintationCodePage ProviderPage = new ProviderInvintationCodePage();
        ProviderPage.clearProviderCode();
        ProviderPage.insertProviderCode("999999");
        ProviderPage.clickSubmit();
        Assert.assertTrue(ProviderPage.isIncorrectProviderCode(), "No message about incorrect code");
    }

    /**
     * Test that system not allows create account without patient full name
     */
    @Test
    public void EmptyFullName() {
        CreateAccountPage AccountPage = checkAccountPage(
                "empty_full_name@company.com",
                "",
                defaultPhone,
                defaultPassword,
                true);
        Assert.assertTrue(AccountPage.isIncorrectFullName(), "No message for user about incorrect Full Name");
    }

    /**
     * Test that system not allows create account without email
     */
    @Test
    public void EmptyEmail() {
        String test_email = "";
        String full_name = "William Never";
        String phone_number = "1234567890";
        String password = "p@ssw0rd";

        CreateAccountPage AccountPage = checkAccountPage(
                "",
                "William Never",
                "1234567890",
                "0",
                true);
        Assert.assertTrue(AccountPage.isIncorrectEmail(), "No message for user about incorrect email");
    }

    /**
     * Test that system verify password length
     */
    @Test
    public void IncorrectPasswordLength() {
        CreateAccountPage AccountPage = checkAccountPage(
                "incorrect_password@company.com",
                defaultPatientName,
                defaultPhone,
                "0",
                true);
        Assert.assertTrue(AccountPage.isIncorrectPassword(), "No message for user about incorrect password");
    }

    /**
     * Test that password meets requirements (length >= 6, with at least one number and one letter)
     */
    @Test
    public void IncorrectPassword() {
        CreateAccountPage AccountPage = checkAccountPage(
                "incorrect_password@company.com",
                defaultPatientName,
                defaultPhone,
                "aaaaaa",
                true);
        Assert.assertTrue(AccountPage.isIncorrectPassword(), "No message for user about incorrect password");
    }

    @AfterTest
    public void afterTest() {
        WebDriverFactory.finishBrowser();
    }
}
