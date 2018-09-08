package davisbase;

import java.io.RandomAccessFile;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.ArrayList;
import java.util.Arrays;
import static java.lang.System.out;
import java.util.HashMap;

public class TableTraverser {
  private RandomAccessFile tableFile;

  public TableTraverser(String tableName){
    try{
      this.tableFile = new RandomAccessFile(tableName, "rw");
    }catch(Exception e){
      System.out.println(e);
    }
  }
}
