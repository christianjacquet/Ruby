package com.digitalemu.world;

public class SubChunk {
	private Voxel[][][] subChunk;
	private boolean notVisible=true;
	private boolean empty=true;
	
	public SubChunk(int voxelsInSubChunk){
		this.subChunk = new Voxel[voxelsInSubChunk][voxelsInSubChunk][voxelsInSubChunk];
		for(int x=0; x<voxelsInSubChunk; x++){
			for(int y=0; y<voxelsInSubChunk; y++){
				for(int z=0; z<voxelsInSubChunk; z++){
					//System.out.println("x y z "+x+" "+y+" "+z);
					this.subChunk[x][y][z]= new Voxel((short) 4);
				}
			}
		}
	}
	
//	public Voxel[][][] getSubChunk() {
//		return this.subChunk;
//	}
//
//	public void setSubChunk(Voxel[][][] subChunk) {
//		this.subChunk = subChunk;
//	}

	public boolean isNotVisible() {
		return notVisible;
	}

	public void setNotVisible(boolean notVisible) {
		this.notVisible = notVisible;
	}

	public boolean isEmpty() {
		return empty;
	}

	public void setEmpty(boolean empty) {
		this.empty = empty;
	}

}
