package com.digitalemu.world;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.Texture;

//import com.digitalemu.test.GameLoopDListMax;
import com.digitalemu.world.GPS;

public class World {
	private Chunk3[][][] chunk; 
	private DlistGps[] dListGps;
	private GPS centerOfMap;		// always point at center of map in Ram
	private int worldBaseInRam;
	private int chunkBase;			// Size of a chunk
	private int mapRadius;			// distance from center of map in ram to its edge

	private Textures texture;
	
	// This is a quick lookuptable to find if a chunk exist in RAM
	// The key is made up of the chunks GPS coordinates as a string
	private HashMap<String,Chunk3> chunkMap= new HashMap<String,Chunk3>(); 

	
	/* Constructor
	 * The world is built up by voxels.
	 * A symmetrical 3D array of voxels with the base chunkBase, is called a chunk.
	 * A symmetrival 3D array of chunks with the base worldBaseInRam is kept in RAM.
	 * The constructor instantiates chunks to match worldBaseInRam
	 */
	public World(int worldBaseInRam, int chunkBase, Textures texture){
		this.texture = texture;
		GPS tgps;
		this.worldBaseInRam=worldBaseInRam;
		this.chunkBase=chunkBase;
		this.mapRadius = (worldBaseInRam*chunkBase/2)-1;
		msg("Radius:"+mapRadius);
		this.centerOfMap = new GPS(mapRadius,mapRadius,mapRadius);
		this.chunk = new Chunk3[this.worldBaseInRam][this.worldBaseInRam][this.worldBaseInRam];
		this.dListGps = new DlistGps[worldBaseInRam*worldBaseInRam*worldBaseInRam];
		int chunkRadius=worldBaseInRam/2;
		for(int x=0; x<this.worldBaseInRam; x++){
			for(int y=0; y<this.worldBaseInRam; y++){
				for(int z=0; z<this.worldBaseInRam; z++){
					tgps = new GPS(((x-chunkRadius)*chunkBase),((y-chunkRadius)*chunkBase),((z-chunkRadius)*chunkBase));
					this.chunk[x][y][z]= new Chunk3(this.chunkBase, tgps, texture);
					msg("Storing: "+tgps.gps2str());
					chunkMap.put(tgps.gps2str(), this.chunk[x][y][z]);
					msg(" Chunk:"+this.chunk[x][y][z]+" with gps:"+tgps.toSString()+" in chunkMap");
					if(z==0){
						//chunk[x][y][z].generateNormal(x+z);
						chunk[x][y][z].generatePerlin();
					}
					else {
						chunk[x][y][z].generateNormalgrid(z+8);
					}
					chunk[x][y][z].compileDisplayListsDummy();
				}
			}
		}
	}
	
	private void msg(String message){
		//GameLoopDListMax.msg("World: "+message);
	}
	
	public DlistGps[] compileDlist(){
		int n = 0;
		for(int x=0; x<this.worldBaseInRam; x++){
			for(int y=0; y<this.worldBaseInRam; y++){
				for(int z=0; z<this.worldBaseInRam; z++){
					dListGps[n] = new DlistGps(chunk[x][y][z].getDisplayList(),chunk[x][y][z].getGps());
					n++;
				}
			}
		}
		return dListGps;
	}
	
	/* Find index of chunk in ram from GPS coordinates
	 * 
	 */
	public Chunk3 getChunk(GPS gps){
		GPS tgps= new GPS();
		tgps.setSouthNorth(gps.getSouthNorth()/chunkBase);
		tgps.setUpDown(gps.getUpDown()/chunkBase);
		tgps.setWestEast(gps.getWestEast()/chunkBase);
		String s = tgps.gps2str();
		//msg("looking for: "+s);
		if (chunkMap.containsKey(s)){
			//System.out.println("GPS: "+s+" found in chunkMap.");
			return chunkMap.get(s);
		}
		else{
			System.out.println("GPS: "+s+" not in chunkMap.");
		return null;
		}
	}
	
