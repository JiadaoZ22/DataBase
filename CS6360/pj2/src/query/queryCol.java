package query;
import static davisbase.DavisBase.TablePage.pageOffset;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;


import static java.lang.System.out;

public class queryCol {
    public static ArrayList<String> main(RandomAccessFile file, String tablename) {
        ArrayList<String> colArraryList = new ArrayList<>();
        try {
            // Because we already have two tables
            // According to the document
            //                int originalColNum = 8;
            int tablename_type_offset_recordHead = 8;
            int tablename_offset_recordHead = 17;
            int columnname_type_offset_recordHead = 11;
//            int columnname_offset_recordHead;
            int record_num;



            // Here we go
            int count = 1;
            int page_beginPos = 0;
            int recordOffset;
            int pageOffset;

            while(true) {
                // how many records do we have in this page
                file.seek(page_beginPos + 1);
                record_num = file.readByte();

                do{
                    // find the location of record
                    pageOffset = page_beginPos + 6 + 2 * count;
                    file.seek(pageOffset);
                    recordOffset = (int) file.readShort();

                    // judge the table_name
                    // jump the first index of record, that is rowid
                    file.seek(page_beginPos + recordOffset + tablename_type_offset_recordHead);
                    int tablename_length = file.readByte() - 10;
                    System.out.println("table_name_length"+tablename_length);
                    if(tablename_length<1){// it is an redundant loop
                        return colArraryList;
                    }
//                System.out.println("tablename_length"+tablename_length);
                    file.seek(page_beginPos + recordOffset + tablename_offset_recordHead);
                    byte[] byte_tablename = new byte[tablename_length];
                    file.read(byte_tablename, 0, tablename_length);
                    String from_byte_tablename = new String(byte_tablename);
                    // if we find the table we want
                    if (from_byte_tablename.equalsIgnoreCase(tablename)) {
                        // add column to the ArrayList
                        file.seek(page_beginPos + recordOffset + columnname_type_offset_recordHead);
                        int columnname_length = file.readByte() - 10;
                        file.seek(page_beginPos + recordOffset + tablename_offset_recordHead + tablename_length);
                        byte[] byte_columnname = new byte[columnname_length];
                        file.read(byte_columnname, 0, columnname_length);
                        String from_byte_columnname = new String(byte_columnname);
                        colArraryList.add(from_byte_columnname);
                    }
                    // whether it is the last record?
                    count ++;
                    record_num --;
                }while(record_num > 0);// a page is finished
                // compare the content_start with recordOffset
                file.seek(page_beginPos + 2);
                int content_starts = file.readShort();
                if (content_starts <= pageOffset) {
                    // we also gonna consider about PAGE SWITCHING
                    // if this is the MOST RIGHT PAGE
                    file.seek(page_beginPos + 4);
                    int next_pageNum = file.readInt();
                    System.out.println("queryCol-nextpage:"+next_pageNum);
                    if (next_pageNum == -1) {
                        return colArraryList;
                    } else {
                        count = 0;
                        page_beginPos += 512*(next_pageNum-1);
                    }
                }else{
                    System.out.println("queryCol-loop is wrong");
                }
            }
        } catch (IOException e) {
            out.println(e);
        }
        return colArraryList;
    }

}

