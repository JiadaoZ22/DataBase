package davisbase;

import java.io.RandomAccessFile;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.TreeSet;
import static java.lang.System.out;
import davisbase.IndexPage;
import davisbase.TablePageUtils;
import davisbase.Utils;
import davisbase.Constants;
import davisbase.BTree;

public class Index {
  private int indexPageSize;
  private RandomAccessFile indexFile;
  private int columnOffSet; //Position of the column in the table
  private int columnDataType; //Data type of the column
  private String tableName; //Index is created for this table
  private String indexName;
  private RandomAccessFile tableFile; //Pointer to RandomAccessFile for the table
  private static Constants constants = new Constants();
  private BTree<Integer, IndexPage>  tree;
  private boolean isStringDataType;
  private int tableRootPage;
  private int tablePageSize;

  public Index(String indexName, String tableName, int columnOffSet, int columnDataType, boolean isString, int tableRootPage){
    try{
      this.indexFile = new RandomAccessFile(indexName, "rw");
      this.tableFile = new RandomAccessFile(tableName, "rw");
      this.columnOffSet = columnOffSet;
      this.columnDataType = columnDataType;
      this.isStringDataType = isString;
      this.tree = null;
      this.tableRootPage = tableRootPage;
      if(tableRootPage != 0)
          this.tableRootPage = tableRootPage-1;
      this.indexName = indexName;
      this.tableName = tableName;
      this.indexPageSize = 512;
      this.tablePageSize = 512;
    }
    catch(Exception e){
      System.out.println("Error in Index constructor.");
      System.out.println(e);
    }
  }

  public void create(){
    try{
      IndexPage page = new IndexPage(indexFile,0,true);
      tree = new BTree<Integer, IndexPage>();
      tree.put(new Integer(0),page);
      tree.updatePointers();
      //System.out.println("Just created index. BTree size is: " + tree.size());
    }catch(Exception e){
      System.out.println("Error creating index constructor. create() method");
      System.out.println(e);
    }
  }

  public void insert(){
    TablePageUtils tablePageUtils = new TablePageUtils(this.tableName);
    //Find which page to use *** This is the simple case, inserting to first page.
    int indexPageOffSet = 0;
    //Find which page to use ***
    int tablePageOffSet = tableRootPage*tablePageSize;
    try{
      if(tableRootPage != 0){
        //What is the first interior page?
        //System.out.println("Root page is: " + tableRootPage);
        tableFile.seek(tableRootPage*tablePageSize+tablePageSize-constants.IntSize);
        int firstLeafOffset = tableFile.readInt()-1;
        //System.out.println("Read this value in insert index: " + firstLeafOffset);
        tablePageOffSet = firstLeafOffset*512;
      }
      IndexPage page = new IndexPage(this.indexFile,indexPageOffSet,false);
      do{
        //System.out.println("Current page offset is: " + tablePageOffSet);
        int columnNumber = columnOffSet;
        //System.out.println("Currently data from page: " + tablePageOffSet/512);
        HashMap<Integer,ArrayList<Byte>> indexPayloadMap = tablePageUtils.getIndexPayloadInPage(tablePageOffSet,columnNumber);
        Utils utils = new Utils();
        //Using TreeSet to sort all the keys in increasing order.
        TreeSet<Integer> sortedKeySet = new TreeSet<Integer>();
        sortedKeySet.addAll(indexPayloadMap.keySet());
          for(Integer row : sortedKeySet){
            byte [] data = utils.convertByteArrayListToByteArray(indexPayloadMap.get(row));
            int dataLength = data.length;
            if(page.isPageFull(data.length)){
              indexPageOffSet += indexPageSize;
              IndexPage newPage = new IndexPage(this.indexFile,indexPageOffSet,true);
              tree.put(new Integer(tree.size()+1),newPage);
              page = newPage;
            }
            //System.out.println("Read row: " + row.intValue());
            page.insertRecordIntoIndex(row.intValue(),data,isStringDataType);
          }
        tableFile.seek(tablePageOffSet+constants.tableRightPageOffSet);
        //System.out.println("Currently in page: " + tablePageOffSet/512);
        tablePageOffSet = (tableFile.readInt()+(-1))*tablePageSize;
        //System.out.println("Next table page offset is: " + tablePageOffSet);
      }while(tablePageOffSet >= 0);
      tree.updatePointers();
      setRoot();
    }catch(Exception e){
      System.out.println("Error creating index page.");
      System.out.println(e);
    }

  }

  //This is going to be used for initializing a btree for an index that already exists.
  private void initializeBTree(){
      try{
        if(tree.size() == 0 && this.indexFile.length() != 0){
          long indexFileSize = this.indexFile.length();
          int numPages = (int) indexFileSize/indexPageSize;
          byte[] data = new byte[512];
          Utils utils = new Utils();
          for(int pageCount = 0; pageCount < numPages; pageCount++){
            indexFile.read(data, pageCount*512,indexPageSize);
            //tree.put(new Integer(pageCount),utils.boxByteArray(data));
          }
          tree.updatePointers();
        }
      }catch(Exception e){
        System.out.println("Error initializing BTree");
        System.out.println(e);
    }
  }

  private void setRoot(){
    try{
      int rootKey = tree.getRootPageIndex();
      IndexPage rootPage = tree.get(new Integer(rootKey));
      if(tree.size() > 1){
        rootPage.setPageType(false,false);
      }
    }catch(Exception e){
      System.out.println("Error seting page type.");
      System.out.println(e);
    }
  }

}
