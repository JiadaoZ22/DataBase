import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import net.proteanit.sql.DbUtils;

public class sqlOperation {
	public static String password = "123"; 
	
	public static Connection dbConnector(){
		Connection conn = null;
		try {
			conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "jd", password);
//			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", password);
			//JOptionPane.showMessageDialog(null, "Connection Succesful!!!");
			return conn;
		} 
		catch(SQLException ex) {
			//System.out.println("Error in connection: " + ex.getMessage());
			JOptionPane.showMessageDialog(null, ex.getMessage());
			return null;
		}
	}
	
	public static boolean PSQLConnectTest(String inputPassword){
		Connection conn = null;
		try {
			conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "jd", inputPassword);
			//JOptionPane.showMessageDialog(null, "Connection Succesful!!!");
			return true;
		}
		catch(SQLException ex) {
			//System.out.println("Error in connection: " + ex.getMessage());
			//JOptionPane.showMessageDialog(null, ex.getMessage());
			JOptionPane.showMessageDialog(null, "The input PSQL Password is wrong!\nThe Error Hint: \n"+ex.getMessage());
			return false;
		}
	}
	
	
	
	public static ResultSet BookQuery(Connection conn, String ISBN, String title, String Author){
		ResultSet rs = null;
		try {
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
			//here we gonan have an int to compare withn the data in the sql
			//long isbn = Long.parseLong(ISBN);
			//stmt.execute("use library;");
//			System.out.println("Nowing doing the bookquery");
//			String BookQuery = "SELECT isbn, title, name " +
//					"FROM BOOK, BOOK_AUTHORS " + 
//					"WHERE BOOK.Isbn LIKE '%"+ISBN+"%' AND book.Title LIKE '%"+title+"%' AND AUthors.Name LIKE '%"+Author+"%';";
			String BookQuery = "SELECT BOOK.Isbn, Title, Name " +
					"FROM BOOK, BOOK_AUTHORS, AUTHORS " + 
					"WHERE BOOK.Isbn LIKE '%"+ISBN+"%' AND Title LIKE '%"+title+"%' AND Name LIKE '%"+Author+"%' AND " +
					"BOOK.Isbn=BOOK_AUTHORS.Isbn AND BOOK_AUTHORS.Author_id=AUTHORS.Author_id;";/**/
			//
			//String BookQuery = "SELECT Isbn, Title FROM BOOK WHERE Isbn LIKE '%"+ISBN+"%';";
			//String BookQuery = "SELECT Isbn, Title FROM BOOK,BOOK_AUTHORS,AUTHORS WHERE book.Isbn LIKE '%"+ISBN+"%';";
			rs = stmt.executeQuery(BookQuery);
//			System.out.println("rs"+rs.getRow());
			return rs;
		} 
		catch(SQLException ex) {
			//System.out.println("Error in connection: " + ex.getMessage());
			JOptionPane.showMessageDialog(null, ex.getMessage());
			return null;
		}
	}

	public static ResultSet SearchBeforeCheckingIn(Connection conn, String BookID, String Card_id)
	{
		ResultSet rs = null;
		try {
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
			//stmt.execute("use library;");
			String CheckingInQuery = "SELECT book_loans.* FROM book_loans, borrower " +
					"WHERE book_loans.isbn LIKE '%"+BookID+"%' AND book_loans.card_id LIKE '%"+Card_id+"%' " +
					"AND BOOK_LOANS.Card_id=BORROWER.Card_id;";/**/
			//String CheckingInQuery = "SELECT * FROM BOOK_LOANS;";	
			rs = stmt.executeQuery(CheckingInQuery);
			return rs;
		} 
		catch(SQLException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage());
			return null;
		}
	}
	
	public static void CheckingInBooks(String Loan_id, String ISBN)
	{
		try {
			Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "jd", password);
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
//			stmt.execute("use library;");
			
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
//	        Date d = new Date();
//	        String Date_in = sdf.format(d);
	        //JOptionPane.showMessageDialog(null, Date_in);
	        
	        //================Update Date_in Operation================
			String UpdateDateIn = " UPDATE book_loans SET Date_in = current_date WHERE  book_loans.loan_id = "+Loan_id+";";
//			String UpdateDateIn = " UPDATE book_loans SET Date_in = '"+Date_in+"' WHERE  Loan_id = "+Loan_id+";";
			stmt.executeUpdate(UpdateDateIn);
			JOptionPane.showMessageDialog(null, "Checking In this book successfully!!!");
			conn.close();
		} 
		catch(SQLException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage());
		}
	}
	
	public static void CheckingOutBooks(String ISBN, String Card_id)
	{
		ResultSet rs = null;
		try {
			Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "jd", password);
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
			//stmt.execute("use library;");
			
			//===================================Check Checkout Book? >3===================================
			String MaxBooksCheck = "SELECT COUNT(*) AS count FROM book_loans WHERE card_id LIKE '"+Card_id+"' AND Date_in IS NULL; ";
			rs = stmt.executeQuery(MaxBooksCheck); rs.next();
			int cnt1 = Integer.parseInt(rs.getString("count"));
			if(cnt1>=3){
				JOptionPane.showMessageDialog(null, "Each student is allowed to borrow 3 books !");
				rs.close(); conn.close();
				return;
			}
			
			//===================================Check Book Availability===================================
			String AvaiableBooksCheck = "SELECT COUNT(*) AS count FROM book_loans WHERE isbn LIKE '"+ISBN+"' AND Date_in IS NULL; ";
			rs = stmt.executeQuery(AvaiableBooksCheck); rs.next();
			int cnt2 = Integer.parseInt(rs.getString("count"));
			//JOptionPane.showMessageDialog(null, "Number of Books have been borrowed: "+cnt2);
			
			if(cnt2 > 0){
				JOptionPane.showMessageDialog(null, "The book is not avilable!");
				rs.close(); conn.close();
				return;
			}
			
			
			/*===================================Insert Operation===================================*/
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
//	        Date d = new Date();  
//	        String Date_out = sdf.format(d);
//	        //JOptionPane.showMessageDialog(null, Date_out);
//	        
//	        Calendar now = Calendar.getInstance();
//	        now.set(Calendar.DATE,now.get(Calendar.DATE)+14);
//	        String Due_date = sdf.format(now.getTime());
	        //JOptionPane.showMessageDialog(null, Due_date);

			String Insert = " INSERT INTO book_loans(Isbn, Card_id) " +
					"VALUES ('"+ISBN+"', '"+Card_id+"');";
			stmt.executeUpdate(Insert);
			JOptionPane.showMessageDialog(null, "Checking Out this book successfully!!!");
			
			rs.close();
			conn.close();
		} 
		catch(SQLException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage());
		}
	}

	// SSN: 051513637
	public static void CreateNewAccount(String[] infoList)
	{
		String[] nameList = {"SSN", "Fname", "Lname", "Address", "Phone"}; 
		for (int i = 1; i < nameList.length; i++)
			if(infoList[i].length() == 0){
				JOptionPane.showMessageDialog(null, "The '"+nameList[i]+"' is NULL!");
				return;
			}
		// ==========Convert SSN format.==========
		if(infoList[0].length() > 0){ 
			String regEx="[^0-9]";   
			Pattern p = Pattern.compile(regEx);
			Matcher m = p.matcher(infoList[0]);
			String ssn = m.replaceAll("").trim(); 
			if(ssn.length() != 9) {
				JOptionPane.showMessageDialog(null, "The number length of SSN is not equal to 9, please check it!");
				return;
			}
			infoList[0] = ssn.substring(0, 3) + "-" + ssn.substring(3, 5) + "-" + ssn.substring(5, 9);		
		}
		
		// ==========Convert phone format.==========
		if(infoList[4].length() > 0 ) // check phone number
		{
			String regEx="[^0-9]";   
			Pattern p = Pattern.compile(regEx);
			Matcher m = p.matcher(infoList[4]);
			String phone = m.replaceAll("").trim(); 
			if(phone.length() != 10) {
				JOptionPane.showMessageDialog(null, "The length of phone number is not equal to 10, please check it!");
				return;
			}
			infoList[4] = "(" + phone.substring(0, 3) + ") " + phone.substring(3, 6) + "-" + phone.substring(6, 10);
		}

		ResultSet rs = null;
		try {
			Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "jd", password);
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
			//stmt.execute("use library;");
			
			/*================Get Card Id!!! ================*/
			String maxCardNumber = "SELECT MAX(Card_id) as maxCardNumber FROM BORROWER;";
			rs = stmt.executeQuery(maxCardNumber); rs.next();
			String tmp = rs.getString("maxCardNumber");
			if(tmp==null)
				tmp = "6666666";
			String CardNumberStr = tmp.substring(tmp.length()-6, tmp.length()); 
			int CardNum = Integer.parseInt(CardNumberStr) + 1; 
			String BuildCardNo = "ID";
			int tmpInt = 100000;
			for (int i = 1; i <= 6; i++) { 
				BuildCardNo += Integer.toString(CardNum / tmpInt);
				CardNum = CardNum % tmpInt;
				tmpInt = tmpInt/10;
			}
			
			/*================Check SSN Number in BORROWER TEBLE!!! ================*/
			String checkSSN = "SELECT COUNT(Ssn) AS ssnCnt FROM BORROWER WHERE Ssn = '"+infoList[0]+"';";
			rs = stmt.executeQuery(checkSSN); rs.next();
			String ssnCnt = rs.getString("ssnCnt");
			if(Integer.parseInt(ssnCnt) > 0) {
				JOptionPane.showMessageDialog(null, "SSN is existed!");
				return;
			}
			
			/*================Insert New User Data================*/
			String InsertNewUserData = " INSERT INTO BORROWER " +
					"VALUES ('"+BuildCardNo+"', '"+infoList[0]+"', '"+infoList[1]+"','"+infoList[2]+"','"+infoList[3]+"', '"+infoList[4]+"' );";
			stmt.executeUpdate(InsertNewUserData);

			JOptionPane.showMessageDialog(null, "Create New Borrower Sucessully! Please note: Your Card_id is " + BuildCardNo);
			rs.close();
			conn.close();
		}
		catch(SQLException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage());
		}
	}
	
	public static int FinesTableUpdatedDisplay(JTable tableFines, String CardNum, boolean FilterOutPaid)
	{
		ResultSet rs = null;
		try {
			Connection conn  = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "jd", password);
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
			//stmt.execute("use library;");
			String ObtainUpdateList = "(SELECT FINES.Loan_id, (current_date - date_out)*0.25 AS new_fine_amt\n" + 
					"FROM fines, book_loans\n" + 
					"WHERE BOOK_LOANS.Loan_id = FINES.Loan_id AND paid IS FALSE AND Date_in IS NULL\n" + 
					"      AND fine_amt <> (current_date - date_out)*0.25)\n" + 
					"UNION\n" + 
					"(SELECT FINES.Loan_id, (current_date - date_out)*0.25 AS new_fine_amt\n" + 
					"FROM fines, book_loans\n" + 
					"WHERE BOOK_LOANS.Loan_id = FINES.Loan_id AND paid IS FALSE AND Date_in IS NOT NULL\n" + 
					"      AND fine_amt <> (current_date - date_out)*0.25);\n";
//					String ObtainUpdateList = "(SELECT FINES.Loan_id, (current_date - date_out)*0.25 AS new_fine_amt " +
//					"FROM (FINES JOIN BOOK_LOANS ON FINES.Loan_id = BOOK_LOANS.Loan_id) " +
//					"WHERE paid IS FALSE AND Date_in IS NULL AND fine_amt <> (current_date - date_out)*0.25) " +
//					"UNION " +
//					"(SELECT FINES.Loan_id, (current_date - date_out)*0.25 AS new_fine_amt " +
//					"FROM (FINES JOIN BOOK_LOANS ON BOOK_LOANS.Loan_id = FINES.Loan_id) " +
//					"WHERE paid IS FALSE AND Date_in IS NOT NULL AND fine_amt <> (current_date - date_out)*0.25);";
			
			rs = stmt.executeQuery(ObtainUpdateList);
			List<ArrayList<String>> StrList = new ArrayList<ArrayList<String>>();
			StrList.add(new ArrayList<String>()); 
			StrList.add(new ArrayList<String>()); 
			while (rs.next()) {
				String LoanID = rs.getString("Loan_id");
				String newFineAmt = rs.getString("new_fine_amt");
				StrList.get(0).add(LoanID);
				StrList.get(1).add(newFineAmt);
			}
//			stmt.execute( "UPDATE FINES SET fine_amt = "+StrList.get(1).get(0)+" WHERE Loan_id = '"+StrList.get(0).get(0)+"';");
			for(int i=0; i<StrList.get(0).size(); i++) {
				System.out.println("i:"+i);
				String Update = "UPDATE FINES SET fine_amt = "+StrList.get(1).get(i)+" WHERE Loan_id = '"+StrList.get(0).get(i)+"'; ";
				stmt.executeUpdate(Update);
			}

		
			String delete = "DELETE FROM fines WHERE fine_amt < 0;"; 
			stmt.executeUpdate(delete);
			String Insert = 
					"Insert into fines(Loan_id, fine_amt) " +
					"(select BOOK_LOANS.Loan_id, (current_date - date_out)*0.25 from BOOK_LOANS " +
					"WHERE Date_in IS NULL AND Due_Date < current_date AND " +
					"NOT EXISTS (SELECT * FROM FINES WHERE BOOK_LOANS.Loan_id = FINES.Loan_id)) " +
					"UNION " +
					"(select BOOK_LOANS.Loan_id, (date_in - date_out)*0.25 from BOOK_LOANS " +
					"WHERE Date_in IS NOT NULL AND Due_Date < date_in AND " +
					"NOT EXISTS (SELECT * FROM FINES WHERE BOOK_LOANS.Loan_id = FINES.Loan_id)); ";
//			"Insert into FINES(Loan_id, fine_amt, paid) " +
//			"(select BOOK_LOANS.Loan_id, DATEDIFF(date(now()), Due_Date)*0.25, 0 from BOOK_LOANS " +
//			"WHERE Date_in IS NULL AND Due_Date < date(now()) AND " +
//			"NOT EXISTS (SELECT * FROM FINES WHERE BOOK_LOANS.Loan_id = FINES.Loan_id)) " +
//			"UNION " +
//			"(select BOOK_LOANS.Loan_id, DATEDIFF(Date_in, Due_Date)*0.25, 0 from BOOK_LOANS " +
//			"WHERE Date_in IS NOT NULL AND Due_Date < Date_in AND " +
//			"NOT EXISTS (SELECT * FROM FINES WHERE BOOK_LOANS.Loan_id = FINES.Loan_id)); ";
			stmt.executeUpdate(Insert);
			
			//JOptionPane.showMessageDialog(null, "Successfully updates/refreshes entries in the FINES table!");
			String FinesDisplay = "";
			//FinesDisplay = "select Card_no, FINES.*,due_date, date_in from FINES JOIN BOOK_LOANS ON BOOK_LOANS.Loan_id = FINES.Loan_id Order by Card_no; ";
			
			if(CardNum.equals("") && FilterOutPaid == false)
				//FinesDisplay = "select Card_no, FINES.* from FINES JOIN BOOK_LOANS ON BOOK_LOANS.Loan_id = FINES.Loan_id; ";
				FinesDisplay = "select Card_id, SUM(fine_amt) AS SUM_Fine_Amount, paid " +
						"from FINES, BOOK_LOANS WHERE BOOK_LOANS.Loan_id = FINES.Loan_id GROUP BY Card_id, paid; ";
			else if(CardNum.length()>0 && FilterOutPaid == false)
				FinesDisplay = "select Card_id, SUM(fine_amt) AS SUM_Fine_Amount, paid " +
						"from FINES, BOOK_LOANS WHERE BOOK_LOANS.Loan_id = FINES.Loan_id " +
						"AND Card_id = '"+CardNum+"' GROUP BY Card_id, paid ; ";
			else if(CardNum.equals("") && FilterOutPaid == true)
					FinesDisplay = "select Card_id, SUM(fine_amt) AS SUM_Fine_Amount, paid " +
							"from FINES, BOOK_LOANS WHERE BOOK_LOANS.Loan_id = FINES.Loan_id " +
							"AND paid IS FALSE GROUP BY Card_id, paid; ";
			else if(CardNum.length()>0 && FilterOutPaid == true)
					FinesDisplay = "select Card_id, SUM(fine_amt) AS SUM_Fine_Amount, paid " +
							"from FINES, BOOK_LOANS WHERE BOOK_LOANS.Loan_id = FINES.Loan_id " +
							"AND paid IS FALSE AND Card_id = '"+CardNum+"' GROUP BY Card_id, paid; ";
			/**/
			rs = stmt.executeQuery(FinesDisplay);
			
			/** Display "Search Result Number" */
			int rowcount = 0;
			if (rs.last()) {
			  rowcount = rs.getRow();
			  rs.beforeFirst(); // not rs.first() because the rs.next() below will move on, missing the first element
			}
			else rowcount = 0;
			
			tableFines.setModel(DbUtils.resultSetToTableModel(rs));
			
			rs.close();
			conn.close();
			return rowcount;
		} 
		catch(SQLException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage());
			return 0;
		}
	}

	public static void AlterPaid(String Card_no)
	{
		ResultSet rs = null;
		try {
			Connection conn  = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "jd", password);
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
			//stmt.execute("use library;");
			String isAllCheckIn = "SELECT COUNT(FINES.Loan_id) AS cnt " +
					"FROM BOOK_LOANS JOIN FINES ON BOOK_LOANS.Loan_id = FINES.Loan_id " +
					"WHERE Card_id = '"+Card_no+"' AND paid IS FALSE AND Date_in IS NULL;";
			rs = stmt.executeQuery(isAllCheckIn); rs.next();
			String cnt = rs.getString("cnt");
			if(Integer.parseInt(cnt) > 0){
				JOptionPane.showMessageDialog(null, "Failed, Your fined Books are still not checked in!");
				return;
			}
			
			String CardNoPaidQuery = "SELECT FINES.Loan_id " +
					"FROM BOOK_LOANS JOIN FINES ON BOOK_LOANS.Loan_id = FINES.Loan_id " +
					"WHERE Card_id = '"+Card_no+"' AND paid IS FALSE;";
			rs = stmt.executeQuery(CardNoPaidQuery);
			List<String> StrTemp = new ArrayList<String>();
			while (rs.next()) {
				String Loan_id = rs.getString("Loan_id");
				StrTemp.add(Loan_id);
			}
			for(int i=0; i<StrTemp.size(); i++) {
				String Update = "UPDATE FINES SET paid=True WHERE Loan_id = "+StrTemp.get(i)+"; ";
				stmt.executeUpdate(Update);
			}
			JOptionPane.showMessageDialog(null, " Set Paid IS TRUE Successfully!");
			rs.close();
			conn.close();
		} 
		catch(SQLException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage());
		}		
	}
	
	public static void UpdateFINESRecord(String Card_no, double EnterPayment)
	{
		ResultSet rs = null;
		try {
			Connection conn  = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "jd", password);
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
			//stmt.execute("use library;");
			String CardNoPaidQuery = "SELECT FINES.Loan_id " +
					"FROM BOOK_LOANS JOIN FINES ON BOOK_LOANS.Loan_id = FINES.Loan_id " +
					"WHERE Card_id = '"+Card_no+"' AND paid IS TRUE;";
			rs = stmt.executeQuery(CardNoPaidQuery);
			
			List<String> StrTemp = new ArrayList<String>();
			while (rs.next()) {
				String Loan_id = rs.getString("Loan_id");
				StrTemp.add(Loan_id);
			}
		
			stmt.executeUpdate("UPDATE FINES SET fine_amt = "+EnterPayment+" WHERE Loan_id = "+StrTemp.get(0)+"; ");
			for(int i=1; i<StrTemp.size(); i++)
				stmt.executeUpdate("UPDATE FINES SET fine_amt = 0 WHERE Loan_id = "+StrTemp.get(i)+"; ");
	
			JOptionPane.showMessageDialog(null, "Update FINES Record Successfully!");
			rs.close();
			conn.close();
		} 
		catch(SQLException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage());
		}		
	}

}
