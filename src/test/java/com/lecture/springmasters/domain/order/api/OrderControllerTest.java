package com.lecture.springmasters.domain.order.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  @Transactional
  void testCreateOrder() throws Exception {
    String requestBody = """
              {
          "userId": 1,
          "products": [
            {
              "id": 1,
              "quantity": 10
            }
          ]
        }
        """;

    mockMvc.perform(post("/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.result").value(true))
        .andExpect(jsonPath("$.message.id").exists());
  }

  @Test
  void testCreateOrderWithMissingUserId() throws Exception {
    String requestBody = """
        {
            "products": [
                {
                    "id": 1,
                    "quantity": 2
                }
            ]
        }
        """;

    mockMvc.perform(post("/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.error.errorCode").value("VALIDATE_ERROR"));
  }

  @Test
  public void testCreateOrderWithOutOfStock() throws Exception {
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

    mockMvc.perform(post("/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.error.errorCode").value("OUT_OF_STOCK_PRODUCT"))
        .andExpect(jsonPath("$.error.errorMessage").value("재고가 부족합니다."));
  }
}