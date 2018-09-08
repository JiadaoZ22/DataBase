package query;


import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.*;
import static java.lang.System.out;

public class parseQuery {
    /**
     * HERE WE GO!
     * Stub method for executing queries
     * 1) ensure the table to read is existed
     * 2) get the column positions from the davisbase_coloumns
     * 3) get the data of the specific columns
     * 4) dealing with WHERE statement
     * @param queryString is a String of the user inputn
     */
    public static void main(String queryString) {
        System.out.println("STUB: This is the parseQuery method");
        System.out.println("\tParsing the string:\"" + queryString + "\"");
        ArrayList<String> queryTokens
                = new ArrayList<String>(Arrays.asList(queryString.split("\\s+|\\t+")));
        System.out.println(queryTokens.toString());
        // if you miss FROM or put from in a wrong position
        if(queryTokens.size()>=5){
            if (!queryTokens.get(2).equalsIgnoreCase("FROM") ||
                    !queryTokens.get(4).equalsIgnoreCase("WHERE")) {
                out.println("Grammar Error: Please check your command!");
                return;
            }
        }

        boolean CatalogTable = false;
        // 1) check if we have this table under certain path (davis_tables.tbl have this table)
        RandomAccessFile table;
        String Tablename = queryTokens.get(3);
        try {
            // 1. get all the data from table_name.tbl
            if(Tablename.equalsIgnoreCase("davisbase_columns")){
                table = new RandomAccessFile("data/catalog/davisbase_columns.tbl", "r");
                CatalogTable = true;
            }else if(Tablename.equalsIgnoreCase("davisbase_tables")){
                table = new RandomAccessFile("data/catalog/davisbase_tables.tbl", "r");
                CatalogTable = true;
            }else {
                table = new RandomAccessFile("data/user_data/" + Tablename + ".tbl", "r");
            }
            Map<Integer, ArrayList<Object>> dataMap = queryData.main(table);

            // 2. get all the column from davisbase_coloumns
//            String columnsCatalog[][] = DavisBase.TablePage.getColumnsCatalog();
            RandomAccessFile colTable = new RandomAccessFile("data/catalog/davisbase_columns.tbl", "r");
            ArrayList<String> colList = queryCol.main(colTable, Tablename);


            // 3. Whether it contains WHERE
           if(queryTokens.size()>4) {
            if(queryTokens.get(4).equalsIgnoreCase("WHERE")){
                queryWhere.main(colList,dataMap,queryTokens.get(5));
            }
           }

            System.out.println("collist\t"+colList);
            System.out.println("dataMap\t"+dataMap);


            // 4. get the SELECT condition
            int count_rows = 0;
            String selectCondition = queryTokens.get(1);
            // Print table
            System.out.println("Table:" + Tablename);
            // the select condition
            // SELECT * FROM davisbase_columns;
            StringBuilder dividingLines =new StringBuilder();
            if (selectCondition.equalsIgnoreCase("*")) {
                // FIRST PRINT HEAD
                StringBuilder tableHeadString = new StringBuilder();
                for (Object o:colList) {
                    tableHeadString.append("\t|");
                    tableHeadString.append(printAlign.main(o.toString()));
                    dividingLines.append(printAlign.divinglines());
                }
                System.out.println(tableHeadString);
                System.out.println(dividingLines);
                ///////
                if (CatalogTable) {// if this table in catalog
                    // print all cols and all records
                    // iteratively print rows
                    Set<Integer> Keyset = dataMap.keySet();
                    Iterator<Integer> KeysetIterator = Keyset.iterator();
                    ArrayList<Object> value;
                    while (KeysetIterator.hasNext()) {
                        StringBuilder rowString = new StringBuilder();
                        Integer key = KeysetIterator.next();
                        value =dataMap.get(key);
                        for (int j = 0; j < value.size(); j++) {
                            // complete a string of row
                            rowString.append("\t|");
                            rowString.append(printAlign.main(value.get(j).toString()));
                        }
                        count_rows++;
                        System.out.println(rowString);
                    }
                    System.out.println("Totally | "+count_rows+" | rows.");
                }else{// else this table is not in catalog
                    // print all cols and all records
                    // iteratively print rows
                    Set<Integer> Keyset = dataMap.keySet();
                    Iterator<Integer> KeysetIterator = Keyset.iterator();
                    ArrayList<Object> value;
                    while (KeysetIterator.hasNext()) {
                        StringBuilder rowString = new StringBuilder();
                        Integer key = KeysetIterator.next();
                        value =dataMap.get(key);
                        for (int j = 1; j < value.size(); j++) {
                            // complete a string of row
                            rowString.append("\t|");
                            rowString.append(printAlign.main(value.get(j).toString()));
                        }
                        System.out.println(rowString);
                        count_rows ++;
                    }
                    System.out.println("Totally | "+count_rows+" | rows.");
                }
                /*
                else: partial selection
                 */
            } else {
                // FIRST,PRINT HEAD
                //else we only print the cols we want
                String[] colsList = queryTokens.get(1).trim().replace(" ", "").split(",");
//                String dividingLines = new String(new char[colsList.length]).replace("\0", "======================");
                // First, print the table head
                StringBuilder tableHeadString = new StringBuilder();
                ArrayList<Integer> colIndex = new ArrayList<>();
                for (int i = 0; i < colList.size(); i++) {
                    for (String colName : colsList) {
                        if (colList.get(i).equals(colName)) {
                            tableHeadString.append("\t|");
                            tableHeadString.append(printAlign.main(colName));
                            dividingLines.append(printAlign.divinglines());
                            colIndex.add(i);
                        }
                    }
                }
                System.out.println(tableHeadString);
                System.out.println(dividingLines);
                /////
                if (CatalogTable) { // if this table locates in catalog
                    // print rows
                    Set<Integer> Keyset = dataMap.keySet();
                    Iterator<Integer> KeysetIterator = Keyset.iterator();
                    ArrayList<Object> value;
                    while (KeysetIterator.hasNext()) {
                        StringBuilder rowString = new StringBuilder();
                        Integer key = KeysetIterator.next();
                        value = dataMap.get(key);
                        for (int j = 0; j < value.size(); j++) {
                            // complete a string of row
                            if (colIndex.contains(j)) {
                                rowString.append("\t|");
                                rowString.append(printAlign.main(value.get(j).toString()));
                            }
                        }
                        System.out.println(rowString);
                        count_rows++;
                    }
                    System.out.println("Totally | "+count_rows+" | rows.");
                } else {// else not a catalog table
                    // print rows
                    Set<Integer> Keyset = dataMap.keySet();
                    Iterator<Integer> KeysetIterator = Keyset.iterator();
                    ArrayList<Object> value;
                    while (KeysetIterator.hasNext()) {
                        StringBuilder rowString = new StringBuilder();
                        Integer key = KeysetIterator.next();
                        value = dataMap.get(key);
                        for (int j = 1; j < value.size(); j++) {
                            // complete a string of row
                            if (colIndex.contains(j)) {
                                rowString.append("\t|");
                                rowString.append(printAlign.main(value.get(j).toString()));
                            }
                        }
                        System.out.println(rowString);
                        count_rows++;
                    }
                    System.out.println("Totally | "+count_rows+" | rows.");
                }
            }
        } catch (FileNotFoundException e) {
            out.println(e);
        }
    }
}
