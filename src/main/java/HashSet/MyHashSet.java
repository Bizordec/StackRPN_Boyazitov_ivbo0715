package HashSet;

import java.util.ArrayList;

public class MyHashSet {

    private ArrayList<MyLinkedList> hashSetTable = new ArrayList<>();

    public MyHashSet() {
        hashSetTable.add(new MyLinkedList());
    }

    private int myHashCode(String t) {
        return t.hashCode() % hashSetTable.size();
    }

    public void add(String t) {
        if (!hashSetTable.get(myHashCode(t)).contains(t, myHashCode(t), hashSetTable))
            hashSetTable.get(myHashCode(t)).add(t);
    }

    public void remove(String t) {
        hashSetTable.get(myHashCode(t)).remove(t, myHashCode(t), hashSetTable);
    }

    public boolean contains(String t) {
        return hashSetTable.get(myHashCode(t)).contains(t, myHashCode(t), hashSetTable);
    }

    @Override
    public String toString() {
        ArrayList<String> result = new ArrayList<>();
        for (MyLinkedList myLinkedList : hashSetTable)
            result = myLinkedList.printResult();
        return result.toString();
    }
}
