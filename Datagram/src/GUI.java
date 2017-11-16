import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.filechooser.FileSystemView;


import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.awt.event.ActionEvent;

public class GUI {

	private JFrame frmFileTransfer;
	private JTextField tbxHostname;
	private JTextField tbxPortNo;
	private JTextField tbxUsername;
	private EchoClientHelper1 helper;
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
		
		tbxHostname = new JTextField();
		tbxHostname.setBounds(76, 44, 86, 20);
		panel.add(tbxHostname);
		tbxHostname.setColumns(10);
		
		tbxPortNo = new JTextField();
		tbxPortNo.setBounds(76, 78, 86, 20);
		panel.add(tbxPortNo);
		tbxPortNo.setColumns(10);
		
		tbxUsername = new JTextField();
		tbxUsername.setBounds(76, 110, 86, 20);
		panel.add(tbxUsername);
		tbxUsername.setColumns(10);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					helper = new EchoClientHelper1(tbxHostname.getText(), tbxPortNo.getText());			
					String response = helper.login(tbxUsername.getText());			
					
					JOptionPane.showMessageDialog(null, response);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnLogin.setBounds(10, 138, 153, 23);
		panel.add(btnLogin);
		
		JButton btnLogout = new JButton("Logout");
		btnLogout.setBounds(10, 172, 152, 23);
		panel.add(btnLogout);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(195, 11, 351, 209);
		frmFileTransfer.getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		JButton btnUpload = new JButton("Upload...");
		btnUpload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					//helper = new EchoClientHelper1(tbxHostname.getText(), tbxPortNo.getText());				
					JFileChooser uploadChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
					int returnValue = uploadChooser.showOpenDialog(null);
					if(returnValue == JFileChooser.APPROVE_OPTION){

						File selectedFile = uploadChooser.getSelectedFile();
						String fileName = selectedFile.getName();
						Path pathOfFile = Paths.get(selectedFile.getAbsolutePath());
				
						byte[] fileAsByte = Files.readAllBytes(pathOfFile);					
						String response = helper.upload(fileName, fileAsByte);
						
						JOptionPane.showMessageDialog(null, response);
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnUpload.setBounds(129, 51, 89, 23);
		panel_1.add(btnUpload);
		
		JButton btnDownload = new JButton("Download");
		btnDownload.setBounds(129, 116, 89, 23);
		panel_1.add(btnDownload);
	}
}
