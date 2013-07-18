package com.digitalemu.monster;

import org.lwjgl.opengl.GL11;

import com.digitalemu.world.GPS;
import com.digitalemu.world.Material;
import com.digitalemu.world.World;

public class MonsterBase extends GPS{
	private String name;
	private float yaw;
	private float pitch;
	private float speed;
	private float gravity;
	private float height;
	private float width;
	private float depht;
	
	// LookAt fields
	public static int addX, addY, addZ;
	public String HowILook;
	public boolean lookAtMaterialFound;
	public short lookAtMaterial = Material.m_null;
    short       foundMaterial=0;
    public String dir;
    public String hit;
    public GPS	foundMaterialPos = new GPS();
	public static float radx, rady, radz, distx, distz, disty;		


	
public MonsterBase(String name, GPS gps){
	this.name = name;
	this.clone(gps);
	System.out.println("world:"+this.getWorld().getChunkBase());
}

public  GPS lookAt(float pitch, float yaw, int maxDistance){
	addX=0;
	addY=0;
	addZ=0;
	HowILook="";
	lookAtMaterialFound=false;
	foundMaterial = -1;
	HowILook=HowILook+"["+(int)this.getX()+"]["+(int)this.getZ()+"]["+(int)this.getY()+"] "; 
	dir = "unknown";
	hit = "";
	foundMaterialPos.clone(this);
	// Calculate speed in x,z and y direction
	rady = (float)Math.sin(Math.toRadians(pitch));
	radx = (float)Math.sin(Math.toRadians(yaw))*(float)(Math.cos(Math.toRadians(pitch)));
	radz = (float)Math.cos(Math.toRadians(yaw))*(float)(Math.cos(Math.toRadians(pitch)));
	do {
    	// Calculate distance to next x,z and y depending on gps and speed
    	if (radx < 0)	{distx = ((this.getX() % 1)-addX)/radx;}					// west 
    	else 			{distx = ((1-(this.getX() % 1))+addX)/radx;}				// east
    	if (rady < 0)	{disty = ((1-(this.getY() % 1))+addY)/rady;}				// up
    	else 			{disty = ((this.getY() % 1)-addY)/rady;}					// down
    	if (radz < 0)	{distz = ((this.getZ() % 1)-addZ)/radz;}					// south
    	else 			{distz = ((-1-(this.getZ() % 1))+addZ)/radz;}			// north
    	// Calculate shortest distance
    	if(Math.abs(distx)<Math.abs(disty) && Math.abs(distx)<Math.abs(distz)){
			if(yaw<=180){ dir="east"; 	addX++;  hit="left  "; }
			else 		{ dir="west"; 	addX--;  hit="right ";  }
	    	foundMaterialPos.setLongX(this.getLongX()+addX);
    	}else if(Math.abs(disty)<Math.abs(distz) && Math.abs(disty)<Math.abs(distx)){
			if(pitch<0)	{ dir="up"; 	addY++;  hit="botto ";  }
			else 		{ dir="down"; 	addY--;  hit="top   ";  }  
	    	foundMaterialPos.setLongY(this.getLongY()+addY);
		}else{
			if((yaw>270)||(yaw<90))	{ dir="north"; 	addZ--;  hit="front "; }
			else 		{ dir="south"; 	addZ++; hit="Back  "; }
	    	foundMaterialPos.setLongZ(this.getLongZ()+addZ);
    	}
    	//HowILook=HowILook+dir+foundMaterialPos.toMString();
    	GL11.glColor3f(1.0f, 1.0f, 1.0f);
    	//drawWireframeIndex(foundMaterialPos,0f);
		if (this.getWorld().getVoxel(foundMaterialPos) != Material.m_air){
			foundMaterial=this.getWorld().getVoxel(foundMaterialPos);
			lookAtMaterialFound=true;
			return foundMaterialPos;
		}
	} while (Math.abs(addX)<maxDistance && Math.abs(addY)<maxDistance && Math.abs(addZ)<maxDistance);
	return foundMaterialPos;
}

/**
 * detectCollision takes current position and distance as input.
 * It loops through every intermediate Voxel between start and stop.
 * If a solid Voxel in the path is found, it returns the position immediately
 * before the solid Voxel.
 * @param startPos
 * @param endPos
 * @return
 */
public float detectCollision( GPS gps, float distance, float yaw , float pitch, int i){
	addX=0;
	addY=0;
	addZ=0;
	distance+=.3f;
	System.out.print("Collision "+gps.toSString()+" D: "+distance+" yaw:"+yaw+" pitch:"+pitch);
	float totalDistance=0;
	foundMaterialPos.clone(gps);
	// Calculate speed in x,z and y direction
	rady = (float)Math.sin(Math.toRadians(pitch));
	radx = (float)Math.sin(Math.toRadians(yaw))*(float)(Math.cos(Math.toRadians(pitch)));
	radz = (float)Math.cos(Math.toRadians(yaw))*(float)(Math.cos(Math.toRadians(pitch)));
	do {
    	// Calculate distance to next x,z and y depending on gps and speed
    	if (radx < 0)	{distx = ((gps.getX() % 1)-addX)/radx;}					// west 
    	else 			{distx = ((1-(gps.getX() % 1))+addX)/radx;}				// east
    	if (rady < 0)	{disty = ((1-(gps.getY() % 1))+addY)/rady;}				// up
    	else 			{disty = ((gps.getY() % 1)-addY)/rady;}					// down
    	if (radz < 0)	{distz = ((gps.getZ() % 1)-addZ)/radz;}					// south
    	else 			{distz = ((-1-(gps.getZ() % 1))+addZ)/radz;}			// north
    	// Calculate shortest distance
    	if(Math.abs(distx)<Math.abs(disty) && Math.abs(distx)<Math.abs(distz)){
			if(yaw<=180){ dir="east"; 	addX++;  hit="left  "; }
			else 		{ dir="west"; 	addX--;  hit="right ";  }
	    	foundMaterialPos.setLongX(gps.getLongX()+addX);
	    	totalDistance = distx;
    	}else if(Math.abs(disty)<Math.abs(distz) && Math.abs(disty)<Math.abs(distx)){
			if(pitch<0)	{ dir="up"; 	addY++;  hit="botto ";  }
			else 		{ dir="down"; 	addY--;  hit="top   ";  }  
	    	foundMaterialPos.setLongY(gps.getLongY()+addY);
	    	totalDistance = disty;
		}else{
			if((yaw>270)||(yaw<90))	{ dir="north"; 	addZ--;  hit="front "; }
			else 		{ dir="south"; 	addZ++; hit="Back  "; }
	    	foundMaterialPos.setLongZ(gps.getLongZ()+addZ);
	    	totalDistance = distz;
    	}
		if (this.getWorld().getVoxel(foundMaterialPos) != Material.m_air){
			// I ran into something, now I need to back off a little.
			// For X and Z it could be enough with totaldistance-=0.3f;
			// Need to check height also.
			System.out.print(" found "+this.getWorld().getVoxel(foundMaterialPos));
    		System.out.println(" TOT:"+Math.abs(totalDistance));
    		return Math.abs(totalDistance)-0.3f;
			//break;
		}
		System.out.print(" >"+Math.abs(totalDistance));
	} while (distance > Math.abs(totalDistance));
	System.out.println(" DST:"+distance);
		return distance-0.3f;
}


