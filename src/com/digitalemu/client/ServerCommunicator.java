package com.digitalemu.client;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.digitalemu.gui.Monitor;
import com.digitalemu.ruby.Constants;

/**

 */
public class ServerCommunicator implements Runnable{

    private static Socket serverSocket = null;
    private static ObjectInputStream objinput;
    private static ObjectOutputStream objoutput;
    private static Monitor monitor;
    private static int port = 0;
    byte clientSays[] = new byte[4];
    byte absPosition[] = new byte[12];
    long position;
    String username = "";
    String version = "";
    static String ipAddress ="";
    String macAddress ="";
    String hostName ="";
    String message;
    int serverOrder = 0;
    byte client_order_bye = (byte) 127;
    int userId = 0;
    Constants constants = new Constants();
    boolean keepRunning = true;
    int errorCounter=0;
    
	public final int CLIENT_LOGIN_APPROVED = 3;
	public final int CLIENT_LOGIN_DENIED = 4;
    

    public ServerCommunicator(int port) {
    	setPort(port);
        monitor = new Monitor("ServerCommunicator");
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
    		monitor.println("Sent: "+message);
    	}
    	catch(IOException ioException){
    		ioException.printStackTrace();
    	}	
    }
    
    public void close(){
    	keepRunning = false;
    }
    

    
    private static void opensocket()
	{
		try{
			monitor.println("Connecting...");
			setServerSocket(new Socket("localhost", getPort()));
			monitor.println("Connected to localhost in port "+getPort());
			objoutput = new ObjectOutputStream(getServerSocket().getOutputStream());
			objinput = new ObjectInputStream(getServerSocket().getInputStream());
	        ipAddress = serverSocket.getInetAddress().getHostAddress();
		}
		catch(UnknownHostException unknownHost){
			System.err.println("You are trying to connect to an unknown host!");
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
    
    public void run() {
        try {           
        	opensocket();
        	monitor.println("Starts to listen");
            do{
				serverOrder = objinput.read(); 
				switch (serverOrder){
					case CLIENT_LOGIN_APPROVED :
						monitor.println("login approved");
						Client.loginApproved();
						break;
					case CLIENT_LOGIN_DENIED :
						String reason = getString();
						monitor.println("login denied");
						Client.loginDenied(reason);
						break;
					case 5 :  break;				
				default:
					errorCounter++;
					monitor.println("Error ("+errorCounter+") Unknown order from server: "+serverOrder);	
					break;
				}
			}while(keepRunning);
            objoutput.close();
            objinput.close();
            monitor.close();
        } catch (IOException e) {
            //report exception somewhere.
            e.printStackTrace();
        }
    }


	public static Socket getServerSocket() {
		return serverSocket;
	}

	public static void setServerSocket(Socket serverSocket) {
		ServerCommunicator.serverSocket = serverSocket;
	}

	public static int getPort() {
		return port;
	}

	public static void setPort(int port) {
		ServerCommunicator.port = port;
	}

}