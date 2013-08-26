package com.digitalemu.monster;


import org.lwjgl.opengl.GL11;

import com.digitalemu.world.Chunk3;
import com.digitalemu.world.GPS;
import com.digitalemu.world.Material;

public class MovableEntity extends GPS{
	private String name;
	private float lookAtYaw;
	private float lookAtPitch;
	private float horizontalSpeed=0.2f;
	private float minHorizontalSpeed = 0.1f;
	private float verticalSpeed=0;
	private float horizontalForce=0;
	private float verticalForce=0;
	private float acceleration=1;
	private float deceleration=1;
	private float collisionRebounce=0;
	private float yaw = 0;
	private float pitch = 0;
	private ASWDdir aswd;
	private float height= 1.8f;
	private float width=0.5f;
	private float depth=0.5f;
	private float halfWidth=width/2;
	private float halfDepth=depth/2f;
	private float halfHeight=height/2;
	private float gravity = 9.82f;
	
	// LookAt fields
	public static int 	addX, addY, addZ;
	public String 		HowILook;
	public boolean 		lookAtMaterialFound;
	public short 		lookAtMaterial = Material.m_null;
    short       		foundMaterial=0;
    public GPS			lookAtPos = new GPS();
	public static float radx, rady, radz, distx, distz, disty;		
	
