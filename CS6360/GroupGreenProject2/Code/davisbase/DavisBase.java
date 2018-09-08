/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package davisbase;

import java.io.RandomAccessFile;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.sql.Timestamp;
import davisbase.IndexPage;
import query.parseQuery;

import static java.lang.System.out;

public class DavisBase {

    static String prompt = "davisql> ";
    static boolean isExit = false;
    static int pageSize = 512;

    static String tablesCatalogDataType[] = {"TEXT", "SMALLINT"};
    static String columnsCatalogDataType[] = {"TEXT", "TEXT", "TEXT", "TINYINT", "TEXT"};

    static Scanner scanner = new Scanner(System.in).useDelimiter(";");

    /**
     * ***********************************************************************
     * Main method
     */
    public static void main(String[] args) {

        //splashScreen();
        System.out.println("Do you want to erase all data "
                + "and generate a new database?");
        System.out.print("Yes or No? ");
        Scanner choiceScanner = new Scanner(System.in);
        String choice = choiceScanner.next();
        while (!(choice.equalsIgnoreCase("yes") || choice.equalsIgnoreCase("no"))) {
            System.out.println("I do not understant your choice.");
            System.out.println(line("-", 80));
            System.out.println("Do you want to erase all data "
                    + "and generate a new database?");
            System.out.print("Yes or No? ");
            choice = choiceScanner.next();
        }

        if (choice.equalsIgnoreCase("Yes")) {
            initializeDataStore();
            out.println("All datas have been erased...");
            System.out.println(line("-", 80));
        } else {
            out.println("Working with the existing database...");
            System.out.println(line("-", 80));
        }

        String userCommand = "";

        while (!isExit) {
            System.out.print(prompt);
            userCommand = scanner.next()
                    .replace("\n", " ").replace("\r", "")
                    .replace("(", " (").replace(")", " )")
                    .trim();

            parseUserCommand(userCommand);
        }

        System.out.println("Exiting...");
    }

    /**
     * ***********************************************************************
     * Static method definitions
     */
    /**
     * Display the splash screen
     */
    public static void splashScreen() {
        System.out.println(line("-", 80));
        System.out.println("Welcome to DavisBaseLite");
        System.out.println("\nType \"help;\" to display supported commands.");
        System.out.println(line("-", 80));
    }

    /**
     * @param s The String to be repeated
     * @param num The number of time to repeat String s.
     * @return String A String object, which is the String s appended to itself
     * num times.
     */
    public static String line(String s, int num) {
        String a = "";
        for (int i = 0; i < num; i++) {
            a += s;
        }
        return a;
    }

    public static void printCmd(String s) {
        System.out.println("\n\t" + s + "\n");
    }

    public static void printDef(String s) {
        System.out.println("\t\t" + s);
    }

    /**
     * Help: Display supported commands
     */
    public static void help() {
        out.println(line("*", 80));
        out.println("SUPPORTED COMMANDS\n");
        out.println("All commands below are case insensitive\n");
        out.println("SHOW TABLES;");
        out.println("\tDisplay the names of all tables.\n");
        //printCmd("SELECT * FROM <table_name>;");
        //printDef("Display all records in the table <table_name>.");
        out.println("SELECT <column_list> FROM <table_name> [WHERE <condition>];");
        out.println("\tDisplay table records whose optional <condition>");
        out.println("\tis <column_name> = <value>.\n");
        out.println("DROP TABLE <table_name>;");
        out.println("\tRemove table data (i.e. all records) and its schema.\n");
        out.println("UPDATE TABLE <table_name> SET <column_name> = <value> [WHERE <condition>];");
        out.println("\tModify records data whose optional <condition> is\n");
        out.println("VERSION;");
        out.println("\tDisplay the program version.\n");
        out.println("HELP;");
        out.println("\tDisplay this help information.\n");
        out.println("EXIT;");
        out.println("\tExit the program.\n");
        out.println(line("*", 80));
    }

    public static void parseUserCommand(String userCommand) {

        // String[] commandTokens = userCommand.split(" ");
        ArrayList<String> commandTokens = new ArrayList<String>(Arrays.asList(userCommand.split(" ")));

        switch (commandTokens.get(0).toLowerCase()) {
            case "select":
                //System.out.println("CASE: SELECT");
                parseQuery.main(userCommand);
                break;

            case "drop":
                if(commandTokens.get(1).toLowerCase().equals("index")){
                  parseDropIndex(userCommand);
                  break;
                }else{
                  dropTable(userCommand);
                  break;
                }

            case "create":
                System.out.println(commandTokens.get(1));
                if(commandTokens.get(1).toLowerCase().equals("index")){
                  parseCreateIndex(userCommand);
                  break;
                }else{
                  parseCreateTable(userCommand);
                  break;
                }

            case "insert":
                //System.out.println("CASE: INSERT");
                parseInsert(userCommand);
                break;

            case "delete":
                System.out.println(commandTokens.get(1));
                if(commandTokens.get(1).toLowerCase().equals("index")){
                  parseDelete(userCommand);
                  break;
                }else{
                  parseDelete(userCommand);
                  break;
				}

            case "update":
                //System.out.println("CASE: UPDATE");
                System.out.println(commandTokens.get(1));
                if(commandTokens.get(1).toLowerCase().equals("index")){
                  parseUpdate(userCommand);
                  break;
                }else{
                  parseUpdate(userCommand);
                  break;
				}

            case "help":
                help();
                break;

            case "exit":
                isExit = true;
                break;

            case "quit":
                isExit = true;
                break;

            default:
                System.out.println("I didn't understand the command: \"" + userCommand + "\"");
                break;
        }
    }

    /**
     * Stub method for dropping tables
     *
     * @param dropTableString is a String of the user input
     */
    public static void dropTable(String dropTableString) {
        System.out.println("STUB: This is the dropTable method.");
        System.out.println("\tParsing the string:\"" + dropTableString + "\"");
		String[] drop=dropTableString.split(" ");
		
		String tableName=drop[2].trim();
		if(!TableExists(tableName))
		{
			System.out.println("Table does not exists!");
			return;
		}
		else
		{
			Table_Update.drop(tableName);	
		}
    }

    /**
     * Stub method for executing queries
     *
     * @param queryString is a String of the user input
     */
    public static void parseQuery(String queryString) {
        System.out.println("STUB: This is the parseQuery method");
        System.out.println("\tParsing the string:\"" + queryString + "\"");
    }

