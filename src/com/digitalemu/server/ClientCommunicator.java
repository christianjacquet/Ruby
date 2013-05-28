package com.digitalemu.server;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.net.Socket;

import com.digitalemu.gui.Monitor;
import com.digitalemu.ruby.Constants;

/**

 */
public class ClientCommunicator implements Runnable{

    protected Socket clientSocket = null;
    protected String serverText   = null;
    private ObjectInputStream objinput;
    private ObjectOutputStream objoutput;
    private Monitor monitor;
    private User user;
    byte clientSays[] = new byte[4];
    byte absPosition[] = new byte[12];
    long position;
    String username = "";
    String version = "";
    String ipAddress ="";
    String macAddress ="";
    String hostName ="";
    String message;
    int receivedbytes = 0;
    byte client_order_bye = (byte) 127;
    int userId = 0;
    Constants constants;
    boolean keepRunning = true;
    

    public ClientCommunicator(Socket clientSocket, String serverText) {
        this.clientSocket = clientSocket;
        this.serverText   = serverText;
        this.ipAddress = clientSocket.getInetAddress().getHostAddress();
        monitor = new Monitor("ClientCommunicator for "+this.ipAddress);
    }
    
    private String getString(){
    	String recString="";
    	try {
			recString = objinput.readUTF();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return recString;
    }
    
    public void setUserId(int userId){
    	this.userId=userId;
    	monitor.println("Received userId: "+userId);
    }
    
    public void sendCommand(int command){
    	try{
    		objoutput.write(command);
    		objoutput.flush();
    		monitor.println("Sent: "+command);
    	}
    	catch(IOException ioException){
    		ioException.printStackTrace();
    	}	
    }
    
    public void sendCommand(String message){
    	try{
    		objoutput.writeUTF(message);
    		objoutput.flush();
    	}
    	catch(IOException ioException){
    		ioException.printStackTrace();
    	}	
    }
    
    public void close(){
    	keepRunning = false;
    }
    
    public void run() {
        try {
            objinput = new ObjectInputStream(clientSocket.getInputStream());
            objoutput = new ObjectOutputStream(clientSocket.getOutputStream());
            //monitor.println("Waiting for client message...");
            do{
            	// Wait for order from client
				//receivedbytes = objinput.read(clientSays);
            	int clientOrder = objinput.read(); 
				switch (clientOrder){
					case 1 :  // Order Login
						username=getString(); 
						version=getString(); 
						hostName=getString();
						macAddress=getString();
						monitor.println("Login by "+username+" @ "+version);
				        Server.login(userId, username, version, macAddress, hostName);
						break;
					case 2 :  // order logout
						Server.logoff(userId);
						break;
					case 3 :  break;				
					default:
						monitor.println("Error. Unexpected order: "+clientOrder );
						break;
				}
				//monitor.println("From client ("+ receivedbytes +")" + clientSays[0]+" , "+clientSays[1]+" , "+clientSays[2]+" , "+clientSays[3]);
				//monitor.println("Waiting for client message...");
				//sendMessage(message);
				//Server.action(clientSays[0],clientSays[1],clientSays[2],clientSays[3]);
			}while(keepRunning);
            objoutput.close();
            objinput.close();
            monitor.close();
        } catch (IOException e) {
            //report exception somewhere.
            e.printStackTrace();
        }
    }
}