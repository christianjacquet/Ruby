package com.digitalemu.world;

import static org.lwjgl.opengl.GL11.GL_AMBIENT_AND_DIFFUSE;
import static org.lwjgl.opengl.GL11.GL_COLOR_MATERIAL;
import static org.lwjgl.opengl.GL11.GL_COMPILE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_DIFFUSE;
import static org.lwjgl.opengl.GL11.GL_FRONT;
import static org.lwjgl.opengl.GL11.GL_LEQUAL;
import static org.lwjgl.opengl.GL11.GL_LIGHT0;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_LIGHT_MODEL_AMBIENT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_NICEST;
import static org.lwjgl.opengl.GL11.GL_PERSPECTIVE_CORRECTION_HINT;
import static org.lwjgl.opengl.GL11.GL_POSITION;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_SHININESS;
import static org.lwjgl.opengl.GL11.GL_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_SPECULAR;
import static org.lwjgl.opengl.GL11.*;

import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glClearDepth;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glColorMaterial;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEndList;
import static org.lwjgl.opengl.GL11.glGenLists;
import static org.lwjgl.opengl.GL11.glHint;
import static org.lwjgl.opengl.GL11.glLight;
import static org.lwjgl.opengl.GL11.glLightModel;
import static org.lwjgl.opengl.GL11.glMaterial;
import static org.lwjgl.opengl.GL11.glMaterialf;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glNewList;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glShadeModel;

import java.awt.image.BufferedImage;
import java.nio.FloatBuffer;
import java.util.Random;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.newdawn.slick.opengl.Texture;

import com.digitalemu.world.Block;
import com.digitalemu.world.SubChunk;
import com.digitalemu.terrain.PerlinNoice2d2;
//import com.digitalemu.test.GameLoopDListMax;

public class Chunk3 {
	private short[][][] voxel;					// The voxels kept in RAM
	private GPS gps;							// This chunks absolute position
	private boolean notVisible;
	private boolean empty;
	private boolean onDisc;
	private boolean hasChanged;
	private int chunkBase;
	public int oglDisplayList;
	private Textures texture;
	public String myId;
	private PerlinNoice2d2 perlinNoise;
	BufferedImage perlin;
	
	public Chunk3(int chunkBase, GPS gps, Textures texture){
		perlinNoise = new PerlinNoice2d2();
		perlin = perlinNoise.getPerlin2dPng(1234);
		this.texture=texture;
		this.chunkBase= chunkBase;
		this.gps=gps;
		this.empty=true;
		this.notVisible=true;
		this.voxel = new short[chunkBase][chunkBase][chunkBase];
		for(int x=0; x<chunkBase; x++){
			for(int y=0; y<chunkBase; y++){
				for(int z=0; z<chunkBase; z++){
					this.voxel[x][y][z]=Material.m_air;
				}
			}
		}
		//GameLoopDListMax.msg("I am chunk: "+gps.toSString());
		this.myId=gps.toSString();		
	}
	
	private void msg(String message){
		//GameLoopDListMax.msg("Chunk3: "+message);
	}
	
	public void generateMatrix(){
		this.empty=false;
		for(int x=0; x<chunkBase; x++){
			this.voxel[x][0][0]=Material.m_rock;
		}
		for(int x=0; x<chunkBase; x++){
			this.voxel[0][x][0]=Material.m_sand;
		}
		for(int x=0; x<chunkBase; x++){
			this.voxel[0][0][x]=Material.m_treeCore;
		}
	}
	
