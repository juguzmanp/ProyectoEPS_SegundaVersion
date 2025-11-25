import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Collection;

public class VentanaPrincipal extends JFrame {

    // 1. Instancia del Modelo
    private SistemaEPS sistema;

    // Componentes para el registro
    private JTextField txtDocumento, txtNombres, txtApellidos, txtEdad;
    private JComboBox<String> cmbTipoDoc, cmbSexo;
    private JTextArea txtSalida; // Para mostrar mensajes de éxito/error y reportes

    // Componentes para las Colas (JList o JTextArea simples por ahora)
    private JList<Usuario> listaCitas;
    private JList<UsuarioUrgencias> listaUrgencias;

    private JPanel crearPanelHistoriaClinica() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Historia Clínica (Vector Disperso)"));

        // Panel para controles de entrada
        JPanel controles = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField txtDocHistoria = new JTextField(8);

        JButton btnVerHistoria = new JButton("Ver Historia");
        btnVerHistoria.addActionListener(e -> manejarVerHistoria(txtDocHistoria));

        JButton btnRegistrarAntecedente = new JButton("Registrar Antecedente");
        btnRegistrarAntecedente.addActionListener(e -> manejarRegistrarAntecedente(txtDocHistoria));

        controles.add(new JLabel("Documento:"));
        controles.add(txtDocHistoria);
        controles.add(btnVerHistoria);
        controles.add(btnRegistrarAntecedente);

        panel.add(controles, BorderLayout.NORTH);

        // Usaremos el área de salida general (txtSalida) para mostrar los resultados.

