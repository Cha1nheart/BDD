package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;
import java.time.Duration;
import static com.codeborne.selenide.Selenide.*;

public class TransferPage {
    private final SelenideElement transferButton = $("[data-test-id='action-transfer']");

    private final SelenideElement amountInput = $("[data-test-id='amount'] input");

    private final SelenideElement fromInput = $("[data-test-id='from'] input");

    private final SelenideElement transferHead = $("[data-test-id='dashboard']");

    private final SelenideElement errorMessage = $("[data-test-id='error-message']");

    public TransferPage() {
        transferHead.shouldBe(Condition.visible);
    }

    public DashBoardPage validTransfer(String transferAmount, DataHelper.CardInfo cardInfo) {
        makeTransfer(transferAmount, cardInfo);
        return new DashBoardPage();
    }

    public void makeTransfer(String transferAmount, DataHelper.CardInfo cardInfo) {
        amountInput.setValue(transferAmount);
        fromInput.setValue(cardInfo.getCardNumber());
        transferButton.click();
    }

    public void errorMessage(String expectedText) {
        errorMessage.shouldHave(Condition
                .exactText(expectedText), Duration.ofSeconds(15))
                .shouldBe(Condition.visible);
    }

}
