package com.digitalemu.world;

public class Voxel {
	private short voxel;
	
	public Voxel(short material){
		this.voxel=material;
	}

	public short getVoxel() {
		return voxel;
	}

	public void setVoxel(short voxel) {
		this.voxel = voxel;
	}
}
