package com.lecture.springmasters.domain.order.api;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

/**
 * 롤백이 안됨.. DB 설정을 통해서 롤백할 수 있는 방법이 있을지도..
 **/
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
class OrderControllerTestRestAssuerd {

  @BeforeAll
  public static void setup() {
    RestAssured.port = 8080;

  }

  @Test
  void 주문_API_테스트() {
    String requestBody = """
        {
            "userId": 1,
            "products": [
                {
                    "id": 1,
                    "quantity": 1000
                }
            ]
        }
        """;

    RestAssured.given()
        .contentType(ContentType.JSON)
        .body(requestBody)
        .when().post("/orders")
        .then()
        .statusCode(200)
        .body("result", equalTo(true))
        .body("message_id", notNullValue())

    ;
  }

}