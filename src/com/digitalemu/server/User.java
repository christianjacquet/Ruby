package com.digitalemu.server;

import java.net.Socket;

import com.digitalemu.gui.Monitor;
import com.digitalemu.ruby.Constants;

public class User {
	private String name ="";
	private String ip = "";
	private String mac = "";
	private String host = "";
	private String version = "";
	private String status = "";
	private int userId = 0;
	private Socket clientSocket;
	private ClientCommunicator clientCommunicator;
	private Thread clientCommunicatorThread;
	private Monitor monitor;
	Constants constants = new Constants();
	
	public User(Socket clientSocket){
		this.ip = clientSocket.getInetAddress().getHostName().toString();
		this.clientSocket = clientSocket;
		monitor = new Monitor("User @ "+this.ip);
        monitor.println("Started ClientCommunicator");
		clientCommunicator = new ClientCommunicator(clientSocket, "Multithreaded Server");
        clientCommunicatorThread = new Thread(clientCommunicator);
        clientCommunicatorThread.start();
	}
	
	public void approved(){
		// TODO Tell client user is approved
		monitor.println("User approved");
		clientCommunicator.sendCommand(constants.CLIENT_LOGIN_APPROVED);
	}
	
	public void clientCommunicatorClose(){
		clientCommunicator.close();
		monitor.close();
		Server.close(userId);		
	}
	
	public void denied(String userStatus){
		// TODO Tell client user was denied access
		clientCommunicator.sendCommand(constants.CLIENT_LOGIN_DENIED);
		clientCommunicator.sendCommand(userStatus);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		clientCommunicator.close();
		Server.close(userId);		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
        monitor.println("Set username: "+this.name);
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
		monitor.println("Set mac: "+this.mac);
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
		monitor.println("Set host: "+this.host);
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
		monitor.println("Set version: "+this.version);
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
		monitor.println("Set userId: "+userId);
		clientCommunicator.setUserId(userId);
	}



	public String getStatus() {
		return status;
	}



	public void setStatus(String status) {
		this.status = status;
	}



	public ClientCommunicator getClientCommunicator() {
		return clientCommunicator;
	}



	public void setClientCommunicator(ClientCommunicator clientCommunicator) {
		this.clientCommunicator = clientCommunicator;
	}
}
