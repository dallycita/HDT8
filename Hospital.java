import java.io.*;
import java.util.*;

public class Hospital {
    private static final String ARCHIVO_PACIENTES = "pacientes.txt";
    private static final String ARCHIVO_ATENDIDOS = "PacientesAtendidos.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        VectorHeap<Paciente> cola = new VectorHeap<>();

        // Cargar los pacientes desde el archivo al iniciar el programa
        cargarPacientes(cola);

        while (true) {
            System.out.println("\nMenú:");
            System.out.println("1. Agregar paciente");
            System.out.println("2. Atender siguiente paciente");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opción: ");
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir salto de línea

            switch (opcion) {
                case 1:
                    agregarPaciente(scanner, cola);
                    break;
                case 2:
                    atenderPaciente(cola);
                    break;
                case 3:
                    System.out.println("Saliendo del sistema...");
                    return;
                default:
                    System.out.println("Opción no válida, intente de nuevo.");
            }
        }
    }

    private static void agregarPaciente(Scanner scanner, VectorHeap<Paciente> cola) {
        System.out.print("Ingrese nombre del paciente: ");
        String nombre = scanner.nextLine();
        System.out.print("Ingrese síntoma del paciente: ");
        String sintoma = scanner.nextLine();
        System.out.print("Ingrese código de emergencia (A-E): ");
        char codigoEmergencia = scanner.next().charAt(0);
        scanner.nextLine(); // Consumir el salto de línea

        Paciente nuevoPaciente = new Paciente(nombre, sintoma, codigoEmergencia);

        // Guardar en el archivo pacientes.txt
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO_PACIENTES, true))) {
            writer.write(nuevoPaciente.toString());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error al guardar el paciente.");
        }

        // Agregar el nuevo paciente a la cola de prioridad en memoria
        cola.insert(nuevoPaciente);
        System.out.println("Paciente agregado correctamente.");
    }

    private static void cargarPacientes(VectorHeap<Paciente> cola) {
        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_PACIENTES))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(", ");
                if (datos.length == 3) {
                    cola.insert(new Paciente(datos[0], datos[1], datos[2].charAt(0)));
                }
            }
        } catch (IOException e) {
            System.out.println("No se encontró el archivo de pacientes o hubo un error al leerlo.");
        }
    }

    private static void atenderPaciente(VectorHeap<Paciente> cola) {
        if (cola.isEmpty()) {
            System.out.println("No hay pacientes en espera.");
            return;
        }

        // Obtener y eliminar el paciente con mayor prioridad
        Paciente atendido = cola.remove();
        System.out.println("\nAtendiendo a: " + atendido);

        // Actualizar pacientes.txt eliminando el paciente atendido
        actualizarArchivoPacientes(atendido);

        // Guardar el paciente atendido en PacientesAtendidos.txt
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO_ATENDIDOS, true))) {
            writer.write(atendido.toString());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error al guardar el paciente atendido.");
        } 
    }

    private static void actualizarArchivoPacientes(Paciente atendido) {
        List<String> pacientesRestantes = new ArrayList<>();

        // Leer todos los pacientes y excluir al que fue atendido
        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_PACIENTES))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (!linea.equals(atendido.toString())) {
                    pacientesRestantes.add(linea);
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo de pacientes.");
        }

        // Reescribir pacientes.txt con los pacientes restantes
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO_PACIENTES))) {
            for (String paciente : pacientesRestantes) {
                writer.write(paciente);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error al actualizar la lista de pacientes.");
        }
    }
}
