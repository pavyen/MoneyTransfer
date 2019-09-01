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
        //Balance is depends on results of other test.
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

        final Transaction transaction = Transaction.builder()
                .accountIdFrom(String.valueOf(fromAccount))
                .accountIdTo(String.valueOf(toAccount))
                .amount(BigDecimal.valueOf(amount))
                .build();
        //It is not necessary to verify request result in this test.
        //The idea to push big amount of transaction and verify that total sum is not changed.
        RestAssured.given()
                .body(transaction)
                .when()
                .post(Constants.CONTEXT_MONEYTRANSFER_TRANSFERS);
    }

    private static void verifyTotalBalance() {
        //Without correct lock amount in account could be overwritten by another thread
        // and in that moment total sum will be different from starting total.
        Assert.assertEquals(getTotalBalance(), startBalance, "Total balance should always be the same.");
    }

    private static BigDecimal getTotalBalance() {
        return MoneyTransferApplication.getAccountService().getAccounts().stream()
                .map(Account::getBalance)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }
}
