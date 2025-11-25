import java.util.*;

public class SistemaEPS {

    private Scanner sc;

    //ESTRUCTURA 1: DICCIONARIO (HashMap)
    //Reemplaza al ArrayList para búsqueda rápida O(1)
    private Map<String, Usuario> diccionarioUsuarios;

    //ESTRUCTURA 2: ÁRBOL BINARIO
    //Para mantener reportes ordenados
    private ArbolBinarioUsuarios arbolUsuarios;

    //ESTRUCTURAS 3 y 4: COLAS
    private Queue<Usuario> colaCitas;
    private PriorityQueue<UsuarioUrgencias> colaUrgencias;

    public SistemaEPS() {
        diccionarioUsuarios = new HashMap<>();
        arbolUsuarios = new ArbolBinarioUsuarios();
        colaCitas = new LinkedList<>();
        colaUrgencias = new PriorityQueue<>(Comparator.comparingInt(UsuarioUrgencias::getNivelTriage));
        sc = new Scanner(System.in);
    }

    public void mostrarMenu() {
        int opcion;
        do {
            System.out.println("\n--- MENU PRINCIPAL SISTEMA EPS ---");
            System.out.println("1. Registrar usuario manual");
            System.out.println("2. Registrar N usuarios aleatorios");
            System.out.println("3. Solicitar cita");
            System.out.println("4. Solicitar atencion urgencias");
            System.out.println("5. Asignar citas aleatorias");
            System.out.println("6. Asignar urgencias aleatorias");
            System.out.println("7. Ver colas de atención");
            System.out.println("8. Atender cita");
            System.out.println("9. Atender urgencia");
            System.out.println("10. Ver Directorio Ordenado (Árbol Binario)");
            System.out.println("11. Registrar Antecedente Médico (Vector Disperso)");
            System.out.println("12. Ver Historia Clínica Paciente");
            System.out.println("13. Salir");
            System.out.print("Seleccione una opcion: ");

            try {
                opcion = sc.nextInt();
                sc.nextLine(); //Limpiar buffer
            } catch (InputMismatchException e) {
                System.out.println("Por favor ingrese un número válido.");
                sc.nextLine();
                opcion = 0;
            }

            switch (opcion) {
                case 1: registrarUsuario(); break;
                case 2:
                    System.out.print("Cantidad de usuarios: ");
                    int cant = sc.nextInt(); sc.nextLine();
                    registrarUsuariosAleatorios(cant);
                    break;
                case 3: solicitarCita(); break;
                case 4: solicitarUrgencia(); break;
                case 5:
                    System.out.print("Cantidad de citas: ");
                    int n = sc.nextInt(); sc.nextLine();
                    asignarCitasAleatorias(n);
                    break;
                case 6:
                    System.out.print("Cantidad de urgencias: ");
                    int m = sc.nextInt(); sc.nextLine();
                    asignarUrgenciasAleatorias(m);
                    break;
                case 7: mostrarColas(); break;
                case 8: atenderCita(); break;
                case 9: atenderUrgencia(); break;
                case 10:
                    System.out.println("\n--- Directorio de Pacientes (Ordenado por ID) ---");
                    arbolUsuarios.mostrarInOrder();
                    break;
                case 11: registrarAntecedente(); break;
                case 12: verHistoriaClinica(); break;
                case 13: System.out.println("Saliendo... "); break;
                default: System.out.println("Opcion no valida"); break;
            }
        } while (opcion != 13);
    }

    private void registrarUsuario() {
        System.out.println("Tipo de documento (1.TI, 2.CC, 3.CE): ");
        int tipo = sc.nextInt(); sc.nextLine();
        String tipoDoc = (tipo == 1) ? "TI" : (tipo == 2) ? "CC" : "CE";

        System.out.print("Numero de documento: ");
        String numDoc = sc.nextLine();

        //Verificamos si ya existe en el Diccionario
        if (diccionarioUsuarios.containsKey(numDoc)) {
            System.out.println("¡Error! Ya existe un usuario con ese documento.");
            return;
        }

        System.out.print("Nombres: ");
        String nombres = sc.nextLine();
        System.out.print("Apellidos: ");
        String apellidos = sc.nextLine();
        System.out.print("Edad: ");
        int edad = sc.nextInt(); sc.nextLine();

        System.out.println("Sexo (1.Masculino, 2.Femenino): ");
        int sex = sc.nextInt(); sc.nextLine();
        String sexo = (sex == 1) ? "Masculino" : "Femenino";

        Usuario usr = new Usuario(tipoDoc, numDoc, nombres, apellidos, edad, sexo);

        //GUARDAMOS EN DICCIONARIO Y ARBOL
        diccionarioUsuarios.put(numDoc, usr);
        arbolUsuarios.insertar(usr);

        System.out.println("Usuario registrado exitosamente.");
    }

    private void registrarUsuariosAleatorios(int cantidad) {
        String[] tiposDoc = {"TI", "CC", "CE"};
        String[] nombres = {"Juan", "Manuel", "Carlos", "María", "Pedro", "Ana", "Luis", "Sofia"};
        String[] apellidos = {"Pérez", "Gómez", "Rodríguez", "Martínez", "López", "Garcia"};
        String[] sexos = {"Masculino", "Femenino"};
        Random rnd = new Random();

        for (int i = 0; i < cantidad; i++) {
            String tipoDoc = tiposDoc[rnd.nextInt(tiposDoc.length)];
            String numDoc = String.valueOf(100000 + rnd.nextInt(900000));
            String nombre = nombres[rnd.nextInt(nombres.length)];
            String apellido = apellidos[rnd.nextInt(apellidos.length)];
            int edad = 1 + rnd.nextInt(90);
            String sexo = sexos[rnd.nextInt(sexos.length)];

            if (!diccionarioUsuarios.containsKey(numDoc)) {
                Usuario u = new Usuario(tipoDoc, numDoc, nombre, apellido, edad, sexo);
                //GUARDAMOS EN AMBAS ESTRUCTURAS
                diccionarioUsuarios.put(numDoc, u);
                arbolUsuarios.insertar(u);
            } else {
                i--; //Reintentar si el ID ya existía
            }
        }
        System.out.println(cantidad + " usuarios aleatorios registrados.");
    }

