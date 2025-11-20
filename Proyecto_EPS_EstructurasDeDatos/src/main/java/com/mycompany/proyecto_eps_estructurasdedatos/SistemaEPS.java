package com.mycompany.proyecto_eps_estructurasdedatos;

import java.util.*;

public class SistemaEPS {

    private Scanner sc;
    private ArrayList<Usuario> usuarios;
    private Queue<Usuario> colaCitas;
    private PriorityQueue<UsuarioUrgencias> colaUrgencias;
    HashMap<Integer, RegistroCitaNormal> matrizDispersa = new HashMap<>();
    int horaActual = 24;
    

    public SistemaEPS() {
        usuarios = new ArrayList<>(); // Se crea un arreglo dinamico para almacenar los usuarios registrados
        colaCitas = new ArrayDeque<>(); // Se crea una cola
        colaUrgencias = new PriorityQueue<>(Comparator.comparingInt(UsuarioUrgencias::getNivelTriage)); //Se crea la cola con prioridad
        sc = new Scanner(System.in);
    }

    public void mostrarMenu() {
        int opcion;
        do {
            System.out.println("\n--- MENU PRINCIPAL ---");
            System.out.println("hora actual:"+ horaActual);
            System.out.println("1. Registrar usuario manual");
            System.out.println("2. Registrar N usuarios aleatorios");
            System.out.println("3. Solicitar cita");
            System.out.println("4. Solicitar atencion urgencias");
            System.out.println("5. Asignar citas aleatorias");
            System.out.println("6. Asignar urgencias aleatorias");
            System.out.println("7. Ver colas");
            System.out.println("8. Atender cita");
            System.out.println("9. Atender urgencia");
            System.out.println("10. Avanzar 1 hora");
            System.out.println("11. Salir");
            System.out.print("Seleccione una opcion: ");

            opcion = sc.nextInt(); //Asigna a la variable el entero que sera ingresado por el usuario
            sc.nextLine(); // Pide una entrada del usuario

            switch (opcion) {
                case 1:
                    registrarUsuario(); // Se llama el método
                    break;
                case 2: {
                    System.out.print("Ingrese la cantidad de usuarios a generar: ");
                    int cantUsuarios = sc.nextInt();
                    sc.nextLine();
                    registrarUsuariosAleatorios(cantUsuarios); // Se escribe tiene como entrada para el método la cantidad de usuarios a generar
                    break;
                }
                case 3:
                    solicitarCita();
                    break;
                case 4:
                    solicitarUrgencia();
                    break;
                case 5:
                    System.out.print("Ingrese la cantidad de citas a asignar aleatoriamente: ");
                    int n = sc.nextInt();
                    sc.nextLine();
                    asignarCitasAleatorias(n);
                    break;
                case 6:
                    System.out.print("Ingrese la cantidad de urgencias a asignar aleatoriamente: ");
                    int cantUsuariosUrg = sc.nextInt();
                    sc.nextLine();
                    asignarUrgenciasAleatorias(cantUsuariosUrg);
                    break;
                case 7:
                    mostrarColas();
                    break;
                case 8:
                    atenderCita();
                    break;
                case 9:
                    atenderUrgencia();
                    break;
                case 10:
                    if (horaActual == 24){
                        horaActual = 1;
                    }
                    else{
                        horaActual++;
                    }
                case 11:
                    System.out.println("Saliendo... ");
                    break;
                default:
                    System.out.println("Opcion no valida");
                    break;
            }
        } while (opcion != 11);
    }

    private void registrarUsuario() {
        System.out.println("Tipo de documento: ");
        System.out.println("1. TI\n2. CC\n3. CE");
        int tipo = sc.nextInt();
        sc.nextLine();

        String tipoDoc;
        switch (tipo) { // Se ejecuta una de las opciones dependiendo de si la entrada coincide
            case 1:
                tipoDoc = "TI";
                break;
            case 2:
                tipoDoc = "CC";
                break;
            case 3:
                tipoDoc = "CE";
                break;
            default:
                tipoDoc = "Desconocido";
                break;
        }

        System.out.print("Numero de documento: ");
        String numDoc = sc.nextLine();

        System.out.print("Nombres: ");
        String nombres = sc.nextLine();

        System.out.print("Apellidos: ");
        String apellidos = sc.nextLine();

        System.out.print("Edad: ");
        int edad = sc.nextInt();
        sc.nextLine();

        int sex;
        do{
            System.out.println("Sexo:\n1. Masculino\n2. Femenino");
            sex = sc.nextInt();
            sc.nextLine();
            if (sex != 1 && sex != 2) {
                System.out.println("Opcion no valida. Intente de nuevo.");
            }
        } while (sex != 1 && sex != 2); // Se espera con un ciclo hasta recibir una entrada valida

        String sexo;
        if(sex==1){
            sexo = "Masculino";
        }
        else{
            sexo = "Femenino";
        }

        Usuario usr = new Usuario(tipoDoc, numDoc, nombres, apellidos, edad, sexo);
        usuarios.add(usr);
        System.out.println("Usuario registrado correctamente.");
    }

