import java.io.*;
import java.net.SocketException;
import java.nio.file.FileSystems;
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
   public static void main(String[] args) throws SocketException {
      int serverPort = 7;    
      if (args.length == 1 )
         serverPort = Integer.parseInt(args[0]); 
      
      String currentUser = "";
      boolean userLoggedIn = false;
      MyServerDatagramSocket mySocket = new MyServerDatagramSocket(serverPort); 
    	  
      while(true) {
	      try {
	    	  	    	  

	    	  
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
	        			  
	        			  byte[] byteFileIn = byteResponse.getBytes();
	        			  System.out.println("Writing to file");
	        			  Files.write(pathToFile, byteFileIn, StandardOpenOption.CREATE);
	        			  System.out.println("Written");
	        			  mySocket.sendMessage(request.getAddress(), request.getPort(), "250: File Successfully uploaded");
	        		  } catch (FileNotFoundException e) {
	        			  mySocket.sendMessage(request.getAddress(), request.getPort(), "275: Error, file not uploaded ");
	        		  }
	        	  } 
	        	  
	        	  else if(message.startsWith("300-DOWNLOAD")) {
	        		  try {	        			  
	        			 	        			  
	        			  String fileDirectory = "C:\\ServerFolders\\" + currentUser;
	        			  File userFolder = new File(fileDirectory);	        			  
	        			  File[] listOfFiles = userFolder.listFiles();
	        			  String filesAsString = "";
	        			          			  
	        			  for(int i = 0; i < listOfFiles.length; i++) {
	        				 filesAsString += listOfFiles[i].getName() + ",";
	        			  }	        			  
	        			  System.out.println("Sending message");
	        			  mySocket.sendMessage(request.getAddress(), request.getPort(), filesAsString);
	        			  System.out.println("Message sent");	        			 
	        			  
	        			  System.out.println("receiving message");
	        			  String fileToDownload = mySocket.receiveMessage();
	        			  System.out.println("received");        			  
	        			  fileToDownload = fileToDownload.trim();	        			  
	        			  Path locationOfFile = Paths.get(fileDirectory, fileToDownload);	        			  
	        			  byte[] fileAsByte = Files.readAllBytes(locationOfFile);	
	        			 
	        			  mySocket.sendMessage(request.getAddress(), request.getPort(), fileAsByte);
	        			  mySocket.sendMessage(request.getAddress(), request.getPort(), "350: Download Successful");
	        		  } catch (FileNotFoundException e) {
	        			  mySocket.sendMessage(request.getAddress(), request.getPort(), "375: Error, file not found ");
	        		  }
	        	  } 
	        	  
	        	  else if(message.startsWith("400-LOGOUT")) {
	        		  
	        		  currentUser = "";
	        		  userLoggedIn = false;
	        		  mySocket.sendMessage(request.getAddress( ), request.getPort( ), "450: " + currentUser + "logged out Successfully");
	        	  }       	  
	          }
	      } catch (Exception ex) {
	          //JOptionPane.showMessageDialog(null, ex.getMessage());
	    	  ex.printStackTrace();
		  } 
      }
   } 
}      
