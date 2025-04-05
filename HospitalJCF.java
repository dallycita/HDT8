import java.util.PriorityQueue;
import java.io.*;
import java.util.*;

/**
 * Clase principal que simula la gestión de pacientes en un hospital.
 * Utiliza una cola de prioridad para atender primero a los pacientes con mayor urgencia.
 * También permite guardar y leer la información desde archivos.
 */
public class HospitalJCF {

    /**
     * Método principal. Muestra un menú con opciones para agregar, atender pacientes o salir.
     *
     * @param args Argumentos de línea de comandos (no se utilizan).
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        PriorityQueue<Paciente> cola = new PriorityQueue<>();

        cargarPacientes(cola);

        while (true) {
            System.out.println("\nMenú:");
            System.out.println("1. Agregar paciente");
            System.out.println("2. Atender siguiente paciente");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opción: ");
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Limpia el buffer

            switch (opcion) {
                case 1:
                    agregarPaciente(scanner, cola);
                    break;
                case 2:
                    atenderPaciente(cola);
                    break;
                case 3:
                    System.out.println("Saliendo...");
                    return;
                default:
                    System.out.println("Opción inválida.");
            }
        }
    }

    /**
     * Agrega un nuevo paciente a la cola y guarda su información en el archivo de texto.
     *
     * @param scanner Scanner para leer los datos desde consola.
     * @param cola    Cola de prioridad donde se agrega el paciente.
     */
    private static void agregarPaciente(Scanner scanner, PriorityQueue<Paciente> cola) {
        System.out.print("Ingrese nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Ingrese síntoma: ");
        String sintoma = scanner.nextLine();
        System.out.print("Ingrese código de emergencia (A-E): ");
        char codigoEmergencia = scanner.next().charAt(0);
        scanner.nextLine();

        Paciente nuevoPaciente = new Paciente(nombre, sintoma, codigoEmergencia);

        // Guardar en archivo
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("pacientes.txt", true))) {
            writer.write(nuevoPaciente.toString());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error al guardar el paciente.");
        }

        cola.add(nuevoPaciente);
        System.out.println("Paciente agregado correctamente.");
    }

    /**
     * Carga los pacientes almacenados en el archivo y los agrega a la cola de prioridad.
     *
     * @param cola Cola donde se agregarán los pacientes cargados del archivo.
     */
    private static void cargarPacientes(PriorityQueue<Paciente> cola) {
        try (BufferedReader br = new BufferedReader(new FileReader("pacientes.txt"))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(", ");
                if (datos.length == 3) {
                    cola.add(new Paciente(datos[0], datos[1], datos[2].charAt(0)));
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo.");
        }
    }

    /**
     * Atiende al paciente con mayor prioridad (el primero en la cola).
     * También lo guarda en un archivo de pacientes atendidos y actualiza el archivo original.
     *
     * @param cola Cola de prioridad de donde se sacará al paciente.
     */
    private static void atenderPaciente(PriorityQueue<Paciente> cola) {
        if (cola.isEmpty()) {
            System.out.println("No hay pacientes.");
            return;
        }

        Paciente atendido = cola.poll();
        System.out.println("\nAtendiendo: " + atendido);

        // Guardar en archivo de pacientes atendidos
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("PacientesAtendidos.txt", true))) {
            writer.write(atendido.toString());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error al guardar paciente atendido.");
        }

        actualizarArchivoPacientes(atendido);
    }

    /**
     * Actualiza el archivo de pacientes, eliminando al paciente que ya fue atendido.
     *
     * @param atendido Paciente que fue atendido y debe eliminarse del archivo original.
     */
    private static void actualizarArchivoPacientes(Paciente atendido) {
        List<String> pacientesRestantes = new ArrayList<>();

        // Leer todos los pacientes que siguen en espera
        try (BufferedReader br = new BufferedReader(new FileReader("pacientes.txt"))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (!linea.equals(atendido.toString())) {
                    pacientesRestantes.add(linea);
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo.");
        }

        // Escribir el archivo nuevamente sin el paciente atendido
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("pacientes.txt"))) {
            for (String paciente : pacientesRestantes) {
                writer.write(paciente);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error al actualizar el archivo.");
        }
    }
}
