package davisbase;

import java.io.RandomAccessFile;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import static java.lang.System.out;
import davisbase.Constants;

public class IndexPage {
  private RandomAccessFile pageFile;
  private static Constants constants = new Constants();
  private int pageOffSet;
  int pageSize;

  public IndexPage(){

  }

  public int getPageOffSet(){
    return pageOffSet;
  }


  public IndexPage(RandomAccessFile pageFile, int pageOffSet, byte [] data){
    try{
      this.pageFile = pageFile;
      this.pageOffSet = pageOffSet;
      this.pageFile.seek(pageOffSet+constants.IndexFirstPointerOffSet);
      this.pageFile.write(data);
      //System.out.println("Created page with offset " + pageOffSet + " ***********************");
      //System.out.println("Copy index page constructor was called. ***********************");
    }catch(Exception e){
      System.out.println("Error initializing Index Page.");
      System.out.println(e);
    }
  }
  public IndexPage(RandomAccessFile pageFile, int pageOffSet, boolean newPage){
    this.pageFile = pageFile;
    this.pageOffSet = pageOffSet;
    this.pageSize = 512;
    if(newPage)
      this.createIndexPage();
    //System.out.println("Created page with offset " + pageOffSet + " ***********************");
  }

  //initializes the page
  public void createIndexPage(){
    boolean isTree = false;
    boolean isLeaf = true;
    this.createFreshPage();
    this.setPageType(isTree,isLeaf);
    this.setNumRecords(0);
    this.setContentLocation(pageOffSet + 511); //Check this, it may not be true ^^
    this.setPointerToLeftPage(-1);
    this.setPointerToRightPage(-1);
  }

  //Creates a new page of size pageSize
  private void createFreshPage(){
    try{
      this.pageFile.seek(this.pageOffSet);
      this.pageFile.write(new byte[pageSize]);
    }catch(Exception e){
      System.out.println("Error creating fresh index page. createFreshPage() method");
      System.out.println(e);
    }
  }

  //Use this method to set page type (first two bytes of the page)
  //If Table leaf then 0x0d (61 base 10)
  //If Table Inner then 0x05 (5 base 10)
  //If Index leaf then 0x0a (10 base 10)
  //If Index Inner then 0x02 (2 base 10)
  public void setPageType(boolean isTree, boolean isLeaf){
    try{
      pageFile.seek(this.pageOffSet + constants.PageTypeOffSet);
      if(isTree && isLeaf){
        pageFile.writeByte(constants.LeafPageNode);
      }else if (isTree & !isLeaf){
        pageFile.writeByte(constants.InnerPageNode);
      }
      else if (!isTree & isLeaf){
        pageFile.writeByte(constants.LeafIndexNode);
      }
      else if (!isTree & !isLeaf){
        pageFile.writeByte(constants.InnerIndexNode);
      }
    }catch(Exception e){
      System.out.println("Error setting page type for Index Page.");
      System.out.println(e);
    }
  }

  private boolean isLeafIndex(){
    try{
      pageFile.seek(this.pageOffSet + constants.PageTypeOffSet);
      byte data = pageFile.readByte();
      if(data == constants.LeafIndexNode)
        return true;
    }catch(Exception e){
      System.out.println("Error getting page type for Index Page.");
      System.out.println(e);
    }
    return false;
  }

  //Sets the number of records on this page
  private void setNumRecords(int numRecordsInDecimal){
    try{
      this.pageFile.seek(this.pageOffSet + constants.NumRecordsOffSet);
      this.pageFile.writeByte(numRecordsInDecimal);
    }
    catch (Exception e){
      System.out.println("Error setting number of records for Index Page.");
      System.out.println(e);
    }
  }

  //Sets the content location
  private void setContentLocation(int contentLocationInDecimal){
    try{
      this.pageFile.seek(this.pageOffSet + constants.PageContentOffSet);
      this.pageFile.writeShort(contentLocationInDecimal);
    }
    catch (Exception e){
      System.out.println("Error setting content location for Index Page.");
      System.out.println(e);
    }
  }

  //Sets the pointer to next page
  public void setPointerToRightPage(int pointerToNextPageInDecimal){
    try{
      //System.out.println("Setting right page to " + pointerToNextPageInDecimal);
      //if(pointerToNextPageInDecimal != -1)
        //System.out.println("Setting right pointer to: " + pointerToNextPageInDecimal + " on page: " + this.pageOffSet/512);
      this.pageFile.seek(this.pageOffSet + constants.RightPageOffSet);
      this.pageFile.writeInt(pointerToNextPageInDecimal);
    }
    catch (Exception e){
      System.out.println("Error setting pointers to right page for Index Page.");
      System.out.println(e);
    }
  }

  public void setPointerToLeftPage(int pointerToPreviousPageInDecimal){
    try{
      //if(pointerToPreviousPageInDecimal != -1)
        //System.out.println("Setting left pointer to: " + pointerToPreviousPageInDecimal + " on page: " + this.pageOffSet/512);

      if(pointerToPreviousPageInDecimal != -1){
        //setPageType(false,false);
      }
      this.pageFile.seek(this.pageOffSet + constants.LeftPageOffSet);
      this.pageFile.writeInt(pointerToPreviousPageInDecimal);
    }
    catch (Exception e){
      System.out.println("Error setting pointers to right page for Index Page.");
      System.out.println(e);
    }
  }

