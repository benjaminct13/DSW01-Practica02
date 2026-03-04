package com.example.empleados.repository;

import com.example.empleados.domain.EmpleadoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpleadoRepository extends JpaRepository<EmpleadoEntity, String> {
}
