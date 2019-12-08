package main.java.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import java.util.List;

public class HomePage {
    WebDriver driver;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//a[text()='30 Day Free Trial']")
    WebElement freeTrail;
    @FindBy(xpath = "//input[@name='firstName']")
    WebElement firstName;
    @FindBy(xpath = "//input[@name='lastName']")
    WebElement lastName;
    @FindBy(xpath = "//input[@name='email']")
    WebElement email;
    @FindBy(xpath = "//button[@id='submit-signup-button']")
    WebElement signUpButton;
    @FindBy(xpath = "//p[@id='signup-error']")
    WebElement errorMessage;

    @FindBy(xpath = "//button[@aria-label='Back to Login']")
    WebElement backToLogin;
    @FindBy(xpath = "//button[@id='reset-password-button']")
    WebElement forgotPassword;
    @FindBy(xpath = "//input[@icon='email']")
    WebElement forgotEmail;

    @FindBy(xpath = "//button[@aria-label='Request password reset']")
    WebElement resetPasswordButton;

    @FindBy(xpath = "//div[contains(@class,'Password') and contains(@class,'Message')]//div")
    WebElement resetPasswordMessage;

    @FindBy(xpath = "//input[@id='username-field']")
    WebElement userName;

    @FindBy(xpath = "//input[@id='password-field']")
    WebElement password;

    @FindBy(xpath = "//button[@id='login-button']")
    WebElement loginButton;

    @FindBy(xpath = "//p[@id='auth-error-message']")
    WebElement loginErrorMessage;



    public WebElement getFreeTrail() {
        return freeTrail;
    }

    public WebElement getFirstName() {
        return firstName;
    }

    public WebElement getLastName() {
        return lastName;
    }

    public WebElement getEmail() {
        return email;
    }

    public WebElement getSignUpButton() {
        return signUpButton;
    }

    public WebElement getErrorMessage() {
        return errorMessage;
    }

    public WebElement getBackToLogin() {
        return backToLogin;
    }

    public WebElement getForgotPassword() {
        return forgotPassword;
    }

    public WebElement getForgotEmail() {
        return forgotEmail;
    }

    public WebElement getResetPasswordButton() {
        return resetPasswordButton;
    }

    public WebElement getResetPasswordMessage() {
        return resetPasswordMessage;
    }
    public WebElement getUserName() {
        return userName;
    }
    public WebElement getPassword() {
        return password;
    }
    public WebElement getLoginButton() {
        return loginButton;
    }
    public WebElement getLoginErrorMessage() {
        return loginErrorMessage;
    }

}
