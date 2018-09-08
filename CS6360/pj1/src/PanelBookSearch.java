import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import net.proteanit.sql.DbUtils;


public class PanelBookSearch extends JLayeredPane {
	private static final long serialVersionUID = 1L;
	
	private JTextField textISBN;
	private JTextField textTitle;
	private JTextField textAuthor;
	private JTable BookSearchTable;
	private JLabel lableResultNum;
	
	Connection dbConnection = null; // ......
	
	public void ButtonBookSearchEvent()
	{
//		System.out.println(textISBN.getText());
		if(textISBN.getText().length()==0 && textTitle.getText().length()==0
				&& textAuthor.getText().length()==0)
			JOptionPane.showMessageDialog(null, "Please input content in 'ISBN, Book Title, Author' Field!");
		else{
			try {
				dbConnection = sqlOperation.dbConnector(); /** obtain the Connection */
				/** sqlQuery from input dbConnection */
				ResultSet rs = sqlOperation.BookQuery(dbConnection, textISBN.getText(), textTitle.getText(), textAuthor.getText());
				/** Display "Search Result Number" */
				int rowcount = 0;
				if (rs.last()) {
				  rowcount = rs.getRow();
				  rs.beforeFirst(); 
				}
				else rowcount = 0;
				lableResultNum.setText("Search Result Number: " + rowcount);
				
				/** Using "rx2xml.jar" to display data into table from ResultSet */
				BookSearchTable.setModel(DbUtils.resultSetToTableModel(rs));
				/** Once rs is closed, it could not be used! */
				rs.close();
				dbConnection.close();
			} catch (SQLException e) {
				//e.printStackTrace();
				JOptionPane.showMessageDialog(null, e.getMessage());
			}
		}
	}
	
	public PanelBookSearch() {
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 94, 969, 388);
		add(scrollPane);
		
		textISBN = new JTextField();
		textISBN.setBounds(118, 41, 111, 21);
		add(textISBN);
		textISBN.setColumns(10);
		
		textTitle = new JTextField();
		textTitle.setBounds(120, 8, 439, 21);
		add(textTitle);
		textTitle.setColumns(10);
		
		textAuthor = new JTextField();
		textAuthor.setBounds(351, 41, 208, 21);
		add(textAuthor);
		textAuthor.setColumns(10);
		
		JLabel Label_BookTitle = new JLabel("Book Title");
		Label_BookTitle.setFont(new Font("TimesRoman", Font.BOLD, 14));
		Label_BookTitle.setBounds(36, 10, 81, 15);
		add(Label_BookTitle);
		
		JLabel Label_ISBN = new JLabel("ISBN");
		Label_ISBN.setFont(new Font("TimesRoman", Font.BOLD, 14));
		Label_ISBN.setBounds(36, 43, 54, 15);
		add(Label_ISBN);
		
		JLabel Label_Author = new JLabel("Author");
		Label_Author.setFont(new Font("TimesRoman", Font.BOLD, 14));
		Label_Author.setBounds(250, 43, 100, 15);
		add(Label_Author);
		
		BookSearchTable = new JTable();
		scrollPane.setViewportView(BookSearchTable); 
		
		JButton ButtonBookSearch = new JButton("Book Search");
		ButtonBookSearch.setForeground(Color.green);
		ButtonBookSearch.setFont(new Font("TimesRoman", Font.BOLD, 13));
		ButtonBookSearch.setBounds(702, 15, 125, 43);
		add(ButtonBookSearch);
		
		lableResultNum = new JLabel("");
		lableResultNum.setBounds(785, 68, 194, 15);
		add(lableResultNum);
		
		ButtonBookSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ButtonBookSearchEvent();
			}
		});
	}
}
