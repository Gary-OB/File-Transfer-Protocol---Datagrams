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
	
	static int serverPort = 7;   
	static MyServerDatagramSocket mySocket; 
	static DatagramMessage request;
	
    static String currentUser = "";
    static boolean userLoggedIn = false;
    
    public static void main(String[] args) throws SocketException {
      
    	mySocket = new MyServerDatagramSocket(serverPort);
    	  
    	if (args.length == 1 )
	         serverPort = Integer.parseInt(args[0]); 
      
      	while(true) {
	      	try {	    	  
	    	      	  
	    	  	System.out.println("Echo server ready.");  
 		  
	          	request = mySocket.receiveMessageAndSender();
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
	        		  		upload(message);	        			  	
	        		  	} catch (FileNotFoundException e) {
	        			  	mySocket.sendMessage(request.getAddress(), request.getPort(), "275-FILENOTUPLOADED");
	        		  	}
	        	  	} 
	        	  
	        	  	else if(message.startsWith("300-DOWNLOADREQUEST")) {
	        		  	try {	        			  
	        			 	download();        			  	        			  	
	        		  	} catch (FileNotFoundException e) {
	        			  	mySocket.sendMessage(request.getAddress(), request.getPort(), "375-FILENOTFOUND");
	        		  	}
	        	  	} 
	        	  
	        	  	else if(message.startsWith("400-LOGOUT")) {       		  
	        		  	logout();
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
    
    public static void upload(String message) throws IOException{
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
    }
    
    public static void download() throws IOException{
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
	  	Path locationOfFile = Paths.get(fileDirectory, fileToDownload);	        			  
	  	byte[] fileAsByte = Files.readAllBytes(locationOfFile);	
	 
	  	mySocket.sendMessage(request.getAddress(), request.getPort(), fileAsByte);
	  	mySocket.sendMessage(request.getAddress(), request.getPort(), "350-DOWNLOADSUCCESSFUL");
    }
    
    public static void logout() throws IOException{
    	currentUser = "";
	  	userLoggedIn = false;
	  	mySocket.sendMessage(request.getAddress( ), request.getPort( ), "450-LOGOUTSUCCESSFUL");
    }
}      
