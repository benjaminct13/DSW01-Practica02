package com.example.empleados.integration;

import com.example.empleados.repository.EmpleadoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class EmpleadoCreateIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @BeforeEach
    void setup() {
        empleadoRepository.deleteAll();
    }

    @Test
    void shouldCreateEmployeeWithGeneratedClave() throws Exception {
        String payload = """
            {
              \"nombre\": \"Juan Perez\",
              \"direccion\": \"Calle 1\",
              \"telefono\": \"123456\"
            }
            """;

        mockMvc.perform(post("/api/empleados")
                .with(httpBasic("admin", "admin123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.clave").value("E-001"));
    }

    @Test
    void shouldRejectPayloadWithClaveField() throws Exception {
        String payload = """
            {
              \"clave\": \"E-999\",
              \"nombre\": \"Juan Perez\",
              \"direccion\": \"Calle 1\",
              \"telefono\": \"123456\"
            }
            """;

        mockMvc.perform(post("/api/empleados")
                .with(httpBasic("admin", "admin123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
            .andExpect(status().isBadRequest());
    }
}
