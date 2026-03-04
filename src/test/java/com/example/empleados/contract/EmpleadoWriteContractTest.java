package com.example.empleados.contract;

import com.example.empleados.controller.EmpleadoController;
import com.example.empleados.dto.EmpleadoResponse;
import com.example.empleados.dto.UpdateEmpleadoRequest;
import com.example.empleados.service.EmpleadoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmpleadoController.class)
class EmpleadoWriteContractTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmpleadoService empleadoService;

    @Test
    void shouldUpdateEmployee() throws Exception {
        EmpleadoResponse response = new EmpleadoResponse();
        response.setClave("E-001");
        response.setNombre("Nuevo");
        response.setDireccion("Nueva Dir");
        response.setTelefono("321");

        when(empleadoService.update(any(String.class), any(UpdateEmpleadoRequest.class))).thenReturn(response);

        String payload = """
            {
              \"nombre\": \"Nuevo\",
              \"direccion\": \"Nueva Dir\",
              \"telefono\": \"321\"
            }
            """;

        mockMvc.perform(put("/api/empleados/E-001")
                .with(httpBasic("admin", "admin123"))
            .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nombre").value("Nuevo"));
    }

    @Test
    void shouldDeleteEmployee() throws Exception {
        doNothing().when(empleadoService).delete("E-001");

        mockMvc.perform(delete("/api/empleados/E-001")
            .with(httpBasic("admin", "admin123"))
            .with(csrf()))
            .andExpect(status().isNoContent());
    }
}
