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
        Assert.assertEquals(TestConstants.HASH_SHOULD_BE_THE_SAME, clonedTransaction.hashCode(), testTransaction.hashCode());
        Assert.assertEquals(TestConstants.STRING_REPRESENTATION_SHOULD_BE_THE_SAME, clonedTransaction.toString(), testTransaction.toString());
        Assert.assertTrue(TestConstants.OBJECTS_COULD_BE_COMPARED, clonedTransaction.canEqual(testTransaction));
        Assert.assertEquals(TestConstants.OBJECTS_SHOULD_BE_EQUAL, clonedTransaction, testTransaction);
    }
}
