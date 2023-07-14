package ru.netology.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.DashBoardPage;
import ru.netology.page.LoginPage;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.DataHelper.*;

public class MoneyTransferTest {

    DashBoardPage dashBoardPage;

    @BeforeEach
    void setup() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCode();
        dashBoardPage = verificationPage.validVerify(verificationCode);
    }

    @Test
    void shouldTransferFromFirstToSecond() {
        var firstCardInfo = getFirstCard();
        var secondCardInfo = getSecondCard();
        var firstCardBalance = dashBoardPage.getCardBalance(getFirstCard());
        var secondCardBalance = dashBoardPage.getCardBalance(getSecondCard());
        var amount = validAmount(firstCardBalance);
        var expectedBalanceFirstCard = firstCardBalance - amount;
        var expectedBalanceSecondCard = secondCardBalance + amount;
        var transferPage = dashBoardPage.selectCardToTransfer(secondCardInfo);
        dashBoardPage = transferPage.validTransfer(String.valueOf(amount), firstCardInfo);
        var actualBalanceFirstCard = dashBoardPage.getCardBalance(firstCardInfo);
        var actualBalanceSecondCard = dashBoardPage.getCardBalance(secondCardInfo);
        assertEquals(expectedBalanceFirstCard, actualBalanceFirstCard);
        assertEquals(expectedBalanceSecondCard, actualBalanceSecondCard);
    }

    @Test
    void shouldNotTransferFromFirstToSecond() {
        var firstCardInfo = getFirstCard();
        var secondCardInfo = getSecondCard();
        var firstCardBalance = dashBoardPage.getCardBalance(getFirstCard());
        var secondCardBalance = dashBoardPage.getCardBalance(getSecondCard());
        var amount = invalidAmount(secondCardBalance);
        var transferPage = dashBoardPage.selectCardToTransfer(firstCardInfo);
        transferPage.makeTransfer(String.valueOf(amount), secondCardInfo);
        transferPage.errorMessage("Ошибка! Произошла ошибка");
        var actualBalanceFirstCard = dashBoardPage.getCardBalance(firstCardInfo);
        var actualBalanceSecondCard = dashBoardPage.getCardBalance(secondCardInfo);
        assertEquals(firstCardBalance, actualBalanceFirstCard);
        assertEquals(secondCardBalance, actualBalanceSecondCard);
    }
}