    /**
     * Stub method for updating records
     *
     * @param updateString is a String of the user input
     */
    public static void parseUpdate(String updateString) {
        System.out.println("STUB: This is the Updatetable method");
        System.out.println("Parsing the string:\"" + updateString + "\"");
		//String userCommand = "";
		String[] updates = updateString.toLowerCase().split("set");
		String[] table = updates[0].trim().split(" ");
		String tablename = table[1].trim();
		String set_value=null;
		String where=null;
		if (!TableExists(tablename)) {
			System.out.println("Table does not Exist");
			return;
		}
		
		if (updates[1].contains("where")) {
			String[] findupdate = updates[1].split("where");
			set_value = findupdate[0].trim();
			where = findupdate[1].trim();
			Table_Update.update(tablename, parsewhereString(set_value), parsewhereString(where));
		} else {
			set_value=updates[1].trim();
			String[] no_where = new String[0];
			Table_Update.update(tablename, parsewhereString(set_value), no_where);
		}
    }
//==========================================================================
	private static String[] parsewhereString(String where) {
		// TODO Auto-generated method stub
		// column_name = value
		String[] comp = new String[3];
		String[] arr = new String[2];
		
		if(where.contains("=")){
			arr = where.split("=");
			comp[0]=arr[0].trim();
			comp[1]="=";
			comp[2]=arr[1].trim();
		}
		else if(where.contains(">")){
			arr = where.split(">");
			comp[0]=arr[0].trim();
			comp[1]=">";
			comp[2]=arr[1].trim();
		}
		else if(where.contains("<")){
			arr = where.split("<");
			comp[0]=arr[0].trim();
			comp[1]="<";
			comp[2]=arr[1].trim();
		}
		else if(where.contains(">=")){
			arr = where.split(">=");
			comp[0]=arr[0].trim();
			comp[1]=">=";
			comp[2]=arr[1].trim();
		}
		else if(where.contains("<=")){
			arr = where.split("<=");
			comp[0]=arr[0].trim();
			comp[1]="<=";
			comp[2]=arr[1].trim();
		}
		else if(where.contains("<>")){
			arr = where.split("<>");
			comp[0]=arr[0].trim();
			comp[1]="<>";
			comp[2]=arr[1].trim();
		}
		// {"column_name", "=", "value"}
		return comp;
	}

	private static boolean TableExists(String tableName) {
		// TODO Auto-generated method stub
		String filename =tableName+".tbl";
		File catalog = new File("data/catalog");
		String[] tablenames = catalog.list();
		for (String string : tablenames) {
			if(filename.equals(string))
				return true;
		}
		File userdata = new File("data/user_data");
		String[] tables = userdata.list();
		for (String string : tables) {
			if(filename.equals(string))
				return true;
		}	
		return false;
	}






    // create table
/*===========================================================================*/
    public static void parseCreateTable(String createTableString) {

        String createTableTokens[] = createTableString.split("\\(");
        String command[] = createTableTokens[0].trim().split(" ");
        String columnsList[] = createTableTokens[1].trim().split(",");

        // Define table name and path
        String tableName = command[2].toLowerCase().trim();

        // if table already exist, return
        TablePage tablePage = new TablePage(tableName);
        if (tablePage.fileSize != 0) {
            out.println("Table has already created! Can not duplicate.");
            return;
        }

        // create a .tbl file to contain initial table data
        tablePage.newLeafPage();

        // insert a row into davisbase_tables.tbl file
        TablePage tablesCatalog = new TablePage("davisbase_tables");
        String tableDataList[] = {tableName, "0"};
        tablesCatalog.writeRecord(tableDataList, tablesCatalogDataType);

        // insert columns into davisbase_columns.tbl file
        TablePage columnsCatalog = new TablePage("davisbase_columns");
        int ordinalPos = 0;

        for (int i = 0; i < columnsList.length; i++) {
            String columnToken[] = columnsList[i]
                    .replace("\t", " ").replace(")", "")
                    .trim().split("\\s+");

            ordinalPos++;
            // table_name, column_name, data_type, ordinal_position, is_nullable
            String columnsCatalogList[] = {tableName, columnToken[0],
                columnToken[1].toUpperCase(), Integer.toString(ordinalPos), "YES"};
            if ((columnToken.length > 2 && columnToken[2].equalsIgnoreCase("not"))
                    || (columnToken.length > 4 && columnToken[4].equalsIgnoreCase("not"))) {
                columnsCatalogList[4] = "NO";
            }
            //for(String temp : columnsCatalogList)
            //    System.out.print(temp + ", ");
            //out.println();
            columnsCatalog.writeRecord(columnsCatalogList, columnsCatalogDataType);
        }
    }

    // insert into table
/*===========================================================================*/
    public static void parseInsert(String insertString) {

        //ArrayList<String> insertTokens
        //        = new ArrayList<String>(Arrays.asList(insertString.split("\\s+|\\t+")));
        String insertTokens[] = insertString.split("(?i)values");
        String command[] = insertTokens[0]
                .replace("(", " ( ").replace(")", " ) ")
                .trim().split("\\s+");
        String values[] = insertTokens[1]
                .replace("(", "").replace(")", "")
                .trim().split(",");

        for (int i = 0; i < values.length; i++) {
            values[i] = values[i].trim();
        }

        String tableName = command[command.length - 1].toLowerCase().trim();

        // create a .tbl file to contain table data
        TablePage tablePage = new TablePage(tableName);

        // check if the table exit, if not -> return
        if (tablePage.fileSize == 0) {
            System.out.println("No such table!");
            return;
        }

        String columnsCatalog[][] = tablePage.getColumnsCatalog();
        // columnsCatalog has i columns, for each column,
        // j=0: table name, 1: column name, 2: data type, 3: nullable
        int numDatas = columnsCatalog.length;
        String columnNames[] = new String[numDatas];
        String dataTypes[] = new String[numDatas];
        String nullable[] = new String[numDatas];
        String datas[] = new String[numDatas];

        for (int i = 0; i < numDatas; i++) {
            columnNames[i] = columnsCatalog[i][1];
            dataTypes[i] = columnsCatalog[i][2];
            nullable[i] = columnsCatalog[i][3];
        }

        // INSERT INTO TABLE (Column1, Column2, ...) Table_name Values ...
        if (command.length > 4) {
            String commandTokens[] = insertTokens[0]
                    .replace("(", " ( ").replace(")", " ) ")
                    .trim().split("\\(|\\)");
            String columns[] = commandTokens[1].split(",");

            for (int i = 0; i < columns.length; i++) {
                columns[i] = columns[i].trim();
            }

            for (int i = 0; i < numDatas; i++) {
                for (int j = 0; j < columns.length; j++) {
                    if (columnNames[i].equalsIgnoreCase(columns[j])) {
                        datas[i] = values[j];
                    }
                }
            }

            for (int i = 0; i < numDatas; i++) {
                if (datas[i] == null || datas[i].equalsIgnoreCase("NULL")) {
                    dataTypes[i] = "NULL";
                }
            }
        } // INSERT INTO TABLE Table_name Values ...
        else {
            for (int i = 0; i < numDatas; i++) {
                if (i >= values.length || values[i].equalsIgnoreCase("NULL")) {
                    dataTypes[i] = "NULL";
                } else {
                    datas[i] = values[i];
                }
            }
        }

        // get rid of the "" if exist
        for (int i = 0; i < numDatas; i++) {
            if (datas[i] != null && (datas[i].charAt(0) == '"' || datas[i].charAt(0) == '\'')) {
                datas[i] = datas[i].substring(1, datas[i].length() - 1);
            }
        }

        // check the null constraint
        for (int i = 0; i < numDatas; i++) {
            if (dataTypes[i].equals("NULL") && nullable[i].equals("NO")) {
                out.println("Data in column \"" + columnNames[i] + "\" cannot be NULL!");
                return;
            }
        }

        tablePage.writeRecord(datas, dataTypes);
    }