	/* Find index of chunk in ram from GPS coordinates
	 * 
	 */
	public Chunk3 getChunk(long westEast, long northSouth, long upDown){

		String s = GPS.gps2str((westEast/chunkBase),(northSouth/chunkBase),(upDown/chunkBase));
		if (chunkMap.containsKey(s)){
			return chunkMap.get(s);
		}
		else{
			System.out.println("GPS: "+s+" not in chunkMap.");
		return null;
		}
	}
	
	public short getVoxel(float westEast, float northSouth, float upDown){
		return getVoxel( (long) westEast,  (long) northSouth,  (long) upDown);
	}
	
	public short getVoxel(long westEast, long northSouth, long upDown){
		Chunk3 chunk=getChunk(new GPS(westEast, northSouth, upDown));
		int ew = (int) (westEast - chunk.getGps().getWestEast());
		int sn = (int) (northSouth - chunk.getGps().getSouthNorth());
		int ud = (int) (upDown - chunk.getGps().getUpDown());
		//msg("GPS: "+gps.gps2str()+" Chunk: "+chunk.getGps().gps2str()+" WE: "+ew+" SN: "+sn+" UD: "+ud+" Material: "+chunk.getVoxel(ew, sn, ew));
		return chunk.getVoxel(ew, ud, sn);
	}
	
	public short getVoxel(GPS gps){
		Chunk3 chunk=getChunk(gps);
		int ew = (int) (gps.getWestEast() - chunk.getGps().getWestEast());
		int sn = (int) (gps.getSouthNorth() - chunk.getGps().getSouthNorth());
		int ud = (int) (gps.getUpDown() - chunk.getGps().getUpDown());
		//msg("GPS: "+gps.gps2str()+" Chunk: "+chunk.getGps().gps2str()+" WE: "+ew+" SN: "+sn+" UD: "+ud+" Material: "+chunk.getVoxel(ew, sn, ew));
		return chunk.getVoxel(ew, ud, sn);
	}
	
	public void setVoxel(GPS gps, short material){
		Chunk3 chunk=getChunk(gps);
		int ew = (int) (gps.getWestEast() - chunk.getGps().getWestEast());
		int sn = (int) (gps.getSouthNorth() - chunk.getGps().getSouthNorth());
		int ud = (int) (gps.getUpDown() - chunk.getGps().getUpDown());
		chunk.setVoxel(ew, sn, ud, material);
	}
	
	public void setVoxel(long x, long y, long z, short material){
		setVoxel(new GPS(x,y,z), material);
	}
	

	public void generateTerrain(long seed){
		Random generator = new Random(seed);
		for(int x=0; x<this.worldBaseInRam; x++){
			for(int y=0; y<this.worldBaseInRam; y++){
				for(int z=0; z<this.worldBaseInRam; z++){
					if(y==0){
						chunk[x][y][z].generateNormal(x+z);
						chunk[x][y][z].compileDisplayListsDummy();
					}
				}
			}
		}		
	}
	

	
	private void pyramide(GPS gps){
		int max =10;
		for (int height=1; height<max; height++){
			for (long x=(gps.getWestEast()-height); x<(gps.getWestEast()+height); x++){
				for (long y=(gps.getSouthNorth()-height); y<(gps.getSouthNorth()+height); y++){
					setVoxel(x,y,height, Material.m_rock);
				}
			}
		}
	}
	
	public class DlistGps{
		private int displayList;
		private GPS dlGPS;
		
		public DlistGps(int displayList, GPS dlGPS){
			this.displayList = displayList;
			this.dlGPS = dlGPS;
		}
		
		public int getDisplayList() {
			return displayList;
		}
		public void setDisplayList(int displayList) {
			this.displayList = displayList;
		}
		public GPS getDlGPS() {
			return dlGPS;
		}
		public void setDlGPS(GPS dlGPS) {
			this.dlGPS = dlGPS;
		}
		
	}



}
