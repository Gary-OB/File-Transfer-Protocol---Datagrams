import java.net.*;

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
   
   public String download(String message) throws SocketException, IOException {                                                                                 
	   	String mess = "200-DOWNLOAD " + message;    
	  	mySocket.sendMessage( serverHost, serverPort, mess);

	  	String response = mySocket.receiveMessage();
	  	return response;
   } 
   
   public String upload(String message, byte[] file) throws SocketException, IOException {                                                                                 
	    String mess = "300-UPLOAD " + message;    
		mySocket.sendMessage( serverHost, serverPort, mess);
	
		String response = mySocket.receiveMessage();
		
		if(response.equals("225-REQUESTRECEIVED")){
			mySocket.sendMessage( serverHost, serverPort, file);
			response = mySocket.receiveMessage();
		} 
		return response;
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
