package com.home.task.moneytransfer.models;

import org.junit.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class TransactionTest {

    private Transaction testTransaction;

    @BeforeTest
    public void setUp() {
        this.testTransaction = Transaction.builder()
                .accountIdFrom("1")
                .accountIdTo("2")
                .amount(BigDecimal.TEN)
                .transactionDate(LocalDateTime.now(ZoneOffset.UTC))
                .build();
    }

    @Test
    public void shouldGenerateMethods() {
        final Transaction clonedTransaction = testTransaction.toBuilder().build();
        Assert.assertEquals("Hash should be the same.", clonedTransaction.hashCode(), testTransaction.hashCode());
        Assert.assertEquals("String representation should be the same.", clonedTransaction.toString(), testTransaction.toString());
        Assert.assertTrue("Objects could be compared.", clonedTransaction.canEqual(testTransaction));
        Assert.assertEquals("Objects should be equal.", clonedTransaction, testTransaction);
    }
}
