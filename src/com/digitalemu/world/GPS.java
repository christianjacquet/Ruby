package com.digitalemu.world;

import java.text.DecimalFormat;

import org.lwjgl.util.vector.Vector3f;

//import com.digitalemu.monster.MovableEntity.moveDir;

public class GPS extends Vector3f{
	private static final long serialVersionUID = 1L;
	World world;
	short material=-1;
	public enum BoxVertice {BLF, BLB, BRF, BRB, TLF, TLB, TRF, TRB } 
	public enum VoxelSide { UNKNOWN, LEFT, RIGHT, FRONT, BACK, TOP, BOTTOM }
	public enum Direction { UNKNOWN, NORTH, SOUTH, EAST, WEST, UP, DOWN}
	public enum ASWDdir {UNKNOWN, FORWARD, BACKWARD, LEFT, RIGHT, UP, DOWN}
	private VoxelSide hit = VoxelSide.UNKNOWN;
	private Direction direction = Direction.UNKNOWN;
	private float distance = 0;
	long longX;		// West  = -	East  = +
	long longZ;		// North = - 	South = + 
	long longY;		// Up    = +    Down  = -
	
	public GPS(){
		this.longX=0;
		this.longZ=0;
		this.longY=0;
		this.x=0;
		this.z=0;
		this.y=0;
	}
	
	public GPS(World world){
		this.longX=0;
		this.longZ=0;
		this.longY=0;
		this.x=0;
		this.z=0;
		this.y=0;
		this.world = world;
	}
	
	public GPS(long x, long y, long z){
		this.longX=x;
		this.longZ=z;
		this.longY=y;
		this.x = this.longX % world.getChunkBase();
		this.y = this.longY % world.getChunkBase();
		this.z = this.longZ % world.getChunkBase();
	}
	
	public GPS(float x, float y, float z){
		this.x= x;
		this.z= z;
		this.y= y;
		this.longX=(long) x;
		this.longZ=(long) z;
		this.longY=(long) y;
	}
	
	public GPS(float x, float y, float z, World world){
		this.x = x;
		this.y = y;
		this.z = z;
		this.longX=(long) x;
		this.longZ=(long) z;
		this.longY=(long) y;
		this.world = world;
	}
	
	public GPS(Vector3f vector){
		this.x = vector.x;
		this.y = vector.y;
		this.z = vector.z;
		this.longX=(long)vector.x;
		this.longY=(long)vector.y;
		this.longZ=(long)vector.z;
	}
	
	public GPS clone(GPS gps){
		this.x = gps.getX();
		this.y = gps.getY();
		this.z = gps.getZ();
		this.longX = gps.getLongX();
		this.longZ = gps.getLongZ();
		this.longY = gps.getLongY();
		this.world = gps.getWorld();
		this.hit   = gps.getHit();
		this.direction = gps.getDirection();
		this.material = gps.getMaterial();
		this.distance = gps.getDistance();
		return this;
	}
	

	public long getLongX(){
		return this.longX;
	}
	
	public long getLongZ(){
		return this.longZ;
	}
	
	public long getLongY(){
		return this.longY;
	}
	

	public void setWorld(World world){
		this.world = world;
	}

	public World getWorld(){
		return this.world;
	}
	
	public void setHit(VoxelSide hit){
		this.hit = hit;
	}

	public VoxelSide getHit(){
		return this.hit;
	}
	public void setMaterial(short material){
		this.material = material;
	}

	public short getMaterial(){
		return this.material;
	}
	
	public void setLongX(long val){
		this.longX=val;
		this.x = this.longX % world.getChunkBase();
	}
	
	public void setLongZ(long val){
		this.longZ=val;
		this.z = this.longZ % world.getChunkBase();
	}
	
	public void setLongY(long val){
		this.longY=val;
		this.y = this.longY % world.getChunkBase();
	}
	
	/**
	 * Add float value to existing float value of GPS.x
	 * @param val
	 * @return sum
	 */
	public float add2FloatX(float val){
		this.x = this.x + val;
		this.longX = this.longX - (this.longX % world.getChunkBase()) + (int) this.x;
		return this.x;
	}
	
	/**
	 * Add float value to existing float value of GPS.y
	 * @param val
	 * @return sum
	 */
	public float add2FloatY(float val){
		this.y = this.y + val;
		this.longY = this.longY - (this.longY % world.getChunkBase()) + (int) this.y;
		return this.y;
	}
	
	/**
	 * Add float value to existing float value of GPS.z
	 * @param val
	 * @return sum
	 */
	public float add2FloatZ(float val){
		this.z = this.z + val;
		this.longZ = this.longZ - (this.longZ % world.getChunkBase()) + (int) this.z;
		return this.z;
	}
	
	public void setFloatX(float val){
		this.x = val;
		this.longX = this.longX - (this.longX % world.getChunkBase()) + (int) val;
		System.out.print(" SETX:"+this.x);
	}
	
