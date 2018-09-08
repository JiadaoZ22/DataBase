package query;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


import static java.lang.System.out;

public class queryData {
    public static Map<Integer, ArrayList<Object>> main(RandomAccessFile file) {
        int count = 1;
        int record_start_position;
        int row_id;
        int page_beginPos = 0;
        int recordOffset;
        int data_type;
        int data_length;
        byte[] text;
        int record_num;
        int record_length;
        int pageOffset = 0;
        Map<Integer, ArrayList<Object>> DataMap = new HashMap<>();


        try {
            // we gonna take all the records from table_name.tbl
            // Here we go

            while (true) {
//                System.out.println("begin_position"+page_beginPos);
                // traverse all the pages
                // how many records do we have?
                file.seek(page_beginPos + 1);
                record_num = file.readByte();
                file.seek(page_beginPos + 2);
                record_start_position = file.readShort();
                //System.out.println("record num"+record_num);

//                recordOffset = (int) file.readShort();
//                file.seek(recordOffset + page_beginPos + 6);
//                record_length = file.readByte();
                do {

                    // traverse all records in this page
                    // find the location of record

                    pageOffset = page_beginPos + 6 + 2 * count;

                    if (pageOffset >= record_start_position) {
                        break;
                    }
                    file.seek(pageOffset);
                    // get how many cols we have in a record
                    recordOffset = (int) file.readShort();
                    int cur_dataType_offset = 7;
                    int cur_payload = 0;
                    // how long is this record
                    file.seek(page_beginPos + recordOffset + 6);
                    record_length = (int) file.readByte();
                    ArrayList<Object> eachRecord = new ArrayList<>();

                    // now we gonna get row_id first
                    file.seek(page_beginPos + recordOffset + 2);
                    row_id = file.readInt();
                    eachRecord.add(row_id);


                    // get all the data of thjs records
                    for (int i = 0; i < record_length; i++) {
                        file.seek(page_beginPos + recordOffset + cur_dataType_offset + i);
                        data_type = file.readByte();

                        if (data_type == 0) {
                            // value is null, content is zero size
                            eachRecord.add(null);
                        } else if (data_type == 1) {
                            // data is 1 Byte int
                            file.seek(page_beginPos + recordOffset + 7 + record_length + cur_payload);
                            int data = file.readByte();
                            eachRecord.add(data);
                            cur_payload += 1;
                        } else if (data_type == 2) {
                            // data is 2 Bytes int
                            file.seek(page_beginPos + recordOffset + 7 + record_length + cur_payload);
                            int data = file.readShort();
                            eachRecord.add(data);
                            cur_payload += 2;
                        } else if (data_type == 3) {
                            // data is 4 Bytes int
                            file.seek(page_beginPos + recordOffset + 7 + record_length + cur_payload);
                            int data = file.readInt();
                            eachRecord.add(data);
                            cur_payload += 4;
                        } else if (data_type == 4) {
                            // data is 8 Bytes int
                            file.seek(page_beginPos + recordOffset + 7 + record_length + cur_payload);
                            byte[] byteArr = new byte[8];
                            file.read(byteArr, 0, 8);
                            BigInteger data = new BigInteger(byteArr);
                            eachRecord.add(data);
                            cur_payload += 8;
                        } else if (data_type == 5) {
                            // data is 4 Bytes float
                            file.seek(page_beginPos + recordOffset + 7 + record_length + cur_payload);
                            Float data = file.readFloat();
                            eachRecord.add(data);
                            cur_payload += 4;
                        } else if (data_type == 6) {
                            // data is 8 Bytes double
                            file.seek(page_beginPos + recordOffset + 7 + record_length + cur_payload);
                            Double data = file.readDouble();
                            eachRecord.add(data);
                            cur_payload += 8;
                        } else if (data_type == 7 || data_type == 8) {
                            file.seek(page_beginPos + recordOffset + 7 + record_length + cur_payload);
                            Long data = file.readLong();
                            Date date = new Date(data);
                            eachRecord.add(date);
                            cur_payload += 8;
                        } else if (data_type == 9) {
                            // data is reserved
                            System.out.println("this data type is reversed!");
                        } else if (data_type >= 10) {
                            // data is TEXT
                            data_length = data_type - 10;
                            file.seek(page_beginPos + recordOffset + 7 + record_length + cur_payload);
                            text = new byte[data_length];
                            file.read(text, 0, data_length);
                            String data = new String(text);
                            eachRecord.add(data);
                            cur_payload += data_length;
                        } else {
                            System.out.println("Error in your data type!");
                        }
                    }
                    DataMap.put(count, eachRecord);
                    count += 1;
                    record_num -= 1;
                    // traversed all the records in this page!
                } while (record_num > 0);
                // move to next page
                file.seek(page_beginPos + 4);
                int next_page_num = file.readInt();
//                System.out.println("queryData-next page:"+next_page_num);
                if (next_page_num == -1) {
                    // current page is the rightmost
                    return DataMap;
                } else if(next_page_num >=1) {
                    page_beginPos = (next_page_num-1) * 512;
                    if(page_beginPos==512)
                        page_beginPos +=512;
                }
            }
        } catch (Exception e) {
            System.err.println("queryData"+e.getClass().toString()+e.getMessage());
        }
        return DataMap;
    }
}
