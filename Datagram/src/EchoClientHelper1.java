import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import javax.swing.JOptionPane;

import java.io.*;

/**
 * This class is a module which provides the application logic
 * for an Echo client using connectionless datagram socket.
 * @author M. L. Liu
 */
public class EchoClientHelper1 {
   private MyClientDatagramSocket mySocket;
   private InetAddress serverHost;
   private int serverPort;

   EchoClientHelper1(String hostName, String portNum) 
      throws SocketException, UnknownHostException { 
  	   this.serverHost = InetAddress.getByName(hostName);
  		this.serverPort = Integer.parseInt(portNum);

  		
   	this.mySocket = new MyClientDatagramSocket(); 
   } 
	
   public String login( String message) throws SocketException, IOException {                                                                                 
	   	String mess = "100-LOGIN " + message;    	   	
      	mySocket.sendMessage( serverHost, serverPort, mess);

      	String response = mySocket.receiveMessage();
      	return response;
   }  
   
   public String upload(String message, byte[] file) throws SocketException, IOException {                                                                                 
	    String mess = "200-UPLOAD " + message;    

		mySocket.sendMessage( serverHost, serverPort, mess);
		mySocket.receiveMessage();
		mySocket.sendMessage( serverHost, serverPort, file);
		String response = mySocket.receiveMessage();
		
		return response;
   } 
   
   public String[] populateDownloadArray(String message) throws SocketException, IOException {                                                                                 
	   	String mess = "300-LISTFILES";    
	  	mySocket.sendMessage( serverHost, serverPort, mess);
	  	String response = mySocket.receiveMessage();
	  	String[] fileList = response.split(",");	  	
	  	
	  	return fileList;
   }
   
   public void download(String location, String fileToDownload) throws SocketException, IOException {                                                                                 
	  	mySocket.sendMessage( serverHost, serverPort, fileToDownload);
	  	byte[] response = mySocket.receiveByteArray();
	  	
	  	Path toClientFolder = Paths.get(location);
	  	
	  	Files.write(toClientFolder, response, StandardOpenOption.CREATE);
   }
   
   public String logout(String message) throws SocketException, IOException {                                                                                 
		String mess = "400-LOGOUT " + message;    
		mySocket.sendMessage( serverHost, serverPort, mess);

		String response = mySocket.receiveMessage();
		return response;
   } 

   public void done( ) throws SocketException {
      mySocket.close( );
   } 

} 
