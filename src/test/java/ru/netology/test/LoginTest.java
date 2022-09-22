package ru.netology.test;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.netology.dataHelper.DataHelper;
import ru.netology.dataHelper.SQLHelper;
import ru.netology.page.DashBoard;
import ru.netology.page.Login;
import ru.netology.page.UserData;
import ru.netology.page.VerificationPage;

import static com.codeborne.selenide.Selenide.open;

public class LoginTest {

    UserData userData;
    Login login;

    @BeforeEach
    public void setup() {
        open("http://localhost:9999/");
        userData = DataHelper.validUser();
        login = new Login();
    }

    @AfterEach
    public void after() {
        SQLHelper.resetStatus(userData.getName());
        SQLHelper.resetVerifyCode();
    }

    @AfterAll
    public static void afterAll(){
        SQLHelper.resetBase();
    }

    @Test
    public void validData() {
        login.input(userData.getName(), userData.getPassword());
        VerificationPage verification = new VerificationPage();
        verification.input(DataHelper.validVerifyCode(userData.getName()));
        DashBoard dashboard = new DashBoard();
        dashboard.visiblePage();
    }

    @Test
    public void emptyData() {
        login.input(null, null);
        login.emptyData();
    }

    @Test
    public void emptyDataLogin() {
        login.input(null, userData.getPassword());
        login.emptyLogin();
    }

    @Test
    public void emptyDataPassword() {
        login.input(userData.getName(), null);
        login.emptyPassword();
    }

    @Test
    public void wrongLogin() {
        login.input("Victor", userData.getPassword());
        login.failedInputData();
    }

    @Test
    public void blockedUser() {
        login.input(userData.getName(), DataHelper.randomPass());
        login.failedInputData();
        login.input(userData.getName(), DataHelper.randomPass());
        login.failedInputData();
        login.input(userData.getName(), DataHelper.randomPass());
        login.failedInputData();
        login.input(userData.getName(), DataHelper.randomPass());
        login.failedInputData();
        DataHelper.assertStatus(userData.getName());
    }

    @Test
    public void emptyVerifyCode() {
        login.input(userData.getName(), userData.getPassword());
        VerificationPage verification = new VerificationPage();
        verification.input(null);
        verification.emptyField();
    }

    @Test
    public void wrongVerifyCode() {
        login.input(userData.getName(), userData.getPassword());
        VerificationPage verification = new VerificationPage();
        verification.input(DataHelper.invalidVerifyCode());
        verification.failedCode();
    }
}
