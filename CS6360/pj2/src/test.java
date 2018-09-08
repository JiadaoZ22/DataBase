import java.io.File;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;

public class test {
    public static void main(String[] args){
//        try{
//            File dataDir = new File("data");
//            dataDir.mkdirs();
//            //delete old table files
//            String[] oldTableFiles;
//
//            File catalogDir = new File("data/catalog");
//            // delete old table files
//            oldTableFiles = catalogDir.list();
//            for(int i = 0; i < oldTableFiles.length; i++) {
//                File anOldFile = new File(catalogDir, oldTableFiles[i]);
//                anOldFile.delete();
//            }
//        }catch (SecurityException e){
//            System.out.println(e);
//        }

//        String dividingLines = new String(new char[10]).replace("\0", "=");
//        System.out.println("dividing-lines"+dividingLines);

//        try {
//            String temp2 = "2018-01-01";
//            temp2 = temp2 + "_00:00:00";
//            Date date = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").parse(temp2);
//            System.out.println(date);
//            Long timestamp = date.getTime();
//            Date dT = new Date(timestamp);
//            System.out.println("date"+dT);
//        }catch (Exception e){
//            System.out.println(e);
//        }
        String st = "a=b";
        System.out.println(st.contains("="));

    }

}
