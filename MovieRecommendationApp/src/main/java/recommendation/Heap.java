package recommendation;

import java.util.ArrayList;
import java.util.List;

public class Heap<T extends User> {

    private List<T> elements;

    public Heap() {
        elements = new ArrayList<>();
    }

    public void add(T element) {
        elements.add(element);
        siftUp(elements.size() - 1);
    }

    public T removeTop() {
        if (elements.isEmpty()) {
            throw new IllegalStateException("Heap is empty");
        }
        T top = elements.get(0);
        int lastIndex = elements.size() - 1;
        elements.set(0, elements.get(lastIndex));
        elements.remove(lastIndex);
        siftDown(0);
        return top;
    }

    public int size() {
        return elements.size();
    }

    public boolean isEmpty() {
        return elements.isEmpty();
    }

    private void siftUp(int index) {
        int parentIndex = getParentIndex(index);
        while (index > 0 && compare(elements.get(index), elements.get(parentIndex)) < 0) {
            swap(index, parentIndex);
            index = parentIndex;
            parentIndex = getParentIndex(index);
        }
    }

    private void siftDown(int index) {
        int leftChildIndex = getLeftChildIndex(index);
        int rightChildIndex = getRightChildIndex(index);
        int minIndex = index;

        if (leftChildIndex < elements.size() && compare(elements.get(leftChildIndex), elements.get(minIndex)) < 0) {
            minIndex = leftChildIndex;
        }

        if (rightChildIndex < elements.size() && compare(elements.get(rightChildIndex), elements.get(minIndex)) < 0) {
            minIndex = rightChildIndex;
        }

        if (minIndex != index) {
            swap(index, minIndex);
            siftDown(minIndex);
        }
    }

    private int getParentIndex(int index) {
        return (index - 1) / 2;
    }

    private int getLeftChildIndex(int index) {
        return (2 * index) + 1;
    }

    private int getRightChildIndex(int index) {
        return (2 * index) + 2;
    }

    private void swap(int i, int j) {
        T temp = elements.get(i);
        elements.set(i, elements.get(j));
        elements.set(j, temp);
    }

    // Custom comparator logic
    private int compare(User a, User b) {
        // Compare based on the similarity values
        double similarityA = a.getSimilarity();
        double similarityB = b.getSimilarity();

        // compare Return -1 if a < b, 0 if a == b, and 1 if a > b this for min heap
        return Double.compare(similarityA, similarityB)*-1;
    }
}
