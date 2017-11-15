import java.io.*;

import javax.swing.JOptionPane;

/**
 * This module contains the application logic of an echo server
 * which uses a connectionless datagram socket for interprocess 
 * communication.
 * A command-line argument is required to specify the server port.
 * @author M. L. Liu
 */

public class EchoServer1 {
   public static void main(String[] args) {
      int serverPort = 7;    
      if (args.length == 1 )
         serverPort = Integer.parseInt(args[0]); 
      
      String currentUser;
      boolean userLoggedIn = false;
    	  
      while(true) {
	      try {
	    	  	    	  
	   	   	  MyServerDatagramSocket mySocket = new MyServerDatagramSocket(serverPort); 
	          System.out.println("Echo server ready.");  
	         
	          DatagramMessage request = mySocket.receiveMessageAndSender();
	          System.out.println("Request received");
	          
	          String message = request.getMessage( );
	
	          if(!userLoggedIn) {
	        	  if(message.startsWith("100-LOGIN")) {
	        		  currentUser = message.replace("100-LOGIN", "").trim();
	        		  String userDirectory = "C:/ServerFolders/" + currentUser;
	            	
	        		  if (!new File(userDirectory.trim()).exists()) {
	        			  new File(userDirectory.trim()).mkdirs();
	        			  userLoggedIn = true;
	        			  mySocket.sendMessage(request.getAddress( ), request.getPort( ), currentUser + "150: Logged in Successfully");
	        		  }
	            		          
	        	  } else {
	            	mySocket.sendMessage(request.getAddress( ), request.getPort( ), "400 Not Found");
	        	  } 
	          } else {
	        	  if(message.startsWith("200")) {
	        		  
	        	  } 
	        	  
	        	  else if(message.startsWith("300")) {
	        		  
	        	  } 
	        	  
	        	  else if(message.startsWith("400")) {
	        		  
	        	  }       	  
	          }
	      } catch (Exception ex) {
	          ex.printStackTrace( );
	          System.exit(0);
		  } 
      }
   } 
}      
