import java.util.*;

/**
 * Clase que representa una cola de prioridad mínima usando un heap binario.
 * Usa una lista dinámica (ArrayList) para almacenar los elementos.
 *
 * @param <E> Tipo de dato que se va a guardar en el heap. Debe ser comparable.
 */
class VectorHeap<E extends Comparable<E>> {
    private List<E> heap;

    /**
     * Constructor que crea un heap vacío.
     */
    public VectorHeap() {
        heap = new ArrayList<>();
    }

    /**
     * Agrega un elemento al heap y lo acomoda en la posición correcta
     * para mantener el orden de prioridad.
     *
     * @param item Elemento que se quiere agregar.
     */
    public void insert(E item) {
        heap.add(item);
        siftUp(heap.size() - 1);
    }

    /**
     * Elimina y devuelve el elemento con mayor prioridad (el más pequeño).
     * Si el heap está vacío, devuelve {@code null}.
     *
     * @return El elemento con mayor prioridad o {@code null} si está vacío.
     */
    public E remove() {
        if (heap.isEmpty()) return null;
        E min = heap.get(0);
        heap.set(0, heap.remove(heap.size() - 1));
        siftDown(0);
        return min;
    }

    /**
     * Verifica si el heap está vacío.
     *
     * @return {@code true} si no hay elementos, {@code false} en caso contrario.
     */
    public boolean isEmpty() {
        return heap.isEmpty();
    }

    /**
     * Mueve un elemento hacia arriba en el heap si su prioridad es mayor (es menor)
     * que la de su padre, hasta que quede en la posición correcta.
     *
     * @param index Posición del elemento que se va a mover hacia arriba.
     */
    private void siftUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (heap.get(index).compareTo(heap.get(parent)) >= 0) break;
            Collections.swap(heap, index, parent);
            index = parent;
        }
    }

    /**
     * Mueve un elemento hacia abajo en el heap si su prioridad es menor
     * que la de alguno de sus hijos, hasta que quede en la posición correcta.
     *
     * @param index Posición del elemento que se va a mover hacia abajo.
     */
    private void siftDown(int index) {
        int left, right, smallest;
        while ((left = 2 * index + 1) < heap.size()) {
            right = left + 1;
            smallest = (right < heap.size() && heap.get(right).compareTo(heap.get(left)) < 0) ? right : left;
            if (heap.get(index).compareTo(heap.get(smallest)) <= 0) break;
            Collections.swap(heap, index, smallest);
            index = smallest;
        }
    }
}
