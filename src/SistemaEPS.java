import java.util.*;

public class SistemaEPS {

    private Map<String, Usuario> diccionarioUsuarios;
    private ArbolBinarioUsuarios arbolUsuarios;
    private Queue<Usuario> colaCitas;
    private PriorityQueue<UsuarioUrgencias> colaUrgencias;

    public SistemaEPS() {
        diccionarioUsuarios = new HashMap<>();
        arbolUsuarios = new ArbolBinarioUsuarios();
        colaCitas = new LinkedList<>();
        colaUrgencias = new PriorityQueue<>(Comparator.comparingInt(UsuarioUrgencias::getNivelTriage));
    }

    public String registrarUsuario(String tipoDoc, String numDoc, String nombres, String apellidos, int edad, String sexo) {
        if (diccionarioUsuarios.containsKey(numDoc)) {
            return "¡Error! Ya existe un usuario con documento " + numDoc;
        }

        // Validación básica de tipos de documentos
        if (!tipoDoc.equals("TI") && !tipoDoc.equals("CC") && !tipoDoc.equals("CE")) {
            return "¡Error! Tipo de documento no válido. Use TI, CC o CE.";
        }

        Usuario usr = new Usuario(tipoDoc, numDoc, nombres, apellidos, edad, sexo);

        // Guardamos en Diccionario y Árbol
        diccionarioUsuarios.put(numDoc, usr);
        arbolUsuarios.insertar(usr);

        return "Usuario " + usr.getNombres() + " registrado exitosamente.";
    }

    public Usuario buscarUsuarioPorDocumento(String doc) {
        return diccionarioUsuarios.get(doc);
    }

    // --- MÉTODOS DE GESTIÓN DE COLAS ---

    /**
     * Solicita una cita (añade a la cola FIFO).
     * @return Mensaje de estado.
     */
    public String solicitarCita(String doc) {
        Usuario usr = buscarUsuarioPorDocumento(doc);
        if (usr != null) {
            colaCitas.add(usr);
            return "Usuario agregado a la cola de citas.";
        } else {
            return "Usuario no encontrado.";
        }
    }

    /**
     * Solicita una urgencia (añade a la PriorityQueue).
     * @return Mensaje de estado.
     */
    public String solicitarUrgencia(String doc, int nivelTriage) {
        if (nivelTriage < 1 || nivelTriage > 5) {
            return "Nivel de triage inválido (debe ser 1-5).";
        }

        Usuario usr = buscarUsuarioPorDocumento(doc);
        if (usr != null) {
            colaUrgencias.add(new UsuarioUrgencias(usr, nivelTriage));
            return "Usuario agregado a la cola de urgencias con prioridad " + nivelTriage;
        } else {
            return "Usuario no encontrado.";
        }
    }

    /**
     * Atiende la próxima cita (FIFO).
     * @return Mensaje con el usuario atendido o que la cola está vacía.
     */
    public String atenderCita() {
        Usuario atendido = colaCitas.poll();
        if (atendido != null) {
            return "Atendiendo cita de: " + atendido.toString();
        } else {
            return "No hay usuarios en la cola de citas.";
        }
    }

    /**
     * Atiende la urgencia de mayor prioridad.
     * @return Mensaje con el usuario atendido o que la cola está vacía.
     */
    public String atenderUrgencia() {
        UsuarioUrgencias atendido = colaUrgencias.poll();
        if (atendido != null) {
            return "Atendiendo urgencia (Triage " + atendido.getNivelTriage() + ") de: " + atendido.toString();
        } else {
            return "No hay usuarios en la cola de urgencias.";
        }
    }

    // --- MÉTODOS PARA EL VECTOR DISPERSO (HISTORIA CLÍNICA) ---

    /**
     * Registra un antecedente en el vector disperso del usuario.
     * @return Mensaje de estado.
     */
    public String registrarAntecedente(String doc, int codigoCIE, String descripcion) {
        Usuario usr = diccionarioUsuarios.get(doc);
        if (usr != null) {
            usr.agregarAntecedente(codigoCIE, descripcion);
            return "Antecedente (" + codigoCIE + ") registrado para " + usr.getNombres();
        } else {
            return "Usuario no encontrado.";
        }
    }

    /**
     * Devuelve el resumen de la historia clínica.
     * @return String con la historia clínica o mensaje de no encontrado.
     */
    public String obtenerHistoriaClinica(String doc) {
        Usuario usr = diccionarioUsuarios.get(doc);
        if (usr != null) {
            return "PACIENTE: " + usr.getNombres() + "\n" + usr.mostrarHistoriaClinica();
        } else {
            return "Usuario no encontrado.";
        }
    }

    // --- MÉTODOS PARA DEVOLVER COLECCIONES (VISTAS) ---

    /**
     * @return La colección de usuarios en la Cola de Citas (para mostrar en JList).
     */
    public Collection<Usuario> obtenerColaCitas() {
        return colaCitas;
    }

    /**
     * @return La colección de usuarios en la Cola de Urgencias.
     * Nota: Se usa una copia para no alterar el orden de la PriorityQueue al iterar.
     */
    public Collection<UsuarioUrgencias> obtenerColaUrgenciasOrdenada() {
        // Creamos una copia para poder iterar en el orden de prioridad sin vaciar la original
        PriorityQueue<UsuarioUrgencias> copia = new PriorityQueue<>(colaUrgencias);
        List<UsuarioUrgencias> listaOrdenada = new ArrayList<>();
        while (!copia.isEmpty()) {
            listaOrdenada.add(copia.poll());
        }
        return listaOrdenada;
    }

    /**
     * @return La colección de todos los usuarios registrados (para reportes no ordenados).
     */
    public Collection<Usuario> obtenerTodosUsuarios() {
        return diccionarioUsuarios.values();
    }

    /**
     * Ejecuta el recorrido In-Order del Árbol Binario y lo retorna como String para mostrar.
     * @return String con el reporte de usuarios ordenados por documento.
     */
    public String obtenerDirectorioOrdenado() {
        StringBuilder sb = new StringBuilder();

        // Ahora llamamos al método obtenerReporteInOrder
        arbolUsuarios.obtenerReporteInOrder(sb);

        if (sb.length() == 0) {
            return "El directorio está vacío.";
        }
        return sb.toString();
    }

    // NOTA: Para que obtenerDirectorioOrdenado funcione, debes agregar este método
    // público auxiliar a la clase ArbolBinarioUsuarios:
    /*
    public void obtenerInOrderRec(NodoArbol nodo, StringBuilder sb) {
        if (nodo != null) {
            obtenerInOrderRec(nodo.izquierdo, sb);
            sb.append(" - ").append(nodo.usuario).append("\n");
            obtenerInOrderRec(nodo.derecho, sb);
        }
    }
    */
}