    // delete from table
/*===========================================================================*/
    public static void parseDelete(String insertString) {

        String deleteTokens[] = insertString.split("(?i)where");
        String command[] = deleteTokens[0].trim().split("\\s+");
        String condition[] = getCondition(insertString);

        String tableName = command[3].toLowerCase().trim();

        String columnName = condition[0];
        String comparison = condition[1];
        String compareValue = condition[2];
        if(compareValue.charAt(0) == '"' || compareValue.charAt(0) == '\'')
            compareValue = compareValue.substring(1, compareValue.length() - 1);


        TablePage tablePage = new TablePage(tableName);

        // check if the table exit, if not -> return
        if (tablePage.fileSize == 0) {
            System.out.println("No such table!");
            return;
        }

        String columnsCatalog[][] = tablePage.getColumnsCatalog();
        // columnsCatalog has i columns, for each column,
        // j=0: table name, 1: column name, 2: data type, 3: nullable
        int numDatas = columnsCatalog.length;
        String columnNames[] = new String[numDatas];
        String dataTypes[] = new String[numDatas];

        for (int i = 0; i < numDatas; i++) {
            columnNames[i] = columnsCatalog[i][1];
            dataTypes[i] = columnsCatalog[i][2];
        }

        int columnNum = -1;

        for (int i = 0; i < numDatas; i++) {
            if (columnNames[i].equalsIgnoreCase(columnName)) {
                columnNum = i + 1;
            }
        }

        if (columnNum == -1) {
            out.println("There is no column named " + columnName);
            return;
        }

        tablePage.findRecord(columnNum, dataTypes[columnNum - 1], comparison, compareValue);
    }

      /**
      *  Stub method for creating new indexes
      *  @param createIndexString is a String of the user input
      */
      public static void parseCreateIndex(String createIndexString) {

        //System.out.println("STUB: Calling your method to create an index");
        //System.out.println("Parsing the string:\"" + createIndexString + "\"");
        ArrayList<String> createIndexTokens = new ArrayList<String>(Arrays.asList(createIndexString.split(" ")));

        /* Define index file name */
        String indexFileName = "data/user_data/" + createIndexTokens.get(2).toLowerCase().trim() + ".idx";
        String tableFileNameWithoutPath = createIndexTokens.get(4).toLowerCase().trim();
        String tableFileName = "data/user_data/" + createIndexTokens.get(4).toLowerCase().trim() + ".tbl";
        //String columnTokens[] = createIndexTokens.split("(?i)values");
        /*  Code to create a .idx file to contain table data */
        try {
          // create a .tbl file to contain table data
          TablePage tablePage = new TablePage(tableFileNameWithoutPath);

          // check if the table exit, if not -> return
          if (tablePage.fileSize == 0) {
            System.out.println("No such table!");
            return;
          }

          String columnsCatalog[][] = tablePage.getColumnsCatalog ();

          int numDatas = columnsCatalog.length;
          int columnNumber = 0;
          int columnDataType = 0;
          boolean isString = false;
          int tableRootPage = tablePage.getRootPage();
          String columnName = createIndexString.split("[\\(\\)]")[1];
          columnName = columnName.toLowerCase().trim();
          //lastname
          for (int i = 0; i < numDatas; i ++)
          {
            if(columnsCatalog[i][1].toLowerCase().trim().equals(columnName)){
              columnNumber = i;
              columnDataType = getDataTypeCode(columnsCatalog[i][2]);
              if(columnDataType > 9){
                isString = true;
              }
              break;
            }
          }

          //System.out.println("Data type is: " + columnDataType);
          //System.out.println("Column number is: " + columnNumber);

          Index index = new Index(indexFileName, tableFileName, columnNumber, columnDataType, isString, tableRootPage);
          System.out.println("Index: " + createIndexTokens.get(2) + " has been created succesfully.");
          index.create();
          index.insert();

        }
        catch(Exception e) {
          System.out.println(e);
        }
      }

  /**
  *  Stub method for creating new indexes
  *  @param parseDropIndex is a String of the user input
  */
  public static void parseDropIndex(String dropIndexString){
    ArrayList<String> dropIndexTokens = new ArrayList<String>(Arrays.asList(dropIndexString.split(" ")));
    String indexFileName = "data/user_data/" + dropIndexTokens.get(2).toLowerCase().trim() + ".idx";

    try{
      RandomAccessFile index = new RandomAccessFile(indexFileName,"rw");
      if(index.length() == 0){
        System.out.println("The index does not exist.");
      }else{
        index.setLength(0);
        System.out.println("Index " + dropIndexTokens.get(2) + " is dropped.");
      }
    }catch(Exception e){
      System.out.println("Error dropping index.");
      System.out.println(e);
    }
  }
//==============================================================================
    public static class TablePage {

        public static RandomAccessFile file;
        public static String tableName;
        public static String filePath;
        // the current file size
        public static int fileSize;
        public static int pageNum;
        public static int pageOffset;

        TablePage(String inTableName) {
            try {
                tableName = inTableName;
                if (tableName.equals("davisbase_tables")
                        || tableName.equals("davisbase_columns")) {
                    filePath = "data/catalog/" + tableName + ".tbl";
                } else {
                    filePath = "data/user_data/" + tableName + ".tbl";
                }

                file = new RandomAccessFile(filePath, "rw");
                fileSize = (int) file.length();
                pageNum = fileSize / pageSize;
                pageOffset = fileSize - pageSize;
            } catch (Exception e) {
                out.println("Error in accessing file");
                out.println(e);
            }
        }

        public static void newLeafPage() {
            fileSize += pageSize;
            pageOffset += pageSize;
            pageNum += 1;
            try {
                file.setLength(fileSize);
                // move cursor to current page
                file.seek(pageOffset);
                // it is a leaf node page 0x0d
                file.write(0x0D);
                // with no data
                file.write(0x00);
                // start of content is at the end
                file.writeShort(pageSize);
                // and it is the rightmost page
                file.writeInt(-1);

                //out.println(fileName + " page created!");
            } catch (Exception e) {
                out.println("Unable to create new leaf page");
                out.println(e);
            }
        }

        public static void newInteriorPage() {
            fileSize += pageSize;
            pageOffset += pageSize;
            pageNum += 1;
            try {
                file.setLength(fileSize);
                file.seek(pageOffset);
                // 1-byte interior page: 0x05
                file.write(0x05);
                // 1-byte with no data
                file.write(0x00);
                // 2-byte start of content is at the end
                file.writeShort(pageSize);
                // 4-byte rightmost child page pointer
                // have to make sure the next page is the new child page
                file.writeInt(pageNum + 1);

            } catch (Exception e) {
                out.println("Unable to create new interior page");
                out.println(e);
            }
        }

