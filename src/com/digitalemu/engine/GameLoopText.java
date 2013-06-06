package com.digitalemu.engine;


import static org.lwjgl.opengl.GL11.GL_COMPILE;
import static org.lwjgl.opengl.GL11.glEndList;
import static org.lwjgl.opengl.GL11.glGenLists;
import static org.lwjgl.opengl.GL11.glNewList;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Collections;

import javax.imageio.ImageIO;
import org.lwjgl.BufferUtils;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.opengl.Texture;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.ARBVertexBufferObject.*;
import com.digitalemu.gui.Monitor;
import com.digitalemu.world.Material;

import com.digitalemu.world.World;
import com.digitalemu.world.GPS;
import com.digitalemu.world.Textures;
import com.digitalemu.world.Chunk3;
import com.digitalemu.world.World.DlistGps;
import com.digitalemu.text.TrueTypeFont;


public class GameLoopText {
	private static final String String = null;
	private World world;
    private boolean done = false;
    private boolean fullscreen = false;
    private final String windowTitle = "MyCraft";
    private boolean f1 = false;
    private DisplayMode displayMode;
    private int textureIndex = 1;
    public  float walkSpeed = 8.0f;
    private int what2display=8;
    private boolean keyadd=false;    
    private int windowHeight = 800;
    private int windowWidth = 1280;
	private long time;
	private long time2;
	private long seconds;
	private float elapsedTime;
	private float render3d;
	private float render2da;
	private float render2db;
	private float render2dc;
	private float render2dd;
	private int fps=0;
	private long ramTotal=0;
	private long ramFree=0;
	private long ramUsed=0;
	private int lookAtDistance;
	private TrueTypeFont trueTypeFont;
	int lastused=0;
	private final String[] VIEWs={"TOP","BOT","NORTH","SOUTH","EAST","WEST","ALL","OLD","Dummy"};	
	float dx        = 0.0f;
    float dy        = 0.0f;
    float dt        = 0.0f; //length of frame
    float mouseSensitivity = 0.15f;
    float movementSpeed = 10.0f; //move 10 units per second	
    float distance=0.0f;
	//3d vector to store the camera's position in
    private Vector3f    position    = new Vector3f(32.0f, 5.0f, 32.0f);  // starting position of player
    private Vector3f    position2    = new Vector3f(32.0f, 0.0f, 0.0f);
    private Vector3f    collision  = new Vector3f();
    private Vector3f    lastpos  = new Vector3f();
    //the rotation around the Y axis of the camera
    private float       yaw         = 0.0f;
    //the rotation around the X axis of the camera
    private float       pitch       = 0.0f;
    private Textures textureMaterials;
    private Textures textureContainerHUD;
    private Textures textureCraftingHUD;
    private Textures textureInventoryHUD;
    private Textures textureFurnaceHUD;
    private Textures textureConsoleHUD;
	//----------- Variables added for Lighting Test -----------//
	private FloatBuffer matSpecular;
	private FloatBuffer lightPosition;
	private FloatBuffer whiteLight; 
	private FloatBuffer lModelAmbient;
	private int lx=0,ly=0,lz=0;
	static Monitor monitor;
	static Monitor stats;
	private String message ="";
	private enum hudPos {UpperLeft, UpperCenter, UpperRight, CenterLeft, Center, CenterRight, DownLeft, DownCenter, DownRight};
	private static final int BYTES_PER_PIXEL = 4;
	private DlistGps[] dlistGPS;
	private Vector3f lookAtVoxel = new Vector3f(0,0,0);
	private static float radx, rady, radz, distx, distz, disty;		
	float diffx;
	float diffz;
	float diffy;
	float difft;
	static int addX, addY, addZ;
	private GPS lookAtGPS = new GPS(0,0,0);
	private GPS lookAtGPS2 = new GPS(0,0,0);
	private short lookAtMaterial = Material.m_null;
	private short lookAtMaterial2 = Material.m_null;
	private boolean lookAtMaterialFound=false;
	private boolean lookAtMaterialFound2=false;


	BufferedImage test = new BufferedImage(400, 400, BufferedImage.TYPE_INT_ARGB);
	Graphics2D g2d = test.createGraphics();

	
    

	public static void msg(String message){
		monitor.println(message);
	}
	
	
    public static void main(String args[]) {
    	monitor = new Monitor("Server");
		msg("Starting Mycraft");
		stats = new Monitor("stats");
        boolean fullscreen = false;
        if(args.length>0) {
            if(args[0].equalsIgnoreCase("fullscreen")) {
                fullscreen = true;
            }
        }
        GameLoopText l6 = new GameLoopText();
        l6.run(fullscreen);
    }
    
    public int compileInventoryDisplayList(){
        int oglDisplayList = glGenLists(1);
        glNewList(oglDisplayList, GL_COMPILE);{
            //glTranslatef((float) displayMode.getWidth() / 2.0f, (float) displayMode.getHeight() / 3.0f, 0.0f);
        	Texture tex = textureInventoryHUD.getTexture();
        	glTranslatef((float) displayMode.getWidth() / 2.0f, 768.0f, 0.0f);
        	msg("tex height: "+tex.getImageHeight());
            glEnable(GL_TEXTURE_2D);
            
            tex.bind(); // newdawn.slick (same library for my whole program, so this works)

            float hw = tex.getTextureWidth(); // half width
            float hh = tex.getTextureHeight(); // half height

            Vector2f _texPosUpLeft = new Vector2f(0, 0);
            Vector2f _texPosDownRight = new Vector2f(_texPosUpLeft.x + hw, _texPosUpLeft.y + hh);

            _texPosUpLeft.x /= tex.getTextureWidth();
            _texPosUpLeft.y /= tex.getTextureHeight();
            _texPosDownRight.x /= tex.getTextureWidth();
            _texPosDownRight.y /= tex.getTextureHeight();

            glColor3f(1, 1, 1); // Changes this doesn't make any effect
            glBegin(GL_QUADS);
            glTexCoord2f(_texPosUpLeft.x, _texPosUpLeft.y);
            glVertex2f(-hw, -hh);
            glTexCoord2f(_texPosDownRight.x, _texPosUpLeft.y);
            glVertex2f(hw, -hh);
            glTexCoord2f(_texPosDownRight.x, _texPosDownRight.y);
            glVertex2f(hw, hh);
            glTexCoord2f(_texPosUpLeft.x, _texPosDownRight.y);
            glVertex2f(-hw, hh);
        }
        glEnd();
        glEndList();
        return oglDisplayList;
    }

    
    public void renderInventory()
    {
        //Configuration conf = Game.getInstance().getConfiguration();
//      GL11.glMatrixMode(GL_PROJECTION);
//    	GL11.glLoadIdentity();
//    	GLU.gluOrtho2D(0.0f, (float) displayMode.getWidth(),(float) displayMode.getHeight(), 0.0f);
//    	GL11.glMatrixMode(GL_MODELVIEW);
//    	GL11.glLoadIdentity();
//    	GL11.glTranslatef(0.375f, 0.375f, 0.0f);
//    	GL11.glDisable(GL_DEPTH_TEST);

        glTranslatef((float) displayMode.getWidth() / 2.0f, (float) displayMode.getHeight() / 2.0f, 0.0f);

        glEnable(GL_TEXTURE_2D);
        Texture tex = textureConsoleHUD.getTexture();

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getTextureID()); // Select Our Texture
        //tex.bind(); // newdawn.slick (same library for my whole program, so this works)

