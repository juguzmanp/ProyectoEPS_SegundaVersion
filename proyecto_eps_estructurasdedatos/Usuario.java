package com.mycompany.proyecto_eps_estructurasdedatos;

public class Usuario {

    private String tipoDocumento;
    private String numeroDocumento;
    private String nombres;
    private String apellidos;
    private int edad;
    private String sexo;

    public Usuario(String tipoDocumento, String numeroDocumento,
                   String nombres, String apellidos, int edad, String sexo) {
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.edad = edad;
        this.sexo = sexo;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public String getNombres() {
        return nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public int getEdad() {
        return edad;
    }

    public String getSexo() {
        return sexo;
    }

    @Override
    public String toString() {
        return nombres + " " + apellidos + " (" + tipoDocumento + " " + numeroDocumento + ")";
    }
}