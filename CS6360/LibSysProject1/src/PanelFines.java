import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;


public class PanelFines extends JLayeredPane {
	private static final long serialVersionUID = 1L;
	
	private JTable tableFines;
	private JTextField textCardNumber;
	private JCheckBox CheckBoxFilterOutPaid;
	private JLabel labelFinesNum;
	
	public PanelFines() {
		JScrollPane scrollPaneFine = new JScrollPane();
		scrollPaneFine.setBounds(350, 20, 600, 500);
		//scrollPaneFine.setBounds(142, 139, 713, 318);
		add(scrollPaneFine);
		
		tableFines = new JTable();
		scrollPaneFine.setViewportView(tableFines);
		
		labelFinesNum = new JLabel("");
		labelFinesNum.setBounds(682, 467, 173, 15);
		add(labelFinesNum);
		
		//=======================Refresh Fines=======================
		JButton ButtonRefreshFines = new JButton("Show FINES");
		ButtonRefreshFines.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String CardNum = textCardNumber.getText().trim();
				if(CardNum.length()>0 && CardNum.length()!=8)
					JOptionPane.showMessageDialog(null, "The length of Card No. is not equal to 8 !!!");
				
				boolean FilterOutPaid = CheckBoxFilterOutPaid.isSelected();
				int cnt = sqlOperation.FinesTableUpdatedDisplay(tableFines, CardNum, FilterOutPaid);
				labelFinesNum.setText("Search Result Number: " + cnt);
			}
		});
		ButtonRefreshFines.setForeground(Color.green);
		ButtonRefreshFines.setFont(new Font("TimesRoman", Font.BOLD, 14));
		ButtonRefreshFines.setBounds(100, 180, 190, 40);
//		ButtonRefreshFines.setBounds(300, 74, 200, 45);
		add(ButtonRefreshFines);
		
		//=======================Enter Payment=======================
		JButton ButtonEnterPayment = new JButton("Update Fine Amount");
		ButtonEnterPayment.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				//BigDecimal EnterPayment = new BigDecimal("0.00");
				String Card_no = (String)tableFines.getValueAt(tableFines.getSelectedRow(), 0);
				Object paid = tableFines.getValueAt(tableFines.getSelectedRow(), 2);
				
				//=========Pay the fines tableFinesĬ=========
				Object ob = (Object)tableFines.getValueAt(tableFines.getSelectedRow(), 1);
				double EnterPayment = Double.parseDouble(ob.toString());
				//JOptionPane.showMessageDialog(null, EnterPayment); return;
				
				if(paid.equals(null))
					JOptionPane.showMessageDialog(null, "Because paid=FALSE, Fail to Update!");	
				else
					sqlOperation.UpdateFINESRecord(Card_no, EnterPayment);/**/		
			}
		});
		ButtonEnterPayment.setForeground(Color.green);
		ButtonEnterPayment.setFont(new Font("TimesRoman", Font.BOLD, 14));
		ButtonEnterPayment.setBounds(100, 230, 190, 40);
		add(ButtonEnterPayment);
		
		//=======================Enter Paid=======================
		JButton ButtonPaid = new JButton("Pay Now");
		ButtonPaid.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Object paid = tableFines.getValueAt(tableFines.getSelectedRow(), 2);
				String Card_no = (String)tableFines.getValueAt(tableFines.getSelectedRow(), 0);
				if(paid.equals(false))
					sqlOperation.AlterPaid(Card_no);
				else
					JOptionPane.showMessageDialog(null, "This book has been paid!");
			}
		});
		ButtonPaid.setForeground(Color.green);
		ButtonPaid.setFont(new Font("TimesRoman", Font.BOLD, 15));
		ButtonPaid.setBounds(100, 280, 190, 40);
		add(ButtonPaid);
		
		textCardNumber = new JTextField();
//		textCardNumber.setBounds(142, 60, 118, 23);
		textCardNumber.setBounds(117, 60, 150, 25);
		add(textCardNumber);
		textCardNumber.setColumns(10);
		
		JLabel LabelCardNumber = new JLabel("Card_id :");
//		LabelCardNumber.setBounds(20, 62, 150, 20);
		LabelCardNumber.setFont(new Font("TimesRoman", Font.BOLD, 13));
		LabelCardNumber.setBounds(142, 40, 91, 15);
		add(LabelCardNumber);
		
		CheckBoxFilterOutPaid = new JCheckBox("Filter Out Paid Fines");
		CheckBoxFilterOutPaid.setFont(new Font("TimesRoman", Font.BOLD, 11));
		CheckBoxFilterOutPaid.setBounds(102, 96, 150, 23);
		add(CheckBoxFilterOutPaid);
	}
}
