package com.digitalemu.world;

public class Block {
	public final static byte AIR=14;
	public final byte BEDROCK=18;
	public final byte GRANITE=02;
	public final byte SAND=19;
	public final byte DIRT=05;
	public final byte WATER=5;
	public final byte CACTUS = 70;
	public final byte TREE = 21;
	byte block = AIR;
	
	public Block (byte material){
		this.block = material;
	}
	
	public byte getMaterial(){
		return this.block;
	}


}
