package bank.ooptest;

import androidx.annotation.NonNull;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Consumer;

public class ListSED<T> implements IListD<T>, Iterable<T> {
    NodeSED<T> head;
    public ListSED(){

    }

    @Override
    public int length() {
        NodeSED<T> head = this.head;
        NodeSED<T> current;
        NodeSED<T> next;
        int counter = 0;

        current = head;

        if (head == null){
            return counter;
        }
        while(true){
            counter++;
            next = current.getNext();
            if (next == null) {
                break;
            }
            current = next;
        }
        return counter;
    }

    @Override
    public T get(int i) {
        int length = this.length();
        NodeSED<T> head = this.head;
        NodeSED<T> current;
        NodeSED<T> next;
        current = head;
        int counter = 0;

        if (i < 0 || i > length || head == null){
            return null;
        }
        next = current.getNext();

        while (next != null){
            if (i == counter){
                return current.getValue();
            }

            current = next;
            next = current.getNext();
            counter++;
        }
        if (i == counter){
            return current.getValue();
        }

        return null;
    }


    @Override
    public boolean isEmpty() {
        return this.head == null;
    }

    @Override
    public void add(T value) {
        NodeSED<T> newNode = new NodeSED<>(value, null);

        if (this.head == null) {
            this.head = newNode;
            return;
        }

        NodeSED<T> current = this.head;

        while (current.getNext() != null) {
            current = current.getNext();
        }

        current.setNext(newNode);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListSED<?> listSED = (ListSED<?>) o;
        return Objects.equals(head, listSED.head);
    }

    @Override
    public int hashCode() {
        return Objects.hash(head);
    }

    @Override
    public void insert(int i, T value) {
        NodeSED<T> current;
        NodeSED<T> next;
        NodeSED<T> prev;
        int length = this.length();
        int counter = 0;
        current = this.head;
        prev = current;
        NodeSED<T> newNode = new NodeSED<>(value, null);

        if (i < 0 || i > length){
            return;
        }

        if (i == 0){
            if (head == null){
                this.head = newNode;
            } else {
                newNode.setNext(this.head);
                this.head = newNode;
            }
            return;
        }

        if (i == length){
            add(value);
            return;
        }

        next = current.getNext();
        while (next != null){
            if (i == counter) {
                newNode.setNext(current);
                prev.setNext(newNode);
                break;
            }

            prev = current;
            current = next;
            next = current.getNext();
            counter++;
        }
        if (i == counter){
            newNode.setNext(current);
            prev.setNext(newNode);

        }
    }

    @Override
    public void delete(int i) {
        int length = this.length();
        NodeSED<T> current = this.head;
        NodeSED<T> next;
        NodeSED<T> prev;
        prev = current;
        int counter = 0;

        if (i < 0 || i > length || head == null){
            return;
        }

        next = current.getNext();

        if (i == counter){
            this.head = next;
            return; // el camion de la basura pasa por mi nodo perdido?
        }

        while (next != null){
            if (i == counter){ //can be optimized
                prev.setNext(next);
            }

            prev = current;
            current = next;
            next = current.getNext();
            counter++;
        }
        if (i == counter){
            prev.setNext(next);
        }
    }




    public void delete(NodeSED<T> nodeToDelete) {
        NodeSED<T> current = this.head;
        NodeSED<T> prev = null;

        // Traverse the list to find the nodeToDelete and its previous node
        while (current != null && current != nodeToDelete) {
            prev = current;
            current = current.getNext();
        }

        // If the nodeToDelete is found
        if (current == nodeToDelete) {
            // If nodeToDelete is the head
            if (prev == null) {
                this.head = current.getNext();
            } else {
                prev.setNext(current.getNext());
            }
        }
    }

    @Override
    public int search(T value) {
        NodeSED<T> current;
        NodeSED<T> next;
        int counter = 0;
        current = this.head;

        // if I do a normal is empty thing, what should I return? -1?
        if (this.isEmpty()){
            throw new NoSuchElementException("The list is empty");
        }

        next = current.getNext();

        while(next != null){
            if (value.equals(current.getValue())){
                return counter;
            }
            counter++;
            current = next;
            next = current.getNext();
        }
        if (value.equals(current.getValue())){
            return counter;
        }

        throw new NoSuchElementException("Value not found in the list");
    }

    @NonNull
    @Override
    public Iterator<T> iterator() {
        return new ListSEDIterator();
    }

    private class ListSEDIterator implements Iterator<T> {
        private NodeSED<T> current = head;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            T data = current.getValue();
            current = current.getNext();
            return data;
        }
    }

    @Override
    public void forEach(@NonNull Consumer<? super T> action) {
        Iterable.super.forEach(action);
    }

    @NonNull
    @Override
    public Spliterator<T> spliterator() {
        return Iterable.super.spliterator();
    }
}