	public void generateTestMap(){
		for (int x = 0; x<chunkBase; x++){
			for (int z = 0; z<chunkBase; z++){
				for (int y = 0; y<chunkBase; y++){
					if(y==0){this.voxel[x][y][z]=1;}
					else {this.voxel[x][y][z]=Material.m_air;}
					if((x==chunkBase-1) && (y<z)){ 
						this.voxel[x][y][z]=18;}
				}
			}
		}
		this.voxel[9][1][9]=3;
		this.voxel[10][1][9]=3;
		this.voxel[11][1][9]=3;
		this.voxel[9][2][9]=4;
		this.voxel[10][2][9]=4;
		this.voxel[11][2][9]=4;
		this.voxel[9][3][9]=5;
		this.voxel[10][3][9]=5;
		this.voxel[11][3][9]=5;
		this.voxel[0][0][0]=8;
		this.voxel[0][1][0]=8;
		this.voxel[0][2][0]=8;
		this.voxel[8][0][8]=8;
		this.voxel[8][1][8]=8;
		this.voxel[8][2][8]=8;
		this.voxel[4][3][4]=8;
		this.voxel[chunkBase-1][chunkBase-1][chunkBase-1]=2;
		this.voxel[chunkBase-1][chunkBase-2][chunkBase-1]=2;
		this.voxel[chunkBase-1][chunkBase-3][chunkBase-1]=2;

		
	}
	
	public void generatePerlin(){
		this.empty=false;
		Byte height;
		msg("generate perlin");
		for(int leftRight=0; leftRight<chunkBase; leftRight++){
			for(int frontBack=0; frontBack<chunkBase; frontBack++){
				height=(byte) perlin.getRGB(leftRight, frontBack);
				height++;
				for(int upDown=0; upDown<chunkBase; upDown++){
					if (upDown<64){ 
						if(upDown<height){
							msg(height+" ");
							if (upDown<3){
								this.voxel[leftRight][upDown][frontBack]=18;
							}
							else if(upDown<6){
								this.voxel[leftRight][upDown][frontBack]=2;
							}
							else {
								this.voxel[leftRight][upDown][frontBack]=1;
							}
							
						}
						else{
							//msg("air ");
							this.voxel[leftRight][upDown][frontBack]=Material.m_air;
						}
					}	
				}
			}
		}
		msg("Done generate perlin");
	}
	
	public void generateNormalgrid(int change){
		msg("Generate: "+Material.m_rock+change+" for "+myId);
		this.empty=false;
		int half = chunkBase/2;
//		for(int x=1; x<chunkBase-1; x++){
//			this.voxel[x][1][chunkBase-1]=(short) (Material.m_clay+change);
//			this.voxel[x][chunkBase-1][chunkBase-1]=(short) (Material.m_clay+change);
//			this.voxel[x][1][half]=(short) (Material.m_clay+change);
//			this.voxel[x][half][half]=(short) (Material.m_clay+change);
//			this.voxel[1][1][x]=(short) (Material.m_clay+change);
//			this.voxel[1][chunkBase-1][x]=(short) (Material.m_clay+change);
//			this.voxel[chunkBase-1][1][x]=(short) (Material.m_clay+change);
//			this.voxel[chunkBase-1][half][x]=(short) (Material.m_clay+change);
//			this.voxel[x][x][half]=(short) (Material.m_clay+change);
//		}
	}
	
	public void generateNormal(int change){
		msg("Generate: "+Material.m_rock+change+" for "+myId);
		this.empty=false;
//		for(int leftRight=0; leftRight<chunkBase; leftRight++){
//			for(int upDown=0; upDown<chunkBase; upDown++){
//				for(int frontBack=0; frontBack<chunkBase; frontBack++){
//					if(upDown<23){this.voxel[leftRight][upDown][frontBack]=Material.m_rock;}
//					else if(upDown<24){this.voxel[leftRight][upDown][frontBack]=Material.m_water1;}
//					else if(upDown<25){this.voxel[leftRight][upDown][frontBack]=Material.m_grass;}
//					else if(upDown<26){this.voxel[leftRight][upDown][frontBack]=(short) change;}
//					else {this.voxel[leftRight][upDown][frontBack]=Material.m_air;}
//				}
//			}
//		}
//		for(int x=1; x<chunkBase-1; x++){
//			this.voxel[x+1][25][x]=Material.m_air;
//			this.voxel[x][24][x]=Material.m_air;
//			this.voxel[x][25][x]=Material.m_air;
//			this.voxel[x-1][25][x]=Material.m_air;
//		}
	}
	