    //Metodo vector disperso
    private void registrarAntecedente() {
        System.out.print("Ingrese documento del paciente: ");
        String doc = sc.nextLine();
        Usuario usr = diccionarioUsuarios.get(doc); // Búsqueda Rápida en Diccionario

        if (usr != null) {
            System.out.println("Paciente: " + usr.getNombres());
            System.out.print("Ingrese Código CIE (Enfermedad): ");
            int codigo = sc.nextInt(); sc.nextLine();
            System.out.print("Ingrese descripción del diagnóstico: ");
            String desc = sc.nextLine();

            usr.agregarAntecedente(codigo, desc); // Llenamos el vector disperso
            System.out.println("Antecedente registrado.");
        } else {
            System.out.println("Usuario no encontrado.");
        }
    }

    private void verHistoriaClinica() {
        System.out.print("Ingrese documento del paciente: ");
        String doc = sc.nextLine();
        Usuario usr = diccionarioUsuarios.get(doc);

        if (usr != null) {
            System.out.println("\n>>> RESUMEN CLÍNICO: " + usr);
            System.out.println(usr.mostrarHistoriaClinica());
        } else {
            System.out.println("Usuario no encontrado.");
        }
    }

    private Usuario buscarUsuarioPorDocumento(String doc) {
        // OPTIMIZACIÓN: Ya no recorremos un ArrayList, usamos el Mapa (O(1))
        return diccionarioUsuarios.get(doc);
    }

    private void solicitarCita() {
        System.out.print("Ingrese numero de documento: ");
        String doc = sc.nextLine();
        Usuario usr = buscarUsuarioPorDocumento(doc);
        if (usr != null) {
            colaCitas.add(usr);
            System.out.println("Agregado a cola de citas.");
        } else {
            System.out.println("Usuario no encontrado.");
        }
    }

    private void solicitarUrgencia() {
        System.out.print("Ingrese numero de documento: ");
        String doc = sc.nextLine();
        Usuario usr = buscarUsuarioPorDocumento(doc);
        if (usr != null) {
            int nivelTriage;
            do {
                System.out.println("Nivel triage (1-Emergencia a 5-Consulta): ");
                nivelTriage = sc.nextInt(); sc.nextLine();
            } while (nivelTriage < 1 || nivelTriage > 5);

            colaUrgencias.add(new UsuarioUrgencias(usr, nivelTriage));
            System.out.println("Agregado a urgencias con Triage " + nivelTriage);
        } else {
            System.out.println("Usuario no encontrado.");
        }
    }

    private void asignarCitasAleatorias(int cantidad) {
        if (diccionarioUsuarios.isEmpty()) {
            System.out.println("No hay usuarios.");
            return;
        }
        // Convertimos los valores del mapa a lista temporal para sacar aleatorios
        List<Usuario> listaTemp = new ArrayList<>(diccionarioUsuarios.values());
        Random rnd = new Random();
        for (int i = 0; i < cantidad; i++) {
            colaCitas.add(listaTemp.get(rnd.nextInt(listaTemp.size())));
        }
        System.out.println(cantidad + " citas asignadas.");
    }

    private void asignarUrgenciasAleatorias(int cantidad) {
        if (diccionarioUsuarios.isEmpty()) return;

        List<Usuario> listaTemp = new ArrayList<>(diccionarioUsuarios.values());
        Random rnd = new Random();
        for (int i = 0; i < cantidad; i++) {
            Usuario u = listaTemp.get(rnd.nextInt(listaTemp.size()));
            if (existeEnColaUrgencias(u.getNumeroDocumento())) {
                i--; continue;
            }
            colaUrgencias.add(new UsuarioUrgencias(u, 1 + rnd.nextInt(5)));
        }
        System.out.println(cantidad + " urgencias asignadas.");
    }

    private boolean existeEnColaUrgencias(String doc) {
        for (UsuarioUrgencias u : colaUrgencias) {
            if (u.getNumeroDocumento().equals(doc)) return true;
        }
        return false;
    }

    private void mostrarColas() {
        System.out.println("\n--- Cola de citas (" + colaCitas.size() + ") ---");
        for (Usuario user : colaCitas) System.out.println(user);

        System.out.println("\n--- Cola de urgencias (" + colaUrgencias.size() + ") ---");
        PriorityQueue<UsuarioUrgencias> copia = new PriorityQueue<>(colaUrgencias);
        while (!copia.isEmpty()) System.out.println(copia.poll());
    }

    private void atenderCita() {
        if (!colaCitas.isEmpty()) System.out.println("Atendiendo a: " + colaCitas.poll());
        else System.out.println("Cola vacía.");
    }

    private void atenderUrgencia() {
        if (!colaUrgencias.isEmpty()) System.out.println("Atendiendo urgencia de: " + colaUrgencias.poll());
        else System.out.println("Cola urgencias vacía.");
    }
}
