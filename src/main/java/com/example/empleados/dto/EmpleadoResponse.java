package com.example.empleados.dto;

public class EmpleadoResponse {

    private String clave;
    private String nombre;
    private String direccion;
    private String telefono;
    private DepartamentoResumenResponse departamento;

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public DepartamentoResumenResponse getDepartamento() {
        return departamento;
    }

    public void setDepartamento(DepartamentoResumenResponse departamento) {
        this.departamento = departamento;
    }
}
