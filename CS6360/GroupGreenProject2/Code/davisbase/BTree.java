/******************************************************************************
 *  Compilation:  javac BTree.java
 *  Execution:    java BTree
 *  Dependencies: StdOut.java
 *
 *  B-tree.
 *
 *  Limitations
 *  -----------
 *   -  Assumes M is even and M >= 4
 *   -  should b be an array of children or list (it would help with
 *      casting to make it a list)
 *
 ******************************************************************************/

/**
 *  The {@code BTree} class represents an ordered symbol table of generic
 *  key-value pairs.
 *  It supports the <em>put</em>, <em>get</em>, <em>contains</em>,
 *  <em>size</em>, and <em>is-empty</em> methods.
 *  A symbol table implements the <em>associative array</em> abstraction:
 *  when associating a value with a key that is already in the symbol table,
 *  the convention is to replace the old value with the new value.
 *  Unlike {@link java.util.Map}, this class uses the convention that
 *  values cannot be {@code null}�setting the
 *  value associated with a key to {@code null} is equivalent to deleting the key
 *  from the symbol table.
 *  <p>
 *  This implementation uses a B-tree. It requires that
 *  the key type implements the {@code Comparable} interface and calls the
 *  {@code compareTo()} and method to compare two keys. It does not call either
 *  {@code equals()} or {@code hashCode()}.
 *  The <em>get</em>, <em>put</em>, and <em>contains</em> operations
 *  each make log<sub><em>m</em></sub>(<em>n</em>) probes in the worst case,
 *  where <em>n</em> is the number of key-value pairs
 *  and <em>m</em> is the branching factor.
 *  The <em>size</em>, and <em>is-empty</em> operations take constant time.
 *  Construction takes constant time.
 *  <p>
 *  For additional documentation, see
 *  <a href="https://algs4.cs.princeton.edu/62btree">Section 6.2</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 */
 package davisbase;

 import davisbase.Index;
 import davisbase.IndexPage;
 import davisbase.TablePageUtils;
 import davisbase.Constants;
 import davisbase.Utils;

 import java.io.RandomAccessFile;
 import java.io.File;
 import java.io.FileReader;
 import java.util.Scanner;
 import java.util.SortedMap;
 import java.util.ArrayList;
 import java.util.Arrays;
 import static java.lang.System.out;

 import java.util.HashMap;

public class BTree<Key extends Comparable<Key>, Value>  {
    // max children per B-tree node = M-1
    // (must be even and greater than 2)
    private static final int M = 4;

    private Node root;       // root of the B-tree
    private int height;      // height of the B-tree
    private int n;           // number of key-value pairs in the B-tree

    // helper B-tree node data type
    private static final class Node {
        private int m;                             // number of children
        private Entry[] children = new Entry[M];   // the array of children

        // create a node with k children
        private Node(int k) {
            m = k;
        }
    }

    // internal nodes: only use key and next
    // external nodes: only use key and value
    private static class Entry {
        private Comparable key;
        private final Object val;
        private Node next;     // helper field to iterate over array entries

        public Entry(Comparable key, Object val, Node next) {
            this.key  = key;
            this.val  = val;
            this.next = next;
        }
    }

    /**
     * Initializes an empty B-tree.
     */
    public BTree() {
        root = new Node(0);
    }

    /**
     * Returns true if this symbol table is empty.
     * @return {@code true} if this symbol table is empty; {@code false} otherwise
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Returns the number of key-value pairs in this symbol table.
     * @return the number of key-value pairs in this symbol table
     */
    public int size() {
        return n;
    }

    /**
     * Returns the height of this B-tree (for debugging).
     *
     * @return the height of this B-tree
     */
    public int height() {
        return height;
    }


