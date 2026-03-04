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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class EmpleadoReadIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @BeforeEach
    void setup() {
        empleadoRepository.deleteAll();
    }

    @Test
    void shouldReadByClaveAndList() throws Exception {
        String payload = """
            {
              \"nombre\": \"Ana\",
              \"direccion\": \"Calle 2\",
              \"telefono\": \"55555\"
            }
            """;

        mockMvc.perform(post("/api/empleados")
                .with(httpBasic("admin", "admin123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
            .andExpect(status().isCreated());

        mockMvc.perform(get("/api/empleados/E-001").with(httpBasic("admin", "admin123")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.clave").value("E-001"));

        mockMvc.perform(get("/api/empleados").with(httpBasic("admin", "admin123")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].clave").value("E-001"));
    }
}
