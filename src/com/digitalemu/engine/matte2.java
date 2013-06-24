
package com.digitalemu.engine;

import org.lwjgl.util.vector.Vector3f;

import com.digitalemu.world.Material;

public class matte2 {
	boolean lookAtMaterialFound = false;
	static Vector3f gps = new Vector3f();
	static float radx,radz,rady, depy, distx, distz, disty, diffx,diffy,diffz,difft;
	static float yaw =0;
	static float pitch =90;
	static int addX, addY, addZ;
	private int lookAtDistance;
	
	
	
	
    public static void lookAt(Vector3f gps, int maxDistance){
    	addX=0;
    	addY=0;
    	addZ=0;
    	String dir = "unknown";
    	String hit = "";
    	// Calculate speed in x,z and y direction
//    	rady = (float)Math.sin(Math.toRadians(pitch));
//    	radx = (float)Math.sin(Math.toRadians(yaw));
//    	radz = (float)Math.cos(Math.toRadians(yaw));
    	depy = (float)(Math.asin(Math.toRadians(pitch)));
    	rady = (float)Math.sin(Math.toRadians(pitch));
    	radx = (float)Math.sin(Math.toRadians(yaw))*(float)(Math.cos(Math.toRadians(pitch)));
    	radz = (float)Math.cos(Math.toRadians(yaw))*(float)(Math.cos(Math.toRadians(pitch)));
//    	depy = 1-(Math.abs(pitch)/90);
//    	rady = (float)Math.sin(Math.toRadians(pitch));
//    	radx = (float)Math.sin(Math.toRadians(yaw))*(1-(Math.abs(pitch)/90));
//    	radz = (float)Math.cos(Math.toRadians(yaw))*(1-(Math.abs(pitch)/90));
    	
    	System.out.println("x: "+gps.x+" z: "+gps.z+" y: "+gps.y+" radx: "+radx+" radz: "+radz+" rady: "+rady+" depy: "+depy+"Math.toRadians(pitch) "+Math.toRadians(pitch));
    	System.out.println("sin : "+(float)(Math.sin(Math.toRadians(pitch))));
    	System.out.println("asin: "+(float)(Math.asin(Math.toRadians(pitch))));
    	System.out.println("cos: "+(float)(Math.cos(Math.toRadians(pitch))));
    	System.out.println("acos: "+(float)(Math.acos(Math.toRadians(pitch))));
    	System.out.println("tan: "+(float)(Math.tan(Math.toRadians(pitch))));
    	System.out.println("atan: "+(float)(Math.atan(Math.toRadians(pitch))));
    	System.out.println(" gps "+gps.y+" %1: "+(gps.y % 1)+
    			" gps%-0 "+((gps.y % 1)-0)+" x1 "+(((gps.y % 1)-0)/rady)+
    			" gps%-1 "+((gps.y % 1)-1)+" x2 "+(((gps.y % 1)-1)/rady));

    	do {
    		try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	// Calculate distance to next x,z and y depending on gps and speed
//	    	if (radx < 0)	{distx = ((gps.x % 1)+addX)/(radx);}				// west 
//	    	else 			{distx = ((((int)gps.x + 1)-gps.x)+addX)/(radx);}	// east
//	    	if (rady < 0)	{disty = ((gps.y % 1)+addY)/(rady);}				// down
//	    	else 			{disty = ((((int)gps.y + 1)-gps.y)+addY)/(rady);}	// up
//	    	if (radz < 0)	{distz = ((gps.z % 1)+addZ)/(radz);}				// north
//	    	else 			{distz = ((((int)gps.z + 1)-gps.z)+addZ)/(radz);}	// south
	    	
	    	if (radx < 0)	{distx = ((gps.x % 1)-addX)/radx;}					// west 
	    	else 			{distx = ((1-(gps.x % 1))+addX)/radx;}				// east
	    	if (rady < 0)	{disty = ((gps.y % 1)+addY)/rady;}					// up
	    	else 			{disty = ((1-(gps.y % 1))-addY)/rady;}				// down
	    	if (radz < 0)	{distz = ((gps.z % 1)-addZ)/radz;}					// south
	    	else 			{distz = ((-1-(gps.z % 1))+addZ)/radz;}				// north
	    	// Calculate shortest distance
	    	if(Math.abs(distx)<Math.abs(disty) && Math.abs(distx)<Math.abs(distz)){
				if(yaw<=180){ dir="east"; 	addX++;  hit="left  "; }
				else 		{ dir="west"; 	addX--;  hit="right "; }
	    	}else if(Math.abs(disty)<Math.abs(distz) && Math.abs(disty)<Math.abs(distx)){
				if(pitch>0)	{ dir="down"; 	addY--;  hit="top "; }
				else 		{ dir="up"; 	addY++;  hit="bott  "; }    	
			}else{
				if((yaw>270)||(yaw<90))	{ dir="north"; 	addZ--;  hit="front ";}
				else 		{ dir="south"; 	addZ++; hit="Back  ";}
	    	}
	
	    	System.out.println("Hit "+hit+" ["+(((int)gps.x)+addX)+"]["+(((int)gps.y)+addY)+"["+(((int)gps.z)+addZ)+"] distx: "+distx+" disty: "+disty+" distz: "+distz+" direction: "+dir+" addX: "+addX+" addZ: "+addZ+" addY: "+addY);
    	} while (Math.abs(addX)<maxDistance && Math.abs(addY)<maxDistance && Math.abs(addZ)<maxDistance);
    }

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		gps.x=9.5f;
		gps.z=-5.5f;
		gps.y=3.765f;
		lookAt(gps,5);

	}

}