        public static void writeRecord(String[] dataList, String[] dataTypeList) {
            int rowid = getRowid() + 1;
            int numColumns = dataList.length;

            // get all dataType code in an array, and payload
            int dataTypeCode[] = new int[numColumns];
            // payload initially has 1-byte numColumns + numColumns-byte dataTypes, except the datas
            int payload = 1 + numColumns;
            for (int i = 0; i < numColumns; i++) {
                if (dataTypeList[i].equals("TEXT")) {
                    dataTypeCode[i] = 10 + dataList[i].length();
                    payload += dataList[i].length();
                } else {
                    dataTypeCode[i] = getDataTypeCode(dataTypeList[i]);
                    payload += getDataTypeSize(dataTypeList[i]);
                }
            }

            // record length: 2-byte payload + 4-byte rowid + payload
            int recordLength = 6 + payload;
            int recordOffset;

            try {
                // move to the position of number of records
                file.seek(pageOffset + 1);
                int numRecords = file.readByte();
                // get the previous recordOffset
                recordOffset = file.readShort();
                // calculate the new recordOffset
                recordOffset = recordOffset - recordLength;

                // check if inserting the new record will overflow
                // (8-byte header + 2*(numRecord+1) > recordOffset --> overflow
                if ((8 + 2 * (numRecords + 1)) > recordOffset) {
                    // save the current right sibling position
                    int tempOffset = pageOffset + 4;
                    // test
                    updateInteriorPage(rowid);

                    // update right sibling pointer
                    file.seek(tempOffset);
                    file.writeInt(pageNum + 1);

                    // generate new leaf page and update
                    // need to update upper level leaf!!!
                    newLeafPage();
                    numRecords = 0;
                    recordOffset = pageSize - recordLength;
                }

                // update number of records
                numRecords++;
                // move currsor back
                file.seek(pageOffset + 1);
                file.writeByte(numRecords);

                // update recordOffset
                file.seek(pageOffset + 2);
                file.writeShort(recordOffset);

                // update the record list
                file.seek(pageOffset + 6 + 2 * numRecords);
                file.writeShort(recordOffset);

                // write record
                file.seek(pageOffset + recordOffset);
                // 2-byte payload
                file.writeShort(payload);
                // 4-byte rowid
                file.writeInt(rowid);
                // 1-byte numColumns;
                file.writeByte(numColumns);
                // numColumns-byte dataTypeCode
                for (int i = 0; i < numColumns; i++) {
                    file.writeByte(dataTypeCode[i]);
                }
                // write data
                for (int i = 0; i < numColumns; i++) {
                    switch (dataTypeList[i].toUpperCase()) {
                        case "NULL":
                            break;
                        case "TINYINT":
                            file.writeByte(Integer.parseInt(dataList[i]));
                            break;
                        case "SMALLINT":
                            file.writeShort(Integer.parseInt(dataList[i]));
                            break;
                        case "INT":
                            file.writeInt(Integer.parseInt(dataList[i]));
                            break;
                        case "BIGINT":
                            file.writeLong(Long.parseLong(dataList[i]));
                            break;
                        case "FLOAT":
                            file.writeFloat(Float.parseFloat(dataList[i]));
                            break;
                        case "DOUBLE":
                            file.writeDouble(Double.parseDouble(dataList[i]));
                            break;
                        case "DATETIME":
                            String temp = dataList[i];
                            Date dateTime = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").parse(temp);
                            file.writeLong(dateTime.getTime());
                            // To read: new Timestamp(file.readLong())
                            break;
                        case "DATE":
                            String temp2 = dataList[i];
                            temp2 = temp2 + "_00:00:00";
                            Date date = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").parse(temp2);
                            file.writeLong(date.getTime());
                            // To read: new Timestamp(file.readLong())
                            break;
                        case "TEXT":
                            file.writeBytes(dataList[i]);
                            break;
                    }
                }
                //file.seek(504);
                //System.out.println( new Timestamp(file.readLong()) );
            } catch (Exception e) {
                out.println("Error in writing record");
                out.println(e);
            }
        }