	public void generateWorldChunk(int posHeight){
		Block block = new Block((byte) 0);
		Random material = new Random();
		if (posHeight==0){
			for(int x=0; x<chunkBase; x++){
				for(int y=0; y<chunkBase; y++){					
					for(int z=0; z<chunkBase; z++){
						if(y==0){
							this.voxel[x][y][z]=18; 
						}
						else{
							if (y<9){
								this.voxel[x][y][z]=02;
							}
							else{
								if (y<11){
									this.voxel[x][y][z]=05;
								}
								else {
									this.voxel[x][y][z]=14;
								}
							}
						}
					}
				}
			}
			// Just put some random stuff on the ground.
			int x =0;
			int y = 0;
			byte m = 0;
			for(int o=0; o<200; o++){
				x = material.nextInt(chunkBase);
				y = material.nextInt(chunkBase);
				m = (byte) (material.nextInt(64) + 1);
				voxel[x][11][y]= m;
				for(int zz = 12; zz<material.nextInt(chunkBase-1); zz++){				
					voxel[x][zz][y]=m;
				}

			}
		}
		else {
			this.voxel=null;
		}
	}
	
	
    private static float[] getMaterialIndexes3(short material){
    	float[] indexes= new float[2];
    	byte indy = (byte) (material/16);
    	byte indx = (byte) (material - (indy*16));
    	indexes[0]= (indx*0.0625f);
    	indexes[1]= (indy*0.0625f);
    	return indexes;
    }
	
    public int compileDisplayListsDummy(){
    	msg("Generate DisplayList for "+myId);
        int textureID;
    	

    	float[] indexes= new float[2];
    	// Offset to find the correct texture in texture map
    	float offset = 0.0620f;
    	float offset2 = 0.001f;
    	
    	// displayList from south
    	FloatBuffer lightPosition;
    	FloatBuffer matSpecular;
    	FloatBuffer whiteLight; 
    	FloatBuffer lModelAmbient;
  		matSpecular = BufferUtils.createFloatBuffer(4);
  		matSpecular.put(1.0f).put(1.0f).put(1.0f).put(1.0f).flip(); 		
  		lightPosition = BufferUtils.createFloatBuffer(4);
  		lightPosition.put(0.0f).put(0.0f).put(-0.0f).put(0.0f).flip();  		
  		whiteLight = BufferUtils.createFloatBuffer(4);
  		whiteLight.put(0.5f).put(0.0f).put(0.5f).put(0.0f).flip();  		
  		lModelAmbient = BufferUtils.createFloatBuffer(4);
  		lModelAmbient.put(1).put(1).put(1).put(1.0f).flip();
        lightPosition = BufferUtils.createFloatBuffer(4);
		lightPosition.put(0).put(0).put(0).put(0.0f).flip();
		glShadeModel(GL_SMOOTH);
  		glMaterial(GL_FRONT, GL_SPECULAR, matSpecular);				// sets specular material color
  		glMaterialf(GL_FRONT, GL_SHININESS, 50.0f);					// sets shininess
  		
  		glLight(GL_LIGHT0, GL_POSITION, lightPosition);				// sets light position
  		glLight(GL_LIGHT0, GL_SPECULAR, whiteLight);				// sets specular light to white
  		glLight(GL_LIGHT0, GL_DIFFUSE, whiteLight);					// sets diffuse light to white
  		glLightModel(GL_LIGHT_MODEL_AMBIENT, lModelAmbient);		// global ambient light 
  		
  		glEnable(GL_LIGHTING);										// enables lighting
  		glEnable(GL_LIGHT0);										// enables light0
  		
  		glEnable(GL_COLOR_MATERIAL);								// enables opengl to use glColor3f to define material color
  		glColorMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE);
//  		glNormal3f(1, 0, 0);
//  		glEnable(GL_CULL_FACE);    	
//  		glCullFace(GL_BACK);
//    	glClearColor(0.5f, 0.5f, 0.5f, 0.0f); // sets background to grey
//
//        glClearDepth(1.0f); // clear depth buffer
//
//        glEnable(GL_DEPTH_TEST); // Enables depth testing
//
//        glDepthFunc(GL_LEQUAL); // sets the type of test to use for depth testing
//
//        glMatrixMode(GL_PROJECTION); // sets the matrix mode to project
//
//        
//
//        float fovy = 45.0f;
//
//        //float aspect = DISPLAY_MODE.getWidth() / (float)DISPLAY_MODE.getHeight();
//
//        float zNear = 0.1f;
//
//        float zFar = 100.0f;
//
//        //GLU.gluPerspective(fovy, aspect, zNear, zFar);
//
//        
//
//        glMatrixMode(GL_MODELVIEW);
//
//        
//
//        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST); 
//
//        
//
//        //----------- Variables & method calls added for Lighting Test -----------//
//
//        FloatBuffer matSpecular = BufferUtils.createFloatBuffer(4);
//
//        matSpecular.put(1.0f).put(1.0f).put(1.0f).put(1.0f).flip();
//
//        
//
//        FloatBuffer lightPosition = BufferUtils.createFloatBuffer(4);
//
//        lightPosition.put(1.0f).put(1.0f).put(1.0f).put(0.0f).flip();
//
//        
//
//        FloatBuffer whiteLight = BufferUtils.createFloatBuffer(4);
//
//        whiteLight.put(1.0f).put(1.0f).put(1.0f).put(1.0f).flip();
//
//        
//
//        FloatBuffer lModelAmbient = BufferUtils.createFloatBuffer(4);
//
//        lModelAmbient.put(0.5f).put(0.5f).put(0.5f).put(1.0f).flip();
//
//        glShadeModel(GL_SMOOTH);
//
//        glMaterial(GL_FRONT, GL_SPECULAR, matSpecular);                         // sets specular material color
//
//        glMaterialf(GL_FRONT, GL_SHININESS, 50.0f);                                     // sets shininess
//
//        
//
//        glLight(GL_LIGHT0, GL_POSITION, lightPosition);                         // sets light position
//
//        glLight(GL_LIGHT0, GL_SPECULAR, whiteLight);                            // sets specular light to white
//
//        glLight(GL_LIGHT0, GL_DIFFUSE, whiteLight);                                     // sets diffuse light to white
//
//        glLightModel(GL_LIGHT_MODEL_AMBIENT, lModelAmbient);            // global ambient light 
//
//        
//
//        glEnable(GL_LIGHTING);                                                                          // enables lighting
//
//        glEnable(GL_LIGHT0);                                                                            // enables light0
//
//        
//
//        glEnable(GL_COLOR_MATERIAL);                                                            // enables opengl to use glColor3f to define material color
//
//        glColorMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE);                      // tell opengl glColor3f effects the ambient and diffuse properties of material

