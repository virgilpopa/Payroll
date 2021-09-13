import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class PayRollFrame extends JFrame
  implements ActionListener{
	private static JPanel pnlDisplay;
	private static JPanel pnlButtons;
	  private Border lineBorder = 
			  new LineBorder(Color.blue, 2);
	private Dimension displaySize = new Dimension (600,80);
	private Dimension buttonPanelSize = new Dimension (600,50);
	
	private static JButton btnFirst = new JButton("First");
	private static JButton btnPrev = new JButton("Previous");
	private static JButton btnNext = new JButton("Next");
	private static JButton btnLast = new JButton("Last");
	
	private static JTextField txtId = new JTextField(10);
	private static JLabel lblId = new JLabel("Emp Id:");
	
	private static JTextField txtFname = new JTextField(10);
	private static JLabel lblFname = new JLabel("Forename:");
	
	private static JTextField txtSname = new JTextField(10);
	private static JLabel lblSname = new JLabel("Surname:");
	
	private static JTextField txtGender = new JTextField(10);
	private static JLabel lblGender = new JLabel("Gender:");
	
	private static JTextField txtRop = new JTextField(10);
	private static JLabel lblRop = new JLabel("ROP:");
	
	private static JTextField txtHours = new JTextField(10);
	private static JLabel lblHours = new JLabel("Hours:");
	
	private static JTextField txtGross = new JTextField(10);
	private static JLabel lblGross = new JLabel("Gross:");
	
	private static JTextField txtTax = new JTextField(10);
	private static JLabel lblTax = new JLabel("Tax:");
	
	private static JTextField txtNi = new JTextField(10);
	private static JLabel lblNi = new JLabel("N.I:");
	
	private static JTextField txtPen = new JTextField(10);
	private static JLabel lblPen = new JLabel("Pension:");
	
	private static JTextField txtDeds = new JTextField(10);
	private static JLabel lblDeds = new JLabel("Tot Deductions:");
	
	private static JTextField txtNet = new JTextField(10);
	private static JLabel lblNet = new JLabel("Net Pay:");
	
	private static ArrayList<Employee> emps;
	private static Employee emp;
	private static File file;
	private static Scanner fs;
	private static int i=0;
	private static DecimalFormat curr = new DecimalFormat("£0.00");
	
	private static double gross, tax, ni, pen, net, totDeds, stdTime, oTime;
	private static double personal, basic, higher , additional; //tax band allowances
	private static double  niRate, penRate;
	
	public static void main(String[] args) throws Exception {
		PayRollFrame frm = new PayRollFrame();
		frm.setLayout(new BorderLayout());
		frm.add(pnlDisplay, BorderLayout.NORTH);
		frm.add(pnlButtons, BorderLayout.SOUTH);
		
		frm.setTitle("Employee Payroll Details");
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frm.pack();
		frm.setVisible(true);
	}
	
	// Constructor method
	public PayRollFrame() throws Exception {
		pnlDisplay = new JPanel(new GridLayout(0, 8));
		pnlDisplay.setPreferredSize(displaySize);
		pnlDisplay.setBorder(lineBorder);
		
		pnlDisplay.add(lblId);
		pnlDisplay.add(txtId);
		
		pnlDisplay.add(lblFname);
		pnlDisplay.add(txtFname);
		
		pnlDisplay.add(lblSname);
		pnlDisplay.add(txtSname);
		
		pnlDisplay.add(lblGender);
		pnlDisplay.add(txtGender);
		
		pnlDisplay.add(lblRop);
		pnlDisplay.add(txtRop);
		
		pnlDisplay.add(lblHours);
		pnlDisplay.add(txtHours);
		
		pnlDisplay.add(lblGross);
		pnlDisplay.add(txtGross);
		
		pnlDisplay.add(lblTax);
		pnlDisplay.add(txtTax);
		
		pnlDisplay.add(lblNi);
		pnlDisplay.add(txtNi);
		
		pnlDisplay.add(lblPen);
		pnlDisplay.add(txtPen);
		
		pnlDisplay.add(lblDeds);
		pnlDisplay.add(txtDeds);
		
		pnlDisplay.add(lblNet);
		pnlDisplay.add(txtNet);
		
		pnlButtons = new JPanel(new FlowLayout(3, 10, 5)); //left aligned 5 pixel gaps
		pnlButtons.setPreferredSize(buttonPanelSize);
		pnlButtons.setBorder(lineBorder);
		
		//add buttons to button panel. Default FlowLayout
		pnlButtons.add(btnFirst);
		pnlButtons.add(btnPrev);
		pnlButtons.add(btnNext);
		pnlButtons.add(btnLast);
		
		btnFirst.addActionListener(this);
		btnPrev.addActionListener(this);
		btnNext.addActionListener(this);
		btnLast.addActionListener(this);
		
		buildArrayList();
		displayEmp(i);
	}
	private static void buildArrayList() throws Exception {
		file = new File ("data/Peopledetails.txt");
		emps = new ArrayList<Employee>();
		fs = new Scanner(file);
		fs.useDelimiter(",|\\r\n");
		
		while (fs.hasNext()) {
			emp=new Employee();
			emp.setId(fs.nextInt());
			emp.setfName(fs.next());
			emp.setsName(fs.next());
			emp.setGender(fs.next());
			emp.setRop(fs.nextDouble());
			emp.setHrs(fs.nextDouble());
			emps.add(emp);
		}
	}
		
	private static void displayEmp(int i) {
		emp=emps.get(i);
		txtId.setText(String.valueOf(emp.getId())); //id is numeric, needs converting
		txtFname.setText(emp.getfName());
		txtSname.setText(emp.getsName());
		txtGender.setText(emp.getGender());
		txtRop.setText(curr.format(emp.getRop()));
		txtHours.setText(String.valueOf(emp.getHrs()));
		
		calcPayDetails();
		
		txtGross.setText(curr.format(gross));
		txtTax.setText(curr.format(tax));
		txtPen.setText(curr.format(pen));
		txtNi.setText(curr.format(ni));
		txtDeds.setText(curr.format(totDeds));
		txtNet.setText(curr.format(net));
	}
	
	private static void calcPayDetails() {
		if (emp.getHrs()>40) {
			oTime=emp.getHrs() - 40;
			stdTime=40;
		}
		else {
			oTime=0;
			stdTime=emp.getHrs();
		}
		
		gross=( stdTime *emp.getRop() ) + ( oTime * emp.getRop() * 1.5 );
		calcTax();
		calcNi();
		calcPen();
		totDeds=tax + ni + pen;
		net = gross - totDeds;
	}
	
	private static void calcTax() {
		if (gross < 12500 / 52) {
			personal = gross; basic=0; 
			higher=0; additional=0;
		}
		else if (gross < 50000 / 52) {
			personal = 12500 / 52; basic = gross - 12500 / 52; 
			higher = 0; additional = 0; 
		}
		else if (gross < 150000 / 52) {
			personal=12500 / 52;  basic = 37500 / 52; 
			higher = gross - 50000 / 52; additional = 0;
		}
		else {
			personal = 12500 / 52; basic = 37500 / 52;
			higher = 100000 / 52; additional = gross - 150000 / 52;
		}
		tax = personal * 0 + basic * 0.2 + higher * 0.4 + additional * 0.45;
	}
	
	private static void calcNi() {
		if (gross > 50000 / 52)
			niRate=0.02;
		else if (gross >= 8500 / 52)
			niRate=0.12;
		else
			niRate = 0;
		
		ni = gross * niRate;
	}
	
	
	private static void calcPen() {
		if (gross < 27698 / 52)
			penRate = 0.074;
		else if (gross < 37285 / 52)
			penRate = 0.086;
		else if (gross < 44209 / 52)
			penRate = 0.096;
		else if (gross < 58591 / 52)
			penRate = 0.102;
		else if (gross < 79896 / 52)
			penRate = 0.113;
		else
			penRate = 0.117;
		
		pen = gross * penRate;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==btnFirst)
			i=0;
		else if (e.getSource()==btnNext) {
			if (i<emps.size()-1)
				i+=1;
		}
		else if (e.getSource()==btnPrev) {
			if (i>0)
				i-=1;
		}
		else
			i=emps.size()-1;
		
		displayEmp(i);
	}
}