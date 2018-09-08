
package davisbase;

import java.util.TreeMap;

public class Displayer {
	
	public int num_row;
	public TreeMap<Integer, String[]> content;
	public String[] columnNames;
	public int[] col_length;
	
	public Displayer () {
		num_row = 0;
		content = new TreeMap<Integer, String[]>();
		columnNames = null;
		col_length = null;
	}
	
	//2nd
	public void addcontent (int key, String[] val) {
		content.put(key, val);
		num_row += 1;
	}
	
	//1st
	public void addcolumnnames (String[] cn) {
		columnNames = cn;
		col_length = new int[cn.length];
	}
	
	public void update_col_length () {
		for (int i=0; i<columnNames.length; i++) {
			col_length[i] = columnNames[i].length();
		}
		for (int key : content.keySet()) {
			for (int j=0; j<columnNames.length; j++) {
				if (col_length[j] < content.get(key)[j].length()) {
					col_length[j] = content.get(key)[j].length();
				}
			}
		}
	}
	
	public String fix_in (int l, String s) {
		return String.format("%-"+(l+3)+"s", s);
	}
	
	public void prt () {
		update_col_length();
		if (num_row==0) {
			System.out.println("Empty set.");
		} else {
			//print head lines
			for (int l : col_length)
				System.out.print(DavisBase.line("-", (l+4)));
			
			System.out.println();
			//print header (column names)
			for (int i=0; i<columnNames.length; i++)
				System.out.print(fix_in(col_length[i], columnNames[i]) + "|");
			
			System.out.println();
			for (int l : col_length)
				System.out.print(DavisBase.line("-", (l+4)));
			
			System.out.println();
			//print content
			for (int k : content.keySet()) {
				String[] ck = content.get(k);
				for (int j=0; j<ck.length; j++) {
					System.out.print(fix_in(col_length[j], ck[j]) + "|");
				}
				System.out.println();
			}
			for (int l : col_length)
				System.out.print(DavisBase.line("-", (l+4)));
			
			System.out.println();
		}
	}
	
	public static void main (String args[]) {
		String[] cnl = {"123", "1234"};
		Displayer dsp = new Displayer();
		dsp.addcolumnnames(cnl);
		String[] v1 = {"get", "fuck"};
		String[] v2 = {"love", "peaceful"};
		dsp.addcontent(2, v2);
		dsp.addcontent(1, v1);
		dsp.update_col_length();
		for (int i : dsp.col_length) {
			System.out.println(i);
		}
		dsp.prt();
	}

}
