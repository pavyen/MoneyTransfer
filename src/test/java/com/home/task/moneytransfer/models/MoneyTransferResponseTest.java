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
        Assert.assertEquals(TestConstants.HASH_SHOULD_BE_THE_SAME, clonedMoneyTransferResponse.hashCode(), testMoneyTransferResponse.hashCode());
        Assert.assertEquals(TestConstants.STRING_REPRESENTATION_SHOULD_BE_THE_SAME, clonedMoneyTransferResponse.toString(), testMoneyTransferResponse.toString());
        Assert.assertTrue(TestConstants.OBJECTS_COULD_BE_COMPARED, clonedMoneyTransferResponse.canEqual(testMoneyTransferResponse));
        Assert.assertEquals(TestConstants.OBJECTS_SHOULD_BE_EQUAL, clonedMoneyTransferResponse, testMoneyTransferResponse);
    }
}
