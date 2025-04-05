/**
 * La clase Paciente representa a una persona que será atendida en un hospital.
 * Cada paciente tiene un nombre, un síntoma y un codigo que define su prioridad.
 * 
 * La clase implementa la interfaz Comparable
 */
public class Paciente implements Comparable<Paciente> {
    private String nombre;
    private String sintoma;
    private char codigoEmergencia;

    /**
     * Constructor que inicializa un nuevo paciente con sus datos básicos.
     *
     * @param nombre           El nombre del paciente.
     * @param sintoma          El síntoma que presenta.
     * @param codigoEmergencia El código de emergencia (de 'A' a 'E'), donde 'A' es la mayor prioridad.
     */
    public Paciente(String nombre, String sintoma, char codigoEmergencia) {
        this.nombre = nombre;
        this.sintoma = sintoma;
        this.codigoEmergencia = codigoEmergencia;
    }

    /**
     * Obtiene el nombre del paciente.
     *
     * @return El nombre del paciente.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene el síntoma del paciente.
     *
     * @return El síntoma que presenta el paciente.
     */
    public String getSintoma() {
        return sintoma;
    }

    /**
     * Obtiene el código de emergencia del paciente.
     *
     * @return El código de emergencia (letra de 'A' a 'E').
     */
    public char getCodigoEmergencia() {
        return codigoEmergencia;
    }

    /**
     * Compara este paciente con otro, basado en su código de emergencia.
     * Los códigos con letras menores tienen mayor prioridad.
     *
     * @param otro El otro paciente con el que se va a comparar.
     * @return Un número negativo si este paciente tiene mayor prioridad,
     *         positivo si tiene menor prioridad, o cero si son iguales.
     */
    @Override
    public int compareTo(Paciente otro) {
        return Character.compare(this.codigoEmergencia, otro.codigoEmergencia);
    }

    /**
     * Retorna una representación en texto del paciente, usada para guardar en archivos.
     *
     * @return Una cadena con el formato: nombre, síntoma, códigoEmergencia
     */
    @Override
    public String toString() {
        return nombre + ", " + sintoma + ", " + codigoEmergencia;
    }
}