	// CollisionDetect fields
	private boolean blockXEast, blockXWest, blockYUp, blockYDown, blockZNorth, blockZSouth;
	private float	dist2XBorder, dist2YBorder, dist2ZBorder;
	private float	reqDistance;
	private boolean positionFound;
	private Direction CDDir;
	private GPS		CDpos = new GPS();
	private float   CDYaw;
	private float	CDPitch;
	public enum Quadrant { NORTHEAST, SOUTHEAST, SOUTHWEST, NORTWEST }
	private Quadrant quadrant;
	
public MovableEntity(String name, GPS gps){
	this.name = name;
	this.clone(gps);
	this.blockXEast=false;
	this.blockXWest=false;
	this.blockYUp=false;
	this.blockYDown=false;
	this.blockZNorth=false;
	this.blockZSouth=false;
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
	HowILook=HowILook+"["+(int)gps.getX()+"]["+(int)gps.getZ()+"]["+(int)gps.getY()+"] "; 
	lookAtPos.clone(this);
	lookAtPos.setDirection(Direction.UNKNOWN);
	lookAtPos.setHit(VoxelSide.UNKNOWN);
	calculateSpeed(pitch, yaw);
	do {
	calculateDistance(gps);
	findShortestDistance(gps, pitch, yaw);
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

// Collision Detect

private Quadrant getQuadrant(){
	if(yaw > 270)return Quadrant.NORTWEST;
	if(yaw > 180)return Quadrant.SOUTHWEST;
	if(yaw > 90) return Quadrant.SOUTHEAST;
	return Quadrant.NORTHEAST;
}

/** Finds direction and distance to nearest voxel border regarding entity position,
 *   yaw, pitch and entity width and height and filtering out blocked directions
 *   Input: gps, yaw, pitch, width, height
 *   Output: borderDirection, dist2XBorder, dist2YBorder, dist2ZBorder
 */  
private Direction getNearestBorderAndDistance(){
	//System.out.print(" XE:"+blockXEast+" XW:"+blockXWest+" YU:"+blockYUp+" YD:"+blockYDown+" ZN:"+blockZNorth+" ZS:"+blockZSouth+" rx:"+radx+" ry:"+rady+" rz:"+radz+" cx:"+CDpos.x+" cy:"+CDpos.y+" cz:"+CDpos.z);
	if((blockXWest==false) && blockXEast==false){
		if (radx < 0)	{
			if ((CDpos.x %1-halfWidth)<=0){
				dist2XBorder = (1+ ((CDpos.x % 1 - halfWidth)))  /radx; 				// west border cross
			}																			 
			else{
				dist2XBorder = (   (CDpos.x % 1) - halfWidth)  /radx; 					// west
			}
		}
		else 			{
			if((CDpos.x+halfWidth)>=1){
				dist2XBorder = (1- ((CDpos.x % 1 + halfWidth)%1)) /radx; 				// east border cross
			}
			else {
				dist2XBorder = (1- CDpos.x % 1 - halfWidth) /radx; 						// east
			}
		}
		dist2XBorder = Math.abs(dist2XBorder);
	}
	else dist2XBorder=500;
	if((blockYUp==false) && (blockYDown==false)){
		if (rady < 0)	{dist2YBorder = (( (CDpos.y % 1) + halfHeight))/rady; }			// up
		else 			{dist2YBorder = (1-(CDpos.y % 1) - halfHeight) /rady; }			// down
	dist2YBorder = Math.abs(dist2YBorder);
	}
	else dist2YBorder=500;
	if((blockZNorth==false) && (blockZSouth==false)){
		if (radz < 0)	{
			if(CDpos.z %1 + halfWidth >= 0){
				dist2ZBorder = (-1-  ((CDpos.z % 1) + halfWidth))  /radz;
			}
			else{
				dist2ZBorder = (    ((CDpos.z % 1) + halfWidth))  /radz;
			}
		}																				// south
		else{
			System.out.print(" IF:"+(CDpos.z %1-halfWidth));
			if ((CDpos.z %1-halfWidth)<=-1){
				dist2ZBorder = (( 1- (halfWidth-(1+CDpos.z % 1))) /radz);  
			}
			else{
				dist2ZBorder = (( 1+ (CDpos.z % 1) - halfWidth) /radz);  				// north
			}
		}
		dist2ZBorder = Math.abs(dist2ZBorder);
	}
	else dist2ZBorder=500;
	System.out.print(" DX:"+dist2XBorder+" DY:"+dist2YBorder+" DZ:"+dist2ZBorder);
	if(Math.abs(dist2XBorder)<Math.abs(dist2YBorder) && Math.abs(dist2XBorder)<Math.abs(dist2ZBorder)){  									// X shortest
		if(radx<0) {
			System.out.print(" West ");
			CDDir=Direction.WEST;
		}
		else {
			System.out.print(" East ");
			CDDir=Direction.EAST;
		}
		return CDDir;
	}
	else if(Math.abs(dist2YBorder)<Math.abs(dist2XBorder) && Math.abs(dist2YBorder)<Math.abs(dist2ZBorder)){								// Y shortest
		if(rady<0) {
			System.out.print(" Up ");
			CDDir=Direction.UP; 
		}
		else {
			System.out.print(" Down ");
			CDDir=Direction.DOWN;
		}
		return CDDir;
	}
	else {																					// Z shortest
		if(radz<0) {
			System.out.print(" South ");
			CDDir=Direction.SOUTH; 
		}
		else {
			System.out.print(" North ");
			CDDir=Direction.NORTH;
		}
		return CDDir;
	}
}
private boolean isOnNorthBorder(){
	System.out.print(" OnNorth");
	if(Math.abs(CDpos.z)%1 == (1-halfWidth)) { System.out.print("-Y"); return true; }
	else return false;
}

private boolean isOnSouthBorder(){
	System.out.print(" OnSouth");
	if(Math.abs(CDpos.z)%1 == halfWidth) { System.out.print("-Y"); return true; }
	else return false;
}

private boolean isOnWestBorder(){
	System.out.print(" OnWest");
	if(Math.abs(CDpos.x)%1 == halfWidth) { System.out.print("-Y"); return true; }
	else return false;
}

private boolean isOnEastBorder(){
	System.out.print(" OnEast");
	if(Math.abs(CDpos.x)%1 == (1-halfWidth)) { System.out.print("-Y"); return true; }
	else return false;
}

private boolean crossedNorthBorder(){
	System.out.print(" closeToNorth");
	if(Math.abs(CDpos.z)%1 > (1-halfWidth)) { System.out.print("-true"); return true; }
	else return false;
}

private boolean crossedSouthBorder(){
	System.out.print(" closeToSouth");
	if(Math.abs(CDpos.z)%1 < halfWidth) { System.out.print("-true"); return true; }
	else return false;
}

private boolean crossedWestBorder(){
	System.out.print(" closeToWest");
	if(Math.abs(CDpos.x)%1 < halfWidth) { System.out.print("-true"); return true; }
	else return false;
}

private boolean crossedEastBorder(){
	System.out.print(" closeToEast");
	if(Math.abs(CDpos.x)%1 > (1-halfWidth)) { System.out.print("-true"); return true; }
	else return false;
}

private boolean northBorderBlocked(){
	if	(isOnNorthBorder() && CDpos.getWorld().getVoxelPlus(CDpos, 0,0,-1) != Material.m_air ||
		(crossedWestBorder() && CDpos.getWorld().getVoxelPlus(CDpos, -1,0,-1) != Material.m_air) ||
		(crossedEastBorder() && CDpos.getWorld().getVoxelPlus(CDpos, +1,0,-1) != Material.m_air)){
		System.out.print(" blockZnorth");
		return true;
	}
	return false;
}
private boolean eastBorderBlocked(){
	if	(isOnEastBorder() && CDpos.getWorld().getVoxelPlus(CDpos, 1,0,0) != Material.m_air ||
		(crossedNorthBorder() && CDpos.getWorld().getVoxelPlus(CDpos, 1,0,-1) != Material.m_air) ||
		(crossedSouthBorder() && CDpos.getWorld().getVoxelPlus(CDpos, 1,0,+1) != Material.m_air)){
		System.out.print(" blockXeast");
		return true;
	}
	return false;
}

private boolean southBorderBlocked(){
	if	(isOnSouthBorder() && CDpos.getWorld().getVoxelPlus(CDpos, 0,0,+1) != Material.m_air ||
		(crossedWestBorder() && CDpos.getWorld().getVoxelPlus(CDpos, -1,0,+1) != Material.m_air) ||
		(crossedEastBorder() && CDpos.getWorld().getVoxelPlus(CDpos, +1,0,+1) != Material.m_air)){
		System.out.print(" blockZsouth");
		return true;
	}
	return false;
}

private boolean westBorderBlocked(){
	if	(isOnWestBorder() && CDpos.getWorld().getVoxelPlus(CDpos, -1,0,0) != Material.m_air ||
		(crossedNorthBorder() && CDpos.getWorld().getVoxelPlus(CDpos, -1,0,-1) != Material.m_air) ||
		(crossedSouthBorder() && CDpos.getWorld().getVoxelPlus(CDpos, -1,0,+1) != Material.m_air)){
		System.out.print(" blockXwest");
		return true;
	}
	return false;
}

private boolean upBorderBlocked(){
	return false;
}
private boolean downBorderBlocked(){
	return false;
}

 

public void setNewPosition(float distance){
//	CDpos.add2FloatX(distance * (float)Math.sin(Math.toRadians(yaw)));
//	CDpos.add2FloatZ(0-(distance * (float)Math.cos(Math.toRadians(yaw))));
	switch (aswd) {
		case FORWARD : {
			CDpos.add2FloatX(distance * (float)Math.sin(Math.toRadians(yaw)));
			CDpos.add2FloatZ(0-(distance * (float)Math.cos(Math.toRadians(yaw))));
			break;
		}
		case BACKWARD :{
			CDpos.add2FloatX(0-(distance * (float)Math.sin(Math.toRadians(yaw))));
			CDpos.add2FloatZ(distance * (float)Math.cos(Math.toRadians(yaw)));
			break;
		}
		case LEFT : {
			CDpos.add2FloatX(distance * (float)Math.sin(Math.toRadians(yaw-90)));
			CDpos.add2FloatZ(0-(distance * (float)Math.cos(Math.toRadians(yaw-90))));
			break;
		}
		case RIGHT : {
			CDpos.add2FloatX(distance * (float)Math.sin(Math.toRadians(yaw+90)));
			CDpos.add2FloatZ(0-(distance * (float)Math.cos(Math.toRadians(yaw+90))));
			break;
		}
	case DOWN:
		break;
	case UNKNOWN:
		break;
	case UP:
		break;
	default:
		break;
	}
	this.reqDistance -= (distance);
	System.out.print(" SNP:"+yaw+"#"+CDpos.tofString());
	
}


private boolean collisionDetect(){
	getNearestBorderAndDistance();

	switch (CDDir) {
	case NORTH:	
		System.out.print(" -North");
		if(blockZNorth=northBorderBlocked()){
			if(getQuadrant()==Quadrant.NORTWEST) yaw=270;
	    	else yaw=90;
	    	calculateSpeed(pitch, yaw);
			return false;
		}
		if(reqDistance<dist2ZBorder){
			setNewPosition(reqDistance);
			return true;
		}
		System.out.print(" -Short");
	    setNewPosition(dist2ZBorder);
    	CDpos.setFloatZ(((int)CDpos.z)-(1-halfWidth));
    	return false; // Not done, continue

	case SOUTH:
		System.out.print(" -South");
		if(blockZSouth=southBorderBlocked()){
			if(getQuadrant()==Quadrant.SOUTHWEST) yaw=270;
	    	else yaw=90;
	    	calculateSpeed(pitch, yaw);
			return false;
		}
		if(reqDistance<dist2ZBorder){
			setNewPosition(reqDistance);
			return true;
		}
		System.out.print(" -Short");
	    setNewPosition(dist2ZBorder);
    	CDpos.setFloatZ(((int)CDpos.z)-halfWidth);
    	return false; // Not done, continue
    	
	case WEST:
		System.out.print(" -West");
		if(blockXWest=westBorderBlocked()){
			if(getQuadrant()==Quadrant.NORTWEST) yaw=0;
	    	else yaw=180;
	    	calculateSpeed(pitch, yaw);
			return false;
		}
		if(reqDistance<dist2XBorder){
			setNewPosition(reqDistance);
			return true;
		}
		System.out.print(" -Short");
	    setNewPosition(dist2XBorder);
	    CDpos.setFloatX(((int)CDpos.x)+halfWidth);
    	return false; // Not done, continue
    	
	case EAST:
		System.out.print(" -East");
		if(blockXEast=eastBorderBlocked()){
			if(getQuadrant()==Quadrant.NORTHEAST) yaw=0;
	    	else yaw=180;
	    	calculateSpeed(pitch, yaw);
			return false;
		}
		if(reqDistance<dist2XBorder){
			setNewPosition(reqDistance);
			return true;
		}
		System.out.print(" -Short");
	    setNewPosition(dist2XBorder);
	    CDpos.setFloatX(((int)CDpos.x)+(1-halfWidth));
    	return false; // Not done, continue

	case UP:
		blockYUp=true;
		return false;
	//	if(reqDistance>dist2YBorder){
	//	    setNewPosition(dist2YBorder);
	//	    if(upBorderBlocked()){
	//		blockY=true;
	//		return false; // Not done, continue
	//	    }
	//	    CDpos.add2FloatY(0.01f); // pass border slightly
	//	    return false; // Not done, continue
	//	}
	//	setNewPosition(reqDistance);
	//	return true;
	case DOWN:
		blockYDown=true;
		return false;
	//	if(reqDistance>dist2YBorder){
	//	    setNewPosition(dist2YBorder);
	//	    if(downBorderBlocked()){
	//		blockY=true;
	//		return false; // Not done, continue
	//	    }
	//	    CDpos.add2FloatY(-0.01f); // pass border slightly
	//	    return false; // Not done, continue
	//	}
	//	setNewPosition(reqDistance);
	//	return true;
	case UNKNOWN:
		break;
	default:
		break; 
	}
	return false;
}

public void moveEntity(float elapsedTime, float yaw, float pitch, ASWDdir aswd){
	blockZNorth=false;
	blockZSouth=false;
	blockXEast=false;
	blockXWest=false;
	blockYUp=false;
	blockYDown=false;
	this.yaw=yaw;
	this.pitch=pitch;
	this.aswd=aswd;
	this.reqDistance=horizontalSpeed * elapsedTime;
	positionFound=false;
	CDpos.clone(this);
	calculateSpeed(pitch, yaw);
	System.out.print("ME rqd:"+reqDistance);
	int count=1;
	do {
		System.out.print(" ME["+count+"] RD:"+reqDistance+" Yaw:"+(int)yaw+" NSEWUD:");
		if(blockZNorth)System.out.print("N"); else System.out.print("-");
		if(blockZSouth)System.out.print("S"); else System.out.print("-");
		if(blockXEast)System.out.print("E"); else System.out.print("-");
		if(blockXWest)System.out.print("W"); else System.out.print("-");
		if(blockYUp)System.out.print("U"); else System.out.print("-");
		if(blockYDown)System.out.print("D"); else System.out.print("-");

		positionFound=collisionDetect();
		count++;
		if(count>10){System.out.println("..."); System.out.println("krash"); break;}
		if (reqDistance<0.005f) break;
		if ((blockXEast==true) || (blockXWest==true))
			if ((blockYUp==true) || (blockYDown==true))
				if ((blockZNorth==true) || (blockZSouth==true))
					break;
		System.out.println(" ");
	}while (positionFound==false);
	System.out.println("Done: "+CDpos.tofString());
	this.clone(CDpos);
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
//	public void setHorizontalForce(float horizontalForce, float yaw, float pitch, MoveDir dir) {
//		this.horizontalForce = horizontalForce;
//		this.yaw = yaw;
//		this.pitch = pitch;
//		this.moveDir = dir;
//	}



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
