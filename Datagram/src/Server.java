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

public class Server {
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
	        		  String directoryToUser = "C:/ServerFolders/" + currentUser;
	            	
	        		  if (!new File(directoryToUser.trim()).exists()) {
	        			  if (!new File(directoryToUser.trim()).mkdirs()) {
	                            mySocket.sendMessage(request.getAddress(), request.getPort(), "175-INVALIDUSERNAME");
	                      } else {
	                    	    new File(directoryToUser.trim()).mkdirs();
	                    	    userLoggedIn = true;
	                    	    mySocket.sendMessage(request.getAddress(), request.getPort(), "150-LOGINSUCCESSFUL");
	                      }     			  	        			  
	        		  } else {
	        			  userLoggedIn = true;
	        			  mySocket.sendMessage(request.getAddress(), request.getPort(), "150-LOGINSUCCESSFUL");
	        		  }
	        		         		  
	        	  } 
	          } else {
	        	  if(message.startsWith("200-UPLOAD")) {
	        		  try{
	        			  
	        			  System.out.println(currentUser);
	        			  System.out.println(message);
	        			  message = message.replace("200-UPLOAD", "").trim();         			  
	        			  String currentDirectory = "C:/ServerFolders/" + currentUser + "/" + message;
	        			  System.out.println(currentDirectory);
	        			  Path pathToFile = Paths.get(currentDirectory);
	        			  
	        			  System.out.println("Sending message");
	        			  mySocket.sendMessage(request.getAddress(), request.getPort(), "225-UPLOADREQUESTRECEIVED");
	        			  System.out.println("Message Sent");
	        			  byte[] byteFileIn = mySocket.receiveByteArray();	       			  
	        			  System.out.println(byteFileIn.toString());
	        			  
	        			  System.out.println("Writing to file");
	        			  Files.write(pathToFile, byteFileIn, StandardOpenOption.CREATE);
	        			  System.out.println("Written");
	        			  mySocket.sendMessage(request.getAddress(), request.getPort(), "250-UPLOADSUCCESSFUL");
	        		  } catch (FileNotFoundException e) {
	        			  mySocket.sendMessage(request.getAddress(), request.getPort(), "275-FILENOTUPLOADED");
	        		  }
	        	  } 
	        	  
	        	  else if(message.startsWith("300-DOWNLOADREQUEST")) {
	        		  try {	        			  
	        			 	        			  
	        			  String fileDirectory = "C:\\ServerFolders\\" + currentUser;
	        			  File userFolder = new File(fileDirectory);	        			  
	        			  File[] listOfFiles = userFolder.listFiles();
	        			  String filesAsString = "315-DOWNLOADREQUESTRECEIVED ";
	        			          			  
	        			  for(int i = 0; i < listOfFiles.length; i++) {
	        				 filesAsString += listOfFiles[i].getName() + ",";
	        			  }	        			  
	        			  System.out.println("Sending message");
	        			  mySocket.sendMessage(request.getAddress(), request.getPort(), filesAsString);
	        			  System.out.println("Message sent");	        			 
	        			  
	        			  System.out.println("receiving message");
	        			  String fileToDownload = mySocket.receiveMessage();
	        			  fileToDownload = fileToDownload.replace("325-DOWNLOADFILE", "").trim();
	        			  
	        			  System.out.println("received");        			  
	        			  fileToDownload = fileToDownload.trim();	        			  
	        			  Path locationOfFile = Paths.get(fileDirectory, fileToDownload);	        			  
	        			  byte[] fileAsByte = Files.readAllBytes(locationOfFile);	
	        			 
	        			  mySocket.sendMessage(request.getAddress(), request.getPort(), fileAsByte);
	        			  mySocket.sendMessage(request.getAddress(), request.getPort(), "350-DOWNLOADSUCCESSFUL");
	        		  } catch (FileNotFoundException e) {
	        			  mySocket.sendMessage(request.getAddress(), request.getPort(), "375-FILENOTFOUND");
	        		  }
	        	  } 
	        	  
	        	  else if(message.startsWith("400-LOGOUT")) {
	        		  
	        		  currentUser = "";
	        		  userLoggedIn = false;
	        		  mySocket.sendMessage(request.getAddress( ), request.getPort( ), "450-LOGOUTSUCCESSFUL");
	        	  }    
	        	  
	        	  else {
	        		  mySocket.sendMessage(request.getAddress( ), request.getPort( ), "700-INVALIDREQUEST");
	        	  }
	          }
	      } catch (IOException ex) {
	    	  System.out.println("Invalid input: error interpreting message");
		  } 
      }
   } 
}      
