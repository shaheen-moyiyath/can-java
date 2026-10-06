package com.canteen;

import com.canteen.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CanteenApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Context loads successfully")
    void contextLoads() {
    }

    @Test
    @DisplayName("Public menu endpoint returns menu items without authentication")
    void testGetPublicMenu() throws Exception {
        mockMvc.perform(get("/api/menu"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").isNotEmpty());
    }

    @Test
    @DisplayName("Student login with valid credentials returns JWT token")
    void testStudentLogin() throws Exception {
        AuthRequest loginRequest = new AuthRequest("student1", "student123");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.token").isNotEmpty())
                .andExpect(jsonPath("$.data.role").value("STUDENT"));
    }

    @Test
    @DisplayName("Admin login with valid credentials returns JWT token with ADMIN role")
    void testAdminLogin() throws Exception {
        AuthRequest loginRequest = new AuthRequest("admin", "admin123");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.token").isNotEmpty())
                .andExpect(jsonPath("$.data.role").value("ADMIN"));
    }

    @Test
    @DisplayName("Student registration and subsequent order placement flow")
    void testRegistrationAndOrderPlacementFlow() throws Exception {
        String testUser = "student_test_" + System.currentTimeMillis();
        RegisterRequest registerRequest = new RegisterRequest(
                testUser,
                "password123",
                "Test Student",
                testUser + "@student.college.edu",
                null
        );

        // 1. Register student
        MvcResult regResult = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.token").isNotEmpty())
                .andReturn();

        String regJson = regResult.getResponse().getContentAsString();
        String studentToken = objectMapper.readTree(regJson).path("data").path("token").asText();
        assertThat(studentToken).isNotEmpty();

        // 2. Place order with valid JWT
        OrderRequest orderRequest = new OrderRequest(List.of(
                new OrderItemRequest(1L, 2), // 2x Chicken Biryani
                new OrderItemRequest(5L, 1)  // 1x Egg Puffs
        ));

        MvcResult orderResult = mockMvc.perform(post("/api/orders")
                        .header("Authorization", "Bearer " + studentToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").isNumber())
                .andExpect(jsonPath("$.data.total_amount").value(260.00)) // 2*120 + 1*20 = 260
                .andReturn();

        // 3. View my order history
        mockMvc.perform(get("/api/orders/my-history")
                        .header("Authorization", "Bearer " + studentToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].total_amount").value(260.00));
    }

    @Test
    @DisplayName("Admin dashboard kitchen tally and daily report aggregation")
    void testAdminDashboardAndReports() throws Exception {
        // 1. Login as Admin
        AuthRequest adminLogin = new AuthRequest("admin", "admin123");
        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adminLogin)))
                .andExpect(status().isOk())
                .andReturn();

        String adminToken = objectMapper.readTree(loginResult.getResponse().getContentAsString())
                .path("data").path("token").asText();

        // 2. Access Admin live kitchen tally
        mockMvc.perform(get("/api/admin/dashboard/live-counts")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());

        // 3. Access Daily Report
        mockMvc.perform(get("/api/admin/reports/daily")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.total_orders").isNumber())
                .andExpect(jsonPath("$.data.total_revenue").isNumber());
    }

    @Test
    @DisplayName("Unauthorized user cannot access admin live counts")
    void testUnauthorizedAdminAccessFails() throws Exception {
        mockMvc.perform(get("/api/admin/dashboard/live-counts"))
                .andExpect(status().isUnauthorized());
    }
}
