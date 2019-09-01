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
        Assert.assertEquals(TestConstants.HASH_SHOULD_BE_THE_SAME, clonedRequestError.hashCode(), testRequestError.hashCode());
        Assert.assertEquals(TestConstants.STRING_REPRESENTATION_SHOULD_BE_THE_SAME, clonedRequestError.toString(), testRequestError.toString());
        Assert.assertTrue(TestConstants.OBJECTS_COULD_BE_COMPARED, clonedRequestError.canEqual(testRequestError));
        Assert.assertEquals(TestConstants.OBJECTS_SHOULD_BE_EQUAL, clonedRequestError, testRequestError);
    }
}
