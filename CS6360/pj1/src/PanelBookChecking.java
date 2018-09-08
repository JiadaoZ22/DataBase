import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import net.proteanit.sql.DbUtils;


public class PanelBookChecking extends JLayeredPane {
	private static final long serialVersionUID = 1L;
	
	private JTextField textISBN2;
	private JTextField textCardNo;
	private JTextField textBookID;
	private JTextField textCardNo2; 
	private JTable tableCheckIn;
	
	Connection dbConnection = null; // ......
	
	/** ############# Trigger Event1############# */
	public void ButtonCheckingOutEvent()
	{
		if(textISBN2.getText().length() == 0)
			JOptionPane.showMessageDialog(null, "The ISBN is NULL!");
		else if(textISBN2.getText().length() != 10)
			JOptionPane.showMessageDialog(null, "The length of ISBN is not equal to 10!!!");
		else{
			sqlOperation.CheckingOutBooks(textISBN2.getText(), textCardNo.getText());
		}		
	}
	
	/** ############# Trigger Event2############# */
	public void SearchBeforeCheckingInEvent()
	{
		if(textBookID.getText().length()==0 && textCardNo2.getText().length()==0)
			JOptionPane.showMessageDialog(null, "Please input content in 'BookID, Card_id' Field!");
		else{
			try {
				dbConnection = sqlOperation.dbConnector(); /** obtain the Connection */
				
				/** sqlQuery from input dbConnection */
				ResultSet rs = sqlOperation.SearchBeforeCheckingIn(dbConnection, textBookID.getText(), textCardNo2.getText());
				/** Using "rx2xml.jar" to display data into table from ResultSet */
				tableCheckIn.setModel(DbUtils.resultSetToTableModel(rs));
				/** Once rs is closed, it could not be used! */
				rs.close();
				dbConnection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}		
	}
	
	/** ############# Trigger Event3############# */
	public void ButtonCheckingInEvent()
	{
		if(tableCheckIn.getSelectedRow() >= 0) {
			String Date_in = (String)tableCheckIn.getValueAt(tableCheckIn.getSelectedRow(), 5);
//			System.out.println("1");
			if(Date_in != null)
				JOptionPane.showMessageDialog(null, "Failed, because ihis book has already been checked in !!!");
			else
			{
//				System.out.println("2");
				int Loan_id = (Integer)tableCheckIn.getValueAt(tableCheckIn.getSelectedRow(), 0);
				String ISBN = (String)tableCheckIn.getValueAt(tableCheckIn.getSelectedRow(), 1); 
				//JOptionPane.showMessageDialog(null, Integer.toString(Loan_id));
				//JOptionPane.showMessageDialog(null, ISBN);
     			sqlOperation.CheckingInBooks(Integer.toString(Loan_id), ISBN);
				/**/
			}
		}		
	}
	
	/** ############# Initial Panel ############# */
	public PanelBookChecking() {
		JPanel panelCheckingOut = new JPanel();
		panelCheckingOut.setToolTipText("");
		panelCheckingOut.setBounds(20, 67, 249, 266);
		add(panelCheckingOut);
		panelCheckingOut.setLayout(null);
		
		textISBN2 = new JTextField();
		textISBN2.setBounds(114, 32, 103, 21);
		panelCheckingOut.add(textISBN2);
		textISBN2.setColumns(10);
	
		textCardNo = new JTextField();
		textCardNo.setBounds(114, 90, 103, 21);
		panelCheckingOut.add(textCardNo);
		textCardNo.setColumns(10);
		
		JLabel LabelISBN = new JLabel("ISBN");
		LabelISBN.setFont(new Font("TimesRoman", Font.BOLD, 14));
		LabelISBN.setBounds(24, 35, 54, 15);
		panelCheckingOut.add(LabelISBN);
		
		JLabel LabelCardNo = new JLabel("Card_id");
		LabelCardNo.setFont(new Font("TimesRoman", Font.BOLD, 14));
		LabelCardNo.setBounds(24, 89, 72, 15);
		panelCheckingOut.add(LabelCardNo);
		
		//=======================Checking Out Button=======================
		JButton ButtonCheckOut = new JButton("Checking Out");
		ButtonCheckOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/** ############# Executing Trigger Event ############# */
				ButtonCheckingOutEvent();
			}
		});
		ButtonCheckOut.setForeground(Color.green);
		ButtonCheckOut.setFont(new Font("TimesRoman", Font.BOLD, 14));
		ButtonCheckOut.setBounds(48, 184, 133, 44);
		panelCheckingOut.add(ButtonCheckOut);
		
		JPanel panelCheckingIn = new JPanel();
		panelCheckingIn.setBounds(312, 20, 646, 462);
		add(panelCheckingIn);
		panelCheckingIn.setLayout(null);
		
		JLabel LabelBookID = new JLabel("Book ISBN");
		LabelBookID.setFont(new Font("TimesRoman", Font.BOLD, 14));
		LabelBookID.setHorizontalAlignment(SwingConstants.LEFT);
		LabelBookID.setBounds(24, 28, 75, 15);
		panelCheckingIn.add(LabelBookID);
		
		JLabel LabelCardNo2 = new JLabel("Card_id");
		LabelCardNo2.setFont(new Font("TimesRoman", Font.BOLD, 14));
		LabelCardNo2.setHorizontalAlignment(SwingConstants.LEFT);
		LabelCardNo2.setBounds(24, 60, 65, 15);
		panelCheckingIn.add(LabelCardNo2);
		
		textBookID = new JTextField();
		textBookID.setBounds(116, 26, 116, 21);
		panelCheckingIn.add(textBookID);
		textBookID.setColumns(10);
		
		textCardNo2 = new JTextField();
		textCardNo2.setBounds(116, 58, 116, 21);
		panelCheckingIn.add(textCardNo2);
		textCardNo2.setColumns(10);
		
		JScrollPane scrollPaneCheckIn = new JScrollPane();
		scrollPaneCheckIn.setBounds(10, 133, 626, 319);
		panelCheckingIn.add(scrollPaneCheckIn);
		
		tableCheckIn = new JTable();
		scrollPaneCheckIn.setViewportView(tableCheckIn);
		
		//=======================Search Before Checking In=======================
		JButton ButtonSearch = new JButton("Search");
		ButtonSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				/** ############# Executing Trigger Event ############# */
				SearchBeforeCheckingInEvent();
			}
		});
		ButtonSearch.setFont(new Font("TimesRoman", Font.BOLD, 14));
		ButtonSearch.setBounds(264, 41, 93, 53);
		panelCheckingIn.add(ButtonSearch);	
		
		//=======================Checking In Button=======================
		JButton ButtonCheckingIn = new JButton("Checking In");
		ButtonCheckingIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/** ############# Executing Trigger Event ############# */
				ButtonCheckingInEvent();
			}
		});
		ButtonCheckingIn.setForeground(Color.green);
		ButtonCheckingIn.setFont(new Font("TimesRoman", Font.BOLD, 14));
		ButtonCheckingIn.setBounds(458, 38, 123, 59);
		panelCheckingIn.add(ButtonCheckingIn);
	}
}
