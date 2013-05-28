package com.digitalemu.server;

import java.net.Socket;
import java.util.ArrayList;

import com.digitalemu.gui.Monitor;

public class Server implements Runnable{
	private static int serverPort = 25561;
	private static ArrayList<User> user = new ArrayList<User>();
	static Monitor monitor;
	private static Thread clientConnectorThread;
	private static ClientConnector clientConnector;
	
	public Server(){
		
	}
	
	public static int getPort(){
		return serverPort;
	}
	
	public void run(){
		monitor = new Monitor("Server");
		monitor.println("Starting ClientConnector");
		startClientConnector();		
	}
	
	private static String checkUser(User user){
		String userStatus="User IP banned";
		// TODO
		// Check user against blacklist
		// Check user against whitelist
		// Check user version
		// Check user name
		if (user.equals("uabjact")){
			return "approved";
		}
		else {
			return "approved";
		}
	}
	
	public synchronized static void action(byte user, byte action, byte data1, byte data2){
    	monitor.println("User:"+user+" action:"+action+" data1:"+data1+" data2:"+data2);
    }
	
	public synchronized static void userConnect(Socket clientSocket){
		User newUser = new User(clientSocket);
		user.add(newUser);
		int userId = user.lastIndexOf(newUser);
		newUser.setUserId(userId);
		newUser.setStatus(checkUser(newUser));
		monitor.println("UserId: "+userId+" total users: "+user.size());
	}
    
    public synchronized static void login(int userId, String username, String version, String macAddress, String hostName){
    	monitor.println("Login "+username+" ver: "+version+" host: "+hostName+" mac: "+macAddress);
    	user.get(userId).setHost(hostName);
    	user.get(userId).setMac(macAddress);
    	user.get(userId).setVersion(version);
    	user.get(userId).setName(username);
    	String userState = checkUser(user.get(userId));
    	if (userState.equals("approved")){
    		user.get(userId).approved();
    	}
    	else {
    		user.get(userId).denied(userState);
    	}
    }
    
    public synchronized static void logoff(int userId){
    	user.get(userId).clientCommunicatorClose();
    	user.remove(userId);
    	monitor.println("Removed UserId: "+userId+" total users: "+user.size());
    }
    
    public synchronized static void close(int userId){
    	user.remove(userId);
    	monitor.println("Removed UserId: "+userId+" total users: "+user.size());
    }

	
	private void startClientConnector(){
		clientConnector = new ClientConnector(serverPort);
		clientConnectorThread = new Thread(clientConnector);
		clientConnectorThread.start();
	}
}
