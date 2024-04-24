package bank.ooptest;

public class NodeSED<T> implements INodeD<T> {
    private T value;
    private NodeSED<T> next;

    public NodeSED(T value){
        this.value = value;
    }
    public NodeSED(T value, NodeSED<T> next){
        this.value = value;
        this.next = next;
    }

    @Override
    public T getValue(){
        return this.value;
    }

    @Override
    public void setValue(T value){
        this.value = value;
    }


    public NodeSED<T> getNext (){
        return this.next;
    }


    public void setNext(NodeSED<T> nextNode){
        this.next = nextNode;
    }

}
