package com.digitalemu.world;

import org.lwjgl.util.vector.Vector3f;

public class GPS{
	long westEast;		// West  = -	East  = +
	long northSouth;	// North = - 	South = + 
	long upDown;		// Up    = +    Down  = -
	
	public GPS(){
		this.westEast=0;
		this.northSouth=0;
		this.upDown=0;
	}
	
	public GPS(long westEast, long upDown, long southNorth){
		this.westEast=westEast;
		this.northSouth=southNorth;
		this.upDown=upDown;
	}
	
	public GPS(float westEast, float upDown, float southNorth){
		this.westEast=(long) westEast;
		this.northSouth=(long) southNorth;
		this.upDown=(long) upDown;
	}
	
	public GPS(Vector3f vector){
		this.westEast=(long)vector.x;
		this.upDown=(long)vector.y;
		this.northSouth=(long)vector.z;
	}
	
	public long getX(){
		return this.westEast;
	}
	
	public long getZ(){
		return this.northSouth;
	}
	
	public long getY(){
		return this.upDown;
	}
	
	public long getWestEast(){
		return this.westEast;
	}
	
	public long getSouthNorth(){
		return this.northSouth;
	}
	
	public long getUpDown(){
		return this.upDown;
	}
	
	public void setWestEast(long val){
		this.westEast=val;
	}
	
	public void setSouthNorth(long val){
		this.northSouth=val;
	}
	
	public void setUpDown(long val){
		this.upDown=val;
	}
	
	public void up(){
		this.upDown++;
	}

	public void down(){
		this.upDown--;
	}	
		
	public void east(){
		this.westEast++;
	}
	
	public void west(){
		this.westEast--;
	}
	
	public void north(){
		this.northSouth--;
	}
	
	public void south(){
		this.northSouth++;	
	}
	
	public GPS vector3f2GPS(Vector3f vector){
		this.northSouth=(long)vector.z;
		this.upDown=(long)vector.y;
		this.westEast=(long)vector.x;
	return this;	
	}
	
	public void setGPS(GPS gps){
		this.northSouth = gps.northSouth;
		this.upDown = gps.upDown;
		this.westEast = gps.westEast;
	}
	
	public void setGPS(Vector3f gps){
		this.northSouth = (long)gps.z;
		this.upDown = (long)gps.y;
		this.westEast = (long)gps.x;;
	}
	
	public void setGPSRound(Vector3f gps){
		this.northSouth = (long)(gps.z+0.5f);
		this.upDown = (long)(gps.y+0.5f);
		this.westEast = (long)(gps.x+0.5f);
	}
	

	
	
	public static String gps2str(long westEast, long northSouth, long upDown){
		return "x"+Long.toString(westEast)+"y"+Long.toString(upDown)+"z"+Long.toString(northSouth);
	}
	// Convert GPS position to String
	// input: 32,44,789
	// output: "x32y44z789
	public String gps2str(){
		return "x"+Long.toString(this.westEast)+"y"+Long.toString(this.upDown)+"z"+Long.toString(this.northSouth);
	}
	
	public String toMString(){
		return "["+this.westEast+"]["+this.northSouth+"]["+this.upDown+"]";
	}
	
	public String toSString(){
		return "ABS GPS position: EW: "+this.westEast+" NS: "+this.northSouth+" UD: "+this.upDown;
	}
	
	/**
	 * Calculate distance between this x and provided x position
	 * @param long x
	 * @return distance in voxel units
	 */
	public long getDistanceX(long x){
		return Math.abs(this.westEast-westEast);
	}
	
	/**
	 * Calculate distance between this y and provided y position
	 * @param long y
	 * @return distance in voxel units
	 */
	public long getDistanceY(long y){
		return Math.abs(this.northSouth-northSouth);
	}
	
	/**
	 * Calculate distance between this z and provided z position
	 * @param long z
	 * @return distance in voxel units
	 */
	public long getDistanceZ(long z){
		return Math.abs(this.upDown-upDown);
	}
	
	
	/**
	 * Calculate distance between this and provided GPS positions
	 * @param a GPS position
	 * @return distance in voxel units
	 */
	public long getDistance(GPS gps){
		long distance;
		distance = Math.abs(gps.getWestEast()-westEast);
		distance = Math.max(Math.abs(gps.getSouthNorth())-northSouth,distance);
		distance = Math.max(Math.abs(gps.getUpDown())-upDown,distance);
		return distance;
	}
}
