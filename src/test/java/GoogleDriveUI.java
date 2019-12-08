package test.java;

import main.java.framework.Framework;
import main.java.pageobjects.GoogleDrivePage;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.apache.commons.codec.binary.Base64;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class GoogleDriveUI extends Framework {

    GoogleDrivePage googleDrivePage;

    @Test(priority = 0)
    @Parameters({"emailId", "password", "url"})
    public void loginGoogle(String emailId, String password, String url) throws InterruptedException {
        driver.get(url);
        googleDrivePage = new GoogleDrivePage(driver);
        waitForPageLoad();
        setValue(googleDrivePage.getEmail(), emailId);
        clickElement(googleDrivePage.getNextButton());
        Thread.sleep(2000);
        waitForPageLoad();
        setValue(googleDrivePage.getPassword(), new String(Base64.decodeBase64(password)));
        clickElement(googleDrivePage.getNextButton());
        waitForPageLoad();
        Thread.sleep(5000);
        waitUntilElementPresent(googleDrivePage.getNewButton());
        if (driver.getTitle().equals("My Drive - Google Drive"))
            reportPass("Verify the Google Drive Page Title ", " Title should be verified ", "  Title  verified successfully ");
        else
            reportFail("Verify the Google Drive Page Title ", " Title should be verified ", "  Title not verified " + driver.getTitle());

    }

    @Test(priority = 1)
    public void getFilesGDrive() throws InterruptedException {
        waitForPageLoad();
        List<WebElement> elementList = googleDrivePage.getFilesDetails();
        for (int i = 0; i < elementList.size(); i++) {
            reportPass("Verify the Google Drive file", " Files should be displayed  ", elementList.get(i).getAttribute("data-tooltip") + " file is displayed ");
            if (!elementList.get(i).getAttribute("data-tooltip").contains(".")) {
                Actions actions = new Actions(driver);
                actions.doubleClick(elementList.get(i)).build().perform();
                waitForPageLoad();
                Thread.sleep(2000);
                reportPass("Verify the navigation", " user should be navigate between folders  ", "Navigated between folders successfully ");
                driver.navigate().back();
                Thread.sleep(1000);
                waitForPageLoad();
                driver.navigate().refresh();
                waitForPageLoad();
                Thread.sleep(2000);
                elementList = googleDrivePage.getFilesDetails();
            }
        }
    }

    @Test(priority = 3)
    @Parameters({"file"})
    public void uploadFilesGDrive(String file) throws InterruptedException {
        waitForPageLoad();
        clickElement(googleDrivePage.getNewButton());
        clickElement(googleDrivePage.getFileUpload());
        Thread.sleep(5000);
        String strPath = System.getProperty("user.dir") + "\\resources\\" + file;
        strPath = "\"" + strPath + "\"";
        String autoITExecutable = System.getProperty("user.dir") + "\\resources\\drivers\\AutoItFileUpload.exe " + strPath;

        try {
            Runtime.getRuntime().exec(autoITExecutable);
        } catch (IOException e) {
            e.printStackTrace();
        }
        waitForPageLoad();
        Thread.sleep(10000);
        waitUntilElementPresent(googleDrivePage.getVerifyUpload());
        if (googleDrivePage.getVerifyFile().getAttribute("data-tooltip").equals(file))
            reportPass("Verify the upload file", " Files should be uploaded  ", file + " file is uploaded ");
        else reportFail("Verify the upload file", " Files should be uploaded  ", file + " file is not uploaded ");
        //clickElement(googleDrivePage.getCloseButton());
    }

    @Test(priority = 2)
    public void downloadFilesGDrive() throws InterruptedException, IOException {
        FileUtils.cleanDirectory(new File(System.getProperty("user.dir") + "//resources//downloads"));
        waitForPageLoad();
        Thread.sleep(3000);
        String fileName = googleDrivePage.getDownloadFileName().getAttribute("data-tooltip");
        Actions actions = new Actions(driver);
        actions.contextClick(googleDrivePage.getDownloadFile()).build().perform();
        for (int i = 0; i < 8; i++) {
            Thread.sleep(500);
            actions.sendKeys(Keys.ARROW_DOWN).build().perform();
        }
        Thread.sleep(1000);
        actions.sendKeys(Keys.ENTER).build().perform();
        Thread.sleep(6000);
        waitForPageLoad();
        File file = new File(System.getProperty("user.dir") + "//resources//downloads");
        for (File f : file.listFiles()) {
            if (f.getName().equals(fileName))
                reportPass("Verify the downloaded file ", " File should be downloaded ", fileName + "File downloaded successfully ");
            else
                reportPass("Verify the downloaded file ", " File should be downloaded ", fileName + "File not downloaded");

        }
    }
}