    	oglDisplayList = glGenLists(1);
        glNewList(oglDisplayList, GL_COMPILE);{
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTexture().getTextureID()); // Select Our Texture
			GL11.glBegin(GL11.GL_QUADS);


        	int z2;
        	for(int x=0; x<chunkBase; x++){
        		for(int y=0; y<chunkBase; y++){					
        			for(int z=1; z<chunkBase; z++){
        				if ((voxel[x][y][z]!=Material.m_air) && 
        					(voxel[x][y][z-1]==Material.m_air)){
        					// Front Face
        					glColor3f(0.8f,0.8f,0.8f);
        					indexes = getMaterialIndexes3(voxel[x][y][z]);
        					z2=chunkBase- z -1 -chunkBase;
        					GL11.glTexCoord2f(indexes[0]+offset2, indexes[1]+offset);
        					GL11.glVertex3i(x,y,z2+1); // Bottom Left Of The Texture and Quad
        			        GL11.glTexCoord2f(indexes[0]+offset, indexes[1]+offset);
        			        GL11.glVertex3i(x+1,y,z2+1); // Bottom Right Of The Texture and Quad
        			        GL11.glTexCoord2f(indexes[0]+offset, indexes[1]+offset2);
        			        GL11.glVertex3i(x+1,y+1,z2+1); // Top Right Of The Texture and Quad
        			        GL11.glTexCoord2f(indexes[0]+offset2, indexes[1]+offset2);
        			        GL11.glVertex3i(x,y+1,z2+1); // Top Left Of The Texture and Quad
        				}
        			}
        		}
        	}
        	for(int x=0; x<chunkBase; x++){
        		for(int y=0; y<chunkBase; y++){					
        			for(int z=0; z<chunkBase-1; z++){
        				if ((voxel[x][y][z]!=Material.m_air) && 
        					(voxel[x][y][z+1]==Material.m_air)){
        					// Back Face   OK ??
        					glColor3f(0.8f,0.8f,0.8f);
        					indexes = getMaterialIndexes3(voxel[x][y][z]);
        					z2=chunkBase- z -1 -chunkBase;
        			        GL11.glTexCoord2f(indexes[0]+offset, indexes[1]+offset);
        			        GL11.glVertex3i(x,y,z2); // Bottom Right Of The Texture and Quad
        			        GL11.glTexCoord2f(indexes[0]+offset, indexes[1]+offset2);
        			        GL11.glVertex3i(x,y+1,z2); // Top Right Of The Texture and Quad
        			        GL11.glTexCoord2f(indexes[0]+offset2, indexes[1]+offset2);
        			        GL11.glVertex3i(x+1,y+1,z2); // Top Left Of The Texture and Quad
        			        GL11.glTexCoord2f(indexes[0]+offset2, indexes[1]+offset);
        			        GL11.glVertex3i(x+1,y,z2); // Bottom Left Of The Texture and Quad
        				}
        			}
        		}
        	}
        	for(int x=0; x<chunkBase; x++){
        		for(int y=0; y<chunkBase-1; y++){					
        			for(int z=0; z<chunkBase; z++){
        				if ((voxel[x][y][z]!=Material.m_air) && 
        					(voxel[x][y+1][z]==Material.m_air)){
        			        // Top Face
        					glColor3f(1.0f,1.0f,1.0f);
        					indexes = getMaterialIndexes3(voxel[x][y][z]);
        					z2=chunkBase- z -1 -chunkBase;
        			        GL11.glTexCoord2f(indexes[0]+offset2, indexes[1]+offset2);
        			        GL11.glVertex3i(x,y+1,z2); // Top Left Of The Texture and Quad
        			        GL11.glTexCoord2f(indexes[0]+offset2, indexes[1]+offset);
        			        GL11.glVertex3i(x,y+1,z2+1); // Bottom Left Of The Texture and Quad
        			        GL11.glTexCoord2f(indexes[0]+offset, indexes[1]+offset);
        			        GL11.glVertex3i(x+1,y+1,z2+1); // Bottom Right Of The Texture and Quad
        			        GL11.glTexCoord2f(indexes[0]+offset, indexes[1]+offset2);
        			        GL11.glVertex3i(x+1,y+1,z2); // Top Right Of The Texture and Quad
        				}
        			}
        		}
        	}
        	for(int x=0; x<chunkBase; x++){
        		for(int y=1; y<chunkBase; y++){					
        			for(int z=0; z<chunkBase; z++){
        				if ((voxel[x][y][z]!=Material.m_air) && 
        					(voxel[x][y-1][z]==Material.m_air)){
        			        // Bottom Face
        					glColor3f(0.8f,0.8f,0.8f);
        					indexes = getMaterialIndexes3(voxel[x][y][z]);
        					z2=chunkBase- z -1 -chunkBase;
        			        GL11.glTexCoord2f(indexes[0]+offset, indexes[1]+offset2);
        			        GL11.glVertex3i(x,y,z2); // Top Right Of The Texture and Quad
        			        GL11.glTexCoord2f(indexes[0]+offset2, indexes[1]+offset2);
        			        GL11.glVertex3i(x+1,y,z2); // Top Left Of The Texture and Quad
        			        GL11.glTexCoord2f(indexes[0]+offset2, indexes[1]+offset);
        			        GL11.glVertex3i(x+1,y,z2+1); // Bottom Left Of The Texture and Quad
        			        GL11.glTexCoord2f(indexes[0]+offset, indexes[1]+offset);
        			        GL11.glVertex3i(x,y,z2+1); // Bottom Right Of The Texture and Quad
        				}
        			}
        		}
        	}
        	for(int x=0; x<chunkBase-1; x++){
        		for(int y=0; y<chunkBase; y++){					
        			for(int z=0; z<chunkBase; z++){
        				if ((voxel[x][y][z]!=Material.m_air) && 
        					(voxel[x+1][y][z]==Material.m_air)){
        			        // Right face    OK
        					glColor3f(0.6f,0.6f,0.6f);
        					indexes = getMaterialIndexes3(voxel[x][y][z]);
        					z2=chunkBase- z -1 -chunkBase;
        					GL11.glTexCoord2f(indexes[0]+offset, indexes[1]+offset);
        			        GL11.glVertex3i(x+1,y,z2); // Bottom Right Of The Texture and Quad
        			        GL11.glTexCoord2f(indexes[0]+offset, indexes[1]+offset2);
        			        GL11.glVertex3i(x+1,y+1,z2); // Top Right Of The Texture and Quad
        			        GL11.glTexCoord2f(indexes[0]+offset2, indexes[1]+offset2);
        			        GL11.glVertex3i(x+1,y+1,z2+1); // Top Left Of The Texture and Quad
        			        GL11.glTexCoord2f(indexes[0]+offset2, indexes[1]+offset);
        			        GL11.glVertex3i(x+1,y,z2+1); // Bottom Left Of The Texture and Quad
        				}
        			}
        		}
        	}
        	for(int x=1; x<chunkBase; x++){
        		for(int y=0; y<chunkBase; y++){					
        			for(int z=0; z<chunkBase; z++){
        				if ((voxel[x][y][z]!=Material.m_air) && 
        					(voxel[x-1][y][z]==Material.m_air)){
        			        // Left Face   OK
        					glColor3f(0.6f,0.6f,0.6f);
        					indexes = getMaterialIndexes3(voxel[x][y][z]);
        					z2=chunkBase- z -1 -chunkBase;
        			        GL11.glTexCoord2f(indexes[0]+offset2, indexes[1]+offset);
        			        GL11.glVertex3i(x,y,z2); // Bottom Left Of The Texture and Quad
        			        GL11.glTexCoord2f(indexes[0]+offset, indexes[1]+offset);
        			        GL11.glVertex3i(x,y,z2+1); // Bottom Right Of The Texture and Quad
        			        GL11.glTexCoord2f(indexes[0]+offset, indexes[1]+offset2);
        			        GL11.glVertex3i(x,y+1,z2+1); // Top Right Of The Texture and Quad
        			        GL11.glTexCoord2f(indexes[0]+offset2, indexes[1]+offset2);
        			        GL11.glVertex3i(x,y+1,z2); // Top Left Of The Texture and Quad
        				}
        			}
        		}
        	}
            	
        }
        GL11.glEnd();

		
		

        glEndList();
    	return oglDisplayList;
    }



    public int getDisplayList(){
    	return this.oglDisplayList;
    }
    

	
	public short getVoxel(int x, int y, int z){
		z=0-z;
		try {
			return voxel[x][y][z];
		} catch (ArrayIndexOutOfBoundsException e){
			System.out.println("Indexoutofbounds x "+x+" y "+y+" z "+z);
			return Material.m_null;
		}
	}
	
	public void setVoxel(int x, int y, int z, short material){
		voxel[x][y][z]=material;
	}

	public boolean isNotVisible() {
		return notVisible;
	}

	public void setNotVisible(boolean notVisible) {
		this.notVisible = notVisible;
	}

	public boolean isEmpty() {
		return empty;
	}

	public void setEmpty(boolean empty) {
		this.empty = empty;
	}

	public boolean isOnDisc() {
		return onDisc;
	}

	public void setOnDisc(boolean onDisc) {
		this.onDisc = onDisc;
	}

	public boolean isHasChanged() {
		return hasChanged;
	}

	public void setHasChanged(boolean hasChanged) {
		this.hasChanged = hasChanged;
	}

	public GPS getGps() {
		return gps;
	}

	public void setGps(GPS gps) {
		this.gps = gps;
	}
	

}