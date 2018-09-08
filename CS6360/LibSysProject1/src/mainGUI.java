import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JLayeredPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;

import java.awt.Font;
import java.sql.Connection;

public class mainGUI {
	private JFrame frame;

	private JTabbedPane tabbedPane;
	private JLayeredPane PanePassword;
	private PanelBookSearch panelBookSearch;
	private PanelBookChecking panelBookChecking;
	private PanelBorrowerManagement panelBorrowerManagement;
	private PanelFines panelFines;
	
	Connection dbConnection = null; // ......
	
	/** Launch the application. */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					mainGUI window = new mainGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/** ############# Trigger Event############# */
	public void panelPasswordEvent()
	{
		String passWord = "123";
		boolean isConnect;
		isConnect = sqlOperation.PSQLConnectTest(passWord);
		if(isConnect == true) {

			tabbedPane.add("Book Search", panelBookSearch);
			tabbedPane.add("Checking OUT / IN Books", panelBookChecking);
			tabbedPane.add("Borrower Management", panelBorrowerManagement);
			tabbedPane.add("Fines", panelFines);
			
			tabbedPane.remove(PanePassword);
		}		
	}
	
	/** Create the application. */
	public mainGUI() { initialize(); }

	/** Initialize the contents of the frame. */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1000, 600);
//		frame.setBackground(Color.blue);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		// main frame 
		tabbedPane.setBounds(0, 0, 1050, 621);
//		tabbedPane.setBackground(Color.gray);
		frame.getContentPane().add(tabbedPane);
		
		//====================Create different panels===================
		//panelTest = new PanelPassword();
		panelBookSearch = new PanelBookSearch();
		panelBookChecking = new PanelBookChecking();
		panelBorrowerManagement = new PanelBorrowerManagement();
		panelFines = new PanelFines();
		
		//================================panel of Password=====================================
		PanePassword = new JLayeredPane();
		tabbedPane.addTab("WELCOME TO THE lIBRARY SYSTEM!", null, PanePassword, null);
		//tabbedPane.setBounds(20, 20, 100, 20);
		PanePassword.setLayout(null);
		JPanel PasswordPanel = new JPanel();
		PasswordPanel.setBounds(284, 85, 470, 360);
		PasswordPanel.setBackground(Color.gray);
		PanePassword.add(PasswordPanel);
		PasswordPanel.setLayout(null);
		

		JButton ButtonPassword = new JButton("SQL Connect");
		ButtonPassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				/** ############# Executing Trigger Event ############# */
				//panelPasswordEvent();
				
				String passWord = "123";
				boolean isConnect;
				isConnect = sqlOperation.PSQLConnectTest(passWord);
				if(isConnect == true) {
				JOptionPane.showMessageDialog(null, "Connect SQL Succesfully!!!");
				sqlOperation.password = passWord; //
					
					tabbedPane.add("Book Search", panelBookSearch);
					tabbedPane.add("Checking OUT / IN Books", panelBookChecking);
					tabbedPane.add("Borrower Management", panelBorrowerManagement);
					tabbedPane.add("Fines", panelFines);
					
					tabbedPane.remove(PanePassword);
				
			}}
			});
		ButtonPassword.setFont(new Font("TimesRoman", Font.BOLD, 32));
		ButtonPassword.setForeground(Color.black);
		ButtonPassword.setBounds(104, 135, 256, 100);
		PasswordPanel.add(ButtonPassword);
		

	
}
}