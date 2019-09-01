package com.home.task.moneytransfer.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.home.task.moneytransfer.MoneyTransferApplication;
import com.home.task.moneytransfer.models.Account;
import com.home.task.moneytransfer.models.MoneyTransferResponse;
import com.home.task.moneytransfer.models.RequestError;
import com.home.task.moneytransfer.models.Transaction;
import com.home.task.moneytransfer.utils.Constants;
import com.home.task.moneytransfer.utils.ValidationConstants;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.eclipse.jetty.http.HttpStatus;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Test transfers controller routes.
 */
public class TransfersControllerTest {

    private static final String ID_FROM = UUID.randomUUID().toString();
    private static final String ID_TO = UUID.randomUUID().toString();
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeClass
    public static void startServer() {
        MoneyTransferApplication.initApplication();
        RestAssured.baseURI = String.format("http://localhost:%d/", Constants.PORT);

        //Create test accounts.
        Account account = Account.builder()
                .id(ID_FROM)
                .balance(BigDecimal.TEN)
                .build();
        RestAssured.given()
                .body(account)
                .when()
                .post(Constants.CONTEXT_MONEYTRANSFER_ACCOUNTS);
        account = Account.builder()
                .id(ID_TO)
                .balance(BigDecimal.TEN)
                .build();
        RestAssured.given()
                .body(account)
                .when()
                .post(Constants.CONTEXT_MONEYTRANSFER_ACCOUNTS);
    }

