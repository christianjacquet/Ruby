package com.digitalemu.monster;

import java.util.Arrays;

import org.lwjgl.opengl.GL11;

import com.digitalemu.world.Chunk3;
import com.digitalemu.world.GPS;
import com.digitalemu.world.Material;

public class MovableEntity extends GPS{
	private String name;
	private float lookAtYaw;
	private float lookAtPitch;
	private float MoveYaw;
	private float MovePitch;
	private float horizontalSpeed=1;
	private float minHorizontalSpeed = 0.1f;
	private float verticalSpeed=0;
	private float horizontalForce=0;
	private float verticalForce=0;
	private float acceleration=1;
	private float deceleration=1;
	private float collisionRebounce=0;
	private moveDir direction=moveDir.UNKNOWN;
	private float yaw = 0;
	private float pitch = 0;
	private float height= 1.8f;
	private float width=0.5f;
	private float depht=0.5f;
	private float gravity = 9.82f;
	
	// LookAt fields
	public static int 	addX, addY, addZ;
	public String 		HowILook;
	public boolean 		lookAtMaterialFound;
	public short 		lookAtMaterial = Material.m_null;
    short       		foundMaterial=0;
    public GPS			lookAtPos = new GPS();
	public static float radx, rady, radz, distx, distz, disty;		
	
	// collisionDetect fields
	public enum moveDir {UNKNOWN, FORWARD, BACKWARD, LEFT, RIGHT, UP, DOWN};
	private float[]		colDist;
	private GPS[]		colDistGps;
    public GPS			collisionPos = new GPS();
    int i = 0;

    // CD2 fields
    public final int axelX=0;
    public final int axelY=0;
    public final int axelZ=0;

