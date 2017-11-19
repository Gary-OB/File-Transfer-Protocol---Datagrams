import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.filechooser.FileSystemView;

import com.sun.xml.internal.ws.util.StringUtils;

import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;

public class GUI {

	private JFrame frmFileTransfer;
	private JTextField tbxHostname;
	private JTextField tbxPortNo;
	private JTextField tbxUsername;
	private EchoClientHelper1 helper;
	
	private JButton btnLogin = new JButton("Login");
	private JButton btnLogout = new JButton("Logout");
	private JButton btnUpload = new JButton("Upload...");
	private JButton btnDownload = new JButton("Download...");
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
		frmFileTransfer.setResizable(false);
		frmFileTransfer.setTitle("File Transfer");
		frmFileTransfer.setBounds(100, 100, 247, 326);
		frmFileTransfer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmFileTransfer.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(10, 11, 211, 187);
		frmFileTransfer.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblLoginDetails = new JLabel("Login Details");
		lblLoginDetails.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblLoginDetails.setBounds(66, 11, 85, 22);
		panel.add(lblLoginDetails);
		
		JLabel lblHostname = new JLabel("Hostname");
		lblHostname.setBounds(10, 44, 65, 14);
		panel.add(lblHostname);
		
		JLabel lblPortNo = new JLabel("Port No");
		lblPortNo.setBounds(10, 69, 65, 14);
		panel.add(lblPortNo);
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setBounds(10, 94, 65, 14);
		panel.add(lblUsername);
		
		tbxHostname = new JTextField();
		tbxHostname.setBounds(85, 41, 116, 20);
		panel.add(tbxHostname);
		tbxHostname.setColumns(10);
		
		tbxPortNo = new JTextField();
		tbxPortNo.setBounds(85, 66, 116, 20);
		panel.add(tbxPortNo);
		tbxPortNo.setColumns(10);
		
		tbxUsername = new JTextField();
		tbxUsername.setBounds(85, 91, 116, 20);
		panel.add(tbxUsername);
		tbxUsername.setColumns(10);
		
		tbxHostname.setText("localhost");
		tbxPortNo.setText("7");
		
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					helper = new EchoClientHelper1(tbxHostname.getText(), tbxPortNo.getText());			
					String response = helper.login(tbxUsername.getText());			
					
					JOptionPane.showMessageDialog(null, response, "Logged in", JOptionPane.INFORMATION_MESSAGE);
					enableControls(true);
				
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnLogin.setBounds(10, 119, 191, 23);
		panel.add(btnLogin);
		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				try {
					String response = helper.logout();
					enableControls(false);
					JOptionPane.showMessageDialog(null, response, "Logged out", JOptionPane.INFORMATION_MESSAGE);
					tbxUsername.setText("");
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}			
			}
		});
		
		
		btnLogout.setEnabled(false);
		btnLogout.setBounds(10, 153, 191, 23);
		panel.add(btnLogout);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(10, 209, 211, 78);
		frmFileTransfer.getContentPane().add(panel_1);
		panel_1.setLayout(null);
				
		btnUpload.setEnabled(false);
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
		btnUpload.setBounds(10, 11, 191, 23);
		panel_1.add(btnUpload);
		
		
		btnDownload.setEnabled(false);
		btnDownload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
			
					String[] arrayOfFilenames = helper.populateDownloadArray();
					
					String fileToDownload = (String) JOptionPane.showInputDialog(null, "Choose file to download... ", "Download", 
							JOptionPane.QUESTION_MESSAGE, null, arrayOfFilenames, arrayOfFilenames[1]);
					
					fileToDownload = fileToDownload.trim();
					
					JFileChooser downloadPathChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
					downloadPathChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					
					downloadPathChooser.showSaveDialog(null);
				
					File folderForDownload = downloadPathChooser.getSelectedFile(); //getCurrentDirectory();
						
					String folderName = folderForDownload.getPath();
					
					
					
					helper.download(folderName, fileToDownload);	
						
					JOptionPane.showMessageDialog(null, "Downloaded File", "Success", JOptionPane.INFORMATION_MESSAGE);
					
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnDownload.setBounds(10, 45, 191, 23);
		panel_1.add(btnDownload);
	}
	
	public void enableControls(boolean yesOrNo) {
		
		btnLogin.setEnabled(!yesOrNo);
		tbxHostname.setEnabled(!yesOrNo);
		tbxPortNo.setEnabled(!yesOrNo);
		tbxUsername.setEnabled(!yesOrNo);
		
		btnLogout.setEnabled(yesOrNo);
		btnUpload.setEnabled(yesOrNo);
		btnDownload.setEnabled(yesOrNo);
	}
	
}