    @Test
    public void testMakeTransfer_success() throws JsonProcessingException {
        final Transaction transaction = Transaction.builder()
                .accountIdFrom(ID_FROM)
                .accountIdTo(ID_TO)
                .amount(BigDecimal.TEN)
                .build();
        final MoneyTransferResponse moneyTransferResponse = new MoneyTransferResponse();
        moneyTransferResponse.setSuccess(true);
        RestAssured.given()
                .body(transaction)
                .when()
                .post(Constants.CONTEXT_MONEYTRANSFER_TRANSFERS)
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED_201)
                .contentType(ContentType.JSON)
                .body(Matchers.equalTo(mapper.writeValueAsString(moneyTransferResponse)));
    }

    @Test
    public void testMakeTransfer_fail_negativeNumber() throws JsonProcessingException {
        final Transaction transaction = Transaction.builder()
                .accountIdFrom(ID_FROM)
                .accountIdTo(ID_TO)
                .amount(BigDecimal.TEN.negate())
                .build();

        final MoneyTransferResponse moneyTransferResponse = new MoneyTransferResponse();
        moneyTransferResponse.setSuccess(false);
        moneyTransferResponse.setError(
                new RequestError(ValidationConstants.AMOUNT_SHOULD_BE_POSITIVE)
        );

        RestAssured.given()
                .body(transaction)
                .when()
                .post(Constants.CONTEXT_MONEYTRANSFER_TRANSFERS)
                .then()
                .assertThat()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.BAD_REQUEST_400)
                .body(Matchers.equalTo(mapper.writeValueAsString(moneyTransferResponse)));
    }

    @Test
    public void testMakeTransfer_fail_notEnoughMoney() throws JsonProcessingException {
        final Transaction transaction = Transaction.builder()
                .accountIdFrom(ID_FROM)
                .accountIdTo(ID_TO)
                .amount(BigDecimal.valueOf(100))
                .build();

        final MoneyTransferResponse moneyTransferResponse = new MoneyTransferResponse();
        moneyTransferResponse.setSuccess(false);
        moneyTransferResponse.setError(
                new RequestError(ValidationConstants.AMOUNT_TO_TRANSFER_IS_GREATER_THAN_ACCOUNT_BALANCE)
        );

        RestAssured.given()
                .body(transaction)
                .when()
                .post(Constants.CONTEXT_MONEYTRANSFER_TRANSFERS)
                .then()
                .assertThat()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.BAD_REQUEST_400)
                .body(Matchers.equalTo(mapper.writeValueAsString(moneyTransferResponse)));
    }

    @Test
    public void testMakeTransfer_fail_accountNotExist_AccountIdFrom() throws JsonProcessingException {
        final Transaction transaction = Transaction.builder()
                .accountIdFrom("100")
                .accountIdTo(ID_TO)
                .amount(BigDecimal.TEN)
                .build();

        final MoneyTransferResponse moneyTransferResponse = new MoneyTransferResponse();
        moneyTransferResponse.setSuccess(false);
        moneyTransferResponse.setError(
                new RequestError(ValidationConstants.WRONG_ACCOUNT_FROM_ID)
        );

        RestAssured.given()
                .body(transaction)
                .when()
                .post(Constants.CONTEXT_MONEYTRANSFER_TRANSFERS)
                .then()
                .assertThat()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.BAD_REQUEST_400)
                .body(Matchers.equalTo(mapper.writeValueAsString(moneyTransferResponse)));
    }

    @Test
    public void testMakeTransfer_fail_accountNotExist_AccountIdTo() throws JsonProcessingException {
        final Transaction transaction = Transaction.builder()
                .accountIdFrom(ID_FROM)
                .accountIdTo("200")
                .amount(BigDecimal.TEN)
                .build();

        final MoneyTransferResponse moneyTransferResponse = new MoneyTransferResponse();
        moneyTransferResponse.setSuccess(false);
        moneyTransferResponse.setError(
                new RequestError(ValidationConstants.WRONG_ACCOUNT_TO_ID)
        );

        RestAssured.given()
                .body(transaction)
                .when()
                .post(Constants.CONTEXT_MONEYTRANSFER_TRANSFERS)
                .then()
                .assertThat()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.BAD_REQUEST_400)
                .body(Matchers.equalTo(mapper.writeValueAsString(moneyTransferResponse)));
    }

    @Test
    public void testMakeTransfer_fail_accountsTheSame() throws JsonProcessingException {
        final Transaction transaction = Transaction.builder()
                .accountIdFrom(ID_FROM)
                .accountIdTo(ID_FROM)
                .amount(BigDecimal.TEN)
                .build();

        final MoneyTransferResponse moneyTransferResponse = new MoneyTransferResponse();
        moneyTransferResponse.setSuccess(false);
        moneyTransferResponse.setError(
                new RequestError(ValidationConstants.ACCOUNTS_SHOULD_BE_DIFFERENT)
        );

        RestAssured.given()
                .body(transaction)
                .when()
                .post(Constants.CONTEXT_MONEYTRANSFER_TRANSFERS)
                .then()
                .assertThat()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.BAD_REQUEST_400)
                .body(Matchers.equalTo(mapper.writeValueAsString(moneyTransferResponse)));
    }

    @Test
    public void testMakeTransfer_fail_accountIdFromIsNull() throws JsonProcessingException {
        final Transaction transaction = Transaction.builder()
                .accountIdTo(UUID.randomUUID().toString())
                .amount(BigDecimal.TEN)
                .build();

        final MoneyTransferResponse moneyTransferResponse = new MoneyTransferResponse();
        moneyTransferResponse.setSuccess(false);
        moneyTransferResponse.setError(
                new RequestError(ValidationConstants.ACCOUNT_ID_FROM_SHOULD_NOT_BE_BLANK)
        );

        RestAssured.given()
                .body(transaction)
                .when()
                .post(Constants.CONTEXT_MONEYTRANSFER_TRANSFERS)
                .then()
                .assertThat()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.BAD_REQUEST_400)
                .body(Matchers.equalTo(mapper.writeValueAsString(moneyTransferResponse)));
    }

    @Test
    public void testMakeTransfer_fail_accountIdToIsNull() throws JsonProcessingException {
        final Transaction transaction = Transaction.builder()
                .accountIdFrom(UUID.randomUUID().toString())
                .amount(BigDecimal.TEN)
                .build();

        final MoneyTransferResponse moneyTransferResponse = new MoneyTransferResponse();
        moneyTransferResponse.setSuccess(false);
        moneyTransferResponse.setError(
                new RequestError(ValidationConstants.ACCOUNT_ID_TO_SHOULD_NOT_BE_BLANK)
        );

        RestAssured.given()
                .body(transaction)
                .when()
                .post(Constants.CONTEXT_MONEYTRANSFER_TRANSFERS)
                .then()
                .assertThat()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.BAD_REQUEST_400)
                .body(Matchers.equalTo(mapper.writeValueAsString(moneyTransferResponse)));
    }

    @Test
    public void testMakeTransfer_fail_amountIsNull() throws JsonProcessingException {
        final Transaction transaction = Transaction.builder()
                .accountIdFrom(UUID.randomUUID().toString())
                .accountIdTo(UUID.randomUUID().toString())
                .build();

        final MoneyTransferResponse moneyTransferResponse = new MoneyTransferResponse();
        moneyTransferResponse.setSuccess(false);
        moneyTransferResponse.setError(
                new RequestError(ValidationConstants.AMOUNT_SHOULD_NOT_BE_NULL)
        );

        RestAssured.given()
                .body(transaction)
                .when()
                .post(Constants.CONTEXT_MONEYTRANSFER_TRANSFERS)
                .then()
                .assertThat()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.BAD_REQUEST_400)
                .body(Matchers.equalTo(mapper.writeValueAsString(moneyTransferResponse)));
    }

}
