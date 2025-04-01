import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VectorHeapTest {
    private VectorHeap<Paciente> heap;

    @BeforeEach
    void setUp() {
        heap = new VectorHeap<>();
    }

    @Test
    void testInsertAndRemove() {
        Paciente p1 = new Paciente("Ana", "Dolor de cabeza", 'C');
        Paciente p2 = new Paciente("Luis", "Fiebre", 'A');
        Paciente p3 = new Paciente("Maria", "Fractura", 'B');

        heap.insert(p1);
        heap.insert(p2);
        heap.insert(p3);

        // Primero debe salir el de mayor prioridad ('A' es menor que 'B' y 'C')
        assertEquals(p2, heap.remove());
        // Luego el de prioridad 'B'
        assertEquals(p3, heap.remove());
        // Finalmente el de prioridad 'C'
        assertEquals(p1, heap.remove());
    }

    @Test
    void testIsEmpty() {
        assertTrue(heap.isEmpty());
        heap.insert(new Paciente("Carlos", "Dolor muscular", 'D'));
        assertFalse(heap.isEmpty());
    }

    @Test
    void testRemoveFromEmptyHeap() {
        assertNull(heap.remove()); // Si está vacío, debe devolver null
    }
}
