import java.io.*;
import java.util.Scanner;





public class Hospital {
    private static final String ARCHIVO_PACIENTES = "pacientes.txt";
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        VectorHeap<Paciente> cola = new VectorHeap<>();
        boolean salir = false;

        while (!salir) {
            System.out.println("\n--- SISTEMA DE EMERGENCIAS ---");
            System.out.println("1. Agregar paciente");
            System.out.println("2. Atender paciente");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opción: ");
            int opcion = scanner.nextInt();
            scanner.nextLine(); 

            switch (opcion) {
                case 1:
                    agregarPaciente(scanner);
                    break;
                case 2:
                    cargarPacientes(cola);
                    if (!cola.isEmpty()) {
                        System.out.println("Paciente atendido: " + cola.remove());
                    } else {
                        System.out.println("No hay pacientes en la cola.");
                    }
                    break;
                case 3:
                    salir = true;
                    System.out.println("Saliendo del sistema...");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }
        scanner.close();
    }

    private static void agregarPaciente(Scanner scanner) {
        System.out.print("Nombre del paciente: ");
        String nombre = scanner.nextLine();
        System.out.print("Síntoma: ");
        String sintoma = scanner.nextLine();
        System.out.print("Código de emergencia (A-E): ");
        char codigo = scanner.next().toUpperCase().charAt(0);
        scanner.nextLine();

        if (codigo < 'A' || codigo > 'E') {
            System.out.println("Código inválido. Debe ser una letra entre A y E.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO_PACIENTES, true))) {
            writer.write(nombre + ", " + sintoma + ", " + codigo);
            writer.newLine();
            System.out.println("Paciente agregado correctamente.");
        } catch (IOException e) {
            System.out.println("Error al escribir en el archivo: " + e.getMessage());
        }
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
            System.out.println("Error al leer el archivo: " + e.getMessage());
        }
    }
}
