package com.example.empleados.service;

import com.example.empleados.domain.EmpleadoEntity;
import com.example.empleados.domain.DepartamentoEntity;
import com.example.empleados.dto.CreateEmpleadoRequest;
import com.example.empleados.dto.DepartamentoResumenResponse;
import com.example.empleados.dto.EmpleadoResponse;
import com.example.empleados.dto.UpdateEmpleadoRequest;
import com.example.empleados.repository.DepartamentoRepository;
import com.example.empleados.repository.EmpleadoRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class EmpleadoService {

    private final EmpleadoRepository empleadoRepository;
    private final DepartamentoRepository departamentoRepository;
    private final ClaveEmpleadoGenerator claveEmpleadoGenerator;

    public EmpleadoService(
        EmpleadoRepository empleadoRepository,
        DepartamentoRepository departamentoRepository,
        ClaveEmpleadoGenerator claveEmpleadoGenerator
    ) {
        this.empleadoRepository = empleadoRepository;
        this.departamentoRepository = departamentoRepository;
        this.claveEmpleadoGenerator = claveEmpleadoGenerator;
    }

    @Transactional
    public EmpleadoResponse create(CreateEmpleadoRequest request) {
        EmpleadoEntity entity = new EmpleadoEntity();
        entity.setClave(claveEmpleadoGenerator.nextClave());
        entity.setNombre(request.getNombre().trim());
        entity.setDireccion(request.getDireccion().trim());
        entity.setTelefono(request.getTelefono().trim());
        entity.setDepartamento(findDepartamento(request.getDepartamentoId()));

        try {
            EmpleadoEntity saved = empleadoRepository.save(entity);
            return toResponse(saved);
        } catch (DataIntegrityViolationException exception) {
            throw new DataIntegrityViolationException("No fue posible crear el empleado por conflicto de datos", exception);
        }
    }

    @Transactional(readOnly = true)
    public EmpleadoResponse getByClave(String clave) {
        EmpleadoEntity empleado = empleadoRepository.findById(clave)
            .orElseThrow(() -> new NoSuchElementException("Empleado no encontrado para clave: " + clave));
        return toResponse(empleado);
    }

    @Transactional(readOnly = true)
    public List<EmpleadoResponse> listAll() {
        return empleadoRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional
    public EmpleadoResponse update(String clave, UpdateEmpleadoRequest request) {
        EmpleadoEntity empleado = empleadoRepository.findById(clave)
            .orElseThrow(() -> new NoSuchElementException("Empleado no encontrado para clave: " + clave));

        empleado.setNombre(request.getNombre().trim());
        empleado.setDireccion(request.getDireccion().trim());
        empleado.setTelefono(request.getTelefono().trim());
        empleado.setDepartamento(findDepartamento(request.getDepartamentoId()));

        EmpleadoEntity updated = empleadoRepository.save(empleado);
        return toResponse(updated);
    }

    @Transactional
    public void delete(String clave) {
        if (!empleadoRepository.existsById(clave)) {
            throw new NoSuchElementException("Empleado no encontrado para clave: " + clave);
        }
        empleadoRepository.deleteById(clave);
    }

    private EmpleadoResponse toResponse(EmpleadoEntity entity) {
        EmpleadoResponse response = new EmpleadoResponse();
        response.setClave(entity.getClave());
        response.setNombre(entity.getNombre());
        response.setDireccion(entity.getDireccion());
        response.setTelefono(entity.getTelefono());
        DepartamentoResumenResponse departamento = new DepartamentoResumenResponse();
        departamento.setId(entity.getDepartamento().getId());
        departamento.setNombre(entity.getDepartamento().getNombre());
        response.setDepartamento(departamento);
        return response;
    }

    private DepartamentoEntity findDepartamento(String departamentoId) {
        return departamentoRepository.findById(departamentoId)
            .orElseThrow(() -> new NoSuchElementException("Departamento no encontrado para id: " + departamentoId));
    }
}
