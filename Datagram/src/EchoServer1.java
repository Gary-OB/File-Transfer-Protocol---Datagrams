import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

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
      
      String currentUser = "";
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
	        		  }    
	        		  
	        		  userLoggedIn = true;
        			  mySocket.sendMessage(request.getAddress(), request.getPort(), "150: " + currentUser + " logged in Successfully");
	        	  } 
	          } else {
	        	  if(message.startsWith("200-UPLOAD")) {
	        		  try{
	        			  System.out.println(currentUser);
	        			  System.out.println(message);
	        			  message = message.replace("200-UPLOAD", "").trim();         			  
	        			  String userDirectory = "C:/ServerFolders/" + currentUser + "/" + message;
	        			  System.out.println(userDirectory);
	        			  Path pathToFile = Paths.get(userDirectory);
	        			  
	        			  System.out.println("Sending message");
	        			  mySocket.sendMessage(request.getAddress(), request.getPort(), "225-REQUESTRECEIVED");
	        			  System.out.println("Message Sent");
	        			  String byteResponse = mySocket.receiveMessage();
	        			  
	        			  System.out.println(byteResponse);
	        			  
	        			  byte[] byteFileIn = byteResponse.getBytes(); //request.getFileByteArray();
	        			  System.out.println("Writing tp file");
	        			  Files.write(pathToFile, byteFileIn, StandardOpenOption.CREATE);
	        			  System.out.println("Written");
	        			  mySocket.sendMessage(request.getAddress(), request.getPort(), "250: File Successfully uploaded");
	        		  } catch (FileNotFoundException e) {
	        			  mySocket.sendMessage(request.getAddress(), request.getPort(), "275: Error, file not uploaded ");
	        		  }
	        	  } 
	        	  
	        	  else if(message.startsWith("300-LISTFILES")) {
	        		  try {	        			  
	        			  message = message.replace("300-DOWNLOAD", "").trim();
	        			  
	        			  String fileDirectory = "C:/ServerFolders/" + currentUser + "/" + message.trim();
                                            
	        			  byte[] fileByteArray = Files.readAllBytes(new File(fileDirectory).toPath());
	        			  mySocket.sendMessage(request.getAddress(), request.getPort(), fileByteArray);
	        			  mySocket.sendMessage(request.getAddress(), request.getPort(), "350: Download Successful");
	        		  } catch (FileNotFoundException e) {
	        			  mySocket.sendMessage(request.getAddress(), request.getPort(), "375: Error, file not found ");
	        		  }
	        	  } 
	        	  
	        	  else if(message.startsWith("400-LOGOUT")) {
	        		  currentUser = "";
	        		  userLoggedIn = false;
	        		  mySocket.sendMessage(request.getAddress( ), request.getPort( ), currentUser + "450: Logged out Successfully");
	        	  }       	  
	          }
	      } catch (Exception ex) {
	          ex.printStackTrace( );
		  } 
      }
   } 
}      