    private void registrarUsuariosAleatorios(int cantidad) {
        String[] tiposDoc = {"TI", "CC", "CE"};
        String[] nombres = {"Juan", "Manuel", "Carlos", "María", "Pedro", "Ana", "Luis", "Carla", "Lucía", "Sara"};
        String[] apellidos = {"Pérez", "Gómez", "Rodríguez", "Martínez", "López", "Gonzales", "Hernandez", "Agudelo", "García", "Cruz"};
        String[] sexos = {"Masculino", "Femenino"};
        Random rnd = new Random();

        for (int i = 0; i < cantidad; i++) {
            String tipoDoc = tiposDoc[rnd.nextInt(tiposDoc.length)];
            String numDoc = String.valueOf(100000 + rnd.nextInt(900000));
            String nombre = nombres[rnd.nextInt(nombres.length)];
            String apellido = apellidos[rnd.nextInt(apellidos.length)];
            int edad = 1 + rnd.nextInt(99);
            String sexo = sexos[rnd.nextInt(sexos.length)];

            Usuario u = new Usuario(tipoDoc, numDoc, nombre, apellido, edad, sexo);
            usuarios.add(u);
        }
        System.out.println(cantidad + " usuarios aleatorios registrados.");
    }

    private void asignarCitasAleatorias(int cantidad) {
        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios registrados.");
            return;
        }
        Random rnd = new Random();
        for (int i = 0; i < cantidad; i++) {
            Usuario u = usuarios.get(rnd.nextInt(usuarios.size()));
            colaCitas.add(u);
        }
        System.out.println(cantidad + " citas aleatorias asignadas.");
    }

    private void asignarUrgenciasAleatorias(int cantidad) {
        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios registrados.");
            return;
        }
        Random rnd = new Random();
        for (int i = 0; i < cantidad; i++) {
            Usuario u = usuarios.get(rnd.nextInt(usuarios.size()));
            int nivelTriage = 1 + rnd.nextInt(5);
            colaUrgencias.add(new UsuarioUrgencias(u, nivelTriage));
        }
        System.out.println(cantidad + " urgencias aleatorias asignadas.");
    }

    private Usuario buscarUsuarioPorDocumento(String doc) {
        for (Usuario usr : usuarios) {
            if (usr.getNumeroDocumento().equals(doc)) return usr;
        }
        return null;
    }

    private void solicitarCita() {
        System.out.print("Ingrese numero de documento: ");
        String doc = sc.nextLine();
        Usuario usr = buscarUsuarioPorDocumento(doc);
        if (usr != null) {
            System.out.print("Ingrese la fecha: ");
            int hora = sc.nextInt();
            if (hora >=0 && hora <= 24){
                if (!matrizDispersa.containsKey(hora)) {
                    RegistroCitaNormal nuevaCita = new RegistroCitaNormal(usr,hora);
                    matrizDispersa.put(hora , nuevaCita);
                }
                else{
                    System.out.print("Horario no disponible");
                }
            }
            else{
                System.out.print("Hora no válida");
            }
            System.out.println("Usuario agregado a la cola de citas.");
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
            do{
                System.out.print("Ingrese nivel de urgencia: ");
                System.out.println("1. Triage I (Emergencia)");
                System.out.println("2. Triage II (Urgencia)");
                System.out.println("3. Triage III (Urgencia menor)");
                System.out.println("4. Triage IV (Consulta prioritaria)");
                System.out.println("5. Triage V (Consulta externa)");
                nivelTriage = sc.nextInt();
                sc.nextLine();
                if(nivelTriage < 1 || nivelTriage > 5){
                    System.out.println("Opcion no valida, intente de nuevo.");
                }
            } while (nivelTriage < 1 || nivelTriage > 5);

            colaUrgencias.add(new UsuarioUrgencias(usr, nivelTriage));
            System.out.println("Usuario agregado a la cola de urgencias con prioridad " + nivelTriage);
        } else {
            System.out.println("Usuario no encontrado.");
        }
    }

    private void mostrarColas() {
        System.out.println("\n--- Cola de citas ---");
        for (Usuario user : colaCitas) { //Recorre los objetos de tipo Usuario en la cola
            System.out.println(user); //Muestra en la consola los elementos
        }

        System.out.println("\n--- Cola de urgencias ---");
        PriorityQueue<UsuarioUrgencias> copia = new PriorityQueue<>(colaUrgencias); //Crea una copia de la cola con prioridad, para no alterar la original
        while (!copia.isEmpty()) { // Ejecuta el comando mientras la lista no este vacía
            UsuarioUrgencias usUrg = copia.poll(); // Saca los elementos de la lista copiada en orden de prioridad
            System.out.println(usUrg); // Imprime los elementos en el orden mencionado en la línea anterior
        }
    }

    private void atenderCita() {
        RegistroCitaNormal citaEliminada = matrizDispersa.remove(horaActual);
        if (citaEliminada != null) {
            System.out.println("Cita atendida del usuario" + citaEliminada.usuario.getNombres());
        }
        else{
            System.out.println("No se encontró una cita en la hora actual");
        }
    }

    private void atenderUrgencia() {
        UsuarioUrgencias atendido = colaUrgencias.poll();
        if (atendido != null) System.out.println("Atendiendo urgencia de: " + atendido);
        else System.out.println("No hay usuarios en la cola de urgencias.");
    }
}