    GPS wantedPos = new GPS();
    GPS tempPos = new GPS();
    GPS[] boundingBox;
    float[] speed;
    float[] distance;
    
	
public MovableEntity(String name, GPS gps){
	boundingBox[0] = new GPS();
	boundingBox[1] = new GPS();
	boundingBox[2] = new GPS();
	boundingBox[3] = new GPS();
	speed = new float[3];
	distance = new float[3];
	colDistGps = new GPS[4];
	colDistGps[0] = new GPS();
	colDistGps[1] = new GPS();
	colDistGps[2] = new GPS();
	colDistGps[3] = new GPS();
	colDist = new float[4];
	this.name = name;
	this.clone(gps);
	System.out.println("world:"+this.getWorld().getChunkBase());
}

/**
 * 
 * @param elapsedTime
 */
public void tick(float elapsedTime){
	if (horizontalForce > horizontalSpeed){
		horizontalSpeed += horizontalForce * acceleration;
		if (horizontalSpeed > horizontalForce){
			horizontalSpeed = horizontalForce;
		}
	}
	else {
		horizontalSpeed -= horizontalSpeed * deceleration;
		if ( horizontalSpeed < minHorizontalSpeed){
			horizontalSpeed = 0;
		}
	}
	if (verticalForce > verticalSpeed){
		verticalSpeed += verticalForce * acceleration;
		if (verticalSpeed > verticalForce){
			verticalSpeed = verticalForce;
		}
	}
	else {
		verticalSpeed -= horizontalSpeed * deceleration;
		if ( verticalSpeed < minHorizontalSpeed){
			verticalSpeed = 0;
		}
	}
	
	
	horizontalSpeed = horizontalForce * acceleration;
	this.setDistance((horizontalSpeed * elapsedTime));
	// Calculate pitch from elapsed time, gravity and vertical speed v= v+(9.81*time)
	verticalSpeed = (verticalSpeed +(elapsedTime*gravity));
}



public  GPS lookAt(GPS gps, float pitch, float yaw, int maxDistance){
	addX=0;
	addY=0;
	addZ=0;
	HowILook="";
	lookAtMaterialFound=false;
	foundMaterial = -1;
	//System.out.println("L-at: "+gps.tofString()); 

	HowILook=HowILook+"["+(int)gps.getX()+"]["+(int)gps.getZ()+"]["+(int)gps.getY()+"] "; 
	lookAtPos.clone(this);
	lookAtPos.setDirection(Direction.UNKNOWN);
	lookAtPos.setHit(VoxelSide.UNKNOWN);
	//System.out.println(lookAtPos.tofString()+" pitch: "+(int)pitch+" yaw: "+(int)yaw+" dist: "+maxDistance+" lookat");
	// Calculate speed in x,z and y direction
	calculateSpeed(pitch, yaw);
//	rady = (float)Math.sin(Math.toRadians(pitch));
//	radx = (float)Math.sin(Math.toRadians(yaw))*(float)(Math.cos(Math.toRadians(pitch)));
//	radz = (float)Math.cos(Math.toRadians(yaw))*(float)(Math.cos(Math.toRadians(pitch)));
	do {
    	// Calculate distance to next x,z and y depending on gps and speed
		calculateDistance(gps);
//    	if (radx < 0)	{distx = ((gps.getX() % 1)-addX)/radx;}					// west 
//    	else 			{distx = ((1-(gps.getX() % 1))+addX)/radx;}				// east
//    	if (rady < 0)	{disty = ((1-(gps.getY() % 1))+addY)/rady;}				// up
//    	else 			{disty = ((gps.getY() % 1)-addY)/rady;}					// down
//    	if (radz < 0)	{distz = ((gps.getZ() % 1)-addZ)/radz;}					// south
//    	else 			{distz = ((-1-(gps.getZ() % 1))+addZ)/radz;}			// north
    	// Calculate shortest distance
		findShortestDistance(gps, pitch, yaw);
//    	if(Math.abs(distx)<Math.abs(disty) && Math.abs(distx)<Math.abs(distz)){
//			if(yaw<=180){ 
//				lookAtPos.setDirection(Direction.EAST); 	
//				lookAtPos.setHit(VoxelSide.LEFT);
//				addX++;  
//			}
//			else 		{ 
//				lookAtPos.setDirection(Direction.WEST);
//				lookAtPos.setHit(VoxelSide.RIGHT);
//				addX--;  
//			}
//	    	lookAtPos.setLongX(gps.getLongX()+addX);
//	    	lookAtPos.setDistance(distx);
//    	}else if(Math.abs(disty)<Math.abs(distz) && Math.abs(disty)<Math.abs(distx)){
//			if(pitch<0)	{ 
//				lookAtPos.setDirection(Direction.UP);
//				lookAtPos.setHit(VoxelSide.BOTTOM);
//				addY++;  
//			}
//			else 		{ 
//				lookAtPos.setDirection(Direction.DOWN);
//				lookAtPos.setHit(VoxelSide.TOP);
//				addY--;  
//			}  
//	    	lookAtPos.setLongY(gps.getLongY()+addY);
//	    	lookAtPos.setDistance(disty);
//		}else{
//			if((yaw>270)||(yaw<90))	{ 
//				lookAtPos.setDirection(Direction.NORTH); 
//				lookAtPos.setHit(VoxelSide.FRONT);
//				addZ--;  
//			}
//			else 		{ 
//				lookAtPos.setDirection(Direction.SOUTH);
//				lookAtPos.setHit(VoxelSide.BACK);
//				addZ++; 
//			}
//	    	lookAtPos.setLongZ(gps.getLongZ()+addZ);
//	    	lookAtPos.setDistance(distz);
//    	}
    	//HowILook=HowILook+dir+foundMaterialPos.toMString();
    	GL11.glColor3f(1.0f, 1.0f, 1.0f);
		if (gps.getWorld().getVoxel(lookAtPos) != Material.m_air){
			foundMaterial=gps.getWorld().getVoxel(lookAtPos);
			lookAtMaterialFound=true;
			lookAtPos.setMaterial(gps.getWorld().getVoxel(lookAtPos));
			return lookAtPos;
		}
	} while (Math.abs(addX)<maxDistance && Math.abs(addY)<maxDistance && Math.abs(addZ)<maxDistance);
	return lookAtPos;
}


private void calculateSpeed(float pitch, float yaw){
	rady = (float)Math.sin(Math.toRadians(pitch));
	radx = (float)Math.sin(Math.toRadians(yaw))*(float)(Math.cos(Math.toRadians(pitch)));
	radz = (float)Math.cos(Math.toRadians(yaw))*(float)(Math.cos(Math.toRadians(pitch)));
}

private void calculateDistance(GPS gps){
	if (radx < 0)	{distx = ((gps.getX() % 1)-addX)/radx;}					// west 
	else 			{distx = ((1-(gps.getX() % 1))+addX)/radx;}				// east
	if (rady < 0)	{disty = ((1-(gps.getY() % 1))+addY)/rady;}				// up
	else 			{disty = ((gps.getY() % 1)-addY)/rady;}					// down
	if (radz < 0)	{distz = ((gps.getZ() % 1)-addZ)/radz;}					// south
	else 			{distz = ((-1-(gps.getZ() % 1))+addZ)/radz;}			// north
}

private void findShortestDistance(GPS gps, float pitch, float yaw){
	if(Math.abs(distx)<Math.abs(disty) && Math.abs(distx)<Math.abs(distz)){
		if(yaw<=180){ 
			lookAtPos.setDirection(Direction.EAST); 	
			lookAtPos.setHit(VoxelSide.LEFT);
			addX++;  
		}
		else 		{ 
			lookAtPos.setDirection(Direction.WEST);
			lookAtPos.setHit(VoxelSide.RIGHT);
			addX--;  
		}
    	lookAtPos.setLongX(gps.getLongX()+addX);
    	lookAtPos.setDistance(distx);
	}else if(Math.abs(disty)<Math.abs(distz) && Math.abs(disty)<Math.abs(distx)){
		if(pitch<0)	{ 
			lookAtPos.setDirection(Direction.UP);
			lookAtPos.setHit(VoxelSide.BOTTOM);
			addY++;  
		}
		else 		{ 
			lookAtPos.setDirection(Direction.DOWN);
			lookAtPos.setHit(VoxelSide.TOP);
			addY--;  
		}  
    	lookAtPos.setLongY(gps.getLongY()+addY);
    	lookAtPos.setDistance(disty);
	}else{
		if((yaw>270)||(yaw<90))	{ 
			lookAtPos.setDirection(Direction.NORTH); 
			lookAtPos.setHit(VoxelSide.FRONT);
			addZ--;  
		}
		else 		{ 
			lookAtPos.setDirection(Direction.SOUTH);
			lookAtPos.setHit(VoxelSide.BACK);
			addZ++; 
		}
    	lookAtPos.setLongZ(gps.getLongZ()+addZ);
    	lookAtPos.setDistance(distz);
	}
}

public  GPS cd2(GPS gps, float pitch, float yaw, int maxDistance, moveDir dir){
	addX=0;
	addY=0;
	addZ=0;

	wantedPos.clone(this);
	wantedPos.setDirection(Direction.UNKNOWN);
	wantedPos.setHit(VoxelSide.UNKNOWN);
	
	// Calculate speed in x,y and z direction
	calculateSpeed(pitch, yaw);
	switch (dir){
	case FORWARD : {
		wantedPos.add2FloatX(this.getWidth()/2);
	}
	}
	// Calculate distance from my current position
	calculateDistance(wantedPos);
	
	
	do {
    	// Calculate distance to next x,z and y border depending on this.gps and speed

    	// Calculate shortest distance
    	if(Math.abs(distx)<Math.abs(disty) && Math.abs(distx)<Math.abs(distz)){
			if(yaw<=180){ 
				wantedPos.setDirection(Direction.EAST); 	
				wantedPos.setHit(VoxelSide.LEFT);
				addX++;  
			}
			else 		{ 
				wantedPos.setDirection(Direction.WEST);
				wantedPos.setHit(VoxelSide.RIGHT);
				addX--;  
			}
			wantedPos.setLongX(gps.getLongX()+addX);
			wantedPos.setDistance(distx);
    	}else if(Math.abs(disty)<Math.abs(distz) && Math.abs(disty)<Math.abs(distx)){
			if(pitch<0)	{ 
				wantedPos.setDirection(Direction.UP);
				wantedPos.setHit(VoxelSide.BOTTOM);
				addY++;  
			}
			else 		{ 
				wantedPos.setDirection(Direction.DOWN);
				wantedPos.setHit(VoxelSide.TOP);
				addY--;  
			}  
			wantedPos.setLongY(gps.getLongY()+addY);
			wantedPos.setDistance(disty);
		}else{
			if((yaw>270)||(yaw<90))	{ 
				wantedPos.setDirection(Direction.NORTH); 
				wantedPos.setHit(VoxelSide.FRONT);
				addZ--;  
			}
			else 		{ 
				wantedPos.setDirection(Direction.SOUTH);
				wantedPos.setHit(VoxelSide.BACK);
				addZ++; 
			}
			wantedPos.setLongZ(gps.getLongZ()+addZ);
			wantedPos.setDistance(distz);
    	}
    	//HowILook=HowILook+dir+foundMaterialPos.toMString();
		if (gps.getWorld().getVoxel(lookAtPos) != Material.m_air){
			foundMaterial=gps.getWorld().getVoxel(lookAtPos);
			lookAtMaterialFound=true;
			lookAtPos.setMaterial(gps.getWorld().getVoxel(lookAtPos));
			return lookAtPos;
		}
	} while (Math.abs(addX)<maxDistance && Math.abs(addY)<maxDistance && Math.abs(addZ)<maxDistance);
	return lookAtPos;
}




public void cd(float time, float yaw, float pitch, float distance, moveDir dir){
	// I have my x,y,z position
	// calculate speed
	calculateSpeed(pitch, yaw);
	// calculate desired x,y,z position
	// calculate all eight bounding box coordinates
	boundingBox[GPS.BoxVertice.BLF.ordinal()]
			.clone(this)
			.moveRelative((this.getWidth()/2), moveDir.LEFT, pitch, yaw)		// Bottom LEFT Forward
			.moveRelative((this.getDepht()/2), moveDir.FORWARD, pitch, yaw);
	boundingBox[GPS.BoxVertice.BRF.ordinal()]
			.clone(this)
			.moveRelative((this.getWidth()/2), moveDir.RIGHT, pitch, yaw)		// Bottom RIGHT Forward
			.moveRelative((this.getDepht()/2), moveDir.FORWARD, pitch, yaw);
	boundingBox[GPS.BoxVertice.BLB.ordinal()]
			.clone(this)
			.moveRelative((this.getWidth()/2), moveDir.LEFT, pitch, yaw)		// Bottom LEFT Backward
			.moveRelative((this.getDepht()/2), moveDir.BACKWARD, pitch, yaw);
	boundingBox[GPS.BoxVertice.BRB.ordinal()]
			.clone(this)
			.moveRelative((this.getWidth()/2), moveDir.RIGHT, pitch, yaw)		// Bottom RIGHT Backward
			.moveRelative((this.getDepht()/2), moveDir.BACKWARD, pitch, yaw);
	boundingBox[GPS.BoxVertice.TLF.ordinal()]
			.clone(boundingBox[GPS.BoxVertice.BLF.ordinal()])					// Top LEFT Forward
			.moveRelative(this.getHeight(), moveDir.UP, pitch, yaw);	
	boundingBox[GPS.BoxVertice.TRF.ordinal()]
			.clone(boundingBox[GPS.BoxVertice.BRF.ordinal()])					// Top RIGHT Forward
			.moveRelative(this.getHeight(), moveDir.UP, pitch, yaw);
	boundingBox[GPS.BoxVertice.TLB.ordinal()]
			.clone(boundingBox[GPS.BoxVertice.BLB.ordinal()])					// Top LEFT Backward
			.moveRelative(this.getHeight(), moveDir.UP, pitch, yaw);
	boundingBox[GPS.BoxVertice.TRB.ordinal()]
			.clone(boundingBox[GPS.BoxVertice.BRB.ordinal()])					// Top RIGHT Backward
			.moveRelative(this.getHeight(), moveDir.UP, pitch, yaw);
	
	
	
	
	boundingBox[GPS.BoxVertice.BLF.ordinal()].setFloatX((this.getX()+((this.getWidth()/2) * (float)Math.sin(Math.toRadians(yaw-90)))));
	boundingBox[GPS.BoxVertice.BLF.ordinal()].setFloatY((this.getY()-((this.getWidth()/2)) * (float)Math.cos(Math.toRadians(yaw-90))));
	//  Top left
	boundingBox[GPS.BoxVertice.BLF.ordinal()].clone(boundingBox[GPS.BoxVertice.BLF.ordinal()]); 
	boundingBox[1].setFloatY(this.getY()+this.getHeight());
	// Bottom right
	boundingBox[2].clone(this); 
	boundingBox[2].setFloatX((this.getX()+((this.getWidth()/2) * (float)Math.sin(Math.toRadians(yaw+90)))));
	boundingBox[2].setFloatY((this.getY()-((this.getWidth()/2)) * (float)Math.cos(Math.toRadians(yaw+90))));
	// Top right
	boundingBox[3].clone(boundingBox[2]); 
	boundingBox[3].setFloatY(this.getY()+this.getHeight());
    // calculate distance to nearest voxel edge
	calculateDistance(tempPos);

	// Loop
	//   run lookAt for all four bounding box corners  
	
}

public void collisionDetect(float time, float yaw, float pitch, float distance, moveDir dir){
	collisionPos.clone(this);
	collisionPos.setDirection(Direction.UNKNOWN);
	collisionPos.setHit(VoxelSide.UNKNOWN);
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
    	else 			{distz = ((-1-(this.getZ() % 1))+addZ)/radz;}				// north
    	// Calculate shortest distance
    	if(Math.abs(distx)<Math.abs(disty) && Math.abs(distx)<Math.abs(distz)){
			if(yaw<=180){ 
				collisionPos.setDirection(Direction.EAST); 	
				collisionPos.setHit(VoxelSide.LEFT);
				addX++;  
			}
			else 		{ 
				collisionPos.setDirection(Direction.WEST);
				collisionPos.setHit(VoxelSide.RIGHT);
				addX--;  
			}
			collisionPos.setLongX(this.getLongX()+addX);
			collisionPos.setDistance(distx);
    	}else if(Math.abs(disty)<Math.abs(distz) && Math.abs(disty)<Math.abs(distx)){
			if(pitch<0)	{ 
				collisionPos.setDirection(Direction.UP);
				collisionPos.setHit(VoxelSide.BOTTOM);
				addY++;  
			}
			else 		{ 
				collisionPos.setDirection(Direction.DOWN);
				collisionPos.setHit(VoxelSide.TOP);
				addY--;  
			}  
			collisionPos.setLongY(this.getLongY()+addY);
			collisionPos.setDistance(disty);
		}else{
			if((yaw>270)||(yaw<90))	{ 
				collisionPos.setDirection(Direction.NORTH); 
				collisionPos.setHit(VoxelSide.FRONT);
				addZ--;  
			}
			else 		{ 
				collisionPos.setDirection(Direction.SOUTH);
				collisionPos.setHit(VoxelSide.BACK);
				addZ++; 
			}
			collisionPos.setLongZ(this.getLongZ()+addZ);
			collisionPos.setDistance(distz);
    	}
 
    	// Check if NOT solid ground below me
    	if (collisionPos.getWorld().getVoxel(collisionPos.getLongX(), (collisionPos.getLongY()-1), collisionPos.getLongZ())==Material.m_air){
    		
    	}
    	
	} while (distance>collisionPos.getDistance());
	switch (dir) {
		case FORWARD : {
			this.add2FloatX(distance * (float)Math.sin(Math.toRadians(yaw)));
			this.add2FloatZ(0-(distance * (float)Math.cos(Math.toRadians(yaw))));
			this.getY();
			break;
		}
		case BACKWARD :{
			this.add2FloatX(0-(distance * (float)Math.sin(Math.toRadians(yaw))));
			this.add2FloatZ(distance * (float)Math.cos(Math.toRadians(yaw)));
			break;
		}
		case LEFT : {
			this.add2FloatX(distance * (float)Math.sin(Math.toRadians(yaw-90)));
			this.add2FloatZ(0-(distance * (float)Math.cos(Math.toRadians(yaw-90))));
			break;
		}
		case RIGHT : {
			this.add2FloatX(distance * (float)Math.sin(Math.toRadians(yaw+90)));
			this.add2FloatZ(0-(distance * (float)Math.cos(Math.toRadians(yaw+90))));
			break;
		}
	}
}

