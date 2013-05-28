package com.digitalemu.server;


import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

import com.digitalemu.gui.Monitor;

// This is a multithreaded io server
// This class handles client connection requests
// A new ClientCommunicator thread is started for each new client
public class ClientConnector implements Runnable{

    protected int          serverPort   = 2004;
    protected ServerSocket serverSocket = null;
    protected boolean      isStopped    = false;
    protected Thread       runningThread= null; 
    static Monitor monitor;

    public ClientConnector(int serverPort){
    	this.serverPort = serverPort;
    }

    public void run(){
    	monitor = new Monitor("ClientConnector");
        synchronized(this){
            this.runningThread = Thread.currentThread();
        }
        openServerSocket();
        while(! isStopped()){
            Socket clientSocket = null;
            try {
                clientSocket = this.serverSocket.accept();
                monitor.println("Connection from: "+clientSocket.getInetAddress().getHostAddress());
            } catch (IOException e) {
                if(isStopped()) {
                    monitor.println("Server Stopped.") ;
                    return;
                }
                throw new RuntimeException(
                    "Error accepting client connection", e);
            }
            monitor.println("Tell Server "+clientSocket.getInetAddress().getHostAddress()+" is connected.");
            Server.userConnect(clientSocket);
        }
        monitor.println("Server Stopped.") ;
    }

    public synchronized void stopClientCommunicator(ClientCommunicator clientCommunicator){
    	
    }
    
    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop(){
    	monitor.println("Got order to close...");
        this.isStopped = true;
        //monitor.close();
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }

    private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(this.serverPort);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port 8080", e);
        }
    }

}