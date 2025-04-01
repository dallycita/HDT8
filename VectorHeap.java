import java.util.*;

class VectorHeap<E extends Comparable<E>> {
    private List<E> heap;

    public VectorHeap() {
        heap = new ArrayList<>();
    }

    public void insert(E item) {
        heap.add(item);
        siftUp(heap.size() - 1);
    }

    public E remove() {
        if (heap.isEmpty()) return null;
        E min = heap.get(0);
        heap.set(0, heap.remove(heap.size() - 1));
        siftDown(0);
        return min;
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }

    private void siftUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (heap.get(index).compareTo(heap.get(parent)) >= 0) break;
            Collections.swap(heap, index, parent);
            index = parent;
        }
    }

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
