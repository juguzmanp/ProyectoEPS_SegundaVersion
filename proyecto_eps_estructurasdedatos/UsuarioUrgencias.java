package com.mycompany.proyecto_eps_estructurasdedatos;

public class UsuarioUrgencias extends Usuario{

    private int nivelTriage;

    public UsuarioUrgencias(Usuario base, int nivelTriage) {
        super(base.getTipoDocumento(),
                base.getNumeroDocumento(),
                base.getNombres(),
                base.getApellidos(),
                base.getEdad(),
                base.getSexo());
        this.nivelTriage = nivelTriage;
    }

    public int getNivelTriage() {
        return nivelTriage;
    }

    @Override
    public String toString() {
        return super.toString() + " - Nivel de triage " + nivelTriage;
    }
}