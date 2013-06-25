package com.digitalemu.world;

import org.lwjgl.util.vector.Vector3f;

/**
 * The class GPS extends and replaces Vector3f because I hate the name Vector3f and love GPS.
 * Vector3f is a 32 bit float and good enough for generating a chunk, but useless for a big world.
 * GPS are long are 64bit integer.
 * GPS are used to directly address any single Voxel in the world, no matter in which chunk it resides.
 * Vector3f are used for addressing each point in 3D space for a chunk.
 * All moving objects needs an instance of GPS.
 * @author Xtian
 *
 */
public class GPS2 extends Vector3f{
	private long gpsX;
	private long gpsY;
	private long gpsZ;
	
	
	public GPS2(){
		this.x=0;		// Vector3f
		this.y=0;		// Vector3f
		this.z=0;		// Vector3f
		this.gpsX=0;	// GPS long
		this.gpsY=0;	// GPS long
		this.gpsZ=0;	// GPS long
	}
	
	public void setGPS2(long x, long y, long z){
		this.x=0;		// Vector3f
		this.y=0;		// Vector3f
		this.z=0;		// Vector3f
		this.gpsX=x;	// GPS long
		this.gpsY=y;	// GPS long
		this.gpsZ=z;	// GPS long
	}
	
	public void setGPS2(Vector3f v){
		this.x=v.getX();	// Vector3f
		this.y=v.getY();	// Vector3f
		this.z=v.getZ();	// Vector3f
	}
	
	public GPS2(int x, int y, int z){
		this.x=x;
		this.y=y;
		this.z=z;
	}
	
	public void setX(long val){
		this.x=val;
	}
	
	public void setY(long val){
		this.y=val;
	}
	
	public void setZ(long val){
		this.z=val;
	}
	
	public void upOne(){
		this.y++;
	}

	public void downOne(){
		this.y--;
	}	
	
	public void westOne(){
		this.x--;
	}

	public void eastOne(){
		this.x++;
	}	
	
	public void northOne(){
		this.z--;
	}

	public void southOne(){
		this.z++;
	}
	
	public static String gps2str(long x, long y, long z){
		return "x"+Long.toString(x)+"y"+Long.toString(y)+"z"+Long.toString(z);
	}
		
	public GPS2 east(GPS2 position){
		position.x--;
		return position;
	}
	
	public GPS2 west(GPS2 position){
		position.x++;
		return position;
	}
	
	public GPS2 north(GPS2 position){
		position.z++;
		return position;
	}
	
	public GPS2 south(GPS2 position){
		position.z--;
		return position;
	}

		public long getGpsX(){
		return this.gpsX;
	}
	
	public long getGpsY(){
		return this.gpsY;
	}
	
	public long getGpsZ(){
		return this.gpsZ;
	}
	

}
