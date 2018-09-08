package davisbase;

import java.io.RandomAccessFile;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.ArrayList;
import java.util.Arrays;
import static java.lang.System.out;
import davisbase.Constants;
import java.util.HashMap;

public class TablePageUtils {
  private RandomAccessFile tableFile;
  private static Constants constants = new Constants();

  public TablePageUtils(String tableName){
    try{
      //System.out.println("Trying to create table page utils. Table name is: " + tableName);
      this.tableFile = new RandomAccessFile(tableName, "rw");
    }catch(Exception e){
      System.out.println("Error creating page utils");
      System.out.println(e);
    }
  }

  //Get data from table page. This will be used for retriving data to populate index.
  public HashMap<Integer,ArrayList<Byte>> getIndexPayloadInPage(int pageOffSet, int columnPosition){
    ArrayList<Integer> rowIDs = getRowIDsInPage(pageOffSet);
    ArrayList<ArrayList<Byte>> data = getDataInPage(pageOffSet, columnPosition);
    HashMap<Integer,ArrayList<Byte>> indexPayloadMap = new HashMap<Integer,ArrayList<Byte>>();

    for(int index = 0; index < rowIDs.size(); index++){
      indexPayloadMap.put(rowIDs.get(index),data.get(index));
    }

    return indexPayloadMap;
  }

  public ArrayList<Integer> getRowIDsInPage(int pageOffSet){
    ArrayList<Integer> rowIDs = new ArrayList<Integer>();
    try{
        int id = 0;
        ArrayList<Short> dataPointers = getPointersToDataInPage(pageOffSet);
        for(int index = 0; index < dataPointers.size(); index++){
            tableFile.seek(dataPointers.get(index)+constants.PayloadOffSet);
            id = tableFile.readInt();
            //System.out.println("Row id is: " + id);
            rowIDs.add(id);
        }
    }catch(Exception e){
      System.out.println("Error getting row ids");
      System.out.println(e);
    }
    return rowIDs;
  }

  public ArrayList<ArrayList<Byte>> getDataInPage(int pageOffSet, int columnPosition){
    //Gather all data for this column and return as ArrayList<ArrayList<Byte>>
    //Inner list is the data, outer list is for all the rows
    ArrayList<ArrayList<Byte>> data = new ArrayList<ArrayList<Byte>>();
    try{
      ArrayList<Short> dataPointers = getPointersToDataInPage(pageOffSet);
      //Do this in a loop, each row could be of varying length
      for(int index = 0; index<dataPointers.size(); index++){
        //dataPointer mentions where the content for each row begins
        data.add(getContentForColumnInRecord(dataPointers.get(index), columnPosition));
      }
    }catch(Exception e){
      System.out.println("Error getting data in table page");
      System.out.println(e);
    }
    return data;
  }

  //This is for single page only!
  private ArrayList<Short> getPointersToDataInPage(int pageOffSet){
    ArrayList<Short> dataPointersArray = new ArrayList<Short>();
    try{
      //Begining of the page content = pageOffSet
      if(isLeafNode(pageOffSet)){
        //Go to first pointer
        tableFile.seek(pageOffSet + constants.TableFirstPointerOffSet);
        short pointerToData = -1;
        int index = 0;
        short pageNo = new Integer(pageOffSet/512).shortValue();
        while(pointerToData != 0){
          pointerToData = tableFile.readShort();
          int temp = pointerToData;
          if(pointerToData > 0){
            if(pageNo != 0){
              temp = new Integer(pointerToData + (pageNo*512)).shortValue();
              dataPointersArray.add(new Integer(pointerToData + (pageNo*512)).shortValue());   //^^This line was added to match the new table format. ^^
            }
            else{
              dataPointersArray.add(pointerToData);   //^^This line was added to match the new table format. ^^
            }
          }
          index+=2;
          tableFile.seek(pageOffSet + index + constants.TableFirstPointerOffSet);
        }
      }
    }catch(Exception e){
      System.out.println("Error getting pointers to data in table page");
      System.out.println(e);
    }
    return dataPointersArray;
  }

  private byte getNumColumnsInRecord(int rowOffSet){
    byte numColumns = 0;
    try{
      //dataPointers.get(rowOffSet)+constants.PayloadOffSet+constants.RowIdOffSet
      tableFile.seek(rowOffSet+constants.PayloadOffSet+constants.RowIdOffSet);
      numColumns = tableFile.readByte();
    }catch(Exception e){
      System.out.println("Error getting pointers number of columns in table page");
      System.out.println(e);
    }
    return numColumns;
  }

  //This will be executed for each row.
  //Return byte array.
  private ArrayList<Byte> getContentForColumnInRecord(int rowOffSet, int columnPosition){
    ArrayList<Byte> rowData = new ArrayList<Byte>();
    try{
      byte numColumns = getNumColumnsInRecord(rowOffSet);
      tableFile.seek(rowOffSet+constants.PayloadOffSet+constants.RowIdOffSet+constants.NumRecordsOffSet+columnPosition);
      byte columnDataType = tableFile.readByte();

      //System.out.println("Num colums: " + numColumns);

      ArrayList<Byte> dataTypes = new ArrayList<Byte>();
      //For each column find the data type, you will need this to calculate offset for column of interest.
      //This needs to be done only for the columns prior to the column of interest
      for(int index = 0; index < columnPosition; index++){
        tableFile.seek(rowOffSet+constants.PayloadOffSet+constants.RowIdOffSet+constants.NumRecordsOffSet+index);
        byte columnType = tableFile.readByte();
        dataTypes.add(columnType);
        //System.out.println("ColumnType: " + columnType);
      }

      int columnOffSet = rowOffSet+constants.PayloadOffSet+constants.RowIdOffSet+constants.NumRecordsOffSet+numColumns*constants.DataTypeOffSet;
      for(int index = 0; index < dataTypes.size(); index++){
          columnOffSet += getDataTypeSize(dataTypes.get(index));
      }

      tableFile.seek(columnOffSet);
      byte [] data = new byte[getDataTypeSize(columnDataType)];
      tableFile.read(data);

      String temp = "";
      for(int index = 0; index < data.length; index++){
        rowData.add(data[index]);
        temp+=data[index] + " ";
      }
      //System.out.println("Value at this offset is: " + tableFile.readInt());
      //System.out.println("Row data size is: " + rowData.size());
      //System.out.println("Data is : " + temp);
    }catch(Exception e){
      System.out.println("Error getting column content in table page");
      System.out.println(e);
    }
    return rowData;
  }

  public boolean isLeafNode(int pageOffSet){
    boolean isLeaf = false;
    try{
      this.tableFile.seek(pageOffSet);
      byte pageType = this.tableFile.readByte();
      if(pageType == constants.LeafPageNode){
        isLeaf = true;
      }
    }catch(Exception e){
      System.out.println("Error getting page type for table page");
      System.out.println(e);
    }
    return isLeaf;
  }

  public int getDataTypeSize(int dataType){
    switch(dataType){
        case 1: return(constants.TinyIntSize);
        case 2: return(constants.SmallIntSize);
        case 3: return(constants.IntSize);
        case 4: return(constants.BigIntSize);
        case 5: return(constants.FloatSize);
        case 6: return(constants.DoubleSize);
        case 7: return(constants.DateTimeSize);
        case 8: return(constants.DateTypeSize);
        default: return(dataType-10);
    }
  }

}