        float hw = tex.getTextureWidth()/2; 
        float hh = tex.getTextureHeight()/2; 

        Vector2f _texPosUpLeft = new Vector2f(0, 0);
        Vector2f _texPosDownRight = new Vector2f(_texPosUpLeft.x + hw, _texPosUpLeft.y + hh);

        _texPosUpLeft.x /= hw;
        _texPosUpLeft.y /= hh;
        _texPosDownRight.x /= hw;
        _texPosDownRight.y /= hh;
        //Fmsg("u.x "+_texPosUpLeft.x+" u.y "+_texPosUpLeft.y+" d.x "+_texPosDownRight.x+" d.y "+_texPosDownRight.y+" hw "+hw+" hh"+hh);
        glColor3f(1, 1, 1); // Changes this doesn't make any effect
        glBegin(GL_QUADS);
        glTexCoord2f(_texPosUpLeft.x, _texPosUpLeft.y);
        glVertex2f(-hw, -hh);
        glTexCoord2f(_texPosDownRight.x, _texPosUpLeft.y);
        glVertex2f(hw, -hh);
        glTexCoord2f(_texPosDownRight.x, _texPosDownRight.y);
        glVertex2f(hw, hh);
        glTexCoord2f(_texPosUpLeft.x, _texPosDownRight.y);
        glVertex2f(-hw, hh);
        glEnd();
    }
    
    public static int loadTexture(BufferedImage image){
        
        int[] pixels = new int[image.getWidth() * image.getHeight()];
          image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

          ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * BYTES_PER_PIXEL); //4 for RGBA, 3 for RGB
          
          for(int y = 0; y < image.getHeight(); y++){
              for(int x = 0; x < image.getWidth(); x++){
                  int pixel = pixels[y * image.getWidth() + x];
                  buffer.put((byte) ((pixel >> 16) & 0xFF));     // Red component
                  buffer.put((byte) ((pixel >> 8) & 0xFF));      // Green component
                  buffer.put((byte) (pixel & 0xFF));               // Blue component
                  buffer.put((byte) ((pixel >> 24) & 0xFF));    // Alpha component. Only for RGBA
              }
          }

          buffer.flip(); //FOR THE LOVE OF GOD DO NOT FORGET THIS

          // You now have a ByteBuffer filled with the color data of each pixel.
          // Now just create a texture ID and bind it. Then you can load it using 
          // whatever OpenGL method you want, for example:

        int textureID = glGenTextures(); //Generate texture ID
          glBindTexture(GL_TEXTURE_2D, textureID); //Bind texture ID
          
          //Setup wrap mode
          glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
          glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

          //Setup texture scaling filtering
          glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
          glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
          
          //Send texel data to OpenGL
          glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
        
          //Return the texture ID so we can bind it later again
        return textureID;
     }
    
    public int belowThis(Vector3f vector3f){
    	Vector3f temp = new Vector3f(vector3f.x, vector3f.y-1, vector3f.z);
        return world.getVoxel(toGPS(temp));
    }
    
    public void drawCrossAim(){

    	GL11.glColor3f(0.0f, 1.0f, 1.0f);
        GL11.glBegin(GL11.GL_LINES);
        GL11.glLineWidth(25.0f);
        GL11.glVertex2f( windowWidth/2,windowHeight/2-50);
        GL11.glVertex2f(windowWidth/2,windowHeight/2+50);
        GL11.glVertex2f(windowWidth/2-50,windowHeight/2);
        GL11.glVertex2f(windowWidth/2+50,windowHeight/2);
        GL11.glEnd();
    }
    
    public void showstats(){
    	stats.clear();
        stats.println("World: "+dlistGPS.length); 
        stats.println("FPS: "+fps);
        stats.println("Looptime: "+elapsedTime);
        stats.println("Render3D: "+render3d);
        stats.println("Render2D: "+render2da+" - "+render2db+" - "+render2dc+" - "+render2dd);
        stats.println("X: "+position.x);
        stats.println("Y: "+position.y);
        stats.println("Z: "+position.z);
        stats.println("Yaw: "+(int)yaw+" Pitch: "+(int)pitch);
        stats.println("Yaw.x: "+(float)Math.sin(Math.toRadians(yaw))+" Yaw.y: "+(float)Math.cos(Math.toRadians(yaw)));
        stats.println("Below me: "+(belowThis(position)));
        stats.println("RAM used: "+ramUsed+" free: "+ramFree+" total: "+ramTotal);
        stats.println("X: "+lookAtGPS.getX()+" Y: "+lookAtGPS.getY()+" Z: "+lookAtGPS.getZ()+ " M: "+lookAtMaterial+" F: "+lookAtMaterialFound);
        stats.println("Disrance: "+lookAtDistance);
        stats.println("radx: "+radx+" rady: "+rady+" radz; "+radz);
        stats.println("difx: "+diffx+" dify: "+diffy+" difz: "+diffz+" t: "+difft);
    }
    
    public void simpletext(){
    	
    	//BufferedImage 
//    	test = new BufferedImage(400, 400, BufferedImage.TYPE_INT_ARGB);
//    	Graphics2D g2d = test.createGraphics();

    	g2d.clearRect(0, 0, 400, 400);
    	//GL11.glColor3f(0.0f, 0.0f, 1.0f);
    	g2d.setColor(new Color(0.0f, 0.0f, 0.0f, 1.0f));
    	g2d.fillRect(100, 100, 400, 400);
    

//    	g2d.setColor(new Color(0.0f, 0.0f, 0.0f, 0.3f));
//        g2d.fillRect(000, 000, 400, 400); //A transparent white background
//        g2d.fillRect(000, 000, 400, 400); //A transparent white background
//        g2d.fillRect(000, 000, 400, 400); //A transparent white background
//        g2d.fillRect(000, 000, 400, 400); //A transparent white background
//        g2d.fillRect(000, 000, 400, 400); //A transparent white background
//        g2d.fillRect(000, 000, 400, 400); //A transparent white background
//
//        g2d.fillRect(000, 000, 400, 400); //A transparent white background


        // 0ms
//        g2d.setColor(Color.red);
//        g2d.drawRect(0, 0, 400, 400); //A red frame around the image
//        g2d.fillRect(10, 10, 10, 10); //A red box 

    	
        // 0ms
        g2d.setColor(Color.WHITE);
    	GL11.glColor3f(0.0f, 1.0f, 1.0f);

        //Font font = new Font("Arial", Font.BOLD, 20);
        
        //g2d.setFont(font);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.drawString("World: "+dlistGPS.length, 10, 16); 
        g2d.drawString("FPS: "+fps, 10, 32);
        g2d.drawString("Looptime: "+elapsedTime, 60, 32);
        g2d.drawString("Render3D: "+render3d, 10, 48);
        g2d.drawString("Render2D: "+render2da+" - "+render2db+" - "+render2dc+" - "+render2dd, 10, 64);
        g2d.drawString("X: "+position.x, 10, 80);
        g2d.drawString("Y: "+position.y, 10, 96);
        g2d.drawString("Z: "+position.z, 10, 112);
        g2d.drawString("Yaw: "+(int)yaw+" Pitch: "+(int)pitch, 10, 128);
        g2d.drawString("Yaw.x: "+(float)Math.sin(Math.toRadians(yaw))+" Yaw.y: "+(float)Math.cos(Math.toRadians(yaw)), 10, 144);
        g2d.drawString("Below me: "+(belowThis(position)), 10, 160);
        g2d.drawString("RAM used: "+ramUsed+" free: "+ramFree+" total: "+ramTotal, 10, 176);
        g2d.drawString("X: "+lookAtGPS.getX()+" Y: "+lookAtGPS.getY()+" Z: "+lookAtGPS.getZ()+ " M: "+lookAtMaterial+" F: "+lookAtMaterialFound, 10, 192);
        g2d.drawString("X: "+lookAtGPS2.getX()+" Y: "+lookAtGPS2.getY()+" Z: "+lookAtGPS2.getZ()+ " M: "+lookAtMaterial2+" F: "+lookAtMaterialFound2, 10, 208);
        g2d.drawString("Distance: "+lookAtDistance, 10, 224);
        g2d.drawString("radx: "+radx+" rady: "+rady+" radz; "+radz,10,240);
        g2d.drawString("difx: "+diffx+" dify: "+diffy+" difz: "+diffz+" t: "+difft,10,256);

        int textureID = loadTexture(test); // <---- This stupid call takes 17ms
		render2dd = (Sys.getTime()-time);


        glEnable(GL_TEXTURE_2D); //Enable texturing
        
        

           //glClear(GL_COLOR_BUFFER_BIT);
           
           //Enable blending so the green background can be seen through the texture
           glEnable(GL_BLEND);
           glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
           
           glPushMatrix();
           glTranslatef(2, 2, 0);
           glBindTexture(GL_TEXTURE_2D, textureID);
           glBegin(GL_QUADS);
           {
              glTexCoord2f(0, 0);
              glVertex2f(0, 0);
              
              glTexCoord2f(1, 0);
              glVertex2f(400, 0);
              
              glTexCoord2f(1, 1);
              glVertex2f(400, 400);
              
              glTexCoord2f(0, 1);
              glVertex2f(0, 400);
           }
           glEnd();
    }
    
    public void renderTexture(Texture tex, hudPos pos)
    {
        glTranslatef((float) displayMode.getWidth() / 2.0f, (float) displayMode.getHeight() / 2.0f, 0.0f);
        glEnable(GL_TEXTURE_2D);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getTextureID()); // Select Our Texture
		int dw = displayMode.getWidth()/2;
		int dh = displayMode.getHeight()/2;
		int tw = tex.getTextureWidth();
		int th = tex.getTextureHeight();
		int x=0;
		int y=0;
		switch (pos) {
			case UpperLeft : 	x=0-dw; 	y=0-dh; 	break;
			case UpperCenter : 	x=0-tw/2; 	y=0-dh; 	break;
			case UpperRight : 	x=dw-tw; 	y=0-dh; 	break;
			case CenterLeft : 	x=0-dw; 	y=0-th/2; 	break;
			case Center : 		x=-0-tw/2; 	y=0-th/2; 	break;
			case CenterRight : 	x=dw-tw; 	y=0-tw/2; 	break;
			case DownLeft : 	x=0-dw; 	y=dh-th; 	break;
			case DownCenter : 	x=0-tw/2; 	y=dh-th; 	break;
			case DownRight : 	x=dw-tw; 	y=dh-th; 	break;        	
		}
        Vector2f _texPosUpLeft = new Vector2f(0, 0);
        Vector2f _texPosDownRight = new Vector2f(tw, th);
        _texPosUpLeft.x = 0.0f;
        _texPosUpLeft.y = 0.0f;
        _texPosDownRight.x = 1.0f;
        _texPosDownRight.y = 1.0f;        
        glBegin(GL_QUADS);
        glTexCoord2f(_texPosUpLeft.x, _texPosUpLeft.y);
        glVertex2f(x, y);
        glTexCoord2f(_texPosDownRight.x, _texPosUpLeft.y);
        glVertex2f(x+th, y);
        glTexCoord2f(_texPosDownRight.x, _texPosDownRight.y);
        glVertex2f(x+th, y+tw);
        glTexCoord2f(_texPosUpLeft.x, _texPosDownRight.y);
        glVertex2f(x, y+tw);
        glEnd();
    }
    
    
    public void drawLine(GPS a) {
    	drawLine(new Vector3f(a.getX(), a.getY(),a.getZ()));
    }
    
    public void drawWireframe(GPS a) {
    	drawVoxel(new Vector3f(a.getX(), a.getY(),a.getZ()));
    }
    
    public void drawVoxel(Vector3f a) { 
    	a.setY(a.getY()+1);
    	//a.setX(a.getX()-1);
        GL11.glBegin(GL11.GL_LINES);
        GL11.glLineWidth(10.0f);
        // Top
        GL11.glVertex3f(a.x-0.1f, a.y+0.1f, a.z+0.1f);
        GL11.glVertex3f(a.x-0.1f, a.y+0.1f, a.z+0.9f);        
        GL11.glVertex3f(a.x+0.9f, a.y+0.1f, a.z+0.1f);
        GL11.glVertex3f(a.x+0.9f, a.y+0.1f, a.z+0.9f);
        GL11.glVertex3f(a.x-0.1f, a.y+0.1f, a.z+0.1f);
        GL11.glVertex3f(a.x+0.9f, a.y+0.1f, a.z+0.1f);
        GL11.glVertex3f(a.x-0.1f, a.y+0.1f, a.z+0.9f);
        GL11.glVertex3f(a.x+0.9f, a.y+0.1f, a.z+0.9f);
        // Bottom
        GL11.glVertex3f(a.x+0.1f, a.y-1.1f, a.z+0.1f);
        GL11.glVertex3f(a.x+0.1f, a.y-1.1f, a.z+0.9f);        
        GL11.glVertex3f(a.x+0.9f, a.y-1.1f, a.z+0.1f);
        GL11.glVertex3f(a.x+0.9f, a.y-1.1f, a.z+0.9f);
        GL11.glVertex3f(a.x+0.1f, a.y-1.1f, a.z+0.1f);
        GL11.glVertex3f(a.x+0.9f, a.y-1.1f, a.z+0.1f);
        GL11.glVertex3f(a.x+0.1f, a.y-1.1f, a.z+0.9f);
        GL11.glVertex3f(a.x+0.9f, a.y-1.1f, a.z+0.9f);
        // Side ?
        GL11.glVertex3f(a.x-0.1f, a.y-0.1f, a.z+0.1f);
        GL11.glVertex3f(a.x-0.1f, a.y-0.1f, a.z+0.9f);        
        GL11.glVertex3f(a.x-0.1f, a.y-0.9f, a.z+0.1f);
        GL11.glVertex3f(a.x-0.1f, a.y-0.9f, a.z+0.9f);
        GL11.glVertex3f(a.x-0.1f, a.y-0.1f, a.z+0.1f);
        GL11.glVertex3f(a.x-0.1f, a.y-0.9f, a.z+0.1f);
        GL11.glVertex3f(a.x-0.1f, a.y-0.1f, a.z+0.9f);
        GL11.glVertex3f(a.x-0.1f, a.y-0.9f, a.z+0.9f);
        // Side ?
        GL11.glVertex3f(a.x+1.1f, a.y-0.1f, a.z+0.1f);
        GL11.glVertex3f(a.x+1.1f, a.y-0.1f, a.z+0.9f);        
        GL11.glVertex3f(a.x+1.1f, a.y-0.9f, a.z+0.1f);
        GL11.glVertex3f(a.x+1.1f, a.y-0.9f, a.z+0.9f);
        GL11.glVertex3f(a.x+1.1f, a.y-0.1f, a.z+0.1f);
        GL11.glVertex3f(a.x+1.1f, a.y-0.9f, a.z+0.1f);
        GL11.glVertex3f(a.x+1.1f, a.y-0.1f, a.z+0.9f);
        GL11.glVertex3f(a.x+1.1f, a.y-0.9f, a.z+0.9f);
        // Front
        GL11.glVertex3f(a.x+0.1f, a.y-0.1f, a.z-0.1f);
        GL11.glVertex3f(a.x+0.9f, a.y-0.1f, a.z-0.1f);        
        GL11.glVertex3f(a.x+0.1f, a.y-0.9f, a.z-0.1f);
        GL11.glVertex3f(a.x+0.9f, a.y-0.9f, a.z-0.1f);
        GL11.glVertex3f(a.x+0.1f, a.y-0.1f, a.z-0.1f);
        GL11.glVertex3f(a.x+0.1f, a.y-0.9f, a.z-0.1f);
        GL11.glVertex3f(a.x+0.9f, a.y-0.1f, a.z-0.1f);
        GL11.glVertex3f(a.x+0.9f, a.y-0.9f, a.z-0.1f);
        // Back
        GL11.glVertex3f(a.x+0.1f, a.y-0.1f, a.z+1.1f);
        GL11.glVertex3f(a.x+0.9f, a.y-0.1f, a.z+1.1f);        
        GL11.glVertex3f(a.x+0.1f, a.y-0.9f, a.z+1.1f);
        GL11.glVertex3f(a.x+0.9f, a.y-0.9f, a.z+1.1f);
        GL11.glVertex3f(a.x+0.1f, a.y-0.1f, a.z+1.1f);
        GL11.glVertex3f(a.x+0.1f, a.y-0.9f, a.z+1.1f);
        GL11.glVertex3f(a.x+0.9f, a.y-0.1f, a.z+1.1f);
        GL11.glVertex3f(a.x+0.9f, a.y-0.9f, a.z+1.1f);      
        GL11.glEnd();
    }
    
    public void drawLine(Vector3f a) {
        GL11.glColor3f(1.0f, 0.0f, 0.0f);
        GL11.glBegin(GL11.GL_LINES);
        GL11.glLineWidth(5.0f);
        GL11.glVertex3f(a.x, a.y, a.z);
        GL11.glVertex3f(a.x, a.y+8, a.z);
        GL11.glEnd();
    }
    
    public void lookAt(Vector3f gps, int maxDistance){
    	addX=0;
    	addY=0;
    	addZ=0;
    	String dir = "unknown";
    	// Calculate speed in x,z and y direction
    	radx = (float)Math.sin(Math.toRadians(yaw));
    	radz = (float)Math.cos(Math.toRadians(yaw));
    	rady = (float)Math.sin(Math.toRadians(pitch));
    	do {
//    		System.out.println(System.currentTimeMillis()+" addx "+addX+" addy "+addY+" addz "+addZ+" x "+gps.x+" y "+gps.y+" z "+gps.z+" yaw "+yaw+" pitch "+pitch);
//    		System.out.println("x: "+gps.x+" z: "+gps.z+" y: "+gps.y+" radx: "+radx+" radz: "+radz+" rady: "+rady);
//	    	System.out.println("distx: "+distx+" disty: "+disty+" distz: "+distz+" direction: "+dir+" addX: "+addX+" addZ: "+addZ+" addY: "+addY);
//	    	try {
//				Thread.sleep(100);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
    		// Calculate distance to next x,z and y depending on gps and speed
	    	if 	(radx <= 0)	{distx = ((gps.x % 1)			  +addX)/(radx);}
	    	else 			{distx = ((((int)gps.x + 1)-gps.x)+addX)/(radx);}
	    	if 	(rady <= 0)	{disty = ((gps.y % 1)			  +addY)/(rady);}
	    	else 			{disty = ((((int)gps.y + 1)-gps.y)+addY)/(rady);}
	    	if 	(radz <= 0)	{distz = ((gps.z % 1)			  +addZ)/(radz);}
	    	else 			{distz = ((((int)gps.z + 1)-gps.z)+addZ)/(radz);}
	    	
	    	// Calculate shortest distance
	    	if(Math.abs(distx)<Math.abs(disty) && Math.abs(distx)<Math.abs(distz)){
				if(distx>=0)	{ dir="east"; 	addX++; }
				else 			{ dir="west"; 	addX--; }
	    	}else if(Math.abs(disty)<Math.abs(distz) && Math.abs(disty)<Math.abs(distx)){
				if(disty>=0)	{ dir="up"; 	addY++; }
				else 			{ dir="down"; 	addY--; }    	
			}else{
				if(distz>=0)	{ dir="north"; 	addZ++; }
				else 			{ dir="south"; 	addZ--; }
	    	}

	    	
	    	
	    	// Check if solid material
	    	lookAtGPS2.setWestEast((long)gps.x+addX);
	    	lookAtGPS2.setSouthNorth((long)gps.z+addZ);
	    	lookAtGPS2.setUpDown((long)gps.y+addY);
    		lookAtMaterial2 = world.getVoxel(lookAtGPS2);
    		if (lookAtMaterial2 != Material.m_air){
    			lookAtMaterialFound2=true;
    			break;
    		}
    	} while (Math.abs(addX)<maxDistance && Math.abs(addY)<maxDistance && Math.abs(addZ)<maxDistance);
    }
    
    public void lookAt(){
    	lookAtMaterialFound = false;
    	lookAtVoxel.x=position.x;
    	lookAtVoxel.y=position.y;
    	lookAtVoxel.z=position.z;
    	radx = (float)Math.sin(Math.toRadians(yaw));
    	radz = (float)Math.cos(Math.toRadians(yaw));
    	rady = (float)Math.sin(Math.toRadians(pitch));
    	diffx = radx - (lookAtVoxel.x % 1);
    	diffz = radz - (lookAtVoxel.z % 1);
    	diffy = rady - (lookAtVoxel.y % 1);
    	difft = diffx+diffy+diffz;
    	for(lookAtDistance=0; lookAtDistance<5; lookAtDistance++){
    		lookAtVoxel.x+=radx;
    		lookAtVoxel.y-=rady;
    		lookAtVoxel.z-=radz;
    		lookAtGPS.setGPSRound(lookAtVoxel);
    		lookAtMaterial = world.getVoxel(lookAtGPS);
    		if (lookAtMaterial != Material.m_air){
    			// This voxel within viewing distance is not air
    			//TODO figure out which side of the voxel I look at
    			//TODO figure out how to draw a colorful square to mark this voxel side
    			lookAtMaterialFound=true;
    			break;
    		}
    	}
    }
    
    public GPS lookAt2(){
    	lookAtMaterialFound = false;
    	lookAtGPS.setGPSRound(lookAtVoxel);
    	radx = (float)Math.sin(Math.toRadians(yaw));		
    	radz = (float)Math.cos(Math.toRadians(yaw));
    	rady = (float)Math.sin(Math.toRadians(pitch));
    	float x1 = position.x;
    	float y1 = position.y;
    	float z1 = position.z;
    	float x2 = x1+radx;								
    	float y2 = y1-rady;
    	float z2 = z1-radz;
    	float x1frac = x1%1;
    	float y1frac = y1%1;
    	float z1frac = z1%1;
    	float x2frac = x2%1;
    	float y2frac = y2%1;
    	float z2frac = z2%1;
//    	if(((int)x2>(int)x1) && ((int)z2>(int)z1) ){  // Direction 0-90 degrees
//    		if ( ((1-x1frac)/((1-x1frac)+(x2frac))) > ( (1-y1frac)/( (1-y1frac)+y2frac) ) ) {
//    			if ( world.getVoxel(x1+1,y1,z1).isSolid ){ return(new GPS(x1+1,y1,z1));}			
//    		}
//    		else if ( world.getVoxel(x1,y1+1,z1).isSolid ){ return(x1,y1+1,z1);}
//    		
//    	}
    	
    	
    	
    	
    	
//    	diffx = radx - (lookAtVoxel.x % 1);
//    	diffz = radz - (lookAtVoxel.z % 1);
//    	diffy = rady - (lookAtVoxel.y % 1);
//    	difft = diffx+diffy+diffz;
    	
      	for(lookAtDistance=0; lookAtDistance<5; lookAtDistance++){
      		if((lookAtVoxel.x % 1)+radx > 1.0f){
      			
      			
      			
      			
      			
      			
      		}
      			lookAtGPS.east();
      			lookAtMaterial = world.getVoxel(lookAtGPS);
      			if (lookAtMaterial != Material.m_air){
        			lookAtMaterialFound=true;
        			break;
      		}
    		
    		
    		
    		lookAtVoxel.x+=radx;
    		lookAtVoxel.y-=rady;
    		lookAtVoxel.z-=radz;
    		
    		
    		lookAtGPS.setGPSRound(lookAtVoxel);
    		lookAtMaterial = world.getVoxel(lookAtGPS);
    		if (lookAtMaterial != Material.m_air){
    			// This voxel within viewing distance is not air
    			//TODO figure out which side of the voxel I look at
    			//TODO figure out how to draw a colorful square to mark this voxel side
    			lookAtMaterialFound=true;
    			break;
    		}
    	}
      	return(new GPS(x1+1,y1,z1));
    }
    
    public void run(boolean fullscreen) {
        this.fullscreen = fullscreen;
        int fps=0;
        int inventorydisplaylist; 
        Font font = new Font("monospaced", Font.BOLD, 16);
        
        

        try {
            init();
            world = new World(1, 128,  textureMaterials);
            dlistGPS = world.compileDlist();
            inventorydisplaylist = compileInventoryDisplayList();
            //world.generateTerrain(123);       
          //hide the mouse
            Mouse.setGrabbed(true);

            while (!done) {
            	time = Sys.getTime();
            	elapsedTime = ((time - time2));
            	lookAt();
            	lookAt(position,6);
            	
            	// Render
            	clearRenderer();
            	// Render 3D Display Lists
                initGL3D();
            	for(int i=0; i<dlistGPS.length; i++){
        			GL11.glLoadIdentity(); // Reset The Current Modelview Matrix
        	        lookThrough(dlistGPS[i].getDlGPS());
        	        glCallList(dlistGPS[i].getDisplayList());
            	}
            	GL11.glColor3f(1.0f, 1.0f, 0.0f);
            	drawVoxel(lookAtVoxel);
//            	GL11.glColor3f(0.0f, 0.0f, 1.0f);
            	if(lookAtMaterialFound) {
            		GL11.glColor3f(1.0f, 0.0f, 0.0f);
                	drawWireframe(lookAtGPS);
            	}
            	else {
            		GL11.glColor3f(0.6f, 0.6f, 0.6f);
                	drawWireframe(lookAtGPS);
            	}
        		drawLine(lookAtVoxel);
            	render3d = (Sys.getTime()-time);
            	// Render 2D HUD
            	initGL2D();
            	drawCrossAim();

    			//GL11.glLoadIdentity(); // Reset The Current Modelview Matrix
    			render2da = (Sys.getTime()-time);

    			simpletext();
    			showstats();
    			render2db = (Sys.getTime()-time);
            	//glCallList(inventorydisplaylist);
            	GL11.glLoadIdentity(); // Reset The Current Modelview Matrix

            	//renderInventory();
            	//GL11.glLoadIdentity(); // Reset The Current Modelview Matrix
            	//renderTexture(textureInventoryHUD.getTexture(),hudPos.DownRight);

            	render2dc = (Sys.getTime()-time);

            	endGL2D();
            	time2=time;
            	//sleep(1000);
                mainloop(elapsedTime);
                //Display.setTitle("MyCraft  FPS: "+elapsedTime);
                fps++;
                if(time2>seconds){
                	seconds=time2+1000;
                	this.fps=fps;
                    //Display.setTitle("MyCraft  FPS: "+fps+" looptime: "+elapsedTime+" display: "+VIEWs[what2display]+" the other...");                	
                    fps=0;
                    ramTotal = Runtime.getRuntime().totalMemory()/1024;    
                    ramFree=Runtime.getRuntime().freeMemory()/1024;
                    ramUsed=ramTotal-ramFree;
                }
                Keyboard.poll();
                Display.processMessages();
                Display.swapBuffers();
            }
            cleanup();
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
    
    private void sleep(int ms){
    	try {

			Thread.sleep(ms);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public GPS toGPS(Vector3f pos){
    	GPS gps = new GPS((long)pos.x, (long)pos.y, (long)pos.z);
    	//System.out.println(gps.toSString());
    	return gps;
    }
    
    public String  position2Str(Vector3f test){
 
    	return "position: "+test.x+" "+test.y+" "+test.z;
    	
    }
    

    
    private Vector3f collisionDetect(Vector3f after, int height){
    	//Vector3f    collision  = new Vector3f();
    	collision.setX(after.getX());
    	collision.setY(after.getY());
    	collision.setZ(after.getZ());
    	if (yaw>270){   	// 270-360
//    		msg("270-360");
    		if((after.getX()%1)<0.3f){
    			collision.setX(after.getX()-1);
    			if (world.getVoxel(toGPS(collision))==Material.m_air){
    	        	collision.setX(after.getX());
    	        }
    	        else{
//    	        	msg("collision x "+world.getVoxel(toGPS(collision)));
    	        	collision.setX(((int)after.getX())+0.3f);
    	        }  			
    		}
    		if((after.getZ()%1)<0.3f){
    			collision.setZ(after.getZ()-1);
    			if (world.getVoxel(toGPS(collision))==Material.m_air){
    	        	collision.setZ(after.getZ());
    	        }
    	        else{
//    	        	msg("collision z "+world.getVoxel(toGPS(collision)));
    	        	collision.setZ(((int)after.getZ())+0.3f);
    	        }  			
    		}
    	}
    	else if (yaw>180){	// 180-270
//    		msg("180-270");
//    		if((after.getX()%1)<0.3f){
    			collision.setX(after.getX()-1);
    			if (world.getVoxel(toGPS(collision))==Material.m_air){
//    				msg("no collision x "+world.getVoxel(toGPS(collision)));
    	        	collision.setX(after.getX());
    	        }
    	        else{
//    	        	msg("collision x "+world.getVoxel(toGPS(collision)));
    	        	collision.setX(((int)after.getX())+0.3f);
    	        }  			
//    		}
//    		if((after.getZ()%1)>0.7f){
    			collision.setZ(after.getZ()+1);
    			if (world.getVoxel(toGPS(collision))==Material.m_air){
//    				msg("no collision z "+world.getVoxel(toGPS(collision)));
    	        	collision.setZ(after.getZ());
    	        }
    	        else{
//    	        	msg("collision z "+world.getVoxel(toGPS(collision)));
    	        	collision.setZ(((int)after.getZ())+0.7f);
    	        }  			
//    		}
    		
    	}
    	else if (yaw> 90){	//  90-180
//    		msg("90-180");
    		if((after.getX()%1)>0.7f){
    			collision.setX(after.getX()+1);
    			if (world.getVoxel(toGPS(collision))==Material.m_air){
    	        	collision.setX(after.getX());
    	        }
    	        else{
//    	        	msg("collision x "+world.getVoxel(toGPS(collision)));
    	        	collision.setX(((int)after.getX())+0.7f);
    	        }  			
    		}
    		if((after.getZ()%1)>0.7f){
    			collision.setZ(after.getZ()+1);
    			if (world.getVoxel(toGPS(collision))==Material.m_air){
    	        	collision.setZ(after.getZ());
    	        }
    	        else{
//    	        	msg("collision z "+world.getVoxel(toGPS(collision)));
    	        	collision.setZ(((int)after.getZ())+0.7f);
    	        }  			
    		}
    		
    	}
    	else {				//   0- 90
//    		msg("0-90");
    		if((after.getX()%1)>0.7f){
    			collision.setX(after.getX()+1);
    			if (world.getVoxel(toGPS(collision))==Material.m_air){
    	        	collision.setX(after.getX());
    	        }
    	        else{
//    	        	msg("collision x "+world.getVoxel(toGPS(collision)));
    	        	collision.setX(((int)after.getX())+0.7f);
    	        }  			
    		}
    		if((after.getZ()%1)<0.3f){
    			collision.setZ(after.getZ()-1);
    			if (world.getVoxel(toGPS(collision))==Material.m_air){
    	        	collision.setZ(after.getZ());
    	        }
    	        else{
//    	        	msg("collision z "+world.getVoxel(toGPS(collision)));
    	        	collision.setZ(((int)after.getZ())+0.3f);
    	        }  			
    		}   		
    	}
    	message = "P1x: "+collision.x+" P1y: "+collision.y+" P1z: "+collision.z;
    	return collision;
    }
    
  //moves the camera forward relative to its current rotation (yaw)
    public void walkForward(float distance)
    {
        position2.setX(position.getX() + (distance * (float)Math.sin(Math.toRadians(yaw))));
        position2.setZ(position.getZ() + (distance * (float)Math.cos(Math.toRadians(yaw))));
        position2.setY(position.getY());
        //msg("P2x: "+position2.x+" P2y: "+position2.y+" P2z: "+position2.z+" sin: "+(distance * (float)Math.sin(Math.toRadians(yaw)))+" cos: "+(float)Math.cos(Math.toRadians(yaw))+" yaw: "+yaw+" distance: "+distance);
        position = collisionDetect(position2,2);
    }
    

     
    //moves the camera backward relative to its current rotation (yaw)
    public void walkBackwards(float distance)
    {
        position.x -= distance * (float)Math.sin(Math.toRadians(yaw));
        position.z -= distance * (float)Math.cos(Math.toRadians(yaw));
    }
     
    //strafes the camera left relitive to its current rotation (yaw)
    public void strafeLeft(float distance)
    {
        position.x += distance * (float)Math.sin(Math.toRadians(yaw-90));
        position.z -= distance * (float)Math.cos(Math.toRadians(yaw-90));
    }
     
    //strafes the camera right relitive to its current rotation (yaw)
    public void strafeRight(float distance)
    {
        position.x += distance * (float)Math.sin(Math.toRadians(yaw+90));
        position.z -= distance * (float)Math.cos(Math.toRadians(yaw+90));
    }
    
  
    private void mainloop(float elapsedTime) {
        if(Keyboard.isKeyDown(Keyboard.KEY_ADD)) {       // Exit if Escape is pressed
        	if (keyadd==false){
        		what2display++;
        		if(what2display>8){
        			what2display=0;
        		}
        		try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		keyadd=true;
        	}
        	else {
        		keyadd=false;
        	}
            //if(textureIndex>255){ textureIndex=0;}
        }
        
        
        
        if(Keyboard.isKeyDown(Keyboard.KEY_SUBTRACT)) {       // Exit if Escape is pressed
            textureIndex--;
            if(textureIndex<0){ textureIndex=255;}
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {       // Exit if Escape is pressed
            done = true;
        }
        if(Display.isCloseRequested()) {                     // Exit if window is closed
            done = true;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_F1) && !f1) {    // Is F1 Being Pressed?
            f1 = true;                                      // Tell Program F1 Is Being Held
            switchMode();                                   // Toggle Fullscreen / Windowed Mode
        }
        if(!Keyboard.isKeyDown(Keyboard.KEY_F1)) {          // Is F1 Being Pressed?
            f1 = false;
        }
        lastused=99;
        distance = (walkSpeed * elapsedTime)/1000;
        if (distance>0.9f){ msg("Distance exceeded : "+distance); distance = 0.9f; }
        if((Keyboard.isKeyDown(Keyboard.KEY_D))&& (lastused!=1)){ strafeRight((walkSpeed * elapsedTime)/1000); lastused=1;}
        if((Keyboard.isKeyDown(Keyboard.KEY_Q))&& (lastused!=2)){ position.y+=((walkSpeed * elapsedTime)/1000); lastused=2; }
        if((Keyboard.isKeyDown(Keyboard.KEY_S))&& (lastused!=3)){ walkBackwards((walkSpeed * elapsedTime)/1000); lastused=3; }
        if((Keyboard.isKeyDown(Keyboard.KEY_A))&& (lastused!=4)){ strafeLeft((walkSpeed * elapsedTime)/1000); lastused=4;}
        if((Keyboard.isKeyDown(Keyboard.KEY_E))&& (lastused!=5)){ position.y-=((walkSpeed * elapsedTime)/1000); lastused=5; }
        if((Keyboard.isKeyDown(Keyboard.KEY_W))&& (lastused!=6)){ walkForward((walkSpeed * elapsedTime)/1000); lastused=6; }
        if((Keyboard.isKeyDown(Keyboard.KEY_NUMPAD7))&& (lastused!=7)){ yaw++; lastused=7; }
        if((Keyboard.isKeyDown(Keyboard.KEY_NUMPAD8))&& (lastused!=8)){ yaw--; lastused=8; }
        if((Keyboard.isKeyDown(Keyboard.KEY_NUMPAD9))&& (lastused!=9)){ pitch++; lastused=9; }
        if((Keyboard.isKeyDown(Keyboard.KEY_NUMPAD0))&& (lastused!=0)){ pitch--; lastused=0; }
        if((Keyboard.isKeyDown(Keyboard.KEY_NUMPAD1))&& (lastused!=1)){ glEnable(GL_LIGHTING); lastused=1; }
        if((Keyboard.isKeyDown(Keyboard.KEY_NUMPAD2))&& (lastused!=2)){ glDisable(GL_LIGHTING); lastused=2; lx=0; lz=0; ly=0;}
        if((Keyboard.isKeyDown(Keyboard.KEY_NUMPAD3))&& (lastused!=3)){ lastused=3; 
        ly--;
        lightPosition.put(lx).put(ly).put(lz).put(0.0f).flip();
        glLight(GL_LIGHT0, GL_POSITION, lightPosition);				// sets light position
        }
        if((Keyboard.isKeyDown(Keyboard.KEY_NUMPAD6))&& (lastused!=6)){ lastused=6; 
        ly++;
        lightPosition.put(lx).put(ly).put(lz).put(0.0f).flip();
        glLight(GL_LIGHT0, GL_POSITION, lightPosition);				// sets light position
        }
        if((Keyboard.isKeyDown(Keyboard.KEY_I))&& (lastused!=98)){ 
        	// Print info regarding anything:
            msg("X:"+position.x+" Y:"+position.y+" Z:"+position.z+" walkspeed: "+walkSpeed+" elapsed: "+elapsedTime+" movement: "+((walkSpeed * elapsedTime)/1000)+" yaw:"+yaw+" pitch:"+pitch);	

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
    


    private void switchMode() {
        fullscreen = !fullscreen;
        try {
            Display.setFullscreen(fullscreen);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public static int createVBOID() {       
        if (GLContext.getCapabilities().GL_ARB_vertex_buffer_object) {
                IntBuffer buffer = BufferUtils.createIntBuffer(1);
                glGenBuffersARB(buffer);
                return buffer.get(0);
        }
        return 0;
    }
    



    
    private float[] getMaterialIndexes(byte material){
    	float[] indexes= new float[2];
    	byte indy = (byte) (material/16);
    	byte indx = (byte) (material - (indy*16));
    	indexes[0]= (indx*0.0625f);
    	indexes[1]= (indy*0.0625f);
    	return indexes;
    }


    //translates and rotate the matrix so that it looks through the camera
    //this dose basic what gluLookAt() does
    public void lookThrough(GPS gps)
    {
        //roatate the pitch around the X axis
        GL11.glRotatef(pitch, 1.0f, 0.0f, 0.0f);
        //roatate the yaw around the Y axis
        GL11.glRotatef(yaw, 0.0f, 1.0f, 0.0f);
        //translate to the position vector's location
        // gps contains the GPS position of a displaylist
        // position contains the position of the player
        GL11.glTranslatef(0-position.x+gps.getWestEast(), 0-position.y+gps.getUpDown(), 0-position.z+gps.getSouthNorth());
        //msg("x"+(0-position.x+gps.getWestEast())+"y"+ (0-position.y+gps.getUpDown())+"z"+ (0-position.z+gps.getSouthNorth()));
    }

	private void clearRenderer(){
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT); // Clear The Screen And The Depth Buffer
        
	}
	 
//		private void Renderer(GPS gps){
//			GL11.glLoadIdentity(); // Reset The Current Modelview Matrix
//	        lookThrough(gps);
//	        int dlist = world.getChunk(gps).getDisplayList();
//	        glCallList(world.getChunk(gps).getDisplayList());
//		}
	 
	 
	 
	 

    private void createWindow() throws Exception {
    	DisplayMode d[] = Display.getAvailableDisplayModes();
    	int highestresolution=0;
    	int preferredmode = 0;
    	for (int i = 0; i < d.length; i++) {
    		msg(d[i].getWidth()+" X "+d[i].getHeight());
    		if ((d[i].getWidth()*d[i].getHeight())>highestresolution){
    			preferredmode=i;
    			highestresolution = (d[i].getWidth()*d[i].getHeight());
    		}   		
    	}
    	msg("Preferred mode: "+d[preferredmode].getWidth()+" X "+d[preferredmode].getHeight());
    	if (fullscreen) {
    		Display.setFullscreen(fullscreen);
    		Display.setDisplayMode(d[preferredmode]);    		
    	}
    	else {
    		Display.setFullscreen(fullscreen); 
    		for (int i = 0; i < d.length; i++) {
    			if (d[i].getWidth() == windowWidth
    					&& d[i].getHeight() == windowHeight
    					&& d[i].getBitsPerPixel() == 32) {
    				displayMode = d[i];
    				break;
    			}
    		}
    		Display.setDisplayMode(displayMode);
    		Display.setTitle(windowTitle);
    	}
        Display.create();
    }
    
    public static BufferedImage loadImage(String ref) {
		BufferedImage bimg = null;
		try {
			bimg = ImageIO.read(new File(ref));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bimg;
	}



    private void init() throws Exception {
        createWindow();
        textureContainerHUD = new Textures("res/consoletest.png");
        textureCraftingHUD = new Textures("res/Texture_red_yellow_016.png");
        textureInventoryHUD = new Textures("res/inventory.png");
        textureFurnaceHUD = new Textures("res/furnace.png"); 
        textureMaterials = new Textures("res/terrain.png");
        textureConsoleHUD = new Textures("res/console.png");
    }

    private void initGL2D() {
    	GL11.glMatrixMode(GL_PROJECTION);
    	GL11.glPushMatrix();
    	GL11.glLoadIdentity();
    	GLU.gluOrtho2D(0.0f, (float) displayMode.getWidth(),(float) displayMode.getHeight(), 0.0f);
    	GL11.glMatrixMode(GL_MODELVIEW);
    	glEnable(GL_COLOR_MATERIAL);
    	glPushMatrix();
    	GL11.glLoadIdentity();
    	glDisable(GL_CULL_FACE);
        glDisable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    	
    	//GL11.glTranslatef(0.375f, 0.375f, 0.0f);
    }
    
    
    private void endGL2D(){	
//    	GL11.glDisable(GL_CULL_FACE);
//    	GL11.glDisable(GL_DEPTH_TEST);
//    	GL11.glEnable(GL_BLEND);
//        GL11.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    	glDisable(GL_BLEND);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);

        glPopMatrix();
        glMatrixMode(GL_PROJECTION);
        glPopMatrix();
        glMatrixMode(GL_MODELVIEW);
    }

    private void initGL3D() {
        GL11.glEnable(GL11.GL_TEXTURE_2D); // Enable Texture Mapping
        GL11.glShadeModel(GL11.GL_SMOOTH); // Enable Smooth Shading
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // Black Background
        GL11.glClearDepth(1.0f); // Depth Buffer Setup
        GL11.glEnable(GL11.GL_DEPTH_TEST); // Enables Depth Testing
        GL11.glDepthFunc(GL11.GL_LEQUAL); // The Type Of Depth Testing To Do
        GL11.glMatrixMode(GL11.GL_PROJECTION); // Select The Projection Matrix
        GL11.glLoadIdentity(); // Reset The Projection Matrix
        //GL11.glOrtho(0, displayMode.getWidth(), displayMode.getHeight(), 0, 1000, -1000);
        // Calculate The Aspect Ratio Of The Window
        GLU.gluPerspective(45.0f, (float) displayMode.getWidth() / (float) displayMode.getHeight(), 0.1f, 1000.0f);
        GL11.glMatrixMode(GL11.GL_MODELVIEW); // Select The Modelview Matrix
        // Really Nice Perspective Calculations
        GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
      //----------- Variables & method calls added for Lighting Test -----------//
//      		initLightArrays();
//      		glShadeModel(GL_SMOOTH);
//      		glMaterial(GL_FRONT, GL_SPECULAR, matSpecular);				// sets specular material color
//      		glMaterialf(GL_FRONT, GL_SHININESS, 50.0f);					// sets shininess
//      		
//      		glLight(GL_LIGHT0, GL_POSITION, lightPosition);				// sets light position
//      		glLight(GL_LIGHT0, GL_SPECULAR, whiteLight);				// sets specular light to white
//      		glLight(GL_LIGHT0, GL_DIFFUSE, whiteLight);					// sets diffuse light to white
//      		glLightModel(GL_LIGHT_MODEL_AMBIENT, lModelAmbient);		// global ambient light 
//      		
//      		glEnable(GL_LIGHTING);										// enables lighting
//      		glEnable(GL_LIGHT0);										// enables light0
//      		
//      		glEnable(GL_COLOR_MATERIAL);								// enables opengl to use glColor3f to define material color
//      		glColorMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE);			// tell opengl glColor3f effects the ambient and diffuse properties of material
//      		//----------- END: Variables & method calls added for Lighting Test -----------//
      	}
      	

      	//------- Added for Lighting Test----------//
      	private void initLightArrays() {
      		matSpecular = BufferUtils.createFloatBuffer(4);
      		matSpecular.put(1.0f).put(1.0f).put(1.0f).put(1.0f).flip();
      		
      		lightPosition = BufferUtils.createFloatBuffer(4);
      		lightPosition.put(-32.0f).put(-32.0f).put(-32.0f).put(0.0f).flip();
      		
      		whiteLight = BufferUtils.createFloatBuffer(4);
      		whiteLight.put(1.0f).put(1.0f).put(1.0f).put(1.0f).flip();
      		
      		lModelAmbient = BufferUtils.createFloatBuffer(4);
      		lModelAmbient.put(0.5f).put(0.5f).put(0.5f).put(1.0f).flip();
      	}
    
    private void cleanup() {
        Display.destroy();
    }

}
