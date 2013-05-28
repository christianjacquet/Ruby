package com.digitalemu.client;
import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.Scanner;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import com.digitalemu.gui.Monitor;
import com.digitalemu.opengl.Graphics2d;
import com.digitalemu.opengl.Voxel;
import com.digitalemu.opengl.Window;
import com.digitalemu.ruby.Constants;
import com.digitalemu.server.ClientConnector;
import com.digitalemu.server.Server;
public class Client{
	//static String ipAddress="";
	static String hostName="";
	static String macAddress="";
	static Socket requestSocket;
	static ObjectOutputStream out;
	static ObjectInputStream in2;
 	String message;
 	static Scanner userInput = new Scanner(System.in);
 	static long lDateTime1;
	static long lDateTime2 = new Date().getTime();
	static int serverPort = 2004;
	static byte[] buf = new byte[4];
	static Constants constants = new Constants();
	private static ServerCommunicator serverCommunicator;
	private static Monitor monitor;
	private static Window window;
	

	
	public static void msg(String msg){
		lDateTime1 = new Date().getTime();
		System.out.format("%,8d", (lDateTime1-lDateTime2));
	    System.out.println(" : "+msg);
	    lDateTime2 = lDateTime1;
	}
	

	public static void loginDenied(String reason){
		monitor.println("Login Denied: "+reason);
	}
	
	public static void loginApproved(){
		monitor.println("Logged in");
	}
	

	private static void getHostData(){
		try {
		    InetAddress addr = InetAddress.getLocalHost();
		    NetworkInterface ni = NetworkInterface.getByInetAddress(addr);
		    byte[] mac = ni.getHardwareAddress();
		    for (int i = 0; i < mac.length; i++) { 
		      macAddress += String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : "");
		    }
		    hostName = addr.getHostName();
		} catch (UnknownHostException e) {
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void sleep1s(){
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private static void startServer(){
		monitor.println("Starting server");
		Thread serverThread = new Thread(new Server(), "thread1");
		serverThread.start();
		sleep1s();
	}
	
	private static void logOn(String name){
		serverCommunicator = new ServerCommunicator(Server.getPort());
		Thread serverCommunicatorThread = new Thread(serverCommunicator);
		serverCommunicatorThread.start();
		getHostData();
		sleep1s();
		serverCommunicator.sendCommand(constants.SERVER_LOGIN_REQUEST);
		serverCommunicator.sendCommand(name);
		serverCommunicator.sendCommand(constants.version);
		serverCommunicator.sendCommand(hostName);
		serverCommunicator.sendCommand(macAddress);
		monitor.println("mac:"+macAddress+" host:"+hostName);
	}
	
	private static void logOff(String name){
		serverCommunicator.sendCommand(constants.SERVER_LOGOUT_REQUEST);
		monitor.println("Sent logOff request for: "+name);
	}
	
	public static void main(String[] args) {
		monitor = new Monitor("Client");
		window = new Window(false);
		Texture tex[] = Graphics2d.loadTextureMap("res/terrain.png",16,16);

		byte b0=0;
		Scanner in = new Scanner(System.in);		
		do{
			System.out.print(" Enter line: ");
			b0 = in.nextByte();
			switch (b0){
				case 1:
					startServer();
					break;
				case 2:
					logOn("uabjact");
					break;
				case 3:
					logOn("kraxxe");
					break;
				case 4:
					logOff("uabjact");
					break;
				default:
					break;
			}
	        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT); // Clear The Screen And The Depth Buffer
	        GL11.glLoadIdentity(); // Reset The Current Modelview Matrix
			Voxel.render(0, 0, -5, 3, tex);
			Voxel.render(0, 0, -3, 4, tex);
			Voxel.render(0, 0, -1, 5, tex);
			Display.update();
		}
		while(!(b0==127));
		msg("Try to stop...");
		serverCommunicator.close();
		sleep1s();
	}
	
	

}