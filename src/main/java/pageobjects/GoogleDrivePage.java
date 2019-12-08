package main.java.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.Factory;

import java.util.List;

public class GoogleDrivePage {

    WebDriver driver;

    public GoogleDrivePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//input[@type='email']")
    WebElement email;
    @FindBy(xpath = "//span[text()='Next']")
    WebElement nextButton;
    @FindBy(xpath = "//input[@type='password']")
    WebElement password;
    @FindBy(xpath = "//div[@draggable='true']//div[@data-tooltip-unhoverable='true']")
    List<WebElement> filesDetails;

    @FindBy(xpath = "//button[@aria-label='New' and not(@data-tooltip)]")
    WebElement newButton;
    @FindBy(xpath = "//div[text()='File upload']")
    WebElement fileUpload;
    @FindBy(xpath = "//div[contains(@aria-label,'upload complete')]")
    WebElement verifyUpload;
    @FindBy(xpath = "//div[@data-target='taskContainer']//span[@data-tooltip and @data-tooltip-only-on-overflow='true']")
    WebElement verifyFile;
    @FindBy(xpath = "//div[@data-tooltip='Close' and not(@data-tooltip-class)]")
    WebElement closeButton;
    @FindBy(xpath = "//div[@role='option']")
    WebElement downloadFile;

    @FindBy(xpath = "//div[@data-tooltip-only-on-overflow='true']")
    WebElement downloadFileName;
    //div[@data-target='taskContainer']//span[@data-tooltip and @data-tooltip-only-on-overflow="true"]


    //Uploading 1 item
    //span[text()='1 upload complete']
    public WebElement getEmail() {
        return email;
    }

    public WebElement getNextButton() {
        return nextButton;
    }

    public WebElement getPassword() {
        return password;
    }

    public List<WebElement> getFilesDetails() {
        return filesDetails;
    }

    public WebElement getNewButton() {
        return newButton;
    }

    public WebElement getFileUpload() {
        return fileUpload;
    }

    public WebElement getVerifyFile() {
        return verifyFile;
    }

    public WebElement getVerifyUpload() {
        return verifyUpload;
    }

    public WebElement getCloseButton() {
        return closeButton;
    }

    public WebElement getDownloadFile() {
        return downloadFile;
    }

    public WebElement getDownloadFileName() {
        return downloadFileName;
    }


}
