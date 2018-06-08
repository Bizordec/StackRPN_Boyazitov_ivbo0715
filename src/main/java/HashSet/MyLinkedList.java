package HashSet;

import java.util.ArrayList;

class MyLinkedList {
    private myNode first;
    private myNode last;
    private ArrayList<String> result = new ArrayList<>();

    private class myNode {
        String item;
        myNode next;
        myNode prev;

        myNode(myNode prev, String element, myNode next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }

        int myHashCodeList(String t, ArrayList<MyLinkedList> hashSetTable) {
            return t.hashCode() % hashSetTable.size();
        }
    }

    void add(String e) {
        final myNode l = last;
        final myNode newNode = new myNode(l, e, null);
        last = newNode;
        if (l == null)
            first = newNode;
        else
            l.next = newNode;
        result.add(newNode.item);
    }

    void remove(String t, int hash, ArrayList<MyLinkedList> hashSetTable) {
        for (myNode x = first; x != null; x = x.next) {
            if (hash == x.myHashCodeList(x.item, hashSetTable)) {
                if (t.equals(x.item)) {
                    result.remove(x.item);
                    unlink(x);
                }
            }
        }
    }

    boolean contains(String t, int hash, ArrayList<MyLinkedList> hashSetTable) {
        return indexOf(t, hash, hashSetTable) != -1;
    }

    private void unlink(myNode x) {
        final myNode next = x.next;
        final myNode prev = x.prev;

        if (prev == null) {
            first = next;
        } else {
            prev.next = next;
            x.prev = null;
        }

        if (next == null) {
            last = prev;
        } else {
            next.prev = prev;
            x.next = null;
        }
        x.item = null;
    }

    private int indexOf(String t, int hash, ArrayList<MyLinkedList> hashSetTable) {
        int index = 0;
        for (myNode x = first; x != null; x = x.next) {
            if (hash == x.myHashCodeList(x.item, hashSetTable)) {
                if (t.equals(x.item))
                    return index;
            }
            index++;
        }
        return -1;
    }

    ArrayList<String> printResult() {
        return result;
    }
}
