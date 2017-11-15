import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;

public class GUI {

	private JFrame frmFileTransfer;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frmFileTransfer.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmFileTransfer = new JFrame();
		frmFileTransfer.setTitle("File Transfer");
		frmFileTransfer.setBounds(100, 100, 572, 270);
		frmFileTransfer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmFileTransfer.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(10, 11, 175, 209);
		frmFileTransfer.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblLoginDetails = new JLabel("Login Details");
		lblLoginDetails.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblLoginDetails.setBounds(56, 11, 85, 22);
		panel.add(lblLoginDetails);
		
		JLabel lblHostname = new JLabel("Hostname");
		lblHostname.setBounds(10, 44, 55, 14);
		panel.add(lblHostname);
		
		JLabel lblPortNo = new JLabel("Port No");
		lblPortNo.setBounds(10, 81, 46, 14);
		panel.add(lblPortNo);
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setBounds(10, 113, 55, 14);
		panel.add(lblUsername);
		
		textField = new JTextField();
		textField.setBounds(76, 44, 86, 20);
		panel.add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setBounds(76, 78, 86, 20);
		panel.add(textField_1);
		textField_1.setColumns(10);
		
		textField_2 = new JTextField();
		textField_2.setBounds(76, 110, 86, 20);
		panel.add(textField_2);
		textField_2.setColumns(10);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.setBounds(10, 138, 153, 23);
		panel.add(btnLogin);
		
		JButton btnLogout = new JButton("Logout");
		btnLogout.setBounds(10, 172, 152, 23);
		panel.add(btnLogout);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(195, 11, 351, 209);
		frmFileTransfer.getContentPane().add(panel_1);
		panel_1.setLayout(null);
	}
}