        return panel;
    }

    // Modelos de datos para las listas
    private DefaultListModel<Usuario> modeloCitas;
    private DefaultListModel<UsuarioUrgencias> modeloUrgencias;

    public VentanaPrincipal() {
        this.sistema = new SistemaEPS();
        configurarVentana();
        crearComponentes();
        actualizarListas();
        setVisible(true);
    }

    private void configurarVentana() {
        setTitle("Sistema EPS - Gestión de Estructuras");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        // Usamos un BorderLayout para organizar los paneles principales
        setLayout(new BorderLayout(10, 10));
    }

    //Metodo principal para organizar todos los paneles
    private void crearComponentes() {
        // Panel Norte: Título o encabezado (opcional)
        JLabel titulo = new JLabel("GESTIÓN DE USUARIOS Y ESTRUCTURAS DE DATOS", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        add(titulo, BorderLayout.NORTH);

        // Panel Central: Distribuye el registro y las colas
        JPanel panelCentral = new JPanel(new GridLayout(1, 2, 10, 10));
        panelCentral.add(crearPanelRegistro());
        panelCentral.add(crearPanelColas());
        add(panelCentral, BorderLayout.CENTER);

        // Panel Sur: Consola de Salida y Reportes (Árbol)
        JPanel panelInferior = new JPanel(new GridLayout(2, 1, 10, 5)); // Usamos dos filas
        panelInferior.add(crearPanelHistoriaClinica()); // <--- ¡AÑADIR AQUÍ!
        panelInferior.add(crearPanelSalidaYReportes());

        add(panelInferior, BorderLayout.SOUTH); // Añadimos el nuevo panel inferior al marco principal
    }

    // --- 2. PANELES SECUNDARIOS ---

    private JPanel crearPanelRegistro() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Registro de Usuarios"));

        // Panel para los campos de texto
        JPanel campos = new JPanel(new GridLayout(6, 2, 5, 5));

        cmbTipoDoc = new JComboBox<>(new String[]{"TI", "CC", "CE"});
        cmbSexo = new JComboBox<>(new String[]{"Masculino", "Femenino"});
        txtDocumento = new JTextField(10);
        txtNombres = new JTextField(10);
        txtApellidos = new JTextField(10);
        txtEdad = new JTextField(10);

        campos.add(new JLabel("Tipo Documento:"));
        campos.add(cmbTipoDoc);
        campos.add(new JLabel("No. Documento:"));
        campos.add(txtDocumento);
        campos.add(new JLabel("Nombres:"));
        campos.add(txtNombres);
        campos.add(new JLabel("Apellidos:"));
        campos.add(txtApellidos);
        campos.add(new JLabel("Edad:"));
        campos.add(txtEdad);
        campos.add(new JLabel("Sexo:"));
        campos.add(cmbSexo);

        panel.add(campos, BorderLayout.NORTH);

        // Botón Registrar
        JButton btnRegistrar = new JButton("Registrar Usuario y Agregar al Árbol");
        btnRegistrar.addActionListener(this::manejarRegistroUsuario);
        panel.add(btnRegistrar, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearPanelColas() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));

        // Panel Cola de Citas (FIFO - LinkedList)
        modeloCitas = new DefaultListModel<>();
        listaCitas = new JList<>(modeloCitas);
        listaCitas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JPanel panelCitas = new JPanel(new BorderLayout());
        panelCitas.setBorder(BorderFactory.createTitledBorder("Citas (FIFO)"));
        panelCitas.add(new JScrollPane(listaCitas), BorderLayout.CENTER);

        JButton btnAtenderCita = new JButton("Atender Cita");
        btnAtenderCita.addActionListener(this::manejarAtenderCita);
        panelCitas.add(btnAtenderCita, BorderLayout.SOUTH);

        // Panel Cola de Urgencias (PriorityQueue)
        modeloUrgencias = new DefaultListModel<>();
        listaUrgencias = new JList<>(modeloUrgencias);
        listaUrgencias.setCellRenderer(new UrgenciasListRenderer()); // Para colorear por Triage
        JPanel panelUrgencias = new JPanel(new BorderLayout());
        panelUrgencias.setBorder(BorderFactory.createTitledBorder("Urgencias (Prioridad)"));
        panelUrgencias.add(new JScrollPane(listaUrgencias), BorderLayout.CENTER);

        JButton btnAtenderUrgencia = new JButton("Atender Urgencia");
        btnAtenderUrgencia.addActionListener(this::manejarAtenderUrgencia);
        panelUrgencias.add(btnAtenderUrgencia, BorderLayout.SOUTH);

        panel.add(panelCitas);
        panel.add(panelUrgencias);

        return panel;
    }

    private JPanel crearPanelSalidaYReportes() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Salida y Reportes"));

        txtSalida = new JTextArea(8, 20);
        txtSalida.setEditable(false);
        JScrollPane scrollSalida = new JScrollPane(txtSalida);

        // Botones de Acciones Secundarias
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        JButton btnSolicitarCita = new JButton("Solicitar Cita por ID");
        btnSolicitarCita.addActionListener(e -> manejarSolicitarAtencion(false));

        JButton btnSolicitarUrgencia = new JButton("Solicitar Urgencia por ID");
        btnSolicitarUrgencia.addActionListener(e -> manejarSolicitarAtencion(true));

        JButton btnVerArbol = new JButton("Ver Directorio Ordenado (Árbol)");
        btnVerArbol.addActionListener(this::manejarVerArbol);

        panelBotones.add(btnSolicitarCita);
        panelBotones.add(btnSolicitarUrgencia);
        panelBotones.add(btnVerArbol);

        panel.add(scrollSalida, BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.NORTH); // Colocamos los botones arriba del área de texto

        return panel;
    }

    // --- 3. MANEJO DE EVENTOS (Controlador) ---

    private void manejarRegistroUsuario(ActionEvent e) {
        try {
            String tipoDoc = (String) cmbTipoDoc.getSelectedItem();
            String doc = txtDocumento.getText().trim();
            String nombres = txtNombres.getText().trim();
            String apellidos = txtApellidos.getText().trim();
            int edad = Integer.parseInt(txtEdad.getText().trim());
            String sexo = (String) cmbSexo.getSelectedItem();

            if (doc.isEmpty() || nombres.isEmpty() || apellidos.isEmpty()) {
                mostrarMensaje("Error: Todos los campos son obligatorios.", true);
                return;
            }

            String resultado = sistema.registrarUsuario(tipoDoc, doc, nombres, apellidos, edad, sexo);
            mostrarMensaje(resultado, resultado.startsWith(" "));

            // Limpiar campos
            txtDocumento.setText("");
            txtNombres.setText("");
            txtApellidos.setText("");
            txtEdad.setText("");

        } catch (NumberFormatException ex) {
            mostrarMensaje("Error: La edad y el documento deben ser números válidos.", true);
        }
    }

    private void manejarSolicitarAtencion(boolean esUrgencia) {
        String doc = JOptionPane.showInputDialog(this, "Ingrese el documento del paciente:");
        if (doc == null || doc.trim().isEmpty()) return;

        String resultado;
        if (esUrgencia) {
            String triageStr = JOptionPane.showInputDialog(this, "Ingrese Nivel de Triage (1-5):");
            try {
                int triage = Integer.parseInt(triageStr.trim());
                resultado = sistema.solicitarUrgencia(doc.trim(), triage);
            } catch (NumberFormatException ex) {
                resultado = "Error: El triage debe ser un número entero (1-5).";
            }
        } else {
            resultado = sistema.solicitarCita(doc.trim());
        }

        mostrarMensaje(resultado, resultado.startsWith(" "));
        actualizarListas();
    }

    private void manejarAtenderCita(ActionEvent e) {
        String resultado = sistema.atenderCita();
        mostrarMensaje(resultado, resultado.startsWith(" "));
        actualizarListas();
    }

    private void manejarAtenderUrgencia(ActionEvent e) {
        String resultado = sistema.atenderUrgencia();
        mostrarMensaje(resultado, resultado.startsWith(" "));
        actualizarListas();
    }

    private void manejarVerArbol(ActionEvent e) {
        String reporte = sistema.obtenerDirectorioOrdenado();
        txtSalida.setText("--- Directorio de Usuarios Ordenado por Documento (Árbol Binario) ---\n" + reporte);
    }

    private void manejarRegistrarAntecedente(JTextField txtDocHistoria) {
        String doc = txtDocHistoria.getText().trim();
        if (doc.isEmpty()) {
            mostrarMensaje(" Ingrese el número de documento para registrar el antecedente.", true);
            return;
        }

        // Pedir los datos del antecedente usando JOptionPane
        String codigoStr = JOptionPane.showInputDialog(this, "Ingrese Código CIE (SOLO NÚMEROS):");
        if (codigoStr == null || codigoStr.isEmpty()) return;

        String descripcion = JOptionPane.showInputDialog(this, "Ingrese Descripción del Diagnóstico:");
        if (descripcion == null || descripcion.isEmpty()) return;

        try {
            int codigo = Integer.parseInt(codigoStr.trim());
            String resultado = sistema.registrarAntecedente(doc, codigo, descripcion);
            mostrarMensaje(resultado, resultado.startsWith("- "));
        } catch (NumberFormatException ex) {
            mostrarMensaje(" Error: El Código CIE debe ser un número válido.", true);
        }
    }

    private void manejarVerHistoria(JTextField txtDocHistoria) {
        String doc = txtDocHistoria.getText().trim();
        if (doc.isEmpty()) {
            mostrarMensaje(" Ingrese el número de documento para ver la historia clínica.", true);
            return;
        }

        // Llamar al modelo para obtener la historia
        String resultado = sistema.obtenerHistoriaClinica(doc);

        // Limpiar el área de salida y mostrar el resultado
        txtSalida.setText("--- Historia Clínica ---\n" + resultado);
        txtSalida.setCaretPosition(0); // Mostrar desde el inicio
    }

    // --- 4. MÉTODOS DE UTILIDAD Y VISTA ---

    // Llena las JList con los datos del Modelo (SistemaEPS)
    private void actualizarListas() {
        // Actualizar Cola de Citas
        modeloCitas.clear();
        sistema.obtenerColaCitas().forEach(modeloCitas::addElement);

        // Actualizar Cola de Urgencias (Ordenada por prioridad)
        modeloUrgencias.clear();
        sistema.obtenerColaUrgenciasOrdenada().forEach(modeloUrgencias::addElement);
    }

    // Muestra el resultado de las operaciones en el área de texto de salida
    private void mostrarMensaje(String mensaje, boolean esExito) {
        String prefijo = esExito ? "- " : "+ ";
        txtSalida.append(prefijo + mensaje + "\n");
        txtSalida.setCaretPosition(txtSalida.getDocument().getLength()); // Scroll al final
    }

    // --- 5. CLASE MAIN PARA EJECUTAR ---
    public static void main(String[] args) {
        // Ejecuta la GUI en el hilo de despacho de eventos de Swing (best practice)
        SwingUtilities.invokeLater(VentanaPrincipal::new);
    }
}