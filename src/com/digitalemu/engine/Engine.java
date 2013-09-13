package com.digitalemu.engine;

import org.newdawn.slick.opengl.Texture;

import com.digitalemu.gui.Monitor;
import com.digitalemu.monster.Player;
import com.digitalemu.world.GPS;
import com.digitalemu.world.Textures;
import com.digitalemu.world.World;

public class Engine {
	private TickEngine tickEngine;
	private Thread tickEngineThread;
	private OpenGL graphicsLoop;
	private Thread graphicsLoopThread;
	private KeyboardAndMouse keyboardAndMouse;
	private Thread keyboardAndMouseThread;
	private static Player player;
	private static Thread playerThread;
	private static World world;
	private static Textures texture;


	/** ************************ START ************************
	 * 
	 * This is the starting point for this game.
	 * Engine starts all services and processes
	 * and, when quit, closes them again.
	 * Nothing is done here while the game is running
	 * 
	 */
	private void run() {
		System.out.println("Engine start on thread "+Thread.currentThread());
		startGraphicsLoop();						// Create instance of OpenGL 
		//loadTextures();								// Load textures for materials, objects and huds
		//world = new World(1, 128,  texture);		// Create a world
		//startPlayer();								// Create player
		System.out.println("Keyboard and mouse listener AAb "+Thread.currentThread());
		graphicsLoop.startDisplay(false, world, player);		// Start OpenGL loop
		System.out.println("Keyboard and mouse listener AA "+Thread.currentThread());
		startKeyboardAndMouse();					// Start keyboard and mouse listener
		startTickEngine();							// Start system timer
		tickEngine.addMOvableEntity2Tick10(player); // Add player to timer
	}
	
	
	public static void main(String args[]) {
        Engine engine = new Engine();
        engine.run();
    }
	
	private void sleep(int ms){
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void loadTextures(){
		try {
			texture = graphicsLoop.init();		// Load textures
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void startPlayer(){
        player    = new Player("Christian", new GPS(9.5f, 2.4f, -5.5f, world));  // starting position of player
        playerThread = new Thread(player);
        playerThread.start();	
	}
	
	private void startGraphicsLoop(){
		graphicsLoop = new OpenGL();
		graphicsLoopThread = new Thread(graphicsLoop);
		graphicsLoopThread.start();	
	}

	private void startTickEngine(){
		tickEngine = new TickEngine();
		tickEngineThread = new Thread(tickEngine);
		tickEngineThread.start();	
	}
	
	private void startKeyboardAndMouse(){
		System.out.println("Keyboard and mouse listener a ");
		keyboardAndMouse = new KeyboardAndMouse(player);
		keyboardAndMouseThread = new Thread(keyboardAndMouse);
		keyboardAndMouseThread.start();	
	}
}
