package com.digitalemu.material;

import com.digitalemu.world.Material;

public class MaterialBase {
	private String name;
	private boolean transparent;
	private boolean solid;
	private byte damage;
	private ShapeBase shape;
	private byte hardness;
	
	// Resistance 					wool	wood	rock	animal	plant	dirt	coal	iron
	private byte velocity;		//	-8		8		12		4		8		0		12		12
	private byte mass;			//	-8		8		12		4		0		4		12		12	
	private byte sharpness;		//	12		12		-8		12		12		4		-8		-8
	private byte digger;		//	0		0		-8		-4		0		12		-8		-8
	private byte attackarea;    //	12		-8		-8		-12		12		8		-8		-8	
	private byte heat;			//	0		1		12		1		1		12		0		8
	private byte water;			//	16		16		16		16		16		0		16		16		
	private byte fire;			//	-16		-8		16		-8		-8		16		-12		16
	private byte electricity;	//	16		-8		16		-8		16		16		16		16
	
	
	// Spawn at reaction with
	public void reactWithFire(){}
	public void reactWithHeat(){}
	public void reactWithForce(Object obj){}
	
	
	
	

}

