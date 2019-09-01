package com.home.task.moneytransfer;

import com.home.task.moneytransfer.models.Account;
import org.junit.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class MoneyTransferApplicationTest {

    @Test
    public void testLoadData() {
        MoneyTransferApplication.main(null);
        final List<Account> accounts = MoneyTransferApplication.getAccountService().getAccounts();
        Assert.assertTrue("Main method should load data from file.", accounts.size() > 20);
    }
}