    /**
     * Returns the value associated with the given key.
     *
     * @param  key the key
     * @return the value associated with the given key if the key is in the symbol table
     *         and {@code null} if the key is not in the symbol table
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public Value get(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to get() is null");
        return search(root, key, height);
    }

    private Value search(Node x, Key key, int ht) {
        Entry[] children = x.children;

        // external node
        if (ht == 0) {
            for (int j = 0; j < x.m; j++) {
                if (eq(key, children[j].key)) return (Value) children[j].val;
            }
        }

        // internal node
        else {
            for (int j = 0; j < x.m; j++) {
                if (j+1 == x.m || less(key, children[j+1].key))
                    return search(children[j].next, key, ht-1);
            }
        }
        return null;
    }


    /**
     * Inserts the key-value pair into the symbol table, overwriting the old value
     * with the new value if the key is already in the symbol table.
     * If the value is {@code null}, this effectively deletes the key from the symbol table.
     *
     * @param  key the key
     * @param  val the value
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void put(Key key, Value val) {
        if (key == null) throw new IllegalArgumentException("argument key to put() is null");
        Node u = insert(root, key, val, height);
        n++;
        if (u == null) return;

        // need to split root
        Node t = new Node(2);
        t.children[0] = new Entry(root.children[0].key, null, root);
        t.children[1] = new Entry(u.children[0].key, null, u);
        root = t;
        height++;
    }

    private Node insert(Node h, Key key, Value val, int ht) {
        int j;
        Entry t = new Entry(key, val, null);

        // external node
        if (ht == 0) {
            for (j = 0; j < h.m; j++) {
                if (less(key, h.children[j].key)) break;
            }
        }

        // internal node
        else {
            for (j = 0; j < h.m; j++) {
                if ((j+1 == h.m) || less(key, h.children[j+1].key)) {
                    Node u = insert(h.children[j++].next, key, val, ht-1);
                    if (u == null) return null;
                    t.key = u.children[0].key;
                    t.next = u;
                    break;
                }
            }
        }

        for (int i = h.m; i > j; i--)
            h.children[i] = h.children[i-1];
        h.children[j] = t;
        h.m++;
        if (h.m < M) return null;
        else         return split(h);
    }

    // split node in half
    private Node split(Node h) {
        Node t = new Node(M/2);
        h.m = M/2;
        for (int j = 0; j < M/2; j++)
            t.children[j] = h.children[M/2+j];
        return t;
    }

    /**
     * Returns a string representation of this B-tree (for debugging).
     *
     * @return a string representation of this B-tree.
     */
    public String toString() {
        return toString(root, height, "") + "\n";
    }

    private String toString(Node h, int ht, String indent) {
        StringBuilder s = new StringBuilder();
        Entry[] children = h.children;

        if (ht == 0) {
            for (int j = 0; j < h.m; j++) {
                //System.out.println("in to string visited leaves: " + children[j].key);
                s.append(indent + children[j].key + " " + children[j].val + "\n"); //These are leaves.
            }
        }
        else {
            for (int j = 0; j < h.m; j++) {
                if (j > 0){
                    s.append(indent + "(" + children[j].key + ")\n"); //children[j] is a parent.
                }
                s.append(toString(children[j].next, ht-1, indent + "     "));
            }
        }
        return s.toString();
    }

    public int updatePointers(){
      int rootKey = updatePointers(root, null, height, 0);
      return rootKey;
    }

    public int getRootPageIndex(){
      return updatePointers()*512;
    }

    private int updatePointers(Node current, Node parent, int ht, int currentNodeIndex){
            Entry[] children = current.children;
            int rootKey = 0;
            IndexPage page1 = null;
            IndexPage page2 = null;

            if (ht == 0) {
//                for(int j = 0; j< current.m; j++){
//                    System.out.println("Visited leaves: " + children[j].key);
//                }
                //System.out.println("All children visited in this node");
                if(children[0] != null && children[0].val != null){
                    page1 = (IndexPage)children[0].val;
                }
                if(children[1] != null && children.length > 1 && children[1].val != null){
                    page2 = (IndexPage)children[1].val;
                }

                if(page2 != null){
                    page1.setPointerToRightPage(page2.getPageOffSet());
                    page2.setPointerToLeftPage(page1.getPageOffSet());
                }

                if(children[1] != null && children.length > 1 && children[1].val != null){
                    page1 = (IndexPage)children[1].val;
                }
                if(children[2] != null && children.length > 2 && children[2].val != null){
                    page2 = (IndexPage)children[2].val;
                }

                if(page2 != null){
                    page1.setPointerToRightPage(page2.getPageOffSet());
                    page2.setPointerToLeftPage(page1.getPageOffSet());
                    page2.setPointerToRightPage(-1);
                }
            }
            else {
                //Iterating through all children of current
                for (int nodeIndex = 0; nodeIndex < current.m; nodeIndex++) {
                    updatePointers(children[nodeIndex].next, current, ht-1, nodeIndex);
                    Integer i = (Integer) children[nodeIndex].key;
                    rootKey = i;
                    //If in this loop, this is an interior node. Set page type to interior
                    IndexPage page = (IndexPage)children[nodeIndex].val;
                    page.setPageType(false,false);
                }
            }

            return rootKey;
    }


