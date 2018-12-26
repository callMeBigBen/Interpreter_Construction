import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.rmi.NotBoundException;
import java.awt.event.ActionEvent;
import java.awt.Font;

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
					org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
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
	
	public void getBackgroundPicture(JLabel bglabel){
		ImageIcon background=new ImageIcon("bg.jpg");
		
		bglabel.setIcon(background);//将图片设置到Jlabel中
		
		bglabel.setBounds(0, 0, background.getIconWidth(), background.getIconHeight());//设图片显示的区域
		frmCalculator.getContentPane().add(bglabel);
		//contentPane.setOpaque(false);//设置面板为透明，在gbLabel之上的JPanel都要设置为透明
	}
	public static void createDotGraph(String dotFormat,String fileName)
	{
	    GraphViz gv=new GraphViz();
	    gv.addln(gv.start_graph());
	    gv.add(dotFormat);
	    gv.addln(gv.end_graph());
	    String type = "jpg";  //输出图文件的格式，以.jpg为例
	    gv.decreaseDpi();
	    gv.decreaseDpi();
	    File out = new File(fileName+"."+ type); 
	    gv.writeGraphToFile( gv.getGraph( gv.getDotSource(), type ), out );
	}
	
	private void initialize() {

		frmCalculator = new JFrame();
		frmCalculator.setTitle("Calculator");
		frmCalculator.setBounds(100, 100, 881, 690);
		frmCalculator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmCalculator.getContentPane().setLayout(null);
		frmCalculator.setResizable(false);

		
		input = new JTextField();
		input.setFont(new Font("宋体", Font.PLAIN, 24));
		input.setBounds(24, 13, 777, 189);
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
						output.setText("结果："+Double.toString(i)+"\r\n逆波兰式为:"+test.NBL);
					}
					catch(Exception e){
						output.setText("Unknown Expression!");
						//output.setText(e.getMessage());
					}
					createDotGraph(test.treeStr, "DotGraph");
					test.treeStr ="";
					
			}
		});
		btnCalculate.setBounds(24, 215, 113, 39);
		frmCalculator.getContentPane().add(btnCalculate);
		
		output = new JTextField();
		output.setFont(new Font("宋体", Font.PLAIN, 20));
		output.setBounds(24, 267, 777, 334);
		frmCalculator.getContentPane().add(output);
		output.setColumns(10);
		
		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				input.setText("");
				output.setText("");
			}
		});
		btnClear.setBounds(681, 215, 120, 39);
		frmCalculator.getContentPane().add(btnClear);
		JLabel bglabel = new JLabel();//创建JLabel
		getBackgroundPicture(bglabel);//方法体见下面代码块
	}

}
