package com.home.task.moneytransfer;

import com.home.task.moneytransfer.models.Transaction;
import com.home.task.moneytransfer.utils.Constants;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.eclipse.jetty.http.HttpStatus;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.UUID;

public class TransfersEndpointTest {

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
                .post(Constants.CONTEXT_MONEYTRANSFER_ACCOUNTS)
                .then()
                .assertThat()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.NO_CONTENT_204);
    }

    @Test
    public void testMakeTransfer_fail_negativeNumber() {
        final Transaction transaction = Transaction.builder()
                .accountIdFrom("1")
                .accountIdTo("2")
                .amount(BigDecimal.TEN.negate())
                .build();
        RestAssured.given()
                .body(transaction)
                .when()
                .post(Constants.CONTEXT_MONEYTRANSFER_ACCOUNTS)
                .then()
                .assertThat()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.BAD_REQUEST_400);
    }

    @Test
    public void testMakeTransfer_fail_notEnoughMoney() {
        final Transaction transaction = Transaction.builder()
                .accountIdFrom("1")
                .accountIdTo("2")
                .amount(BigDecimal.valueOf(100000))
                .build();
        RestAssured.given()
                .body(transaction)
                .when()
                .post(Constants.CONTEXT_MONEYTRANSFER_ACCOUNTS)
                .then()
                .assertThat()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.BAD_REQUEST_400);
    }

    @Test
    public void testMakeTransfer_fail_accountNotExistAccountIdFrom() {
        final Transaction transaction = Transaction.builder()
                .accountIdFrom("100000")
                .accountIdTo("2")
                .amount(BigDecimal.TEN)
                .build();
        RestAssured.given()
                .body(transaction)
                .when()
                .post(Constants.CONTEXT_MONEYTRANSFER_ACCOUNTS)
                .then()
                .assertThat()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.BAD_REQUEST_400);
    }

    @Test
    public void testMakeTransfer_fail_accountNotExistaAccountIdTo() {
        final Transaction transaction = Transaction.builder()
                .accountIdFrom("1")
                .accountIdTo("200000")
                .amount(BigDecimal.TEN)
                .build();
        RestAssured.given()
                .body(transaction)
                .when()
                .post(Constants.CONTEXT_MONEYTRANSFER_ACCOUNTS)
                .then()
                .assertThat()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.BAD_REQUEST_400);
    }

    @Test
    public void testMakeTransfer_fail_accountsTheSame() {
        final Transaction transaction = Transaction.builder()
                .accountIdFrom("1")
                .accountIdTo("1")
                .amount(BigDecimal.TEN)
                .build();
        RestAssured.given()
                .body(transaction)
                .when()
                .post(Constants.CONTEXT_MONEYTRANSFER_ACCOUNTS)
                .then()
                .assertThat()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.BAD_REQUEST_400);
    }

    @Test
    public void testMakeTransfer_fail_accountIdFromIsNull() {
        final Transaction transaction = new Transaction();
        transaction.setAccountIdTo(UUID.randomUUID().toString());
        transaction.setAmount(BigDecimal.TEN);

        RestAssured.given()
                .body(transaction)
                .when()
                .post(Constants.CONTEXT_MONEYTRANSFER_ACCOUNTS)
                .then()
                .assertThat()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.BAD_REQUEST_400);
    }

    @Test
    public void testMakeTransfer_fail_accountIdToIsNull() {
        final Transaction transaction = new Transaction();
        transaction.setAccountIdFrom(UUID.randomUUID().toString());
        transaction.setAmount(BigDecimal.TEN);

        RestAssured.given()
                .body(transaction)
                .when()
                .post(Constants.CONTEXT_MONEYTRANSFER_ACCOUNTS)
                .then()
                .assertThat()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.BAD_REQUEST_400);
    }

    @Test
    public void testMakeTransfer_fail_amountIsNull() {
        final Transaction transaction = new Transaction();
        transaction.setAccountIdFrom(UUID.randomUUID().toString());
        transaction.setAccountIdTo(UUID.randomUUID().toString());

        RestAssured.given()
                .body(transaction)
                .when()
                .post(Constants.CONTEXT_MONEYTRANSFER_ACCOUNTS)
                .then()
                .assertThat()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.BAD_REQUEST_400);
    }

}
