package com.home.task.moneytransfer.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.home.task.moneytransfer.MoneyTransferApplication;
import com.home.task.moneytransfer.models.Account;
import com.home.task.moneytransfer.models.MoneyTransferResponse;
import com.home.task.moneytransfer.models.RequestError;
import com.home.task.moneytransfer.utils.Constants;
import com.home.task.moneytransfer.utils.ValidationConstants;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.eclipse.jetty.http.HttpStatus;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Test accounts controller routes.
 */
public class AccountsControllerTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeClass
    public static void startServer() {
        MoneyTransferApplication.initApplication();
        RestAssured.baseURI = String.format("http://localhost:%d/", Constants.PORT);
    }

    @Test
    public void testCreateAccount_success() {
        final Account account = Account.builder()
                .id(UUID.randomUUID().toString())
                .balance(BigDecimal.TEN)
                .build();
        RestAssured.given()
                .body(account)
                .when()
                .post(Constants.CONTEXT_MONEYTRANSFER_ACCOUNTS)
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED_201);
    }

    @Test
    public void testCreateAccount_fail_balanceIsNull() throws JsonProcessingException {
        final Account account = Account.builder()
                .id(UUID.randomUUID().toString())
                .build();

        final MoneyTransferResponse moneyTransferResponse = new MoneyTransferResponse();
        moneyTransferResponse.setSuccess(false);
        moneyTransferResponse.setError(
                new RequestError(ValidationConstants.BALANCE_SHOULD_NOT_BE_NULL)
        );
        RestAssured.given()
                .body(account)
                .when()
                .post(Constants.CONTEXT_MONEYTRANSFER_ACCOUNTS)
                .then()
                .assertThat()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.BAD_REQUEST_400)
                .body(Matchers.equalTo(mapper.writeValueAsString(moneyTransferResponse)));
    }

    @Test
    public void testReadAccount_exist() {
        //Create test account.
        final Account account = Account.builder()
                .id("1")
                .balance(BigDecimal.TEN)
                .build();
        RestAssured.given()
                .body(account)
                .when()
                .post(Constants.CONTEXT_MONEYTRANSFER_ACCOUNTS);
        //Read test account
        RestAssured.given()
                .pathParam("accountId", 1)
                .when()
                .get(Constants.CONTEXT_MONEYTRANSFER_ACCOUNTS + "/{accountId}")
                .then()
                .assertThat()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.OK_200);
    }

    @Test
    public void testReadAccount_notExist() {
        RestAssured.given()
                .pathParam("accountId", -1)
                .when()
                .get(Constants.CONTEXT_MONEYTRANSFER_ACCOUNTS + "/{accountId}")
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND_404);
    }

    @Test
    public void testReadAccounts_exist() {
        //Create test account.
        final Account account = Account.builder()
                .balance(BigDecimal.TEN)
                .build();
        RestAssured.given()
                .body(account)
                .when()
                .post(Constants.CONTEXT_MONEYTRANSFER_ACCOUNTS);
        //Read test accounts
        final List response = RestAssured.given()
                .when()
                .get(Constants.CONTEXT_MONEYTRANSFER_ACCOUNTS)
                .then()
                .assertThat()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.OK_200)
                .extract()
                .response()
                .as(List.class);
        Assert.assertFalse(response.isEmpty(), "Responce should not be empty.");
    }
}