        public static void findRecord(int columnNum, String dataType, String comparison, String compareValue) {
            /*
            out.println("column number is: " + columnNum);
            out.println("data type is: " + dataType);
            out.println("comparison is: " + comparison);
            out.println("compare value is: " + compareValue);
            */
            try {
                for (int i = 1; i <= pageNum; i++) {
                    int currentPageOffset = (pageNum - 1) * pageSize;
                    file.seek(currentPageOffset);
                    // skip the nonleaf pages
                    if (file.readByte() != 13) {
                        continue;
                    }

                    file.seek(currentPageOffset + 1);
                    int numRecords = file.readByte();

                    for (int recordNum = 1; recordNum <= numRecords; recordNum++) {
                        file.seek(currentPageOffset + 8 + (recordNum - 1) * 2);
                        int recordOffset = file.readShort();
                        if (recordOffset == 0)
                            continue;
                        // move cursor to the record data types
                        file.seek(currentPageOffset + recordOffset + 6);
                        int numColumns = file.readByte();
                        int columnOffset = 0;
                        for (int j = 1; j < columnNum; j++) {
                            columnOffset += getDataTypeSize( file.readByte() );
                        }
                        int dataLength = getDataTypeSize( file.readByte() );
                        // move the cursor to the column
                        file.seek(currentPageOffset + recordOffset
                                + 7 + numColumns + columnOffset);

                        switch (dataType) {
                            case "TINYINT":
                                int data1 = file.readByte();
                                switch (comparison) {
                                    case "=":
                                        if (data1 == Integer.parseInt(compareValue))
                                            deleteRecord(file, currentPageOffset, recordNum--);
                                        break;
                                    case ">":
                                        if (data1 > Integer.parseInt(compareValue))
                                            deleteRecord(file, currentPageOffset, recordNum--);
                                        break;
                                    case "<":
                                        if (data1 < Integer.parseInt(compareValue))
                                            deleteRecord(file, currentPageOffset, recordNum--);
                                        break;
                                    case ">=":
                                        if (data1 >= Integer.parseInt(compareValue))
                                            deleteRecord(file, currentPageOffset, recordNum--);
                                        break;
                                    case "<=":
                                        if (data1 <= Integer.parseInt(compareValue))
                                            deleteRecord(file, currentPageOffset, recordNum--);
                                        break;
                                    default:
                                        out.print("Unknown comparison type!");
                                        return;
                                }
                                break;

                            case "SMALLINT":
                                int data2 = file.readShort();
                                switch (comparison) {case "=":
                                        if (data2 == Integer.parseInt(compareValue))
                                            deleteRecord(file, currentPageOffset, recordNum--);
                                        break;
                                    case ">":
                                        if (data2 > Integer.parseInt(compareValue))
                                            deleteRecord(file, currentPageOffset, recordNum--);
                                        break;
                                    case "<":
                                        if (data2 < Integer.parseInt(compareValue))
                                            deleteRecord(file, currentPageOffset, recordNum--);
                                        break;
                                    case ">=":
                                        if (data2 >= Integer.parseInt(compareValue))
                                            deleteRecord(file, currentPageOffset, recordNum--);
                                        break;
                                    case "<=":
                                        if (data2 <= Integer.parseInt(compareValue))
                                            deleteRecord(file, currentPageOffset, recordNum--);
                                        break;
                                    default:
                                        out.print("Unknown comparison type!");
                                        return;
                                }
                                break;

                            case "INT":
                                int data3 = file.readInt();
                                switch (comparison) {case "=":
                                        if (data3 == Integer.parseInt(compareValue))
                                            deleteRecord(file, currentPageOffset, recordNum--);
                                        break;
                                    case ">":
                                        if (data3 > Integer.parseInt(compareValue))
                                            deleteRecord(file, currentPageOffset, recordNum--);
                                        break;
                                    case "<":
                                        if (data3 < Integer.parseInt(compareValue))
                                            deleteRecord(file, currentPageOffset, recordNum--);
                                        break;
                                    case ">=":
                                        if (data3 >= Integer.parseInt(compareValue))
                                            deleteRecord(file, currentPageOffset, recordNum--);
                                        break;
                                    case "<=":
                                        if (data3 <= Integer.parseInt(compareValue))
                                            deleteRecord(file, currentPageOffset, recordNum--);
                                        break;
                                    default:
                                        out.print("Unknown comparison type!");
                                        return;
                                }
                                break;

                            case "BIGINT":
                                long data4 = file.readLong();
                                switch (comparison) {case "=":
                                        if (data4 == Long.parseLong(compareValue))
                                            deleteRecord(file, currentPageOffset, recordNum--);
                                        break;
                                    case ">":
                                        if (data4 > Long.parseLong(compareValue))
                                            deleteRecord(file, currentPageOffset, recordNum--);
                                        break;
                                    case "<":
                                        if (data4 < Long.parseLong(compareValue))
                                            deleteRecord(file, currentPageOffset, recordNum--);
                                        break;
                                    case ">=":
                                        if (data4 >= Long.parseLong(compareValue))
                                            deleteRecord(file, currentPageOffset, recordNum--);
                                        break;
                                    case "<=":
                                        if (data4 <= Long.parseLong(compareValue))
                                            deleteRecord(file, currentPageOffset, recordNum--);
                                        break;
                                    default:
                                        out.print("Unknown comparison type!");
                                        return;
                                }
                                break;

                            case "FLOAT":
                                float data5 = file.readFloat();
                                switch (comparison) {case "=":
                                        if (data5 == Float.parseFloat(compareValue))
                                            deleteRecord(file, currentPageOffset, recordNum--);
                                        break;
                                    case ">":
                                        if (data5 > Float.parseFloat(compareValue))
                                            deleteRecord(file, currentPageOffset, recordNum--);
                                        break;
                                    case "<":
                                        if (data5 < Float.parseFloat(compareValue))
                                            deleteRecord(file, currentPageOffset, recordNum--);
                                        break;
                                    case ">=":
                                        if (data5 >= Float.parseFloat(compareValue))
                                            deleteRecord(file, currentPageOffset, recordNum--);
                                        break;
                                    case "<=":
                                        if (data5 <= Float.parseFloat(compareValue))
                                            deleteRecord(file, currentPageOffset, recordNum--);
                                        break;
                                    default:
                                        out.print("Unknown comparison type!");
                                        return;
                                }
                                break;

                            case "DOUBLE":
                                double data6 = file.readDouble();
                                switch (comparison) {case "=":
                                        if (data6 == Double.parseDouble(compareValue))
                                            deleteRecord(file, currentPageOffset, recordNum--);
                                        break;
                                    case ">":
                                        if (data6 > Double.parseDouble(compareValue))
                                            deleteRecord(file, currentPageOffset, recordNum--);
                                        break;
                                    case "<":
                                        if (data6 < Double.parseDouble(compareValue))
                                            deleteRecord(file, currentPageOffset, recordNum--);
                                        break;
                                    case ">=":
                                        if (data6 >= Double.parseDouble(compareValue))
                                            deleteRecord(file, currentPageOffset, recordNum--);
                                        break;
                                    case "<=":
                                        if (data6 <= Double.parseDouble(compareValue))
                                            deleteRecord(file, currentPageOffset, recordNum--);
                                        break;
                                    default:
                                        out.print("Unknown comparison type!");
                                        return;
                                }
                                break;

                            case "DATETIME":
                                long data7 = file.readLong();
                                Date dateTime = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").parse(compareValue);
                                long compareDateTime = dateTime.getTime();
                                switch (comparison) {case "=":
                                        if (data7 == compareDateTime)
                                            deleteRecord(file, currentPageOffset, recordNum--);
                                        break;
                                    case ">":
                                        if (data7 > compareDateTime)
                                            deleteRecord(file, currentPageOffset, recordNum--);
                                        break;
                                    case "<":
                                        if (data7 < compareDateTime)
                                            deleteRecord(file, currentPageOffset, recordNum--);
                                        break;
                                    case ">=":
                                        if (data7 >= compareDateTime)
                                            deleteRecord(file, currentPageOffset, recordNum--);
                                        break;
                                    case "<=":
                                        if (data7 <= compareDateTime)
                                            deleteRecord(file, currentPageOffset, recordNum--);
                                        break;
                                    default:
                                        out.print("Unknown comparison type!");
                                        return;
                                }
                                break;

                            case "DATE":
                                long data8 = file.readLong();
                                Date date = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").parse(compareValue + "_00:00:00");
                                long compareDate = date.getTime();
                                //out.println("data8: " + data8);
                                //out.println("compareDate: " + compareDate);
                                switch (comparison) {case "=":
                                        if (data8 == compareDate)
                                            deleteRecord(file, currentPageOffset, recordNum--);
                                        break;
                                    case ">":
                                        if (data8 > compareDate)
                                            deleteRecord(file, currentPageOffset, recordNum--);
                                        break;
                                    case "<":
                                        if (data8 < compareDate)
                                            deleteRecord(file, currentPageOffset, recordNum--);
                                        break;
                                    case ">=":
                                        if (data8 >= compareDate)
                                            deleteRecord(file, currentPageOffset, recordNum--);
                                        break;
                                    case "<=":
                                        if (data8 <= compareDate)
                                            deleteRecord(file, currentPageOffset, recordNum--);
                                        break;
                                    default:
                                        out.print("Unknown comparison type!");
                                        return;
                                }
                                break;

                            case "TEXT":
                                String data9 = "";
                                for(int len = 0; len < dataLength; len++) {
                                    data9 += (char) file.readByte();
                                }

                                if (comparison.equals("=")) {
                                    if (data9.equalsIgnoreCase(compareValue))
                                        deleteRecord(file, currentPageOffset, recordNum--);
                                }
                                else {
                                    out.println("Unknown comparison type!");
                                    return;
                                }
                                break;

                            default:
                                out.println("Unknown dataType");
                                return;
                        }

                    }

                }
            } catch (Exception e) {
                out.println("Error in finding record!");
                out.println(e);
            }
        }

