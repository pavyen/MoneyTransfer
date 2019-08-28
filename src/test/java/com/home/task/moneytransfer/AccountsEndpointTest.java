package com.home.task.moneytransfer;

import com.home.task.moneytransfer.models.Account;
import com.home.task.moneytransfer.utils.Constants;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.eclipse.jetty.http.HttpStatus;
import org.hamcrest.Matchers;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.UUID;

public class AccountsEndpointTest {

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
    public void testCreateAccount_success() {
        final Account account = Account.builder()
                .id(UUID.randomUUID().toString())
                .name(UUID.randomUUID().toString())
                .balance(BigDecimal.TEN)
                .build();
        RestAssured.given()
                .body(account)
                .when()
                .post(Constants.CONTEXT_MONEYTRANSFER_ACCOUNTS)
                .then()
                .assertThat()
                .body(Matchers.equalTo(account))
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.CREATED_201);
    }

    @Test
    public void testCreateAccount_fail_idIsNull() {
        final Account account = new Account();
        account.setName(UUID.randomUUID().toString());
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
    public void testCreateAccount_fail_balanceIsNull() {
        final Account account = new Account();
        account.setId(UUID.randomUUID().toString());
        account.setName(UUID.randomUUID().toString());
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
    public void testReadAccount_exist() {
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
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.NOT_FOUND_404);
    }

}
