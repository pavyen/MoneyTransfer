package com.home.task.moneytransfer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.home.task.moneytransfer.models.MoneyTransferResponse;
import com.home.task.moneytransfer.models.RequestError;
import com.home.task.moneytransfer.models.Transaction;
import com.home.task.moneytransfer.utils.Constants;
import com.home.task.moneytransfer.validator.TransactionValidator;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.eclipse.jetty.http.HttpStatus;
import org.hamcrest.Matchers;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.UUID;

public class TransfersEndpointTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeClass
    public static void startServer() {
        MoneyTransferApplication.initApplication();
        RestAssured.baseURI = String.format("http://localhost:%d/", Constants.PORT);
    }

    @AfterClass
    public static void tearDown() {
        MoneyTransferApplication.stopServer();
    }

    @Test
    public void testMakeTransfer_success() {
        final Transaction transaction = Transaction.builder()
                .accountIdFrom("1")
                .accountIdTo("2")
                .amount(BigDecimal.TEN)
                .build();
        RestAssured.given()
                .body(transaction)
                .when()
                .post(Constants.CONTEXT_MONEYTRANSFER_TRANSFERS)
                .then()
                .assertThat()
                .statusCode(HttpStatus.NO_CONTENT_204);
    }

    @Test
    public void testMakeTransfer_fail_negativeNumber() throws JsonProcessingException {
        final Transaction transaction = Transaction.builder()
                .accountIdFrom("1")
                .accountIdTo("2")
                .amount(BigDecimal.TEN.negate())
                .build();

        final MoneyTransferResponse moneyTransferResponse = new MoneyTransferResponse();
        moneyTransferResponse.setSuccess(false);
        moneyTransferResponse.setError(
                RequestError.builder().message(TransactionValidator.AMOUNT_SHOULD_BE_POSITIVE).build()
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
                .accountIdFrom("1")
                .accountIdTo("2")
                .amount(BigDecimal.valueOf(100000))
                .build();

        final MoneyTransferResponse moneyTransferResponse = new MoneyTransferResponse();
        moneyTransferResponse.setSuccess(false);
        moneyTransferResponse.setError(
                RequestError.builder().message(null).build()
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
    public void testMakeTransfer_fail_accountNotExistAccountIdFrom() throws JsonProcessingException {
        final Transaction transaction = Transaction.builder()
                .accountIdFrom("100000")
                .accountIdTo("2")
                .amount(BigDecimal.TEN)
                .build();

        final MoneyTransferResponse moneyTransferResponse = new MoneyTransferResponse();
        moneyTransferResponse.setSuccess(false);
        moneyTransferResponse.setError(
                RequestError.builder().message(null).build()
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
    public void testMakeTransfer_fail_accountNotExistAccountIdTo() throws JsonProcessingException {
        final Transaction transaction = Transaction.builder()
                .accountIdFrom("1")
                .accountIdTo("200000")
                .amount(BigDecimal.TEN)
                .build();

        final MoneyTransferResponse moneyTransferResponse = new MoneyTransferResponse();
        moneyTransferResponse.setSuccess(false);
        moneyTransferResponse.setError(
                RequestError.builder().message(null).build()
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
                .accountIdFrom("1")
                .accountIdTo("1")
                .amount(BigDecimal.TEN)
                .build();

        final MoneyTransferResponse moneyTransferResponse = new MoneyTransferResponse();
        moneyTransferResponse.setSuccess(false);
        moneyTransferResponse.setError(
                RequestError.builder().message(TransactionValidator.ACCOUNTS_SHOULD_BE_DIFFERENT).build()
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
        final Transaction transaction = new Transaction();
        transaction.setAccountIdTo(UUID.randomUUID().toString());
        transaction.setAmount(BigDecimal.TEN);

        final MoneyTransferResponse moneyTransferResponse = new MoneyTransferResponse();
        moneyTransferResponse.setSuccess(false);
        moneyTransferResponse.setError(
                RequestError.builder().message(TransactionValidator.ACCOUNT_ID_FROM_SHOULD_NOT_BE_BLANK).build()
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
        final Transaction transaction = new Transaction();
        transaction.setAccountIdFrom(UUID.randomUUID().toString());
        transaction.setAmount(BigDecimal.TEN);

        final MoneyTransferResponse moneyTransferResponse = new MoneyTransferResponse();
        moneyTransferResponse.setSuccess(false);
        moneyTransferResponse.setError(
                RequestError.builder().message(TransactionValidator.ACCOUNT_ID_TO_SHOULD_NOT_BE_BLANK).build()
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
        final Transaction transaction = new Transaction();
        transaction.setAccountIdFrom(UUID.randomUUID().toString());
        transaction.setAccountIdTo(UUID.randomUUID().toString());

        final MoneyTransferResponse moneyTransferResponse = new MoneyTransferResponse();
        moneyTransferResponse.setSuccess(false);
        moneyTransferResponse.setError(
                RequestError.builder().message(TransactionValidator.AMOUNT_SHOULD_NOT_BE_NULL).build()
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
