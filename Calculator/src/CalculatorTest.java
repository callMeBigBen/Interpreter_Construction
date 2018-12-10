package test;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.rmi.NotBoundException;
import java.awt.event.ActionEvent;

public class CalculatorTest {
	private JFrame frmCalculator;
	private JTextField input;
	private JTextField output;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CalculatorTest window = new CalculatorTest();
					window.frmCalculator.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public CalculatorTest() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmCalculator = new JFrame();
		frmCalculator.setTitle("Calculator");
		frmCalculator.setBounds(100, 100, 648, 316);
		frmCalculator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmCalculator.getContentPane().setLayout(null);
		
		input = new JTextField();
		input.setBounds(24, 13, 578, 72);
		frmCalculator.getContentPane().add(input);
		input.setColumns(10);
		Calculator test = new Calculator();	
		
		JButton btnCalculate = new JButton("Calculate");
		btnCalculate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
					String str = input.getText().trim();
					try{
						test.read(str);
						double i =0;
						i = test.calculate(test.insertBlanks(str));//调用
						LinkBTree root = test.createBTree(test.NBL);
						test.printTree(root);
						int dept = root.dept();

						output.setText("结果："+Double.toString(i)+"\n逆波兰式为:"+test.NBL+"树层数为"+dept);
					}
					catch(Exception e){
						output.setText("Unknown Expression!");
						//output.setText(e.getMessage());
					}
					
			}
		});
		btnCalculate.setBounds(24, 117, 113, 39);
		frmCalculator.getContentPane().add(btnCalculate);
		
		output = new JTextField();
		output.setBounds(24, 182, 578, 72);
		frmCalculator.getContentPane().add(output);
		output.setColumns(10);
		
		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				input.setText("");
				output.setText("");
			}
		});
		btnClear.setBounds(482, 117, 120, 39);
		frmCalculator.getContentPane().add(btnClear);
	}
}
