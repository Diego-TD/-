package bank.ooptest;

public interface IListD<T> {

    int length ();
    T get(int i);
    boolean isEmpty ();
    void add (T value);
    void insert (int i, T value);
    void delete(int i);
    int search(T value);
    //String print(ListSED<T> list);
}
