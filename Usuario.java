import java.util.HashMap;
import java.util.Map;

public class Usuario {

    private String tipoDocumento;
    private String numeroDocumento;
    private String nombres;
    private String apellidos;
    private int edad;
    private String sexo;

    // ESTRUCTURA: VECTOR DISPERSO
    // Usamos un Map para simular un vector que solo guarda posiciones ocupadas.
    // Key (Integer): Código de la enfermedad (índice del vector).
    // Value (String): Descripción del diagnóstico.
    private Map<Integer, String> historiaClinica;

    public Usuario(String tipoDocumento, String numeroDocumento,
                   String nombres, String apellidos, int edad, String sexo) {
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.edad = edad;
        this.sexo = sexo;
        // Inicializamos el "vector disperso" vacío
        this.historiaClinica = new HashMap<>();
    }

    // --- MÉTODOS PARA EL VECTOR DISPERSO (HISTORIA CLÍNICA) ---

    // Agrega un antecedente. Si el código ya existe, actualiza la descripción.
    public void agregarAntecedente(int codigoEnfermedad, String descripcion) {
        historiaClinica.put(codigoEnfermedad, descripcion);
    }

    // Retorna un String con todos los antecedentes registrados formateados
    public String mostrarHistoriaClinica() {
        if (historiaClinica.isEmpty()) {
            return "   >> No registra antecedentes médicos.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("   >> HISTORIA CLÍNICA (Código: Diagnóstico) <<\n");

        // Recorremos solo los elementos existentes (Eficiencia del Vector Disperso)
        for (Map.Entry<Integer, String> entrada : historiaClinica.entrySet()) {
            sb.append("      - [Cod. ").append(entrada.getKey()).append("]: ")
                    .append(entrada.getValue()).append("\n");
        }
        return sb.toString();
    }

    // --- GETTERS ---

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

    // --- TO STRING ---

    @Override
    public String toString() {
        return nombres + " " + apellidos + " (" + tipoDocumento + " " + numeroDocumento + ")";
    }
}