private GPS checkCollision(float yaw , float pitch, moveDir dir){
	System.out.println("cPos1 "+collisionPos.tofString());

	// Set depth depending on direction
	switch (dir) {
	case FORWARD : { 
		collisionPos.setFloatZ(this.getZ()-(this.getDepht()/2));
		break; }
	case BACKWARD : {
		collisionPos.setFloatZ(this.getZ()+(this.getDepht()/2));
		break; }
	case LEFT : {
		collisionPos.setFloatZ(this.getZ()-(this.getWidth()/2));
		break; }
	case RIGHT : {
		collisionPos.setFloatZ(this.getZ()+(this.getWidth()/2));
		break; }
	}
	System.out.println("cPos2 "+collisionPos.tofString());

	// Find distance to first collision, if any, for each corner.
	collisionPos.setFloatX(this.getX()-(this.getWidth()/2)); 	// lower left corner
	colDistGps[0] = (lookAt(collisionPos, pitch, yaw, 5));
	colDist[0] = Math.abs(colDistGps[0].getDistance());
	collisionPos.setFloatX(this.getX()+(this.getWidth()/2));	// lower right corner
	colDistGps[1] = (lookAt(collisionPos, pitch, yaw, 5));
	colDist[1] = Math.abs(colDistGps[1].getDistance());
	collisionPos.setFloatY(this.getY()+(this.getHeight()));		// upper right corner
	colDistGps[2] = (lookAt(collisionPos, pitch, yaw, 5));
	colDist[2] = Math.abs(colDistGps[2].getDistance());
	collisionPos.setFloatX(this.getX()-(this.getWidth()/2));	// upper left corner
	colDistGps[3] = (lookAt(collisionPos, pitch, yaw, 5));
	colDist[3] = Math.abs(colDistGps[3].getDistance());
	// Find shortest distance to collision, if any
	System.out.println("cPos3 "+collisionPos.tofString());

	Arrays.sort(colDist);
	System.out.println("This: "+this.tofString()+"  "+dir.toString()); 
	System.out.print("Cpos: "+collisionPos.tofString()+" pitch: "+(int)pitch+" yaw: "+(int)yaw+" dist: "+this.getDistance()+" ## ");
	for(int i =0; i < 4;  i++){
		System.out.print(" - "+colDist[i]);
	}
	System.out.println(" ...");
	// Return shortest distance to collision, if any, or return requested distance
	for(i =0; i < 4;  i++){
		if((colDist[i]>0) && (colDist[i]<this.getDistance())){			// I hit something
			colDistGps[i].setDistance(colDist[i]);
			return colDistGps[i];
		}
	}
	collisionPos.setHit(VoxelSide.UNKNOWN);
	return collisionPos;
}

