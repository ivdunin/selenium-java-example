package com.company.selenium.test;

import com.company.selenium.test.configuration.TestsConfig;

/**
 * Created by dunin on 7/7/17.
 */
public final class Consts {
    public static final String BASE_URL = TestsConfig.getConfig().getBaseUrl();

    // Common URLs
    public static final String INSERT_PROVIDER_CODE_PAGE = BASE_URL + "/patient-portal";
    public static final String CREATE_ACCOUNT_PAGE = BASE_URL +  "/patient-portal-2";
    public static final String INSERT_VERIFICATION_CODE_PAGE = BASE_URL +  "/patient-portal-3";
    public static final String CONFIRM_ACCOUNT_PAGE = BASE_URL + "/patient-portal-4";
    public static final String LOGIN_PAGE = BASE_URL + "/user-login";

    // Patient Portal URLs
    public static final String DASHBOARD = BASE_URL + "/patient-dashboard";
    public static final String APPOINTMENTS = BASE_URL + "/appointments";
    public static final String CARE_TEAM = BASE_URL + "/patient-care-team";
    public static final String MESSAGES = BASE_URL + "/patient-messages/inbox";
    public static final String ONLINE_INITIAL_CONSULTS = BASE_URL + "/patient-consults/history";
    public static final String ONLINE_FOLLOW_UPS = BASE_URL + "/patient-follow-ups/history";
    public static final String MEDICAL_HISTORY = BASE_URL + "/medical-history";
    public static final String FORMS = BASE_URL + "/patient-forms/completed";
    public static final String PHOTOS = BASE_URL + "/patient-photos";
    public static final String FINANCIAL_HISTORY = BASE_URL + "/financial-history";
    public static final String ONLINE_STORE = BASE_URL + "/store";

    // MD Portal URLs
    public static final String MD_DASHBOARD = BASE_URL + "/md-dashboard";

    // Other constants
    public static final String NO_VERIFICATION_CODE_FOUND = "NO_VERIFICATION_CODE_FOUND";
    public static final String NO_PASSWORD_FOUND = "NO_PASSWORD_FOUND";

    public static final int USER_TYPE_PATIENT = 1;
    public static final int USER_TYPE_MD = 2;

    public static String APP_TYPE_INITIAL_CONSULT = "Initial Consult";
    public static String APP_TYPE_FOLLOW_UP = "Follow up";
    public static String APP_TYPE_TREATMENT = "Procedure treatment";



    private Consts() { throw new AssertionError(); }
}
