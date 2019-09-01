package com.home.task.moneytransfer.models;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class MoneyTransferResponseTest {

    private MoneyTransferResponse testMoneyTransferResponse;

    @BeforeTest
    public void setUp() {
        this.testMoneyTransferResponse = new MoneyTransferResponse();
        testMoneyTransferResponse.setSuccess(true);
        testMoneyTransferResponse.setError(new RequestError(StringUtils.EMPTY));
    }

    @Test
    public void shouldGenerateMethods() {
        final MoneyTransferResponse clonedMoneyTransferResponse = new MoneyTransferResponse();
        clonedMoneyTransferResponse.setSuccess(true);
        clonedMoneyTransferResponse.setError(new RequestError(StringUtils.EMPTY));
        Assert.assertEquals("Hash should be the same.", clonedMoneyTransferResponse.hashCode(), testMoneyTransferResponse.hashCode());
        Assert.assertEquals("String representation should be the same.", clonedMoneyTransferResponse.toString(), testMoneyTransferResponse.toString());
        Assert.assertTrue("Objects could be compared.", clonedMoneyTransferResponse.canEqual(testMoneyTransferResponse));
        Assert.assertEquals("Objects should be equal.", clonedMoneyTransferResponse, testMoneyTransferResponse);
    }
}
