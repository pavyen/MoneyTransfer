package com.home.task.moneytransfer.models;

import org.junit.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.math.BigDecimal;

public class AccountTest {

    private static final String ID = "1";
    private Account testAccount;

    @BeforeTest
    public void setUp() {
        this.testAccount = Account.builder().balance(BigDecimal.TEN).id(ID).build();
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldFailOnWithdraw_resultNegative() {
        testAccount.withdraw(BigDecimal.TEN.add(BigDecimal.ONE));
        Assert.assertEquals("Balance shouldn't be negative.", testAccount.getBalance(), BigDecimal.TEN);
    }

    @Test
    public void shouldSuccessOnWithdraw_resultZero() {
        testAccount.withdraw(BigDecimal.TEN);
        Assert.assertEquals("Balance should be 0.", testAccount.getBalance(), BigDecimal.ZERO);
    }

    @Test
    public void shouldSuccessOnWithdraw_resultPositive() {
        final BigDecimal currentBalance = testAccount.getBalance();
        testAccount.withdraw(BigDecimal.ONE);
        Assert.assertEquals("Balance should be positive.", testAccount.getBalance(), currentBalance.subtract(BigDecimal.ONE));
    }

    @Test
    public void shouldSuccessOnDeposit() {
        testAccount.deposit(BigDecimal.ONE);
        Assert.assertEquals(BigDecimal.TEN.add(BigDecimal.ONE), testAccount.getBalance());
    }

    @Test
    public void shouldGenerateMethods() {
        final Account clonedAccount = testAccount.toBuilder().build();
        Assert.assertEquals(TestConstants.HASH_SHOULD_BE_THE_SAME, clonedAccount.hashCode(), testAccount.hashCode());
        Assert.assertEquals(TestConstants.STRING_REPRESENTATION_SHOULD_BE_THE_SAME, clonedAccount.toString(), testAccount.toString());
        Assert.assertTrue(TestConstants.OBJECTS_COULD_BE_COMPARED, clonedAccount.canEqual(testAccount));
        Assert.assertEquals(TestConstants.OBJECTS_SHOULD_BE_EQUAL, clonedAccount, testAccount);
    }
}