        public static void deleteRecord(RandomAccessFile file, int pageOffset, int recordNum) {
            //out.println("page offset: " + pageOffset);
            //out.println("record num: " + recordNum);
            try {
                file.seek(pageOffset + 1);
                int numRecord = file.readByte();
                numRecord--;
                file.seek(pageOffset + 1);
                file.writeByte(numRecord);

                // move cursor to the recordOffset
                file.seek(pageOffset + 8 + recordNum * 2);

                int currentPos = file.readShort();

                while (currentPos != 0) {
                    file.seek(file.getFilePointer() - 4);
                    file.writeShort(currentPos);
                    file.seek(file.getFilePointer() + 2);
                    currentPos = file.readShort();
                }
                file.seek(file.getFilePointer() - 4);
                file.writeShort(0);
            } catch (Exception e) {
                out.println("Error in deleting record!");
                out.println(e);
            }
        }

        public static int updateInteriorPage(int rowid) {

            int rootPage = getRootPage();

            try {
                // if rootPage = 0, there is no interior page yet
                // need to generate a new interiorPage as the root page
                if (rootPage == 0) {
                    newInteriorPage();
                    // update root page
                    updateRootPage(pageNum);
                    // update number of records
                    file.seek(pageOffset + 1);
                    file.writeByte(1);
                    // update start of content area
                    // 4-byte rowid + 4-byte left child page pointer = 8
                    int recordOffset = pageSize - 8;
                    file.writeShort(recordOffset);
                    // update right most child page pointer
                    file.writeInt(pageNum + 1);
                    // update 1-st record offset
                    file.seek(pageOffset + 8);
                    file.writeShort(recordOffset);
                    // update the date: rowid key + left child page pointers
                    file.seek(pageOffset + recordOffset);
                    file.writeInt(rowid);
                    file.writeInt(pageNum - 1);
                } else {
                    int interiorPageOffset = (rootPage - 1) * pageSize;
                    // move to the position of rightmost child pointer
                    file.seek(interiorPageOffset + 4);
                    int rightmostChildPageNum = file.readInt();
                    // if the current interior node is not the lowest level
                    // of interior node, keep looking
                    while (rightmostChildPageNum != pageNum) {
                        // need to find the lower level interior node
                        interiorPageOffset = (rightmostChildPageNum - 1) * pageSize;
                        file.seek(interiorPageOffset + 4);
                        rightmostChildPageNum = file.readInt();
                    }

                    // move the cursor to read number of records
                    file.seek(interiorPageOffset + 1);
                    int numRecords = file.readByte();
                    numRecords++;
                    int interiorPageNum = interiorPageOffset / pageSize + 1;
                    // interior page has 8-byte header, and 10-byte (2+4+4)
                    // for each record, so it can fit at most (512-8)/10 = 50
                    // record, if new number of records > 50, overflow!!
                    if (numRecords > 50 && rootPage == interiorPageNum) {
                        int oldRootPage = getRootPage();
                        newInteriorPage();
                        // this will be the new root page
                        // update root page
                        updateRootPage(pageNum);
                        // update number of records
                        file.seek(pageOffset + 1);
                        file.writeByte(1);
                        // update start of content area
                        // 4-byte rowid + 4-byte left child page pointer = 8
                        int recordOffset = pageSize - 8;
                        file.writeShort(recordOffset);
                        // update right most child page pointer
                        file.writeInt(pageNum + 1);
                        // update 1-st record offset
                        file.seek(pageOffset + 8);
                        file.writeShort(recordOffset);
                        // update the date: rowid key + left child page pointers
                        file.seek(pageOffset + recordOffset);
                        file.writeInt(rowid);
                        file.writeInt(oldRootPage);
                        newInteriorPage();
                        updateInteriorPage(rowid);
                    } else if (numRecords > 50) {
                        newInteriorPage();
                        updateInteriorPage(rowid);
                    }
                    // update number of records
                    file.seek(interiorPageOffset + 1);
                    file.writeByte(numRecords);

                    // update start of content offset
                    file.seek(interiorPageOffset + 2);
                    int recordOffset = file.readShort();
                    recordOffset -= 8;
                    file.seek(interiorPageOffset + 2);
                    file.writeShort(recordOffset);

                    // update content offset list
                    // 8-byte header + (numRecords - 1)*2
                    file.seek(interiorPageOffset + 6 + numRecords * 2);
                    file.writeShort(recordOffset);

                    // update rightmost child pointer
                    file.seek(interiorPageOffset + 4);
                    file.writeInt(pageNum + 1);

                    // update record
                    file.seek(interiorPageOffset + recordOffset);
                    file.writeInt(rowid);
                    file.writeInt(pageNum);
                }
            } catch (Exception e) {
                out.println("Error in updating InteriorPage");
                out.println(e);
            }
            return rowid;
        }

        public static int getRowid() {
            int rowid = 0;
            try {
                // move to start of record and read
                file.seek(pageOffset + 2);
                int recordPos = file.readShort();
                if (recordPos == pageSize) {
                    return rowid;
                }

                // move to the last record and past 2-byte payload
                file.seek(pageOffset + recordPos + 2);
                // read rowid
                rowid = file.readInt();
            } catch (Exception e) {
                out.println("Error in getting rowid");
                out.println(e);
            }
            return rowid;
        }