    // comparison functions - make Comparable instead of Key to avoid casts
    private boolean less(Comparable k1, Comparable k2) {
        return k1.compareTo(k2) < 0;
    }

    private boolean eq(Comparable k1, Comparable k2) {
        return k1.compareTo(k2) == 0;
    }


    private void test(){
      BTree<Integer, IndexPage> tree = new BTree<Integer, IndexPage>();
      try{
      //Find the table name, index name, column name.
      //Get the data from table file.
      //Insert into current page.

      String indexName = "TestIndex";
      String indexFileName = ".//data//user_data//" + indexName + ".idx";
      String tableFileName = ".//data//user_data//example.tbl";
      RandomAccessFile indexFile = new RandomAccessFile(indexFileName, "rw");
      int size = 512;
      IndexPage page = new IndexPage(indexFile,size*0,true);
      IndexPage page2 = new IndexPage(indexFile,size*1,true);
      IndexPage page3 = new IndexPage(indexFile,size*2,true);
      IndexPage page4 = new IndexPage(indexFile,size*3,true);
      IndexPage page5 = new IndexPage(indexFile,size*4,true);

      Utils utils = new Utils();
      //Find which page to use *** This is the simple case, inserting to first page.
      int indexPageOffSet = 0;
      //Find which page to use *** This is the simple case, inserting to first page.
      int tablePageOffSet = 0;
      int columnNumber = 2;
      TablePageUtils tablePageUtils = new TablePageUtils(tableFileName);
      HashMap<Integer,ArrayList<Byte>> indexPayloadMap = tablePageUtils.getIndexPayloadInPage(tablePageOffSet,columnNumber);

      byte [] data1 = utils.convertByteArrayListToByteArray(indexPayloadMap.get(new Integer(1)));
      byte [] data2 = utils.convertByteArrayListToByteArray(indexPayloadMap.get(new Integer(2)));

      int rowID = 1;
      for(int i = 0; i < 25; i++){
        if(i%2 == 0)
          page.insertRecordIntoIndex(new Integer(rowID++),data1,true);
        else
          page.insertRecordIntoIndex(new Integer(rowID++),data2,true);
      }
      for(int i = 0; i < 25; i++){
        if(i%2 == 0)
          page2.insertRecordIntoIndex(new Integer(rowID++),data1,true);
        else
          page2.insertRecordIntoIndex(new Integer(rowID++),data2,true);
      }
      for(int i = 0; i < 25; i++){
        if(i%2 == 0)
          page3.insertRecordIntoIndex(new Integer(rowID++),data1,true);
        else
          page3.insertRecordIntoIndex(new Integer(rowID++),data2,true);
      }
      for(int i = 0; i < 25; i++){
        if(i%2 == 0)
          page4.insertRecordIntoIndex(new Integer(rowID++),data1,true);
        else
          page4.insertRecordIntoIndex(new Integer(rowID++),data2,true);
      }
      for(int i = 0; i < 25; i++){
        if(i%2 == 0)
          page5.insertRecordIntoIndex(new Integer(rowID++),data1,true);
        else
          page5.insertRecordIntoIndex(new Integer(rowID++),data2,true);
      }

      //System.out.println("Created # rows: " + (rowID-1));

      tree.put(new Integer(1), page);
      tree.put(new Integer(2), page2);
      tree.put(new Integer(3), page3);
      tree.put(new Integer(4), page4);
      tree.put(new Integer(5), page5);
      tree.updatePointers();

      IndexPage p = tree.get(1);
      Byte [] bytesObject = p.getData();
      //System.out.println("bytesObject length is: " + bytesObject.length);

      byte [] data = utils.unboxByteArray(bytesObject);
      String resultIndex = ".//data//user_data//" + "resultIndex" + ".idx";
      RandomAccessFile resultIndexFile = new RandomAccessFile(resultIndex, "rw");
      resultIndexFile.write(data);

    }catch(Exception e){
      System.out.println(e);
    }
  }

}
