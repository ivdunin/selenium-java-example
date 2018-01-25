package com.company.selenium.test.configuration;

import com.company.selenium.test.configuration.properties.PropertiesLoader;
import com.company.selenium.test.webtestsbase.Browser;
import com.company.selenium.test.configuration.properties.Property;
import com.company.selenium.test.configuration.properties.PropertyFile;

import java.util.Map;

/**
 * Created by Sidelnikov Mikhail on 18.09.14.
 * Class for base tests properties - urls for test, browser name and version
 */
@PropertyFile("config.properties")
public class TestsConfig {

    private static TestsConfig config;

    private void debugEnv() {
        Map<String, String> env = System.getenv();
        for (String envName : env.keySet()) {
            System.out.format("%s=%s%n",
                    envName,
                    env.get(envName));
        }

    }

    public static TestsConfig getConfig() {
        if (config == null) {
            config = new TestsConfig();
        }
        return config;
    }

    public TestsConfig() {
        PropertiesLoader.populate(this);
    }

    @Property("browser.name")
    private String browser = "firefox";

    @Property("browser.version")
    private String version = "";

    @Property("provider.invintation.code")
    private String providerCode = "";

    @Property("provider.name")
    private String providerName = "";

    @Property("provider.email")
    private String providerEmail = "";

    @Property("provider.password")
    private String providerPassword = "";

    @Property("default.password")
    private String defaultPassword = "";

    @Property("default.patient.name")
    private String defaultPatientName = "";

    @Property("default.patient.phone")
    private String defaultPatientPhone = "";

    @Property("default.at.patient.name")
    private String defaultAtPatientName = "";

    @Property("default.at.patient.email")
    private String defaultAtPatientEmail = "";

    @Property("geckodriver.path.linux")
    private String geckodriverLinux = "";

    @Property("geckodriver.path.win")
    private String geckodriverWin = "";

    @Property("base.url")
    private String baseUrl = "http://staging.company.com";

    /**
     * getting browser object
     * @return browser object
     */
    public Browser getBrowser() {
        Browser browserForTests = Browser.getByName(browser);
        if (browserForTests != null) {
            return browserForTests;
        } else {
            throw new IllegalStateException("Browser name '" + browser + "' is not valid");
        }
    }

    /**
     * getting browser version
     * @return browser version
     */
    public String getBrowserVersion() {
        return version;
    }

    /**
     * getting provider code
     * @return provider code
     */
    public String getProviderCode() { return providerCode; }

    /**
     * getting provide name
     * @return provider name
     */
    public String getProviderName() { return providerName; }

    /**
     * getting default password for accounts
     * @return default password
     */
    public String getDefaultPassword() { return defaultPassword; }

    /**
     * getting default patient name for tests
     * @return default name
     */
    public String getDefaultPatientName() { return defaultPatientName; }

    /**
     * getting default patient phone number
     * @return default phone number
     */
    public String getDefaultPatientPhone() { return defaultPatientPhone; }

    /**
     * getting default provider email
     * @return default email
     */
    public String getProviderEmail() { return providerEmail; }

    /**
     * getting default provider password
     * @return default password
     */
    public String getProviderPassword() { return providerPassword; }

    /**
     * getting default automation tests patient email
     * @return default email
     */
    public String getDefaultAtPatientEmail() { return defaultAtPatientEmail; }

    /**
     * getting default automation tests patient name
     * @return default name
     */
    public String getDefaultAtPatientName() { return defaultAtPatientName; }

    /**
     * Get linux geckodriver path
     * @return path to geckodriver
     */
    public String getGeckodriverLinux() { return geckodriverLinux; }

    /**
     * Get Windows geckodriver path
     * @return path to geckodriver
     */
    public String getGeckodriverWin() { return geckodriverWin; }

    /** Get Base URL for test
     * @return if BASE_URL variable set, return value of this variable, else baseUrl from properties.
     */
    public String getBaseUrl() {
        debugEnv();
        String envBaseUrl = System.getenv("BASE_URL");

        if (envBaseUrl != null && !envBaseUrl.isEmpty())
            return System.getenv("BASE_URL");
        else
            return baseUrl;
    }
}
