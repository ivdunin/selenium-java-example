# Company Selenium Tests

## Run tests from command line
`cd rm/at && mvn clean compiler:compile compiler:testCompile resources:resources resources:testResources surefire:test`

## Project Structure

```
.
└── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── company
    │   │           ├── email (Notification Readers class)
    │   │           └── selenium (Test Framework)
    │   │               └── test
    │   │                   ├── configuration
    │   │                   │   └── properties
    │   │                   ├── exceptions
    │   │                   ├── utils
    │   │                   └── webtestsbase
    │   └── resources (Mailbox Properties)
    └── test
        ├── java
        │   └── com
        │       └── company
        │           └── selenium
        │               └── test
        │                   ├── pages (Page Objects classes)
        │                   │   ├── md (MD Page Object)
        │                   │   └── patient (Patient Page Objects)
        │                   └── testng
        │                       ├── listeners
        │                       └── tests (Tests)
        └── resources (Test properties)
```
## Add new tests 
After adding new test classes to tests directory (`rm/at/src/test/java/com/company/selenium/test/testng/tests`), you should also add description to `testng.xml` file.

### Current testng.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd">
<suite name="Company Test Suite" verbose="1" >
  <test name="Login Test">
    <classes>
      <class name="com.company.selenium.test.testng.tests.LoginTest" />
    </classes>
  </test>
  <test name="Create Account Test">
    <classes>
      <class name="com.company.selenium.test.testng.tests.CreateAccountTest" />
    </classes>
  </test>
  <test name="Patient Smoke Test">
    <classes>
      <class name="com.company.selenium.test.testng.tests.PatientSmokeTest" />
    </classes>
  </test>
  <test name="Initial Consult Appointments">
    <classes>
      <class name="com.company.selenium.test.testng.tests.InitialConsultAppointments" />
    </classes>
  </test>
  <test name="Procedure Treatments Appointments">
    <classes>
      <class name="com.company.selenium.test.testng.tests.ProcedureTreatmentsAppointments" />
    </classes>
  </test>
  <test name="Follow Up Appointments">
    <classes>
      <class name="com.company.selenium.test.testng.tests.FollowUpAppointments" />
    </classes>
  </test>
</suite>
```

## Properties files
Store specific properties for every test run

### mailbox.properties
File path: `rm/at/src/main/resources/mailbox.properties`
Properties for class `com.company.email.NotificationsReader`.
Credentials for GMail mailbox. Mailbox and sender name.

### config.properties
File path: `rm/at/src/test/resources/config.properties`
Properties for Tests and Selenium Framework.

E.g.
```properties
# Browser Settings
browser.name=firefox
geckodriver.path.linux=/usr/local/sbin/geckodriver
geckodriver.path.win=geckodriver.exe

# Test Settings
provider.invintation.code=91696
provider.name=John Smith, MD
provider.email=victor@mail.com
provider.password=test12345
default.password=test12345
default.patient.name=William Never
default.patient.phone=1112223333
default.at.patient.name=ATSelenium User
default.at.patient.email=at_selenium_patient@company.com
```