	/**
	 * @return the width
	 */
	public float getWidth() {
		return width;
	}
	/**
	 * @param width the width to set
	 */
	public void setWidth(float width) {
		this.width = width;
	}
	/**
	 * @return the depht
	 */
	public float getDepht() {
		return depht;
	}
	/**
	 * @param depht the depht to set
	 */
	public void setDepht(float depht) {
		this.depht = depht;
	}
	/**
	 * @return the height
	 */
	public float getHeight() {
		return height;
	}
	/**
	 * @param height the height to set
	 */
	public void setHeight(float height) {
		this.height = height;
	}
	/**
	 * @return the gravity
	 */
	public float getGravity() {
		return gravity;
	}
	/**
	 * @param gravity the gravity to set
	 */
	public void setGravity(float gravity) {
		this.gravity = gravity;
	}
	/**
	 * @return the speed
	 */
	public float getSpeed() {
		return speed;
	}
	/**
	 * @param speed the speed to set
	 */
	public void setSpeed(float speed) {
		this.speed = speed;
	}
	/**
	 * @return the pitch
	 */
	public float getPitch() {
		return pitch;
	}
	/**
	 * @param pitch the pitch to set
	 */
	public void setPitch(float pitch) {
		this.pitch = pitch;
	}
	/**
	 * @return the yaw
	 */
	public float getYaw() {
		return yaw;
	}
	/**
	 * @param yaw the yaw to set
	 */
	public void setYaw(float yaw) {
		this.yaw = yaw;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		name = name;
	}

}