  public void insertRecordIntoIndex(int rowID, byte[] data, boolean isString){
    //Find recordOffSet
    int dataLength = data.length;

    int recordOffSet = this.getLastRecordLocation()-(dataLength+constants.PayloadOffSet+constants.RowIdOffSet);
    short shortRecordOffSet = new Integer(recordOffSet).shortValue();

    //Add one to num records
    this.increaseNumRecords();
    //Update content start location
    this.setLastRecordLocation(shortRecordOffSet);
    //Update next pointer
    this.setNextPointerLocation(shortRecordOffSet);
    //Insert data
    int payloadSize = dataLength;
    try{
      pageFile.seek(shortRecordOffSet);
      if(isString){
        payloadSize += 10;
      }
      pageFile.writeShort(payloadSize);
      pageFile.writeInt(rowID);
      pageFile.write(data);
    }catch(Exception e){
      System.out.println("Error Inserting for Index Page.");
      System.out.println(e);
    }

  }

  //To Do
  public void deleteRecord(){

  }

  //Adds one to number of records on this page
  public void increaseNumRecords(){
    try{
        pageFile.seek(pageOffSet + constants.NumRecordsOffSet);
        byte numRecords = pageFile.readByte();
        numRecords++;
        pageFile.seek(pageOffSet + constants.NumRecordsOffSet);
        pageFile.writeByte(numRecords);
    }catch(Exception e){
      System.out.println("Error increasing number of records for Index Page.");
      System.out.println(e);
    }
  }

  private short getLastRecordLocation(){
      short contentLocation = 0;
      try{
          pageFile.seek(pageOffSet + constants.PageContentOffSet);
          contentLocation = pageFile.readShort();
      }catch(Exception e){
        System.out.println("Error retriving last record location for Index Page.");
        System.out.println(e);
      }
      return contentLocation;
  }

  private void setNextPointerLocation(short currentPointer){
    int pointerLocation = pageOffSet + constants.IndexFirstPointerOffSet;
    short nextPointer = -1;
    try{
        pageFile.seek(pointerLocation);
        nextPointer = pageFile.readShort();
        if(nextPointer == 0){
          pageFile.seek(pointerLocation);
          pageFile.writeShort(currentPointer);
        }
        else{
          while(nextPointer != 0){
            pointerLocation+=2;
            pageFile.seek(pointerLocation);
            nextPointer = pageFile.readShort();
          }
          pageFile.seek(pointerLocation);
          pageFile.writeShort(currentPointer);
        }
    }catch(Exception e){
      System.out.println("Error setting next pointer location for Index Page.");
      System.out.println(e);
    }
  }

  private void setLastRecordLocation(short contentLocation){
    try{
        pageFile.seek(pageOffSet + constants.PageContentOffSet);
        pageFile.writeShort(contentLocation);
    }catch(Exception e){
      System.out.println("Error setting last record location for Index Page.");
      System.out.println(e);
    }
  }

  //Returns only data. Not metadata.
  public Byte[] getData(){
    byte [] data = null;
    Byte [] byteObjects = null;
    try{
      data = new byte[pageSize];
      byteObjects = new Byte[data.length];
      //pageFile.seek(pageOffSet + constants.IndexFirstPointerOffSet); Don't use this line for PrincetonBTree
      pageFile.seek(pageOffSet);
      pageFile.readFully(data);
      int i = 0;
      for(byte b: data)
        byteObjects[i++] = b;

    }catch(Exception e){
      System.out.println("Error getting data from Index Page.");
      System.out.println(e);
    }
    return byteObjects;
  }

  public String toString(){
       return "Offset: " + this.pageOffSet ;
       //", leftPagePointer: " + getLeftPagePointer() + ", rightPagePointer: " + getRightPagePointer();
   }

   public boolean isPageFull(int dataLength){
     try{
       pageFile.seek(this.pageOffSet+constants.NumRecordsOffSet);
       short numRecords = pageFile.readByte();
       int metadataLength = 12 + 6 + (numRecords*2); //12 for page headers, 6 for current row metadata (datatype, rowID)
       pageFile.seek(this.pageOffSet+constants.PageContentOffSet);
       short contentOffset = pageFile.readShort();

       while(contentOffset > 512){
         contentOffset = new Integer(contentOffset-512).shortValue();
       }

       int totalPageUsed = (512-contentOffset) + metadataLength;
       int freeSize = 512 - totalPageUsed;

       /*System.out.println("Content offset is: " + contentOffset);
       System.out.println("Content length is: " + (512-contentOffset));
       System.out.println("Metadata length is: " + metadataLength);
       System.out.println("Free size is: " + freeSize);
       System.out.println("Total page used is: " + totalPageUsed);*/
       if(freeSize>dataLength+10){
         return false;
       }
     }catch(Exception e){
       System.out.println("Error checking index page size.");
       System.out.println(e);
     }
     return true;
   }
}