	public void setFloatZ(float val){
		this.z = val;
		this.longZ = this.longZ - (this.longZ % world.getChunkBase()) + (int) val;
		System.out.print(" SETZ:"+this.z);
	}
	
	public void setFloatY(float val){
		this.y = val;
		this.longY = this.longY - (this.longY % world.getChunkBase()) + (int) val;
		System.out.print(" SETY:"+this.y);
	}
	
	public void up(){
		this.longY++;
		this.y = (this.y % 1) + (this.longY % world.getChunkBase());
	}

	public void down(){
		this.longY--;
		this.y = (this.y % 1) + (this.longY % world.getChunkBase());
	}	
		
	public void east(){
		this.longX++;
		this.x = (this.x % 1) + (this.longX % world.getChunkBase());
	}
	
	public void west(){
		this.longX--;
		this.x = (this.x % 1) + (this.longX % world.getChunkBase());
	}
	
	public void north(){
		this.longZ--;
		this.z = (this.z % 1) + (this.longZ % world.getChunkBase());
	}
	
	public void south(){
		this.longZ++;
		this.z = (this.z % 1) + (this.longZ % world.getChunkBase());
	}
	
	// To be removed
	public GPS vector3f2GPS(Vector3f vector){
		this.longZ=(long)vector.z;
		this.longY=(long)vector.y;
		this.longX=(long)vector.x;
	return this;	
	}
	
	public void setGPS(GPS gps){
		this.longZ = gps.getLongX();
		this.longY = gps.getLongY();
		this.longX = gps.getLongZ();
		this.x = gps.getX();
		this.y = gps.getY();
		this.z = gps.getZ();
		this.world = gps.getWorld();
		
	}
	
	// To be removed
	public void setGPS(Vector3f gps){
		this.longZ = (long)gps.z;
		this.longY = (long)gps.y;
		this.longX = (long)gps.x;
		this.x = gps.getX();
		this.y = gps.getY();
		this.z = gps.getZ();
	}
	

	
	// This should be removed, cant work and is plain stupid
	public static String gps2str(long westEast, long northSouth, long upDown){
		return "x"+Long.toString(westEast)+"y"+Long.toString(upDown)+"z"+Long.toString(northSouth);
	}
	
	// Convert GPS position to String
	// input: 32,44,789
	// output: "x32y44z789
	public String gps2str(){
		return "x"+Long.toString(this.longX)+"y"+Long.toString(this.longY)+"z"+Long.toString(this.longZ);
	}
	
	public String toSString(){
		DecimalFormat df = new DecimalFormat("#.##");
		return "GPS X: "+this.longX+" Z: "+this.longZ+" Y: "+this.longY+" Xf: "+df.format(this.x)+" Zf: "+df.format(this.z)+" Yf: "+df.format(this.y);
	}

	public String tofString(){
		DecimalFormat df = new DecimalFormat("#.##");
		return "GPS  Xf: "+df.format(this.x)+" Zf: "+df.format(this.z)+" Yf: "+df.format(this.y)+ "d: "+this.distance+" d: "+direction.toString()+" vs: "+hit.toString();
	}
	/**
	 * @return the direction
	 */
	public Direction getDirection() {
		return direction;
	}

	/**
	 * @param direction the direction to set
	 */
	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	/**
	 * @return the distance
	 */
	public float getDistance() {
		return distance;
	}

	/**
	 * @param distance the distance to set
	 */
	public void setDistance(float distance) {
		this.distance = distance;
	}
	

//	public GPS moveRelative(float distance, moveDir dir, float pitch, float yaw){
//		switch (dir) {
//		case FORWARD : {
//			this.add2FloatX(distance * (float)Math.sin(Math.toRadians(yaw)));
//			this.add2FloatZ(0-(distance * (float)Math.cos(Math.toRadians(yaw))));
//	        break;
//		}
//		case LEFT : {
//			this.add2FloatX(distance * (float)Math.sin(Math.toRadians(yaw-90)));
//			this.add2FloatZ(0-(distance * (float)Math.cos(Math.toRadians(yaw-90))));
//			break;
//		}
//		case RIGHT : {
//			this.add2FloatX(distance * (float)Math.sin(Math.toRadians(yaw+90)));
//			this.add2FloatZ(0-(distance * (float)Math.cos(Math.toRadians(yaw+90))));
//			break;
//		}
//		case BACKWARD : {
//			this.add2FloatX(0-(distance * (float)Math.sin(Math.toRadians(yaw))));
//			this.add2FloatZ(distance * (float)Math.cos(Math.toRadians(yaw)));
//		}
//		case UP : {
//			this.add2FloatY(distance);
//			break;
//		}
//		case DOWN : {
//			this.add2FloatZ(0-distance);
//			break;
//		}
//		default : {
//			System.out.println("Exception in moveRelative, dir unspec. ");
//		}
//		}
//		return this;
//	}

	

}
