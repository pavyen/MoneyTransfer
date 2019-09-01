package com.home.task.moneytransfer.models;

import org.junit.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class RequestErrorTest {

    private RequestError testRequestError;

    @BeforeTest
    public void setUp() {
        this.testRequestError = new RequestError(RequestError.class.getName());
    }

    @Test
    public void shouldGenerateMethods() {
        final RequestError clonedRequestError = new RequestError(RequestError.class.getName());
        Assert.assertEquals("Hash should be the same.", clonedRequestError.hashCode(), testRequestError.hashCode());
        Assert.assertEquals("String representation should be the same.", clonedRequestError.toString(), testRequestError.toString());
        Assert.assertTrue("Objects could be compared.", clonedRequestError.canEqual(testRequestError));
        Assert.assertEquals("Objects should be equal.", clonedRequestError, testRequestError);
    }
}
