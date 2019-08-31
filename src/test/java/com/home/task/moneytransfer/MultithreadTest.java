package com.home.task.moneytransfer;

import com.home.task.moneytransfer.models.Account;
import com.home.task.moneytransfer.models.Transaction;
import com.home.task.moneytransfer.utils.Constants;
import io.restassured.RestAssured;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.math.BigDecimal;

@Slf4j
public class MultithreadTest {

    private static final int COUNT = 10;
    private static BigDecimal startBalance = BigDecimal.ZERO;

    @BeforeClass
    public static void startServer() {
        MoneyTransferApplication.initApplication();
        RestAssured.baseURI = String.format("http://localhost:%d/", Constants.PORT);

        //Create test accounts.
        for (int i = 0; i < COUNT; i++) {
            Account account = Account.builder()
                    .id(String.valueOf(i))
                    .balance(BigDecimal.TEN)
                    .build();
            RestAssured.given()
                    .body(account)
                    .when()
                    .post(Constants.CONTEXT_MONEYTRANSFER_ACCOUNTS);
        }

        startBalance = getTotalBalance();
    }

    @Test(threadPoolSize = 100, invocationCount = 500, timeOut = 10000)
    public void testMultithreadLock() {
        transferMoney((int) (Math.random() * COUNT));
        verifyTotalBalance();
    }

    private void transferMoney(final int fromAccount) {
        final int toAccount = (int) (Math.random() * COUNT);
        final int amount = (int) (Math.random() * COUNT);

        final Transaction transaction = com.home.task.moneytransfer.models.Transaction.builder()
                .accountIdFrom(String.valueOf(fromAccount))
                .accountIdTo(String.valueOf(toAccount))
                .amount(BigDecimal.valueOf(amount))
                .build();

        RestAssured.given()
                .body(transaction)
                .when()
                .post(Constants.CONTEXT_MONEYTRANSFER_TRANSFERS);
    }

    private static void verifyTotalBalance() {
        Assert.assertEquals(getTotalBalance(), startBalance);
    }

    private static BigDecimal getTotalBalance() {
        return MoneyTransferApplication.getAccountService().getAccounts().stream()
                .map(Account::getBalance).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
    }
}
