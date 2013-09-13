package com.digitalemu.engine;

import static org.lwjgl.opengl.GL11.GL_LIGHT0;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_POSITION;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLight;

import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import com.digitalemu.monster.Player;
import com.digitalemu.world.GPS.ASWDdir;

public class KeyboardAndMouse implements Runnable{
	private static int lastused;
	private static float dx,dy,yaw,pitch;
	private static float timeDiff;
	private static float mouseSensitivity = 0.15f;
	private static Player player;
	private static long time;
	private static long time2;
	
	public KeyboardAndMouse(Player player){
		this.player=player;
		time = Sys.getTime();
		time2= time;
	}
	
	@Override
	public void run() {	
		System.out.println("Keyboard and mouse listener running on "+Thread.currentThread());
	}
	
	public static void Tick(){
		time = Sys.getTime();
		Keyboard.poll();
		checkKeyboard();
		System.out.print(".");
		time2 = time;
	}

	private static void checkKeyboard(){
    if(Keyboard.isKeyDown(Keyboard.KEY_ADD)) {       // Exit if Escape is pressed
    }
    
    
    
    if(Keyboard.isKeyDown(Keyboard.KEY_SUBTRACT)) {       // Exit if Escape is pressed
    }
    if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {       // Exit if Escape is pressed
    }
    if(Display.isCloseRequested()) {                     // Exit if window is closed
    }
    lastused=99;
    timeDiff = (time-time2);
    //distance = detectCollision(position,distance, 2);
    //if (distance>0.9f){ msg("Distance exceeded : "+distance); distance = 0.9f; }
    if((Keyboard.isKeyDown(Keyboard.KEY_D))&& (lastused!=1)){ player.moveEntity(timeDiff, yaw, pitch, ASWDdir.RIGHT); lastused=1;}
    if((Keyboard.isKeyDown(Keyboard.KEY_Q))&& (lastused!=2)){ player.moveEntity(timeDiff, yaw, pitch, ASWDdir.UP); lastused=2; }
    if((Keyboard.isKeyDown(Keyboard.KEY_S))&& (lastused!=3)){ player.moveEntity(timeDiff, yaw, pitch, ASWDdir.BACKWARD); lastused=3 ; }
    if((Keyboard.isKeyDown(Keyboard.KEY_A))&& (lastused!=4)){ player.moveEntity(timeDiff, yaw, pitch, ASWDdir.LEFT); lastused=4;}
    if((Keyboard.isKeyDown(Keyboard.KEY_E))&& (lastused!=5)){ player.moveEntity(timeDiff, yaw, pitch+90, ASWDdir.DOWN); lastused=5; }
    if((Keyboard.isKeyDown(Keyboard.KEY_W))&& (lastused!=6)){ player.moveEntity(timeDiff, yaw, pitch, ASWDdir.FORWARD); lastused=6; }
    if((Keyboard.isKeyDown(Keyboard.KEY_NUMPAD7))&& (lastused!=7)){ yaw++; lastused=7; }
    if((Keyboard.isKeyDown(Keyboard.KEY_NUMPAD8))&& (lastused!=8)){ yaw--; lastused=8; }
    if((Keyboard.isKeyDown(Keyboard.KEY_NUMPAD9))&& (lastused!=9)){ pitch++; lastused=9; }
    if((Keyboard.isKeyDown(Keyboard.KEY_NUMPAD0))&& (lastused!=0)){ pitch--; lastused=0; }
    if((Keyboard.isKeyDown(Keyboard.KEY_NUMPAD1))&& (lastused!=1)){ glEnable(GL_LIGHTING); lastused=1; }   
    if((Keyboard.isKeyDown(Keyboard.KEY_I))&& (lastused!=98)){ 
    	lastused=98; 
    }

    //distance in mouse movement from the last getDX() call.
    dx = Mouse.getDX();
    //distance in mouse movement from the last getDY() call.
    dy = Mouse.getDY();

    //controll camera yaw from x movement fromt the mouse
    yaw += (dx * mouseSensitivity);
    if (yaw>360){yaw-=360;}
    if (yaw<0){yaw+=360;}
    
    //controll camera pitch from y movement from the mouse
    pitch-=(dy * mouseSensitivity);
    if (pitch>90){pitch=90;}
    if (pitch<-90){pitch=-90;}
	}

}
