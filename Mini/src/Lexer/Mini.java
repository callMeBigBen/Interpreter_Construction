package Lexer;
import java.awt.EventQueue;
import Parser.*;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.rmi.NotBoundException;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class Mini {
	private JFrame frmCalculator;
	private run lexer = new run();
	private ParserRun parser = new ParserRun();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
					Mini window = new Mini();
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
	public Mini() {
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
	
	private void initialize() {
		frmCalculator = new JFrame();
		frmCalculator.setTitle("Calculator");
		frmCalculator.setBounds(100, 100, 1107, 786);
		frmCalculator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmCalculator.getContentPane().setLayout(null);
		frmCalculator.setResizable(false);
		


		JTextPane textPane = new JTextPane();
		JScrollPane scrollPane = new JScrollPane(textPane);
		scrollPane.setBounds(0, 45, 525, 619);
		frmCalculator.getContentPane().add(scrollPane);
		
		JTextPane textPane_1 = new JTextPane();
		JScrollPane scrollPane_1 = new JScrollPane(textPane_1);
		scrollPane_1.setBounds(533, 45, 524, 619);
		frmCalculator.getContentPane().add(scrollPane_1);
		


		


		JButton btnNewButton = new JButton("Import File");
		btnNewButton.setOpaque(true);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser jfc=new JFileChooser();
				jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES );
				jfc.showDialog(new JLabel(), "选择");
				File file=jfc.getSelectedFile();
				String encoding = "UTF-8";   
		        Long filelength = file.length();  
		        byte[] filecontent = new byte[filelength.intValue()];  
		        try {  
		            FileInputStream in = new FileInputStream(file);  
		            in.read(filecontent);  
		            in.close();  
		            String content = new String(filecontent, encoding); 
		            textPane.setText(content);
		        } catch (FileNotFoundException e) {  
		            e.printStackTrace();  
		        } catch (IOException e) {  
		            e.printStackTrace();  
		        }  
				
			}
		});

		btnNewButton.setBounds(0, 0, 1057, 48);
		frmCalculator.getContentPane().add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Compile");
		btnNewButton_1.setBounds(0, 663, 1057, 46);
		btnNewButton_1.setOpaque(true);
		frmCalculator.getContentPane().add(btnNewButton_1);
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textPane.setText(lexer.getPreprocessedContent(textPane.getText()));
				String result = lexer.runLexer();
				if(lexer.isError) {
					textPane_1.setText(lexer.error);
				}
				else {
					textPane_1.setText(result);
					parser.start(lexer.getTokens());
					String delimiter = "This is the end of lexer\r\n" + 
							"*********************************************\n\n";
					textPane_1.setText(textPane_1.getText()+delimiter+parser.getRetStr());
				}
				
			}
		});

		
		
		JLabel bglabel = new JLabel();//创建JLabel
		getBackgroundPicture(bglabel);
		
	}
}