        // this function will return a list of columns catalog info
        // with i to be the number of columns
        // j=0: table name, 1: column name, 2: data type, 3: isnullable
        public static String[][] getColumnsCatalog() {
            ArrayList<String> columnsCatalogInfo = new ArrayList();
            String result[][] = new String[0][0];
            try {
                RandomAccessFile columnsCatalog
                        = new RandomAccessFile("data/catalog/davisbase_columns.tbl", "rw");
                int fileSize = (int) columnsCatalog.length();
                int numPages = fileSize / pageSize;

                for (int pageNum = 1; pageNum <= numPages; pageNum++) {
                    int pageOffset = (pageNum - 1) * pageSize;
                    // move cursor to current page
                    columnsCatalog.seek(pageOffset);
                    // check if it is not a leaf table page, skip
                    if (columnsCatalog.readByte() != 13) {
                        continue;
                    }
                    int numRecords = columnsCatalog.readByte();

                    for (int recordNum = 1; recordNum <= numRecords; recordNum++) {
                        // record offset is located at 8-byte header + (recordNum-1)*2
                        int recordOffsetPos = 8 + (recordNum - 1) * 2;
                        columnsCatalog.seek(pageOffset + recordOffsetPos);
                        int recordOffset = columnsCatalog.readShort();
                        // 2-byte payload + 4-byte rowid
                        columnsCatalog.seek(pageOffset + recordOffset + 6);
                        int numColumns = columnsCatalog.readByte();
                        // current position is the data structures list
                        // which includes: TEXT, TEXT, TEXT, TINYINT, TEXT
                        // with the first column as the table_name
                        int tableNameLength = getDataTypeSize(columnsCatalog.readByte());
                        // get the tablename
                        // move the cursor to the offSet of table name
                        // 2-byte payload + 4-byte rowid + 1-byte numColumns + numColumns
                        int columnsOffset = 7 + numColumns;
                        columnsCatalog.seek(pageOffset + recordOffset + columnsOffset);

                        String tName = "";
                        for (int len = 0; len < tableNameLength; len++) {
                            tName += (char) columnsCatalog.readByte();
                        }
                        //out.println(tName);
                        // if table name != tableName, skip
                        if (!tName.equalsIgnoreCase(tableName)) {
                            continue;
                        }

                        // testing
                        int columnsDataTypeLength[] = new int[numColumns];
                        // move cursor back to data types list
                        columnsCatalog.seek(pageOffset + recordOffset + 7);
                        for (int i = 0; i < numColumns; i++) {
                            columnsDataTypeLength[i] = getDataTypeSize(columnsCatalog.readByte());
                        }
                        //out.println(dataTypeOffset);

                        // move cursor to the columns
                        columnsCatalog.seek(pageOffset + recordOffset + columnsOffset);
                        //String temp;
                        for (int i = 0; i < numColumns; i++) {
                            if (columnsDataTypeLength[i] == 1) {
                                columnsCatalog.readByte();
                                continue;
                            }
                            String temp = "";
                            for (int len = 0; len < columnsDataTypeLength[i]; len++) {
                                temp += (char) columnsCatalog.readByte();
                            }
                            //out.println(temp);
                            columnsCatalogInfo.add(temp);
                        }
                    }
                }
                int numRows = columnsCatalogInfo.size() / 4;
                result = new String[numRows][4];
                for (int i = 0; i < numRows; i++) {
                    for (int j = 0; j < 4; j++) {
                        result[i][j] = columnsCatalogInfo.get(4 * i + j);
                    }
                }

            } catch (Exception e) {
                out.println("Error in reading from columns catalog!");
                out.println(e);
            }
            return result;
        }

        public static int getRootPage() {
            int rootPage = 0;
            try {
                RandomAccessFile tablesCatalog
                        = new RandomAccessFile("data/catalog/davisbase_tables.tbl", "rw");
                int fileSize = (int) tablesCatalog.length();
                int numPages = fileSize / pageSize;

                for (int pageNum = 1; pageNum <= numPages; pageNum++) {
                    int pageOffset = (pageNum - 1) * pageSize;
                    // move cursor to current page
                    tablesCatalog.seek(pageOffset);
                    // check if it is not a leaf table page, skip
                    if (tablesCatalog.readByte() != 13) {
                        continue;
                    }
                    int numRecords = tablesCatalog.readByte();

                    for (int recordNum = 1; recordNum <= numRecords; recordNum++) {
                        // record offset is located at 8-byte header + (recordNum-1)*2
                        int recordOffsetPos = 8 + (recordNum - 1) * 2;
                        tablesCatalog.seek(pageOffset + recordOffsetPos);
                        int recordOffset = tablesCatalog.readShort();
                        // 2-byte payload + 4-byte rowid
                        tablesCatalog.seek(pageOffset + recordOffset + 6);
                        int numColumns = tablesCatalog.readByte();
                        // current position is the data structures list
                        // which includes: TEXT, SMALLINT
                        // with the first column as the table_name
                        int tableNameLength = getDataTypeSize(tablesCatalog.readByte());
                        // get the tablename
                        // move the cursor to the offSet of table name
                        // 2-byte payload + 4-byte rowid + 1-byte numColumns + numColumns
                        int columnsOffset = 7 + numColumns;
                        tablesCatalog.seek(pageOffset + recordOffset + columnsOffset);

                        String name = "";
                        for (int len = 0; len < tableNameLength; len++) {
                            name += (char) tablesCatalog.readByte();
                        }
                        // if name != tableName, skip
                        if (!name.equals(tableName)) {
                            continue;
                        }
                        // rootPage is the 2nd column in a record
                        // calculate the length of the first column
                        // save it as rootPageOffset
                        int rootPageOffset = 0;
                        // move cursor back to data types list
                        tablesCatalog.seek(pageOffset + recordOffset + 7);
                        rootPageOffset += getDataTypeSize(tablesCatalog.readByte());
                        // move cursor to the rootPage column
                        tablesCatalog.seek(pageOffset + recordOffset + columnsOffset + rootPageOffset);
                        rootPage = tablesCatalog.readShort();
                        //out.println("rootPage: " + rootPage);
                    }
                }
            } catch (Exception e) {
                out.println("Error in reading from columns catalog!");
                out.println(e);
            }
            return rootPage;
        }