public void detectCollision(float elapsedTime, float yaw , float pitch, moveDir dir){
	// TODO
	// Take care of gravity and check if i fall down a hole.
	// Take care of strafing left, right and going backwards.
	// Take care of collision damage
	// Take care of correcting path if I run with an angle into something
	
	// Calculate distance from elapsed time and horizontal speed
	this.setDistance((horizontalSpeed * elapsedTime));
	// Calculate pitch from elapsed time, gravity and vertical speed v= v+(9.81*time)
	verticalSpeed = (verticalSpeed +(elapsedTime*gravity));
	// Clone current position
	collisionPos.clone(this);
	// Check for collision
	collisionPos = checkCollision(yaw , pitch, dir);
	// If collision try sliding
	if (collisionPos.getHit()!=VoxelSide.UNKNOWN){
		System.out.println("Collision detected @ "+collisionPos.tofString());
		if (collisionPos.getHit()==VoxelSide.BOTTOM) {		// I hit the ceiling
			verticalSpeed=0;
			collisionPos.add2FloatY(-0.01f);
			// Maybe horizontal speed should decrease a little too?
			this.setDistance((this.getDistance() - collisionPos.getDistance()) * (float)Math.sin(Math.toRadians(yaw)));
			collisionPos = checkCollision(yaw , pitch, dir);
		}
		if (collisionPos.getHit()==VoxelSide.TOP) {			// I hit the floor
			verticalSpeed=0;
			collisionPos.setFloatX(0.0f);
			// Maybe horizontal speed should decrease a little too?
			this.setDistance((this.getDistance() - collisionPos.getDistance()) * (float)Math.sin(Math.toRadians(yaw)));
			collisionPos = checkCollision(yaw , pitch, dir);
		}
		if (yaw < 90){
			if (collisionPos.getHit()==VoxelSide.FRONT){
				yaw=90;
			}
			else if (collisionPos.getHit()==VoxelSide.RIGHT){
				yaw=0;
			}
			else { 
				System.out.println("Helt fel. yaw "+yaw+" inte front eller right utan "+collisionPos.getHit().toString());
			}
			System.out.println("Collision detected @  old pos "+collisionPos.tofString());
			// Set distance just before collision
			collisionPos.setDistance(this.getDistance()-collisionPos.getDistance()-0.01f);
			// Set new position 
	        this.add2FloatX(collisionPos.getDistance() * (float)Math.sin(Math.toRadians(yaw)));
	        this.add2FloatZ(0-(collisionPos.getDistance() * (float)Math.cos(Math.toRadians(yaw))));
			System.out.println("Collision detected @  new pos "+this.tofString());
			// Calculate sliding distance
			this.setDistance((collisionPos.getDistance()) * (float)Math.sin(Math.toRadians(yaw)));
			System.out.println("Collision detected @ new distance "+collisionPos.tofString());
			collisionPos.clone(this);


			collisionPos = checkCollision(yaw , pitch, dir);
		}
		else if(yaw < 180){
			if (collisionPos.getHit()==VoxelSide.BACK){
				yaw=270;
			}
			else if (collisionPos.getHit()==VoxelSide.RIGHT){
				yaw=180;
			}
			else { 
				System.out.println("Helt fel. yaw "+yaw+" inte back eller right utan "+collisionPos.getHit().toString());
			}
			this.setDistance((this.getDistance() - collisionPos.getDistance()) * (float)Math.sin(Math.toRadians(yaw)));
			collisionPos = checkCollision(yaw , pitch, dir);
		}
		else if(yaw < 270){
			if (collisionPos.getHit()==VoxelSide.BACK){
				yaw=270;
			}
			else if (collisionPos.getHit()==VoxelSide.LEFT){
				yaw=180;
			}
			else { 
				System.out.println("Helt fel. yaw "+yaw+" inte back eller left utan "+collisionPos.getHit().toString());
			}
			this.setDistance((this.getDistance() - collisionPos.getDistance()) * (float)Math.sin(Math.toRadians(yaw)));
			collisionPos = checkCollision(yaw , pitch, dir);
		}
		else {
			if (collisionPos.getHit()==VoxelSide.FRONT){
				yaw=270;
			}
			else if (collisionPos.getHit()==VoxelSide.LEFT){
				yaw=0;
			}
			else { 
				System.out.println("Helt fel. yaw "+yaw+" inte front eller left utan "+collisionPos.getHit().toString());
			}
			this.setDistance((this.getDistance() - collisionPos.getDistance()) * (float)Math.sin(Math.toRadians(yaw)));
			collisionPos = checkCollision(yaw , pitch, dir);
		}
	}
	// Save new position
	switch (dir) {
		case FORWARD : {
			this.add2FloatX(collisionPos.getDistance() * (float)Math.sin(Math.toRadians(yaw)));
			this.add2FloatZ(0-(collisionPos.getDistance() * (float)Math.cos(Math.toRadians(yaw))));
			this.getY();
			break;
		}
		case BACKWARD :{
			this.add2FloatX(0-(collisionPos.getDistance() * (float)Math.sin(Math.toRadians(yaw))));
			this.add2FloatZ(collisionPos.getDistance() * (float)Math.cos(Math.toRadians(yaw)));
			break;
		}
		case LEFT : {
			this.add2FloatX(collisionPos.getDistance() * (float)Math.sin(Math.toRadians(yaw-90)));
			this.add2FloatZ(0-(collisionPos.getDistance() * (float)Math.cos(Math.toRadians(yaw-90))));
			break;
		}
		case RIGHT : {
			this.add2FloatX(collisionPos.getDistance() * (float)Math.sin(Math.toRadians(yaw+90)));
			this.add2FloatZ(0-(collisionPos.getDistance() * (float)Math.cos(Math.toRadians(yaw+90))));
			break;
		}
	}
}

