
package davisbase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class Table_Update {
	
	static String tablename="davisbase_tables";
	static String tablename1 = "davisbase_columns";
	static int pageSize = 512;
	
	public static void initilization() {
		File data = new File("data");
    	if(data.mkdir()){
    		System.out.println("Creating data directory");
			System.out.println();
			data.mkdir();
    		File catalog = new File("data/catalog");
    		if(catalog.mkdir())
			{
				System.out.println("Creating data/catalog directory");
				System.out.println();
				catalog.mkdir();
				createtablestable();
				createColumnsTable();
				File user_data = new File("data/user_data");
				if(user_data.mkdir()){
					System.out.println("Creating data/user_data directory");
					System.out.println();
					user_data.mkdir();
				}
				
			}
			else{
				File user_data = new File("data/user_data");
				if(user_data.mkdir()){
					System.out.println("Creating data/user_data directory");
					System.out.println();
					user_data.mkdir();
				}
				String meta1 = "davisbase_tables.tbl";
				String meta2 = "davisbase_columns.tbl";
				String[] TableFiles = catalog.list();
				boolean check = false;
				for (int i=0; i<TableFiles.length; i++) {
					if(TableFiles[i].equals(meta1))
						check = true;
				}
				if(!check)
					createtablestable();
				check = false;
				for (int i=0; i<TableFiles.length; i++) {
					if(TableFiles[i].equals(meta2))
						check = true;
				}
				if(!check){
					createColumnsTable();
				}
			}
		}
    	else
    	{
    		File catalog = new File("data/catalog");
    		if(catalog.mkdir())
			{
				System.out.println("Creating data/catalog directory");
				System.out.println();
				catalog.mkdir();
				File user_data = new File("data/user_data");
				if(user_data.mkdir()){
					System.out.println("Creating data/user_data directory");
					System.out.println();
					user_data.mkdir();
					createtablestable();
					createColumnsTable();
				}
				
			}
			else{
				File user_data = new File("data/user_data");
				if(user_data.mkdir()){
					System.out.println("Creating data/user_data directory");
					System.out.println();
					user_data.mkdir();
				}
				String meta1 = "davisbase_tables.tbl";
				String meta2 = "davisbase_columns.tbl";
				String[] TableFiles = catalog.list();
				boolean check = false;
				for (int i=0; i<TableFiles.length; i++) {
					if(TableFiles[i].equals(meta1))
						check = true;
				}
				if(!check)
					createtablestable();
				check = false;
				for (int i=0; i<TableFiles.length; i++) {
					if(TableFiles[i].equals(meta2))
						check = true;
				}
				if(!check){
					createColumnsTable();
				}
			}
    	}
	}
	
	static void  createtablestable() {
		String filename= "davisbase_tables.tbl";
		try 
		{
			String path = "data/catalog/";
			RandomAccessFile dt = new RandomAccessFile(path+filename,"rw");
			dt.setLength(pageSize*1);
			
			int recordLocation = 0;
			int currentPage = 0;
			
			int  pageLocation = pageSize * currentPage;
			
			//Write headers
			dt.seek(pageLocation + recordLocation);
			dt.write(0x0D); // leaf table
			dt.write(0x02); // no of cells
			
			int payload_length1=26;
			int recordSize1=32;
			
			int payload_length2=27;
			int recordsize2=33;
			int[] recordpositions = new int[2];
			recordpositions[0]=pageSize-recordSize1;
			recordpositions[1]=recordpositions[0]-recordsize2;
			
			dt.writeShort(recordpositions[1]);
			
			dt.writeInt(-1);
			
			dt.writeShort(recordpositions[0]);
			dt.writeShort(recordpositions[1]);
			
			// write 1st record
			dt.seek(recordpositions[0]);
			dt.writeShort(payload_length1);
			dt.writeInt(1);
			dt.write(0x03);
			dt.write(0x1C);
			dt.write(0x06);
			dt.write(0x05);
			
			dt.writeBytes(tablename);
			dt.writeInt(0);
			dt.writeShort(0);
			
			//insert 2nd record davisbase_columns
			dt.seek(recordpositions[1]);
			dt.writeShort(payload_length2);
			dt.writeInt(2);
			dt.write(0x03);
			dt.write(0x1D);
			dt.write(0x06);
			dt.write(0x05);
			
			dt.writeBytes(tablename1);
			dt.writeInt(0);
			dt.writeShort(0);
			
			dt.close();
		} catch (Exception e) {
			System.out.println("create davisbase_tables.tbl error!");
			e.printStackTrace();
		}
	}
	
	static void createColumnsTable() {
		String filename= "davisbase_columns.tbl";
		String path = "data/catalog/";
		try {
			RandomAccessFile dc = new RandomAccessFile(path+filename, "rw");
			dc.setLength(pageSize*1);
			int recordLocation = 0;
			
			int currentPage = 0;
			int  pageLocation = pageSize * currentPage;
			
			dc.seek(pageLocation + recordLocation);
			dc.write(0x0D);
			
			dc.write(10);
			
			int[] recordPositions = new int[10];
			int[] pay=new int[]{33,39,40,43,34,40,41,39,49,41};
			
			int rec_header=6;
			
			recordPositions[0]=pageSize-pay[0]-rec_header;
			recordPositions[1]=recordPositions[0]-pay[1]-rec_header;
			recordPositions[2]=recordPositions[1]-pay[2]-rec_header;
			recordPositions[3]=recordPositions[2]-pay[3]-rec_header;
			recordPositions[4]=recordPositions[3]-pay[4]-rec_header;
			recordPositions[5]=recordPositions[4]-pay[5]-rec_header;
			recordPositions[6]=recordPositions[5]-pay[6]-rec_header;
			recordPositions[7]=recordPositions[6]-pay[7]-rec_header;
			recordPositions[8]=recordPositions[7]-pay[8]-rec_header;
			recordPositions[9]=recordPositions[8]-pay[9]-rec_header;
			
			dc.writeShort(recordPositions[9]);
			
			dc.writeInt(-1);
			
			for(int i =0;i<10;i++)
				dc.writeShort(recordPositions[i]);
			
			
			//1st record
			dc.seek(recordPositions[0]);
			dc.writeShort(pay[0]);
			dc.writeInt(1);
			
			dc.write(0x05);
			dc.write(0x1C);
			dc.write(0x11);
			dc.write(0x0F);
			dc.write(0x04);
			dc.write(0x0E);
			
			dc.writeBytes(tablename);
			dc.writeBytes("rowid");
			dc.writeBytes("INT");
			dc.write(1);
			dc.writeBytes("NO");
			
			//insert 2nd record
			dc.seek(recordPositions[1]);
			dc.writeShort(pay[1]);
			dc.writeInt(2);
			
			dc.write(0x05);	
			dc.write(0x1C);
			dc.write(0x16);
			dc.write(0x10);
			dc.write(0x04);
			dc.write(0x0E);
			
			dc.writeBytes(tablename);
			dc.writeBytes("table_name");
			dc.writeBytes("TEXT");
			dc.write(2);
			dc.writeBytes("NO");
			
			//insert 3rd record
			dc.seek(recordPositions[2]);
			dc.writeShort(pay[2]);
			dc.writeInt(3);
			dc.write(0x05);
			dc.write(0x1C);
			dc.write(0x18);
			dc.write(0x0F);
			dc.write(0x04);
			dc.write(0x0E);	
			dc.writeBytes(tablename);
			dc.writeBytes("record_count");
			dc.writeBytes("INT");
			dc.write(3);
			dc.writeBytes("NO");
			
			
			//insert 4th record
			dc.seek(recordPositions[3]);
			dc.writeShort(pay[3]);
			dc.writeInt(4);
			dc.write(0x05);
			dc.write(0x1C);
			dc.write(0x16);
			dc.write(0x14);
			dc.write(0x04);
			dc.write(0x0E);	
			dc.writeBytes(tablename);
			dc.writeBytes("avg_length");
			dc.writeBytes("SMALLINT");
			dc.write(4);
			dc.writeBytes("NO");
			
			//insert 5th record
			dc.seek(recordPositions[4]);
			dc.writeShort(pay[4]);
			dc.writeInt(5);
			dc.write(0x05);
			dc.write(0x1D);
			dc.write(0x11);
			dc.write(0x0F);
			dc.write(0x04);
			dc.write(0x0E);
			dc.writeBytes(tablename1);
			dc.writeBytes("rowid");
			dc.writeBytes("INT");
			dc.write(1);
			dc.writeBytes("NO");
			
			//insert 6th record
			dc.seek(recordPositions[5]);
			dc.writeShort(pay[5]);
			dc.writeInt(6);
			dc.write(0x05);
			dc.write(0x1D);
			dc.write(0x16);
			dc.write(0x10);
			dc.write(0x04);
			dc.write(0x0E);
			dc.writeBytes(tablename1);
			dc.writeBytes("table_name");
			dc.writeBytes("TEXT");
			dc.write(2);
			dc.writeBytes("NO");
			
			//insert 7th record
			dc.seek(recordPositions[6]);
			dc.writeShort(pay[6]);
			dc.writeInt(7);
			dc.write(0x05);
			dc.write(0x1D);
			dc.write(0x17);
			dc.write(0x10);
			dc.write(0x04);
			dc.write(0x0E);
			dc.writeBytes(tablename1);
			dc.writeBytes("column_name");
			dc.writeBytes("TEXT");
			dc.write(3);
			dc.writeBytes("NO");
			
			//insert 8th record
			dc.seek(recordPositions[7]);
			dc.writeShort(pay[7]);
			dc.writeInt(8);
			
			dc.write(0x05);
			dc.write(0x1D);
			dc.write(0x15);
			dc.write(0x10);
			dc.write(0x04);
			dc.write(0x0E);
			dc.writeBytes(tablename1);
			dc.writeBytes("data_type");
			dc.writeBytes("TEXT");
			dc.write(4);
			dc.writeBytes("NO");
			
			//insert 9th record
			dc.seek(recordPositions[8]);
			dc.writeShort(pay[8]);
			dc.writeInt(9);
			dc.write(0x05);
			dc.write(0x1D);
			dc.write(0x1C);
			dc.write(0x13);
			dc.write(0x04);
			dc.write(0x0E);
			dc.writeBytes(tablename1);
			dc.writeBytes("ordinal_position");
			dc.writeBytes("TINYINT");
			dc.write(5);
			dc.writeBytes("NO");
			
			//insert 10th record
			dc.seek(recordPositions[9]);
			dc.writeShort(pay[9]);
			dc.writeInt(10);
			dc.write(0x05);
			dc.write(0x1D);
			dc.write(0x17);
			dc.write(0x10);
			dc.write(0x04);
			dc.write(0x0E);
			dc.writeBytes(tablename1);
			dc.writeBytes("is_nullable");
			dc.writeBytes("TEXT");
			dc.write(6);
			dc.writeBytes("NO");
			
			
			dc.close();
			
		} catch (Exception e) {
			System.out.println("create davisbase_columns.tbl error!");
			e.printStackTrace();
		}
	}
	

	public static void SelectQuery(String[] columns, String tableName, String[] comp) {
		/* {*}or{columns_name_array}, "table_name", {column_name, ?, what} */
		int[] ordinal_position = new int[columns.length];
		LinkedHashMap<Integer, String[]> data = new LinkedHashMap<Integer,String[]>();
		if(columns[0].equals("*")){
			ArrayList<String> columnname = getColumns(tableName);
			data = getData(tableName,columnname,comp);
			Displayer dsp = new Displayer();
			dsp.addcolumnnames((String[]) columnname.toArray(new String[columnname.size()]));
			for (int k : data.keySet()) {
				String[] s1 = data.get(k);
				String[] s2 = new String[s1.length + 1];
				s2[0] = Integer.toString(k);
				for (int j=1; j<s2.length; j++) {
					s2[j] = s1[j-1];
				}
				dsp.addcontent(k, s2);
			}
			dsp.prt();
		}
		else
		{
			ordinal_position=getOrdinal_position(columns,tableName);
			ArrayList<String> columnname = getColumns(tableName);
			data = getData(tableName,columnname,comp);
			
			Displayer dsp = new Displayer();
			dsp.addcolumnnames(columns);
			for (int k : data.keySet()) {
				String[] v1 = data.get(k);
				String[] v2 = new String[columns.length];
				for (int j=0; j<ordinal_position.length; j++) {
					if (ordinal_position[j] == 1)
						v2[j] = Integer.toString(k);
					else
						//oridinal_position start from 2 for non-key, string start from 0
						v2[j] = v1[ordinal_position[j]-2];
				}
				dsp.addcontent(k, v2);
			}
			dsp.prt();
		}
	}

	private static LinkedHashMap<Integer, String[]> getData(String tablename2, ArrayList<String> columnname, String[] comp) {
		/* rowid->payload[], [rowid, payload[]] is a record */
		LinkedHashMap<Integer, String[]> data = new LinkedHashMap<Integer,String[]>();
		String pathname;
		if(tablename2.equals("davisbase_tables") || tablename2.equals("davisbase_columns"))
		{
			pathname="data/catalog/";
		}
		else
		{
			pathname="data/user_data/";
		}
		
		try {
			RandomAccessFile file = new RandomAccessFile(pathname+tablename2+".tbl", "rw");
			int length = (int) file.length();
			int numberOfPages = (int) (length/pageSize);
			for(int i =0;i<numberOfPages;i++){
				file.seek(i*pageSize);
				byte filetype = file.readByte();
				if(filetype==0x05)
					continue;
				if(filetype==0x02)
					continue;
				if(filetype==0x0A)
					continue;
				else{
				
					file.seek(i*pageSize+1);
					int n = file.readByte();
					
					short[] offsetlocations = new short[n];
					file.seek(i*pageSize+8);
					
					for (int j =0;j<n;j++) {
						offsetlocations[j]=file.readShort();
					}
					
					for(int j=0;j<n;j++){
						file.seek(offsetlocations[j]+2);
						int rowid=file.readInt();
						int no_of_columns = file.readByte();//except the key
						byte[] serialCodes = new byte[no_of_columns];
						
						for(int k=0;k<no_of_columns;k++)
							serialCodes[k]=file.readByte();
						
						String[] payload = new String[no_of_columns];//store all the column_value except key into a string array
						for(int k=0;k<no_of_columns;k++){
							switch (serialCodes[k]) {
							
							case 0x00:  payload[k] = Integer.toString(file.readByte());
								payload[k] = "null";
								break;
								
							case 0x01:  payload[k] = Integer.toString(file.readShort());
								payload[k] = "null";
								break;
								
							case 0x02:  payload[k] = Integer.toString(file.readInt());
								payload[k] = "null";
								break;
								
							case 0x03:  payload[k] = Long.toString(file.readLong());
								payload[k] = "null";
								break;
								
							case 0x04:  payload[k] = Integer.toString(file.readByte());
								break;
								
							case 0x05:  payload[k] = Integer.toString(file.readShort());
								break;

							case 0x06:  payload[k] = Integer.toString(file.readInt());
								break;
								
							case 0x07:  payload[k] = Long.toString(file.readLong());
								break;

							case 0x08:  payload[k] = String.valueOf(file.readFloat());
								break;

							case 0x09:  payload[k] = String.valueOf(file.readDouble());
								break;
							
							case 0x0A: 	long datetime = file.readLong();
								ZoneId zoneId = ZoneId.of ( "America/Chicago" );
								Instant x = Instant.ofEpochSecond ( datetime ); 
								ZonedDateTime zdt2 = ZonedDateTime.ofInstant ( x, zoneId ); 
								zdt2.toLocalTime();
								payload[k]=zdt2.toLocalDateTime().toString().replace("T", "_");
								break;

							case 0x0B:  long date = file.readLong();
	   							ZoneId zoneId1 = ZoneId.of ( "America/Chicago" );
	   							Instant x1 = Instant.ofEpochSecond ( date ); 
	   							ZonedDateTime zdt3 = ZonedDateTime.ofInstant ( x1, zoneId1 ); 
	   							payload[k]=zdt3.toLocalDate().toString();//YYYY-MM-DD
	   							break;

							default:   
								int len = new Integer(serialCodes[k]-0x0C);
								byte[] bytes = new byte[len];
								
								for(int m = 0; m < len; m++)
									bytes[m] = file.readByte();
									
								payload[k] = new String(bytes);
								break;
							}
						}
						//rowid+payload is the record, while rowid is pre-defined INT
						if(check(payload,rowid,columnname,comp,serialCodes))//serialCodes<->payload(eg. "davisbase_tables")	
							data.put(rowid, payload);
					}
				}
			}
			file.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}

	private static boolean check(String[] payload, int rowid, ArrayList<String> columnname, String[] comp, byte[] serialCodes) {
		//[rowid, payload] <-> columnname
		if(comp.length==0)//means choose all
		{
			return true;
		}
		else
		{
			int pos=0;
			for (String column:columnname) {
				if(column.equals(comp[0]))
					break;
				pos++;
			}
			byte serialcode;
			
			if(pos==0){
				serialcode = 0x06;
			}
			else{
				serialcode = serialCodes[pos-1];//[rowid, payload] <-> columnname
			}
			
			if(serialcode>=0x04 && serialcode<=0x07){
				long value  = Long.parseLong(comp[2]);
				long cmp=0;
				if(pos==0){
					cmp = rowid;
				}
				else{
					cmp = Long.parseLong(payload[pos-1]);
				}
				
				switch (comp[1]) {
				case "=": if(value==cmp)
					return true;
					break;
			
				case "<": if(cmp<value)
					return true;
					break;
			  
				case ">": if(cmp>value)
					return true;
					break;
			  
				case "<=": if(cmp<=value)
					return true;
					break;
					
				case ">=":if(cmp>=value)
					return true;
					break;
					
				case "<>": if(value!=cmp)
					return true;
					break;
			  
				default:
					System.out.println("Undefined operator");
					return false;
				}
			}
			else if(serialcode==0x08 || serialcode==0x09)
			{
				double value = Double.parseDouble(comp[2]);
				double cmp = Double.parseDouble(payload[pos-1]);
				
				switch(comp[1]){
				case "=": if(value==cmp)
					return true;
					break;
					
				case "<": if(cmp<value)
					return true;
					break;
					
				case ">": if(cmp>value)
					return true;
					break;
					
				case "<=": if(cmp<=value)
					return true;
					break;
					
				case ">=": if(cmp>=value)
					return true;
					break;
					
				case "<>": if(value!=cmp)
					return true;
					break;
					
				default:
					System.out.println("Undefined operator");
					return false;
				}
			}
			else{
				String value = comp[2].replaceAll("\"", "");
				value=value.replaceAll("'", "");//used to compare with string
				String cmp = payload[pos-1];
				
				switch(comp[1]){
				
				case "=":
					if (value.equalsIgnoreCase(cmp))
						return true;
					break;
				case "<>":
					if (!value.equalsIgnoreCase(cmp))
						return true;
					break;
					
				default:
					System.out.println("Undefined operator");
					return false;
					
				}
			}
			return false;
		}
	}
	
	private static int[] getOrdinal_position(String[] columns, String tableName2) {
		
		int[] ordinal_positions = new int[columns.length];
		
		try {
			RandomAccessFile dc = new RandomAccessFile("data/catalog/davisbase_columns.tbl", "rw");
			
			long pagelen=dc.length();
			int numberOfPages = (int) (pagelen/pageSize);
			
			for(int i =0;i<numberOfPages;i++){
				dc.seek(i*pageSize);
				byte filetype = dc.readByte();
				if(filetype==0x05)
					continue;
				if(filetype==0x02)
					continue;
				if(filetype==0x0A)
					continue;
				else
				{
					dc.seek(i*pageSize+1);
					int n = dc.readByte();
					
					short[] offsetlocations = new short[n];
					dc.seek(i*pageSize+8);
					
					for (int j =0;j<n;j++){
						offsetlocations[j]=dc.readShort();
						
					}
					
					for(int j=0;j<n;j++){
						
						dc.seek(offsetlocations[j]+2);
						
						dc.seek(offsetlocations[j]+6);
						int no_of_Columns = dc.readByte();
						int record_position = offsetlocations[j]+6;
						dc.seek(record_position+1);
						int table_length=dc.readByte();
						table_length=table_length-12;
						dc.seek(record_position+2);
						int column_name_length = dc.readByte();
						column_name_length = column_name_length -12;
						
						dc.seek(record_position+no_of_Columns+1);
						byte[] tablename= new byte[table_length];
						
						for(int k=0;k<table_length;k++){
							tablename[k]=dc.readByte();
						}
						
						String table = new String(tablename);
						if(table.equals(tableName2))
						{
							byte[] columnname =new byte[column_name_length];
							for(int k=0;k<column_name_length;k++){
								columnname[k]=dc.readByte();
							}
							
							String col = new String(columnname);
							
							for(int x=0;x<columns.length;x++)
							{	
								if(col.equals(columns[x]))
								{
									dc.seek(record_position+3);
									int datatype_length =dc.readByte();
									datatype_length=datatype_length-12;
									dc.seek(record_position+1+no_of_Columns+table_length+column_name_length+datatype_length);
									int ord = dc.readByte();
										
									ordinal_positions[x]=ord;
								}
							}
						}
					}
				}
			}
			dc.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ordinal_positions;
	}

	private static ArrayList<String> getColumns(String tablename2) {
		/* eg. tablename2=="davisbase_columns, return [rowid, table_name, column_name, datatype, ordinal_position, is_nullable]" */
		
		ArrayList<String> columns =new ArrayList<String>();
		
		try {
			RandomAccessFile dc = new RandomAccessFile("data/catalog/davisbase_columns.tbl", "rw");
			
			long pagelen=dc.length();
			int numberOfPages = (int) (pagelen/pageSize);
			for(int i =0;i<numberOfPages;i++){
				dc.seek(i*pageSize);
				byte filetype = dc.readByte();
				if(filetype==0x05)
					continue;
				if(filetype==0x02)
					continue;
				if(filetype==0x0A)
					continue;
				else{
					dc.seek(i*pageSize+1);
					int n = dc.readByte();
					
					short[] offsetlocations = new short[n];
					dc.seek(i*pageSize+8);
					
					for (int j =0;j<n;j++) {
						offsetlocations[j]=dc.readShort();
						
					}
				
					for(int j=0;j<n;j++){
						dc.seek(offsetlocations[j]+6);
						int no_of_Columns = dc.readByte();
						int record_position = offsetlocations[j]+6;
						dc.seek(record_position+1);
						int table_length=dc.readByte();
						
						table_length=table_length-12;
						dc.seek(record_position+2);
						int column_name_length = dc.readByte();
						column_name_length = column_name_length -12;
						
						dc.seek(record_position+no_of_Columns+1);
						byte[] tablename= new byte[table_length];
						for(int k=0;k<table_length;k++){
							tablename[k]=dc.readByte();
						}
						String tbl = new String(tablename);
						
						if(tbl.equals(tablename2))
						{
							byte[] columnname =new byte[column_name_length];
							for(int k=0;k<column_name_length;k++){
								columnname[k]=dc.readByte();
							}
							columns.add(new String(columnname));
						}
					}
				}
			}
			dc.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return columns;
	}

	public static void insert(String tableName2, String[] values_to_insert) {
		
		try {
			String path="data/user_data/";
			if(tableName2.equalsIgnoreCase("davisbase_tables.tbl") || tableName2.equalsIgnoreCase("davisbase_columns.tbl"))
				path="data/catalog/";
			
			@SuppressWarnings("resource")
			RandomAccessFile insertFile = new RandomAccessFile(path+tableName2, "rw");
			
			String[] datatypes = getDataType(tableName2.substring(0,(tableName2.length()-4)));
			String[] Nullables = getNullables(tableName2.substring(0,(tableName2.length()-4)));
			
			for(int i=0;i<values_to_insert.length;i++){
				values_to_insert[i]=values_to_insert[i].replaceAll("\"", "");//delete all the " and '
				values_to_insert[i]=values_to_insert[i].replaceAll("\'", "");
			}
			
			for(int i=0;i<values_to_insert.length;i++){
				if(values_to_insert[i].equalsIgnoreCase("null")&& Nullables[i].equals("NO"))
				{
					System.out.println("Not null value cannot be null");
					return;
				}
			}
			
			int key = Integer.parseInt(values_to_insert[0]);	
			int pageno = getPageNumber(insertFile,key);
			int exists = getKey(insertFile,pageno,key);
			
			if(exists==1){
				System.out.println("Uniqueness constraint voilated");
				return;
			}
			else{
				short payloadsize = getPayloadSize(values_to_insert,datatypes,tableName2.substring(0,tableName2.length()-4));
				
				if(checkrecordSize(insertFile,pageno,payloadsize)){
					insertinto(insertFile,pageno,values_to_insert,datatypes,payloadsize);
				}
				else{
					int newpage=split(insertFile,pageno);
					insertinto(insertFile,newpage-1,values_to_insert,datatypes,payloadsize);
				}
				
			}
			insertFile.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static int getKey(RandomAccessFile insertFile, int pageno, int key) {
		//return 1, then this pageno contains key, 0, then this pageno does not contain key
		try {
			insertFile.seek(pageno*pageSize+1);
			int records = insertFile.read();
			short[] offsetloacations = new short[records];
			
			for(int i=0;i<records;i++){
				insertFile.seek(pageno*pageSize+8+i*2);
				offsetloacations[i]=insertFile.readShort();
				insertFile.seek(offsetloacations[i]+2);
				if(insertFile.readInt()==key)
					return 1;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	private static int split(RandomAccessFile insertFile, int pageNumber) {
		/* split the page numbered by pageNumber */
		int numpages=0;
		try {
			numpages = (int) (insertFile.length()/pageSize);
			numpages= numpages+1;
			insertFile.setLength(pageSize * numpages);
			insertFile.seek((numpages-1)*pageSize);
			insertFile.writeByte(0x0D);
			insertFile.seek((numpages-1)*pageSize+2);
			insertFile.write((numpages)*pageSize);
			
			int midkey = findMiddleRecord(insertFile,pageNumber);
			writenewleafPage(insertFile,pageNumber,numpages,midkey);//split page(pageNumber, 012...) to numpages=pageNum+1, and midkey in pageNum
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return numpages;//total number of pages, last will be index by -1
	}

	private static void writenewleafPage(RandomAccessFile insertFile, int pageNumber, int numpages, int midkey) {
		/**
		 * @param insertFile, table_file to be modified
		 * @param pageNumber, pageNumber being split
		 * @param numpages, new page
		 * @param midkey, mid_key in page(pageNumber)
		 */
		try {
			insertFile.seek((pageNumber*pageSize)+1);
			int numcells = insertFile.read();
			//int numcells1 = midkey-1;
			int numcells1 = (int) Math.ceil((double)numcells / 2);
			int numcells2 = numcells-numcells1;
			
			int size=((numpages)*pageSize);
			
			for(int i=numcells1+1;i<=numcells;i++){//***********
				
				insertFile.seek(pageNumber*pageSize+8+2*i-2);
				short offsetlocation = insertFile.readShort();
				insertFile.seek(offsetlocation);
				int recordsize = insertFile.readShort()+6;
				size = size - recordsize;
				
				byte[] record = new byte[recordsize];
				insertFile.seek(offsetlocation);
				insertFile.read(record);
				
				insertFile.seek(size);
				insertFile.write(record);
				
				//write offset location
				
				insertFile.seek((numpages-1)*pageSize+8+((i-numcells1-1)*2));
				insertFile.writeShort(size);
				
			}
			//new cell content start area at the new page
			
			insertFile.seek((numpages-1)*pageSize+2);
			insertFile.writeShort(size);
			
			//new cell content start area at the current page
			
			insertFile.seek(pageNumber*pageSize+8+(numcells1-1)*2);
			short offset = insertFile.readShort();
			
			insertFile.seek((pageNumber)*pageSize+2);
			insertFile.writeShort(offset);
			
			
			//updating number of records
			insertFile.seek((pageNumber)*pageSize+1);
			insertFile.write(numcells1);
			
			insertFile.seek((numpages-1)*pageSize+1);
			insertFile.write(numcells2);
			
			//setting right pointers.
			
			insertFile.seek(pageNumber*pageSize+4);
			int rightpage = insertFile.readInt();
			
			if(rightpage==-1){
				
				insertFile.seek((numpages-1)*pageSize+4);
				insertFile.writeInt(-1);
				
			}
			else{
				insertFile.seek((numpages-1)*pageSize+4);
				insertFile.writeInt(rightpage);

			}
			insertFile.seek((pageNumber)*pageSize+4);
			insertFile.writeInt(numpages);
				
			
			//parent settings
			
			int parent = getParent(insertFile,pageNumber+1);//get the parent of current
			if(parent==0){
				int parentpage = createInteriorPage(insertFile);
				setParent(insertFile,parentpage,pageNumber,midkey);
				insertFile.seek((parentpage-1)*pageSize+4);
				insertFile.writeInt(numpages); // right child
			}
			else
			{
				if(checkforRightPointer(insertFile,parent,pageNumber+1))
				{
					setParent(insertFile,parent,pageNumber,midkey);
					insertFile.seek((parent-1)*pageSize+4);
					insertFile.writeInt(numpages); // right child
				}
				else{
					setParent(insertFile,parent,numpages,midkey);
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static boolean checkforRightPointer(RandomAccessFile insertFile, int parent, int i) {
		/* check whether page i is the right most of page parent */
		try {
			insertFile.seek((parent-1)*pageSize+4);
			if(insertFile.readInt()==i)
				return true;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private static void setParent(RandomAccessFile insertFile, int parent, int childPage, int midkey) {
		try {
			insertFile.seek((parent-1)*pageSize+1);
			int numrecords = insertFile.read();
			if(checkInteriorRecordFit(insertFile,parent))
			{
				int content=(parent)*pageSize;
				TreeMap<Integer,Short> offsets = new TreeMap<Integer,Short>();
				if(numrecords==0){
					insertFile.seek((parent-1)*pageSize+1);
					insertFile.write(1);
					content = content-8;
					insertFile.writeShort(content);  //cell content star
					insertFile.writeInt(-1);		//right page pointer
					insertFile.writeShort(content);	//offset arrays
					insertFile.seek(content);
					insertFile.writeInt(childPage+1);
					insertFile.writeInt(midkey);

				}
				else{
					insertFile.seek((parent-1)*pageSize+2);
					short cellContentArea = insertFile.readShort();
					cellContentArea = (short) (cellContentArea-8);
					insertFile.seek(cellContentArea);
					insertFile.writeInt(childPage+1);
					insertFile.writeInt(midkey);
					insertFile.seek((parent-1)*pageSize+2);
					insertFile.writeShort(cellContentArea);
					for(int i=0;i<numrecords;i++){
						insertFile.seek((parent-1)*pageSize+8+2*i);
						short off = insertFile.readShort();
						insertFile.seek(off+4);
						int key = insertFile.readInt();
						offsets.put(key, off);
					}
					offsets.put(midkey,cellContentArea);
					insertFile.seek((parent-1)*pageSize+1);
					insertFile.write(++numrecords);
					insertFile.seek((parent-1)*pageSize+8);
					for(Entry<Integer, Short> entry : offsets.entrySet()) {
						 insertFile.writeShort(entry.getValue());
					}
				}
			}
			else{
				splitInteriorPage(insertFile,parent);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void splitInteriorPage(RandomAccessFile insertFile, int parent) {
		
		int newPage = createInteriorPage(insertFile);//newPage-1 for page starting index
		int midKey = findMiddleRecord(insertFile, parent-1);//find the middle key
		writeContentInteriorPage(insertFile,parent,newPage,midKey);//parent and newPage will have one parent
		
		//setting right pointers
		try {
			insertFile.seek((parent-1)*pageSize+4);
			int rightpage = insertFile.readInt();
			insertFile.seek((newPage-1)*pageSize+4);
			insertFile.writeInt(rightpage);
			insertFile.seek((parent-1)*pageSize+4);
			insertFile.writeInt(newPage);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void writeContentInteriorPage(RandomAccessFile insertFile, int parent, int newPage, int midKey) {
		
		try {
			insertFile.seek((parent-1)*pageSize+1);
			int numrecords = insertFile.read();
			//int mid = (int) Math.ceil((double)numrecords/2);
			//int numrecords1 = mid-1;
			int numrecords1 = (int) Math.ceil((double)numrecords/2);
			int numrecords2 = numrecords-numrecords1;
			int size = pageSize;
			for(int i=numrecords1+1;i<=numrecords;i++)
			{
				insertFile.seek((parent-1)*pageSize+8+2*i-2);
				short offset = insertFile.readShort();
				insertFile.seek(offset);
				byte[] data = new byte[8];
				insertFile.read(data);
				size = size-8;
				insertFile.seek((newPage-1)*pageSize+size);
				insertFile.write(data);
				
				//setting offset
				insertFile.seek((newPage-1)*pageSize+8+(i-numrecords1-1)*2);
				insertFile.writeShort(size);
				
			}
			
			//setting number of records
			insertFile.seek((parent-1)*pageSize+1);
			insertFile.write(numrecords1);
			
			insertFile.seek((newPage-1)*pageSize+1);
			insertFile.write(numrecords2);
			
			int int_parent = getParent(insertFile, parent);
			if(int_parent==0){
				int newParent = createInteriorPage(insertFile);
				setParent(insertFile, newParent, parent, midKey);
				insertFile.seek((newParent-1)*pageSize+4);
				insertFile.writeInt(newPage);
			}
			else{
				if(checkforRightPointer(insertFile,int_parent,parent)){
					setParent(insertFile, int_parent, parent, midKey);
					insertFile.seek((int_parent-1)*pageSize+4);
					insertFile.writeInt(newPage);
				}
				else
					setParent(insertFile, int_parent, newPage, midKey);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static boolean checkInteriorRecordFit(RandomAccessFile insertFile, int parent) {
		
		try {
			insertFile.seek((parent-1)*pageSize+1);
			int numrecords = insertFile.read();
			short cellcontent = (short) ((parent)*pageSize-insertFile.readShort());
			int size = 8+numrecords*2+2+cellcontent;
			size = pageSize-size;
			if(size>=8)
				return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	private static int createInteriorPage(RandomAccessFile insertFile) {
		
		int numpages =0;
		try {
			
			numpages= (int) (insertFile.length()/pageSize);
			numpages++;
			insertFile.setLength(numpages*pageSize);
			insertFile.seek((numpages-1)*pageSize);
			insertFile.write(0x05);
			insertFile.seek((numpages-1)*pageSize+2);
			insertFile.writeShort((numpages)*pageSize);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return numpages;
	}
	
	private static int getParent(RandomAccessFile insertFile, int page) {
		/*page start at 1, @1-right_most(child), @2-left_child*/
		try {
			int numpages = (int) (insertFile.length()/pageSize);
			for(int i=0;i<numpages;i++){
				insertFile.seek(i*pageSize);
				if(insertFile.readByte()==0x05){
					insertFile.seek(i*pageSize+4);	
					if(page==insertFile.readInt())
						return i+1;
					insertFile.seek(i*pageSize+1);
					int numrecords = insertFile.read();
					
					if (numrecords>0) 
					{
					for(int j=0;j<numrecords;j++) 
					{
						
						insertFile.seek(i*pageSize+8+2*j);
						short off = insertFile.readShort();
						insertFile.seek(off);
						if(page==insertFile.readInt())
							return i+1;
						
					}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

	private static int findMiddleRecord(RandomAccessFile insertFile, int pageNumber) {
		
		int mid=0;
		
		try {
			insertFile.seek(pageNumber*pageSize);
			byte type = insertFile.readByte();
			insertFile.seek(pageNumber*pageSize+1);
			int noofrecords=insertFile.read();
			
			int mid_record = (int) Math.ceil((double)noofrecords/2);
			insertFile.seek(pageNumber*pageSize+8+(mid_record-1)*2);
			short midoffset = insertFile.readShort();
			if(type==0x0D)
				insertFile.seek(midoffset+2);
			else 
				insertFile.seek(midoffset+4);
				
			mid= insertFile.readInt();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mid;
	}

	private static void insertinto(RandomAccessFile insertFile, int pageNumber, String[] values_to_insert, String[] datatypes, short payloadsize) {
		try {
			insertFile.seek(pageNumber*pageSize+1);
			int no=insertFile.read();
			
			short offset = insertFile.readShort();
			
			short newoffset = (short) (offset -(payloadsize+6));
			short[] offsets = new short[no];
			
			TreeMap<Integer, Short> offsetlocations = new TreeMap<Integer, Short>();
			for(int i=0;i<no;i++){
				insertFile.seek(pageNumber*pageSize+8+i*2);
				offsets[i]=insertFile.readShort();
				insertFile.seek(offsets[i]+2);
				int key = insertFile.readInt();
				offsetlocations.put(key, offsets[i]);
				
			}
			insertFile.seek(newoffset);
			
			byte[] sc= new byte[datatypes.length];
			for(int i=0;i<datatypes.length;i++){
				sc[i]=getSerialCode(datatypes[i]);
			
			}
			insertFile.writeShort(payloadsize);
			insertFile.writeInt(Integer.parseInt(values_to_insert[0]));
			insertFile.write(values_to_insert.length-1);
			
			for (int i=1;i<values_to_insert.length;i++)
			{
				if(values_to_insert[i].equals("null")|| values_to_insert[i].equals("")) {
					sc[i] = checkforNull(datatypes[i]);
				}
				if(sc[i]==0X0C) {
					sc[i]=(byte)(12+values_to_insert[i].length());
				}
			}
			
			for(int i=1;i<sc.length;i++)
				insertFile.write(sc[i]);
			
			writecontent(insertFile,pageNumber,newoffset,sc,values_to_insert);
			insertFile.seek(pageNumber*pageSize+1);
			insertFile.write(no+1);
			insertFile.writeShort(newoffset);
			
			offsetlocations.put(Integer.parseInt(values_to_insert[0]), newoffset);
			insertFile.seek(pageNumber*pageSize+8);
			for(Entry<Integer, Short> entry : offsetlocations.entrySet()) {
				insertFile.writeShort(entry.getValue());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	private static void writecontent(RandomAccessFile insertFile, int pageNumber, short newoffset, byte[] sc, String[] values_to_insert) 
	{
		try {
			insertFile.seek(newoffset+7+sc.length-1);
			
			for(int i=1;i<values_to_insert.length;i++){
				
				switch (sc[i]) {
				
				case 0x00:  insertFile.write(0);
							break;
							
				case 0x01:	insertFile.writeShort(0);
							break;
							
				case 0x02:	insertFile.writeInt(0);
							break;
							
				case 0x03:	insertFile.writeDouble(0);
							break;
							
				case 0x04:	insertFile.write(Integer.parseInt(values_to_insert[i]));
							break;
							
				case 0x05:	insertFile.writeShort(Short.parseShort(values_to_insert[i]));
							break;
							
				case 0x06:	insertFile.writeInt(Integer.parseInt(values_to_insert[i]));
							break;
							
				case 0x07:	insertFile.writeLong(Long.parseLong(values_to_insert[i]));
							break;
				
				case 0x08: 	insertFile.writeFloat(Float.parseFloat(values_to_insert[i]));
							break;
							
				case 0x09:	insertFile.writeDouble(Double.parseDouble(values_to_insert[i]));
							break;
							
				case 0x0A:	String[] dateTime = values_to_insert[i].split("_");
							String[] date = dateTime[0].split("-");
							String[] time = dateTime[1].split(":");
							ZoneId zoneId = ZoneId.of ( "America/Chicago" );
							ZonedDateTime zdt = ZonedDateTime.of(Integer.parseInt(date[0]),Integer.parseInt(date[1]),Integer.parseInt(date[2]),Integer.parseInt(time[0]), Integer.parseInt(time[1]), Integer.parseInt(time[2]),1234,zoneId);
							 
							long epochSeconds = zdt.toInstant().toEpochMilli() / 1000;
							insertFile.writeLong ( epochSeconds );
							break;
							
				case 0x0B:	date = values_to_insert[i].split("-");
							
							zoneId = ZoneId.of ( "America/Chicago" );
							zdt = ZonedDateTime.of(Integer.parseInt(date[0]),Integer.parseInt(date[1]),Integer.parseInt(date[2]),0, 0, 0,1234,zoneId);
							 
							epochSeconds = zdt.toInstant().toEpochMilli() / 1000;
							insertFile.writeLong ( epochSeconds );
							break;
				
				default:    insertFile.writeBytes(values_to_insert[i]);
							break;
				}
				
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static byte checkforNull(String string) {
		
		switch(string.toLowerCase()){
		case "tinyint":
			return 0x00;
		case "smallint":
			return 0x01;
		case "int":
			return 0x02;
		case "real":
			return 0x02;
		case "double":
			return 0x03;
		case "date":
			return 0x03;
		case "datetime":
			return 0x03;
		case "text":
			return 0x0C;
		}
		return 0x00;
	}

	private static byte getSerialCode(String string) {
		
		switch(string.toLowerCase()){
		
		case "int" : return 0x06;
		case "tinyint": return 0x04;
		case "smallint": return 0x05;
		case "bigint" : return 0x07;
		case "real" : return 0x08;
		case "double" : return 0x09;
		case "date" : return 0x0B;
		case "datetime": return 0x0A;
		case "text" : return 0x0C;
		
		default :return 0x0C;		
		
		}
		
	}
	
	/*
	private static int getSerialCodeBytes(byte b) {

		switch(b){
		case 0x00: return 0;
		case 0x01: return 2;
		case 0x02: return 4;
		case 0x03: return 8;
		case 0x04: return 1;
		case 0x05 : return 2;
		case 0x06 : return 4;
		case 0x07 : return 8;
		case 0x08 : return 4;
		case 0x09 : return 8;
		case 0x0A : return 8;
		case 0x0B : return 8;
		default : return b-12;
		}
	}
	*/
	
	private static boolean checkrecordSize(RandomAccessFile insertFile, int pageNumber, short payloadsize) {
		
		try {
			insertFile.seek((pageNumber)*pageSize+1);
			int no=insertFile.read();
			int pageHeaer_size=8+2*no+2;
			
			insertFile.seek((pageNumber)*pageSize+2);
			short startArea =(short)((pageNumber+1)*pageSize- insertFile.readShort());
			
			int spaceUsed=startArea+pageHeaer_size;
			int spaceAvailable = pageSize-spaceUsed;
			int record_size = payloadsize+6;
			if(spaceAvailable>=record_size)
				return true;
				
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	private static short getPayloadSize(String[] values_to_insert, String[] datatypes,String tablename2) {

		short paysize;
		
		paysize=(short)(1+(getColumns(tablename2).size()-1));//exempt key
		
		int size=0;
		for(int i=1;i<datatypes.length;i++){//count from 1..., exempt key
			
			int tmp = calculateBytes(datatypes[i]);
			
			if(tmp==12)
				size+=values_to_insert[i].length();
			else
				size+=tmp;
			
		}
		
		paysize+=(short)size;
		return paysize;
	}

	private static int calculateBytes(String string) {
		
		switch(string.toLowerCase()){
		
		case "int": return 4;	
		case "tinyint": return 1;
		case "smallint": return 2;
		case "bigint": return 8;
		case "real": return 4;
		case "double": return 8;
		case "datetime": return 8;
		case "date": return 8;
		case "text": return 12;
		default : return 0;
		
		}
	}
	
	private static int getPageNumber(RandomAccessFile insertFile, int key) {
		/* get the page_number of the key */
		TreeMap<Integer, Integer> keytopage = new TreeMap<Integer, Integer> (); // store max_key->page_num
		TreeMap<Integer, int[]> pagekeys = new TreeMap<Integer, int[]> ();
		try {
			int pagenum = (int) (insertFile.length()/pageSize);
			for(int i=0;i<pagenum;i++){
				insertFile.seek(i*pageSize);
				byte b = insertFile.readByte();
				if(b==0x0D){
					//if leaf, get keys from leaf
					int[] keys = getKeys(insertFile,i);
					if (keys.length>0) {
						keytopage.put(keys[keys.length-1], i);
						pagekeys.put(i, keys);
					}
				}
			}
		
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (!pagekeys.isEmpty()) {
			for (int k : pagekeys.keySet()) {
				for (int kk : pagekeys.get(k)) {
					if (kk == key) {
						return k;
					}
				}
			}
		}
		if (!keytopage.isEmpty()) {
			for (int k : keytopage.keySet()) {
				if (k>key) {
					return keytopage.get(k);
				}
			}
			return keytopage.get( keytopage.lastKey() );
		}
		return 0;
	}
	
	
	
	private static int[] getKeys(RandomAccessFile insertFile, int i) {
		/* get keys[] from page_number i, 0, 1, ..., only for leaf page */
		int[] keys = null;
		try {
			insertFile.seek(i*pageSize+1);
			int records = insertFile.read();
			short[] offsetLocations = new short[records];
			insertFile.seek(i*pageSize+8);
			keys = new int[records];
			for(int j=0;j<records;j++){
				insertFile.seek(i*pageSize+8+2*j);
				offsetLocations[j]=insertFile.readShort();
				insertFile.seek(offsetLocations[j]+2);
				keys[j]=insertFile.readInt();
			}
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		return keys;
	}
	
	private static String[] getNullables(String tableName2) {
		
		String[] comp = new String[0];
		ArrayList<String> columns = getColumns(tableName2);
		LinkedHashMap<Integer,String[]> data = getData("davisbase_columns", columns, comp);
		
		int k=0;
		String[] nullable = new String[columns.size()];
		for (Map.Entry<Integer, String[]> map : data.entrySet()) {
		String[] values = map.getValue();
		if(values[0].equalsIgnoreCase(tableName2))
			nullable[k++]=values[4];
		}
		return nullable;
	}

	private static String[] getDataType(String tableName2) {
		
		String[] comp = new String[0];
		ArrayList<String> columns = getColumns(tableName2);
		LinkedHashMap<Integer,String[]> data = getData("davisbase_columns", columns, comp);
		
		int k=0;
		String[] dt = new String[columns.size()];
		
		for (Map.Entry<Integer, String[]> map : data.entrySet()) {
			String[] values = map.getValue();
		
			if(values[0].equalsIgnoreCase(tableName2))
				dt[k++]=values[2];
		}
		return dt;
	}
/*
	public static void createTable(RandomAccessFile tableFile, String tableFileName, String[] columnNames) {
		/*
		 * columnNames format, eg:
		 * {row_id INT PRIMARY KEY, first_name TEXT NOT NULL, last_name TEXT NOT NULL, grade_level TINYINT, gpa REAL, birth_date DATE}
		 */
		/*try {
			
			tableFile.setLength(pageSize*1);
			tableFile.seek(0);
			tableFile.write(0x0D);
			tableFile.seek(2);
			tableFile.writeShort(pageSize);
			tableFile.writeInt(-1);
			
			tableFile.close();
			//check for primary key
			String[] temp = columnNames[0].split(" ");
			if(temp.length!=4){
				System.out.println("Please spcify first column as integer primary key!");
				return;
			}
			else if((!temp[1].equalsIgnoreCase("INT")) ){
				System.out.println("Please spcify first column as integer primary key!");
				return;
			}
			else if(!(temp[2].equalsIgnoreCase("PRIMARY")) && !(temp[3].equalsIgnoreCase("KEY"))){
				System.out.println("Please specify first column as primary key");
				return;
			}
			else
			{
				//update davisbase_tables 
				int max_key = get_max_key("davisbase_tables");
				max_key++;
				String[] vals=new String[]{Integer.toString(max_key),tableFileName,Integer.toString(0),Integer.toString(0)};
				insert("davisbase_tables.tbl", vals);//max_key determines the insert pages
				
				//update davisbase_columns;
				int max_col = get_max_key("davisbase_columns");
				for(int i=0;i<columnNames.length;i++){
					String isNullable = null;
				
					String[] cols = columnNames[i].split(" ");
					if(cols.length==4)
					{
						if(cols[2].equalsIgnoreCase("PRIMARY") && cols[3].equalsIgnoreCase("key"))
							isNullable="NO";
						
						if(cols[2].equalsIgnoreCase("NOT") && cols[3].equalsIgnoreCase("NULL"))
							isNullable="NO";
						
					}
					else{
						isNullable="Yes";
					}
					String columnName = cols[0];
					String datatype = cols[1];
					String ordinal_position = String.valueOf(i+1);
					String[] values = {String.valueOf(++max_col),tableFileName,columnName,datatype,ordinal_position,isNullable};
					insert("davisbase_columns.tbl", values);	
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	} 

	private static int get_max_key(String table) {
		//modified 1.1, right most might be deleted to empty states
		int max_key = 0;
		try {
			RandomAccessFile tabel_file = new RandomAccessFile("data/catalog/"+table+".tbl", "rw");
			int totalpage = (int) (tabel_file.length()/pageSize);
			ArrayList<Integer> keylist = new ArrayList<Integer>();
			for (int i=0; i<totalpage; i++) {
				tabel_file.seek(i*pageSize);
				byte type = tabel_file.readByte();
				if (type==0x0D) {
					tabel_file.seek(i*pageSize + 1);
					int num_of_keys_i = tabel_file.read();
					for (int j=0; j<num_of_keys_i; j++) {
						tabel_file.seek(i*pageSize + 8 + 2*j);
						short newoffset = tabel_file.readShort();
						tabel_file.seek(newoffset + 2);
						int newkey = tabel_file.readInt();
						keylist.add(newkey);
					}
				}
			}
			tabel_file.close();
			for (int i=0; i<keylist.size(); i++) {
				if (max_key<keylist.get(i)) {
					max_key = keylist.get(i);
				}
			}
		} catch (Exception e) {
			System.out.println("get_max_key error, "+e);
		}
		return max_key;
	}
	*/
	//----------------------------------------------------------------
	
	public static void drop(String tableName) {
		String[] comp = {"table_name","=",tableName};
		
		//update davisbase_tables
		LinkedHashMap<Integer, String[]> data = new LinkedHashMap<Integer,String[]>();
		ArrayList<String> columnname = getColumns("davisbase_tables");
		data = getData("davisbase_tables",columnname,comp);
		int key=0;
		for (Map.Entry<Integer, String[]> map : data.entrySet()) {
			key=map.getKey();
		}
		
		String[] cond = {"rowid","=",String.valueOf(key)};
		delete("davisbase_tables.tbl", cond);
		
		//update davisbase_columns
		columnname = getColumns("davisbase_columns");
		data = getData("davisbase_columns",columnname,comp);
		for (Map.Entry<Integer, String[]> map : data.entrySet()) {
			int col_key=map.getKey();
			
			String[] con = {"rowid","=",String.valueOf(col_key)};
			delete("davisbase_columns.tbl", con);
		}
		
		//delete file
		File file = new File("data/user_data/", tableName+".tbl"); 
		file.delete();
	}
	
	public static void delete (String tableName2, String[] cond) {
		//delete based on selection condition, tableName2 contains .tbl
		String tbname = tableName2.substring(0, tableName2.length()-4);
		ArrayList<String> columnnames = getColumns(tbname);
		LinkedHashMap<Integer, String[]> data_old = getData(tbname, columnnames, cond);
		for (int k : data_old.keySet()) {
			//delete based on key!
			String[] cond1 = {columnnames.get(0), "=", Integer.toString(k)};
			delete1(tableName2, cond1);
		}
	}
	
	public static void delete1(String tableName2, String[] cond) {
		//delete based on the key(rowid)
		String path="data/user_data/";
		if(tableName2.equalsIgnoreCase("davisbase_tables.tbl") || tableName2.equalsIgnoreCase("davisbase_columns.tbl"))
			path="data/catalog/";
		
		try {
			RandomAccessFile file = new RandomAccessFile(path+tableName2,"rw");
			int key = Integer.parseInt(cond[2]);
			int pageno = getPageNumber(file,key);//find page position of rowid, one record in one page only
			int exists = getKey(file,pageno,key);
			
			if(exists==1){
				try {
					file.seek((pageno)*pageSize+1);
					int records = file.read();
					short[] offsetLocations = new short[records];
					TreeMap<Integer, Short> offsets = new TreeMap<Integer, Short>();
					file.seek((pageno)*pageSize+8);
					
					for(int j=0;j<records;j++){
						file.seek((pageno)*pageSize+8+2*j);
						offsetLocations[j]=file.readShort();
						file.seek(offsetLocations[j]+2);
						int k=file.readInt();
						if(key==k)
							continue;
						else
						{
							offsets.put(k, offsetLocations[j]);//store new offsets still in order
						}
					}
					file.seek((pageno)*pageSize+8);
					int k=0;
					for(Entry<Integer, Short>entry:offsets.entrySet())
					{
						offsetLocations[k++]=entry.getValue();
						file.writeShort(entry.getValue());
					}
					
					records--;
					file.seek((pageno)*pageSize+1);
					file.write(records);
					
					//setting new cell content area
					short min=offsetLocations[0];
					for(int i=1;i<k;i++)
					{
						if(min>offsetLocations[i])
							min=offsetLocations[i];
						
					}
					
					file.seek((pageno)*pageSize+2);
					file.writeShort(min);
	
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else{
				System.out.println("Key doesnot exits! No records to delete");
				return;
			}
			file.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
//=====================================================================================	
	public static void update (String tablename, String[] set, String[] where) {
		// implemented based on delete and insert //
		ArrayList<String> columnname = getColumns(tablename);
		if (set[0].equalsIgnoreCase(columnname.get(0))) {
			System.out.println("Key Should not be modified!");
			return;
		}
		//retrieve all the old data keys need to be modified
		LinkedHashMap<Integer, String[]> data_old = getData(tablename, columnname, where);
		String[] col_name = {set[0]};
		int[] col_pos = getOrdinal_position(col_name, tablename);
		//update the old map
		int colpos = col_pos[0]-2;
		if (data_old.isEmpty()) {
			System.out.println("No records to be updated, please check your update condition!");
			return;
		}
		LinkedHashMap<Integer, String[]> data_new = new LinkedHashMap<Integer, String[]> ();
		for (int key : data_old.keySet()) {
			String[] m1 = data_old.get(key);
			m1[colpos] = set[2];
			data_new.put(key, m1);
		}
		String tablename2 = tablename+".tbl";
		for (int key : data_new.keySet()) {
			String[] ss1 = {columnname.get(0), "=", Integer.toString(key)};
			delete(tablename2, ss1);
			String[] n1 = data_new.get(key);
			String[] n2 = new String[n1.length + 1];
			for (int i=0; i<n2.length; i++) {
				if (i==0) {
					n2[i] = Integer.toString(key);
				} else {
					n2[i] = n1[i-1];
				}
			}
			insert(tablename2, n2);
		}
	}

}