        public static void updateRootPage(int newRootPage) {

            try {
                RandomAccessFile tablesCatalog
                        = new RandomAccessFile("data/catalog/davisbase_tables.tbl", "rw");
                int fileSize = (int) tablesCatalog.length();
                int numPages = fileSize / pageSize;

                for (int pageNum = 1; pageNum <= numPages; pageNum++) {
                    int pageOffset = (pageNum - 1) * pageSize;
                    // move cursor to current page
                    tablesCatalog.seek(pageOffset);
                    // check if it is not a leaf table page, skip
                    if (tablesCatalog.readByte() != 13) {
                        continue;
                    }
                    int numRecords = tablesCatalog.readByte();

                    for (int recordNum = 1; recordNum <= numRecords; recordNum++) {
                        // record offset is located at 8-byte header + (recordNum-1)*2
                        int recordOffsetPos = 8 + (recordNum - 1) * 2;
                        tablesCatalog.seek(pageOffset + recordOffsetPos);
                        int recordOffset = tablesCatalog.readShort();
                        // 2-byte payload + 4-byte rowid
                        tablesCatalog.seek(pageOffset + recordOffset + 6);
                        int numColumns = tablesCatalog.readByte();
                        // current position is the data structures list
                        // which includes: TEXT, SMALLINT
                        // with the first column as the table_name
                        int tableNameLength = getDataTypeSize(tablesCatalog.readByte());
                        // get the tablename
                        // move the cursor to the offSet of table name
                        // 2-byte payload + 4-byte rowid + 1-byte numColumns + numColumns
                        int columnsOffset = 7 + numColumns;
                        tablesCatalog.seek(pageOffset + recordOffset + columnsOffset);

                        String name = "";
                        for (int len = 0; len < tableNameLength; len++) {
                            name += (char) tablesCatalog.readByte();
                        }
                        // if name != tableName, skip
                        if (!name.equals(tableName)) {
                            continue;
                        }
                        // rootPage is the 2nd column in a record
                        // calculate the length of the first column
                        // save it as rootPageOffset
                        int rootPageOffset = 0;
                        // move cursor back to data types list
                        tablesCatalog.seek(pageOffset + recordOffset + 7);
                        rootPageOffset += getDataTypeSize(tablesCatalog.readByte());
                        // move cursor to the rootPage column
                        tablesCatalog.seek(pageOffset + recordOffset + columnsOffset + rootPageOffset);
                        tablesCatalog.writeShort(newRootPage);
                        //out.println("rootPage: " + rootPage);
                    }
                }
            } catch (Exception e) {
                out.println("Error in reading from columns catalog!");
                out.println(e);
            }
        }

    } // end of class TablePage

// define main class functions
/*===========================================================================*/
    public static void initializeDataStore() {

        /* Create data directory at the current location */
        try {
            File dataDir = new File("data");
            //delete old table files
            String[] oldTableFiles;
            /*
			oldTableFiles = dataDir.list();
			for (int i=0; i<oldTableFiles.length; i++) {
				File anOldFile = new File(dataDir, oldTableFiles[i]);
				anOldFile.delete();
			}
             */

            File catalogDir = new File("data/catalog");
            // delete old table files
            oldTableFiles = catalogDir.list();
            for (int i = 0; i < oldTableFiles.length; i++) {
                File anOldFile = new File(catalogDir, oldTableFiles[i]);
                anOldFile.delete();
            }

            File userDataDir = new File("data/user_data");
            // delete old table files
            oldTableFiles = userDataDir.list();
            for (int i = 0; i < oldTableFiles.length; i++) {
                File anOldFile = new File(userDataDir, oldTableFiles[i]);
                anOldFile.delete();
            }

            dataDir.mkdir();
            catalogDir.mkdir();
            userDataDir.mkdir();

        } catch (SecurityException se) {
            out.println("Unable to create data container directory");
            out.println(se);
        }

        // Create tables system catalog
        TablePage tablesCatalog = new TablePage("davisbase_tables");
        tablesCatalog.newLeafPage();

        String tablesCatalogData[] = {"davisbase_tables", "0"};
        tablesCatalog.writeRecord(tablesCatalogData, tablesCatalogDataType);

        tablesCatalogData[0] = "davisbase_columns";
        tablesCatalog.writeRecord(tablesCatalogData, tablesCatalogDataType);

        // Create columns system catalog
        TablePage columnsCatalog = new TablePage("davisbase_columns");
        columnsCatalog.newLeafPage();
        // columns in davisbase_tables
        String row1[] = {"davisbase_tables", "rowid", "INT", "1", "NO"};
        columnsCatalog.writeRecord(row1, columnsCatalogDataType);
        String row2[] = {"davisbase_tables", "table_name", "TEXT", "2", "NO"};
        columnsCatalog.writeRecord(row2, columnsCatalogDataType);
        String row3[] = {"davisbase_tables", "root_page", "SMALLINT", "3", "NO"};
        columnsCatalog.writeRecord(row3, columnsCatalogDataType);
        // columns in davisbase_columns
        String row4[] = {"davisbase_columns", "rowid", "INT", "1", "NO"};
        columnsCatalog.writeRecord(row4, columnsCatalogDataType);
        String row5[] = {"davisbase_columns", "table_name", "TEXT", "2", "NO"};
        columnsCatalog.writeRecord(row5, columnsCatalogDataType);
        String row6[] = {"davisbase_columns", "column_name", "TEXT", "3", "NO"};
        columnsCatalog.writeRecord(row6, columnsCatalogDataType);
        String row7[] = {"davisbase_columns", "data_type", "TEXT", "4", "NO"};
        columnsCatalog.writeRecord(row7, columnsCatalogDataType);
        String row8[] = {"davisbase_columns", "ordinal_position", "TINYINT", "5", "NO"};
        columnsCatalog.writeRecord(row8, columnsCatalogDataType);
        String row9[] = {"davisbase_columns", "is_nullable", "TEXT", "6", "NO"};
        columnsCatalog.writeRecord(row9, columnsCatalogDataType);
    }

    public static int getDataTypeCode(String dataTypeStr) {
        // if -1 is returned, the dataType is unknown
        int dataTypeCode = -1;

        switch (dataTypeStr.toUpperCase()) {

            case "NULL":
                dataTypeCode = 0;
                break;
            case "TINYINT":
                dataTypeCode = 1;
                break;
            case "SMALLINT":
                dataTypeCode = 2;
                break;
            case "INT":
                dataTypeCode = 3;
                break;
            case "BIGINT":
                dataTypeCode = 4;
                break;
            case "FLOAT":
                dataTypeCode = 5;
                break;
            case "DOUBLE":
                dataTypeCode = 6;
                break;
            case "DATETIME":
                dataTypeCode = 7;
                break;
            case "DATE":
                dataTypeCode = 8;
                break;
            case "TEXT":
                dataTypeCode = 10;
                // + TEXT length
                break;
        }
        //System.out.println(dataType);
        return dataTypeCode;
    }

    public static int getDataTypeSize(String dataTypeStr) {
        // if -1 is returned, the dataType is unknown
        int size = -1;

        switch (dataTypeStr.toUpperCase()) {

            case "NULL":
                size = 0;
                break;
            case "TINYINT":
                size = 1;
                break;
            case "SMALLINT":
                size = 2;
                break;
            case "INT":
                size = 4;
                break;
            case "BIGINT":
                size = 8;
                break;
            case "FLOAT":
                size = 4;
                break;
            case "DOUBLE":
                size = 8;
                break;
            case "DATETIME":
                size = 8;
                break;
            case "DATE":
                size = 8;
                break;
            /*
            case "TEXT":
                size will be calculated differently
             */
        }
        return size;
    }

    public static int getDataTypeSize(int dataTypeCode) {
        // if -1 is returned, the dataType is unknown
        int size = -1;

        switch (dataTypeCode) {

            case 0: // NULL
                size = 0;
                break;
            case 1: // TINYINT
                size = 1;
                break;
            case 2: // SMALLINT
                size = 2;
                break;
            case 3: // INT
                size = 4;
                break;
            case 4: // BIGINT
                size = 8;
                break;
            case 5: // FLOAT
                size = 4;
                break;
            case 6: // DOUBLE
                size = 8;
                break;
            case 7: // DATETIME
                size = 8;
                break;
            case 8: // DATE
                size = 8;
                break;
            default: // TEXT
                size = dataTypeCode - 10;
        }
        return size;
    }

    private static String[] getCondition(String query) {
        String[] params = null;
        query = query.trim();
        final Matcher matcher = Pattern.compile("(?i)where").matcher(query);
        if (matcher.find()) {
            String condition = query.substring(matcher.end()).trim();
            String operator = getOperator(condition);
            String[] splited = condition.split(operator);
            if (splited.length == 2) {
                params = new String[3];
                params[0] = splited[0].trim();
                params[1] = operator.trim();
                params[2] = splited[1].trim();
            }
        }
        return params;
    }

    private static String getOperator(String string) {
        if (string.contains(">=")) {
            return ">=";
        } else if (string.contains("<=")) {
            return "<=";
        } else if (string.contains(">")) {
            return ">";
        } else if (string.contains("<")) {
            return "<";
        } else if (string.contains("=")) {
            return "=";
        } else {
            out.println("Unknown comparison!");
            return null;
        }
    }
}
