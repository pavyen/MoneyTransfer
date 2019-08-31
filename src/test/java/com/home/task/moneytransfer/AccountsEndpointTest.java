package com.home.task.moneytransfer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.home.task.moneytransfer.models.Account;
import com.home.task.moneytransfer.models.MoneyTransferResponse;
import com.home.task.moneytransfer.models.RequestError;
import com.home.task.moneytransfer.utils.Constants;
import com.home.task.moneytransfer.utils.ValidationConstants;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.eclipse.jetty.http.HttpStatus;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.UUID;

public class AccountsEndpointTest {

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
    @Ignore
    public void testCreateAccount_fail_idIsNull() {
        final Account account = new Account();
        account.setBalance(BigDecimal.TEN);
        RestAssured.given()
                .body(account)
                .when()
                .post(Constants.CONTEXT_MONEYTRANSFER_ACCOUNTS)
                .then()
                .assertThat()
                .body(Matchers.equalTo(account))
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.BAD_REQUEST_400);
    }

    @Test
    public void testCreateAccount_fail_balanceIsNull() throws JsonProcessingException {
        final Account account = new Account();
        account.setId(UUID.randomUUID().toString());

        final MoneyTransferResponse moneyTransferResponse = new MoneyTransferResponse();
        moneyTransferResponse.setSuccess(false);
        moneyTransferResponse.setError(
                RequestError.builder().message(ValidationConstants.BALANCE_SHOULD_NOT_BE_NULL).build()
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

}
