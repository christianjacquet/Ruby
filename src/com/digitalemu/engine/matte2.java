
package com.digitalemu.engine;

import org.lwjgl.util.vector.Vector3f;

import com.digitalemu.world.Material;

public class matte2 {
	boolean lookAtMaterialFound = false;
	static Vector3f gps = new Vector3f();
	static float radx,radz,rady, distx, distz, disty, diffx,diffy,diffz,difft;
	static float yaw =30;
	static float pitch = 1;
	static int addX, addY, addZ;
	private int lookAtDistance;
	
	
	
	
    public static void lookAt(Vector3f gps, int maxDistance){
    	addX=0;
    	addY=0;
    	addZ=0;
    	String dir = "unknown";
    	// Calculate speed in x,z and y direction
    	radx = (float)Math.sin(Math.toRadians(yaw));
    	radz = (float)Math.cos(Math.toRadians(yaw));
    	rady = (float)Math.sin(Math.toRadians(pitch));
    	do {
	    	// Calculate distance to next x,z and y depending on gps and speed
	    	if (radx < 0){ 
	    		distx = ((gps.x % 1)+addX)/(radx);
	    	}
	    	else {
	    		distx = ((((int)gps.x + 1)-gps.x)+addX)/(radx);
	    	}
	    	if (rady < 0){
	    		disty = ((gps.y % 1)+addY)/(rady);
	    	}
	    	else {
	    		disty = ((((int)gps.y + 1)-gps.y)+addY)/(rady);
	    	}
	    	if (radz < 0){
	    		distz = ((gps.z % 1)+addZ)/(radz);
	    	}
	    	else {
	    		distz = ((((int)gps.z + 1)-gps.z)+addZ)/(radz);
	    	}
	    	// Calculate shortest distance
	    	if(Math.abs(distx)<Math.abs(disty) && Math.abs(distx)<Math.abs(distz)){
				if(distx>0)	{ dir="east"; 	addX++; }
				else 		{ dir="west"; 	addX--; }
	    	}else if(Math.abs(disty)<Math.abs(distz) && Math.abs(disty)<Math.abs(distx)){
				if(disty>0)	{ dir="up"; 	addY++; }
				else 		{ dir="down"; 	addY--; }    	
			}else{
				if(distz>0)	{ dir="north"; 	addZ++; }
				else 		{ dir="south"; 	addZ--; }
	    	}
	
	    	System.out.println("x: "+gps.x+" z: "+gps.z+" y: "+gps.y+" radx: "+radx+" radz: "+radz+" rady: "+rady);
	    	System.out.println("distx: "+distx+" disty: "+disty+" distz: "+distz+" direction: "+dir+" addX: "+addX+" addZ: "+addZ+" addY: "+addY);
    	} while (addX<maxDistance && addY<maxDistance && addZ<maxDistance);
    }

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		gps.x=10.5f;
		gps.z=10.5f;
		gps.y=10.5f;
		lookAt(gps,5);

	}

}
