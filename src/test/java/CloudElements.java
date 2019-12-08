package test.java;

import main.java.framework.Framework;
import main.java.pageobjects.HomePage;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class CloudElements extends Framework {
    HomePage homePage;

    @Test(priority = 0)
    @Parameters({"firstName", "lastName", "emailId"})
    public void signUP(String firstName, String lastName, String emailId) throws InterruptedException {
        waitForPageLoad();
        homePage = new HomePage(driver);
        clickElement(homePage.getFreeTrail());
        setValue(homePage.getFirstName(), firstName);
        setValue(homePage.getLastName(), lastName);
        setValue(homePage.getEmail(), emailId);
        clickElement(homePage.getSignUpButton());
        waitForPageLoad();
        Thread.sleep(5000);
        waitUntilElementPresent(homePage.getErrorMessage());
        if (homePage.getErrorMessage().getText().equalsIgnoreCase("Invalid email. Please use a company email, personal emails such as gmail are not accepted."))
            reportPass("Verify the Sign Up Error Message ", " Error Message should be displayed ", " Error Message verified successfully ");
        else
            reportFail("Verify the Sign Up Error Message ", " Error Message should be displayed ", " Error Message not verified " + homePage.getErrorMessage().getText());

    }

    @Test(priority = 1)
    @Parameters({"emailId", "password"})
    public void loginFieldValidation(String emailId, String password) throws InterruptedException {
        waitForPageLoad();
        //homePage.getFreeTrail().click();
        clickElement(homePage.getBackToLogin());
        waitForPageLoad();
        clickElement(homePage.getLoginButton());
        waitForPageLoad();
        Thread.sleep(2000);
        waitUntilElementPresent(homePage.getLoginErrorMessage());
        if (homePage.getLoginErrorMessage().getText().equalsIgnoreCase("Invalid username or password"))
            reportPass("Verify the Login Error Message ", " Login Error Message should be displayed ", " Login Error Message  verified successfully ");
        else
            reportFail("Verify the Login Error Message ", " Login Error Message should be displayed ", " Login Error Message  not verified " + homePage.getLoginErrorMessage().getText());
        setValue(homePage.getUserName(), emailId);
        waitForPageLoad();
        clickElement(homePage.getLoginButton());
        waitForPageLoad();
        Thread.sleep(2000);
        waitUntilElementPresent(homePage.getLoginErrorMessage());
        if (homePage.getLoginErrorMessage().getText().equalsIgnoreCase("Invalid username or password"))
            reportPass("Verify the Login Error Message ", " Login Error Message should be displayed ", " Login Error Message  verified successfully ");
        else
            reportFail("Verify the Login Error Message ", " Login Error Message should be displayed ", " Login Error Message  not verified " + homePage.getLoginErrorMessage().getText());
        setValue(homePage.getPassword(), password);
        waitForPageLoad();
        clickElement(homePage.getLoginButton());
        waitForPageLoad();
        Thread.sleep(2000);
        waitUntilElementPresent(homePage.getLoginErrorMessage());
        if (homePage.getLoginErrorMessage().getText().equalsIgnoreCase("Invalid username or password"))
            reportPass("Verify the Login Error Message ", " Login Error Message should be displayed ", " Login Error Message  verified successfully ");
        else
            reportFail("Verify the Login Error Message ", " Login Error Message should be displayed ", " Login Error Message  not verified " + homePage.getLoginErrorMessage().getText());

    }


    @Test(priority = 2)
    @Parameters({"emailId"})
    public void resetPassword(String emailId) throws InterruptedException {
        waitForPageLoad();
        //homePage.getFreeTrail().click();
//        homePage.getBackToLogin().click();
//        Thread.sleep(5000);
        clickElement(homePage.getForgotPassword());
        waitForPageLoad();
        setValue(homePage.getForgotEmail(), emailId);
        clickElement(homePage.getResetPasswordButton());
        waitForPageLoad();
        Thread.sleep(2000);
        waitUntilElementPresent(homePage.getResetPasswordMessage());
        if (homePage.getResetPasswordMessage().getText().equalsIgnoreCase("Thanks! Please check your email for your password reset link"))
            reportPass("Verify the Reset Password Message ", " Reset Password Message should be displayed ", " Reset Password Message verified successfully ");
        else
            reportFail("Verify the Reset Password Message ", " Reset Password Message should be displayed ", " Reset Password Message  not verified " + homePage.getResetPasswordMessage().getText());

    }

}
