package main.java.framework;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.annotations.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class Framework {

    public static ExtentHtmlReporter htmlReporter;
    public static ExtentReports extent;
    public static ExtentTest test, testParent;
    public static String reportPath, screenshotFolder;
    public static WebDriver driver;

    @BeforeSuite
    public void beforeSuite() {
        reportPath = System.getProperty("user.dir") + "\\Reports\\" + "ExecutionReport_"
                + new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date());
        new File(reportPath).mkdir();
        screenshotFolder = reportPath + "\\ScreenShots";
        new File(screenshotFolder).mkdir();
        htmlReporter = new ExtentHtmlReporter(reportPath + "//" + "AutomationReport.html");
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
        htmlReporter.config().setDocumentTitle("Automation Report");
        htmlReporter.config().setReportName("Automation Report");
        htmlReporter.config().setTheme(Theme.STANDARD);
        initializeDriver();
        Properties prop = new Properties();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(System.getProperty("user.dir") + "\\resources\\config.properties");
            prop.load(fis);
        } catch (Exception e) {
            e.printStackTrace();
        }
        driver.get(prop.getProperty("URL"));
    }

    @BeforeTest
    public void beforeTest(ITestContext testContext) {
        testParent = extent.createTest(testContext.getName());
    }

    @BeforeMethod
    public void getMethodName(Method method) {
        test = testParent.createNode(method.getName());
        System.out.println("=======Test Execution Starts: " + method.getName());
    }

    @AfterMethod
    public void afterMethod(Method method) {
        extent.flush();

    }

    @AfterSuite
    public void afterSuit() {
        extent.flush();
        driver.close();
        driver = null;
    }

    public WebDriver initializeDriver() {
        String browserName;
        Properties prop = new Properties();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(System.getProperty("user.dir") + "\\resources\\config.properties");
            prop.load(fis);
            browserName = prop.getProperty("browser");
            if (browserName.equalsIgnoreCase("chrome")) {
                System.setProperty("webdriver.chrome.driver",
                        System.getProperty("user.dir") + "\\resources\\drivers\\chromedriver.exe");
                String downloadFilepath = System.getProperty("user.dir") + "\\resources\\downloads";
                HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
                chromePrefs.put("profile.default_content_settings.popups", 0);
                chromePrefs.put("download.default_directory", downloadFilepath);
                ChromeOptions options = new ChromeOptions();
                options.setExperimentalOption("prefs", chromePrefs);
                driver = new ChromeDriver(options);
            }
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return driver;
    }

    public static void reportPass(String strStep, String strExpected, String strActual) {
        reportAll("Pass", strStep, strExpected, strActual, "");
        // System.out.println(strActual);
    }

    public static void reportFail(String strStep, String strExpected, String strActual) {
        reportAll("Fail", strStep, strExpected, "", strActual);
        // System.out.println(strActual);
    }

    public static void reportAll(String strPassFail, String strStep, String strExpected, String strActualPass, String strActualFail) {
        String strScreenShotFile = null;
        String timeStamp = null, strTimeStamp;
        timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        strTimeStamp = timeStamp.replace(".", "_");
        String sFileName = "Screenshot_" + strTimeStamp + ".jpg";
        strScreenShotFile = screenshotFolder + "\\" + sFileName;
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(scrFile, new File(strScreenShotFile));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("***" + strPassFail + "*** - " + strStep + "|" + strExpected + "|" + strActualPass);

        try {
            if (strPassFail.equalsIgnoreCase("Pass")) {
                test.pass(strStep + "|" + strExpected + "|" + strActualPass + " ", MediaEntityBuilder.createScreenCaptureFromPath("ScreenShots\\" + sFileName).build());
            } else if (strPassFail.equalsIgnoreCase("Fail")) {
                test.fail(strStep + "|" + strExpected + "|" + strActualFail, MediaEntityBuilder.createScreenCaptureFromPath("ScreenShots\\" + sFileName).build());
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static boolean waitForPageLoad() {
        WebDriverWait wait = new WebDriverWait(driver, 50);

        // wait for jQuery to load
        ExpectedCondition<Boolean> jQueryLoad = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    return ((Long) ((JavascriptExecutor) driver).executeScript("return jQuery.active") == 0);
                } catch (Exception e) {
                    // no jQuery present
                    return true;
                }
            }
        };

        // wait for Javascript to load
        ExpectedCondition<Boolean> jsLoad = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return ((JavascriptExecutor) driver).executeScript("return document.readyState")
                        .toString().equals("complete");
            }
        };

        return wait.until(jQueryLoad) && wait.until(jsLoad);
    }

    public void clickElement(WebElement webElement) {

        waitForPageLoad();
        waitUntilElementTobeClickable(webElement);
        if (webElement.isEnabled()) {
            try {
                try {
                    webElement.click();
                } catch (Exception e) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", webElement);
                }
            } catch (Exception e) {
                System.out.println("Element Not Clickable:" + e.toString());
            }
        }
    }

    public void setValue(WebElement objElement, String strValue) {
        waitForPageLoad();
        if (!waitUntilElementPresent(objElement)) {
            System.out.println("Element Not Found:" + objElement);
            return;
        }
        if (objElement.isEnabled()) {
            objElement.clear();
            objElement.sendKeys(strValue);

        } else {
            System.out.println("Failed to Set value in Element : '" + objElement + "' : Element is disabled");
        }
    }

    public boolean waitUntilElementPresent(final WebElement webElement) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement element = wait.until(ExpectedConditions.visibilityOf(webElement));
        if (element != null) {
            return true;
        }
        return false;
    }

    public void waitUntilElementTobeClickable(WebElement webElement) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(webElement));
    }


}
