import java.util.*;
class VectorHeap<E extends Comparable<E>> {
    private List<E> heap;

    public VectorHeap() {
        heap = new ArrayList<>();
    }

    public void insert(E item) {
        heap.add(item);
        Collections.sort(heap);
    }

    public E remove() {
        return heap.isEmpty() ? null : heap.remove(0);
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }
}