///**
// * detectCollision takes current position and distance as input.
// * It loops through every intermediate Voxel between start and stop.
// * If a solid Voxel in the path is found, it returns the position immediately
// * before the solid Voxel.
// * @param startPos
// * @param endPos
// * @return
// */
//public float detectCollision( GPS gps, float distance, float yaw , float pitch, int i){
//	addX=0;
//	addY=0;
//	addZ=0;
//	distance+=.3f;
//	System.out.print("Collision "+gps.toSString()+" D: "+distance+" yaw:"+yaw+" pitch:"+pitch);
//	float totalDistance=0;
//	lookAtPos.clone(gps);
//	// Calculate speed in x,z and y direction
//	rady = (float)Math.sin(Math.toRadians(pitch));
//	radx = (float)Math.sin(Math.toRadians(yaw))*(float)(Math.cos(Math.toRadians(pitch)));
//	radz = (float)Math.cos(Math.toRadians(yaw))*(float)(Math.cos(Math.toRadians(pitch)));
//	do {
//    	// Calculate distance to next x,z and y depending on gps and speed
//    	if (radx < 0)	{distx = ((gps.getX() % 1)-addX)/radx;}					// west 
//    	else 			{distx = ((1-(gps.getX() % 1))+addX)/radx;}				// east
//    	if (rady < 0)	{disty = ((1-(gps.getY() % 1))+addY)/rady;}				// up
//    	else 			{disty = ((gps.getY() % 1)-addY)/rady;}					// down
//    	if (radz < 0)	{distz = ((gps.getZ() % 1)-addZ)/radz;}					// south
//    	else 			{distz = ((-1-(gps.getZ() % 1))+addZ)/radz;}			// north
//    	// Calculate shortest distance
//    	if(Math.abs(distx)<Math.abs(disty) && Math.abs(distx)<Math.abs(distz)){
//			if(yaw<=180){ 
//				lookAtPos.setDirection(Direction.EAST); 	
//				lookAtPos.setHit(VoxelSide.LEFT);
//				addX++;  
//			}
//			else 		{ 
//				lookAtPos.setDirection(Direction.WEST);
//				lookAtPos.setHit(VoxelSide.RIGHT);
//				addX--;  
//			}
//	    	lookAtPos.setLongX(this.getLongX()+addX);
//	    	totalDistance = distx;
//    	}else if(Math.abs(disty)<Math.abs(distz) && Math.abs(disty)<Math.abs(distx)){
//			if(pitch<0)	{ 
//				lookAtPos.setDirection(Direction.UP);
//				lookAtPos.setHit(VoxelSide.BOTTOM);
//				addY++;  
//			}
//			else 		{ 
//				lookAtPos.setDirection(Direction.DOWN);
//				lookAtPos.setHit(VoxelSide.TOP);
//				addY--;  
//			}  
//	    	lookAtPos.setLongY(this.getLongY()+addY);
//	    	totalDistance = disty;
//		}else{
//			if((yaw>270)||(yaw<90))	{ 
//				lookAtPos.setDirection(Direction.NORTH); 
//				lookAtPos.setHit(VoxelSide.FRONT);
//				addZ--;  
//			}
//			else 		{ 
//				lookAtPos.setDirection(Direction.SOUTH);
//				lookAtPos.setHit(VoxelSide.BACK);
//				addZ++; 
//			}
//	    	lookAtPos.setLongZ(this.getLongZ()+addZ);
//	    	totalDistance = distz;
//    	}
//		if (this.getWorld().getVoxel(lookAtPos) != Material.m_air){
//			// I ran into something, now I need to back off a little.
//			// For X and Z it could be enough with totaldistance-=0.3f;
//			// Need to check height also.
//			System.out.print(" found "+this.getWorld().getVoxel(lookAtPos));
//    		System.out.println(" TOT:"+Math.abs(totalDistance));
//    		return Math.abs(totalDistance)-0.3f;
//			//break;
//		}
//		System.out.print(" >"+Math.abs(totalDistance));
//	} while (distance > Math.abs(totalDistance));
//	System.out.println(" DST:"+distance);
//		return distance-0.3f;
//}


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
		return verticalSpeed;
	}
	/**
	 * @param gravity the gravity to set
	 */
	public void setGravity(float gravity) {
		this.verticalSpeed = gravity;
	}
	/**
	 * @return the speed
	 */
	public float getSpeed() {
		return horizontalSpeed;
	}
	/**
	 * @param speed the speed to set
	 */
	public void setSpeed(float speed) {
		this.horizontalSpeed = speed;
	}
	/**
	 * @return the pitch
	 */
	public float getPitch() {
		return lookAtPitch;
	}
	/**
	 * @param pitch the pitch to set
	 */
	public void setPitch(float pitch) {
		this.lookAtPitch = pitch;
	}
	/**
	 * @return the yaw
	 */
	public float getYaw() {
		return lookAtYaw;
	}
	/**
	 * @param yaw the yaw to set
	 */
	public void setYaw(float yaw) {
		this.lookAtYaw = yaw;
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



	/**
	 * @return the horizontalForce
	 */
	public float getHorizontalForce() {
		return horizontalForce;
	}



	/**
	 * @param horizontalForce the horizontalForce to set
	 */
	public void setHorizontalForce(float horizontalForce, float yaw, float pitch, moveDir dir) {
		this.horizontalForce = horizontalForce;
		this.yaw = yaw;
		this.pitch = pitch;
		this.direction = dir;
	}



	/**
	 * @return the verticalForce
	 */
	public float getVerticalForce() {
		return verticalForce;
	}



	/**
	 * @param verticalForce the verticalForce to set
	 */
	public void setVerticalForce(float verticalForce, float yaw, float pitch) {
		this.verticalForce = verticalForce;
		this.yaw = yaw;
		this.pitch = pitch;
	}

}
