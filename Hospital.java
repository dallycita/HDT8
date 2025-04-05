import java.io.*;
import java.util.*;

public class Hospital {

    private static final String ARCHIVO_PACIENTES = "pacientes.txt";
    private static final String ARCHIVO_ATENDIDOS = "PacientesAtendidos.txt";

    /**
     * Método principal. Despliega un menú que permite al usuario
     * agregar pacientes, atender al siguiente paciente en prioridad o salir del sistema.
     *
     * @param args Argumentos de línea de comandos.
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        VectorHeap<Paciente> cola = new VectorHeap<>();

        // Carga los pacientes desde archivo al iniciar
        cargarPacientes(cola);

        while (true) {
            System.out.println("\nMenú:");
            System.out.println("1. Agregar paciente");
            System.out.println("2. Atender siguiente paciente");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opción: ");
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Limpia el salto de línea

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

    /**
     * Solicita los datos de un nuevo paciente desde consola, lo agrega a la cola de prioridad
     * y guarda su información en el archivo de pacientes.
     *
     * @param scanner Scanner para capturar datos desde consola.
     * @param cola    Cola de prioridad donde se almacenará el nuevo paciente.
     */
    private static void agregarPaciente(Scanner scanner, VectorHeap<Paciente> cola) {
        System.out.print("Ingrese nombre del paciente: ");
        String nombre = scanner.nextLine();
        System.out.print("Ingrese síntoma del paciente: ");
        String sintoma = scanner.nextLine();
        System.out.print("Ingrese código de emergencia (A-E): ");
        char codigoEmergencia = scanner.next().charAt(0);
        scanner.nextLine(); // Limpiar el buffer

        Paciente nuevoPaciente = new Paciente(nombre, sintoma, codigoEmergencia);

        // Guardar en el archivo
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO_PACIENTES, true))) {
            writer.write(nuevoPaciente.toString());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error al guardar el paciente.");
        }

        cola.insert(nuevoPaciente);
        System.out.println("Paciente agregado correctamente.");
    }

    /**
     * Lee el archivo de pacientes y agrega cada uno a la cola de prioridad al iniciar el sistema.
     *
     * @param cola Cola de prioridad donde se cargarán los pacientes.
     */
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

    /**
     * Atiende al siguiente paciente en la cola (el de mayor prioridad), lo guarda
     * en el archivo de pacientes atendidos y actualiza el archivo original eliminándolo.
     *
     * @param cola Cola de prioridad de donde se obtendrá el paciente a atender.
     */
    private static void atenderPaciente(VectorHeap<Paciente> cola) {
        if (cola.isEmpty()) {
            System.out.println("No hay pacientes en espera.");
            return;
        }

        Paciente atendido = cola.remove();
        System.out.println("\nAtendiendo a: " + atendido);

        actualizarArchivoPacientes(atendido);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO_ATENDIDOS, true))) {
            writer.write(atendido.toString());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error al guardar el paciente atendido.");
        }
    }

    /**
     * Actualiza el archivo de pacientes eliminando al que fue atendido,
     * para que no aparezca nuevamente en la cola al reiniciar el programa.
     *
     * @param atendido Paciente que ya fue atendido y debe eliminarse del archivo.
     */
    private static void actualizarArchivoPacientes(Paciente atendido) {
        List<String> pacientesRestantes = new ArrayList<>();

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
