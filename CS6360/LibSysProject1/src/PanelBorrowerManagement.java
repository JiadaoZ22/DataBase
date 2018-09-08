import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class PanelBorrowerManagement extends JLayeredPane {
	private static final long serialVersionUID = 1L;
	
	private JTextField textSSN;
	private JTextField textFname;
	private JTextField textLname;
	private JTextField textAddress;
	private JTextField textPhone;
	
	public PanelBorrowerManagement() {
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBounds(256, 49, 386, 400);
		add(panel);
		
		JLabel labelFname = new JLabel("Fname");
		labelFname.setFont(new Font("TimesRoman", Font.BOLD, 14));
		labelFname.setBounds(40, 97, 54, 15);
		panel.add(labelFname);
		
		JLabel labelLname = new JLabel("Lname");
		labelLname.setFont(new Font("TimesRoman", Font.BOLD, 14));
		labelLname.setBounds(40, 136, 54, 15);
		panel.add(labelLname);
		
		JLabel labelAddress = new JLabel("Address");
		labelAddress.setFont(new Font("TimesRoman", Font.BOLD, 14));
		labelAddress.setBounds(40, 176, 54, 15);
		panel.add(labelAddress);
		
		JLabel labelSSN = new JLabel("SSN");
		labelSSN.setFont(new Font("TimesRoman", Font.BOLD, 14));
		labelSSN.setBounds(40, 56, 77, 15);
		panel.add(labelSSN);
		
		textSSN = new JTextField();
		textSSN.setColumns(10);
		textSSN.setBounds(127, 56, 136, 21);
		panel.add(textSSN);
		
		textFname = new JTextField();
		textFname.setColumns(10);
		textFname.setBounds(127, 96, 136, 21);
		panel.add(textFname);
		
		textLname = new JTextField();
		textLname.setColumns(10);
		textLname.setBounds(127, 136, 136, 21);
		panel.add(textLname);
		
		textAddress = new JTextField();
		textAddress.setColumns(10);
		textAddress.setBounds(127, 177, 228, 21);
		panel.add(textAddress);
		
		JLabel labelPhone = new JLabel("Phone");
		labelPhone.setFont(new Font("TimesRoman", Font.BOLD, 14));
		labelPhone.setBounds(40, 220, 54, 15);
		panel.add(labelPhone);
		
		textPhone = new JTextField();
		textPhone.setColumns(10);
		textPhone.setBounds(127, 220, 136, 21);
		panel.add(textPhone);
		
		//=======================Create New Account=======================
		JButton buttonCreateAccount = new JButton("Create Account");
		buttonCreateAccount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String SSN = textSSN.getText();
				String Fname = textFname.getText();
				String Lname = textLname.getText();
				String Address = textAddress.getText();
				String Phone = textPhone.getText();
				String[] infoList = {SSN, Fname, Lname, Address, Phone};
				
				//======Create New Account======
				sqlOperation.CreateNewAccount(infoList);
			}
		});
		buttonCreateAccount.setForeground(Color.red);
		buttonCreateAccount.setFont(new Font("TimesRoman", Font.BOLD, 15));
		buttonCreateAccount.setBounds(110, 324, 175, 52);
		panel.add(buttonCreateAccount);
		

	}

}
