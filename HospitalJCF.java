import java.util.PriorityQueue;
import java.io.*;
import java.util.*;

public class HospitalJCF {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        PriorityQueue<Paciente> cola = new PriorityQueue<>(); // Usa PriorityQueue en lugar de VectorHeap

        cargarPacientes(cola);

        while (true) {
            System.out.println("\nMenú:");
            System.out.println("1. Agregar paciente");
            System.out.println("2. Atender siguiente paciente");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opción: ");
            int opcion = scanner.nextInt();
            scanner.nextLine();

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

    private static void atenderPaciente(PriorityQueue<Paciente> cola) {
        if (cola.isEmpty()) {
            System.out.println("No hay pacientes.");
            return;
        }

        Paciente atendido = cola.poll();
        System.out.println("\nAtendiendo: " + atendido);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("PacientesAtendidos.txt", true))) {
            writer.write(atendido.toString());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error al guardar paciente atendido.");
        }

        actualizarArchivoPacientes(atendido);
    }

    private static void actualizarArchivoPacientes(Paciente atendido) {
        List<String> pacientesRestantes = new ArrayList<>();
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
