package com.digitalemu.terrain;


import com.digitalemu.world.Chunk3;
import com.digitalemu.world.GPS;
import com.digitalemu.world.Material;
import com.digitalemu.xml.ConfigurationReader;

public class Gt {
	private SimplexNoise2d climate;
	private SimplexNoise2d npc;
	private SimplexNoise2d structures;
	private SimplexNoise2d groundDynamic;
	private SimplexNoise2d groundBase;
	private SimplexNoise2d soilDepth;
	private SimplexNoise2d peakDynamic;
	private SimplexNoise2d peaks;
	private SimplexNoise3d hollow;
	private int climateVal;
	private int npcVal;
	private int structuresVal;
	private float groundDynamicVal;
	private int groundBaseVal;
	private int soilDepthVal;
	private float peakDynamicVal;
	private float peaksVal;
	private int hollowVal;
	private int waterLevel=48;
	private int blockHeight;
	private short blockType;
	private int peakDynamicTreshold=75;
	private int peakDynamicMax=100;
	private int DesertZone = 10;
	private int SwampZone = 20;
	private int TropicsZone = 30;
	private int NormalZone = 40;
	private int DryZone = 50;
	private int PolarZone = 60;
	private int slopeHight;
	private int peakHeight;
	
	
	public Gt(long seed, int seedDiff){
		climate = new SimplexNoise2d(seed, 100, 32, 128);
		seed-=seedDiff;
		npc = new SimplexNoise2d(seed, 10, 32, 128);
		seed-=seedDiff;
		structures = new SimplexNoise2d(seed, 10, 32, 128);
		seed-=seedDiff;
		groundDynamic = new SimplexNoise2d(seed, 100, 0, 100);
		seed-=seedDiff;
		groundBase = new SimplexNoise2d(seed, 10, 32, 128);
		seed-=seedDiff;
		soilDepth = new SimplexNoise2d(seed, 100, 0, 8);
		seed-=seedDiff;
		peakDynamic = new SimplexNoise2d(seed, 100, 0, peakDynamicMax);
		seed-=seedDiff;
		peaks = new SimplexNoise2d(seed, 10, 32, 128);
		seed-=seedDiff;
		hollow = new SimplexNoise3d(seed, 10, 32, 128);
	}
	

	
	public void generate(Chunk3 chunk){
		for (long x=chunk.getGps().getLongX(); x<chunk.getGps().getLongX()+20; x++){
			for (long z=chunk.getGps().getLongZ(); z<chunk.getGps().getLongZ()+20; z++){
				climateVal=climate.getSimplexNoise2D(x, z);
				groundDynamicVal = groundDynamic.getSimplexNoise2D(x, z)/100;
				groundBaseVal = groundBase.getSimplexNoise2D(x, z);
				peakDynamicVal = peakDynamic.getSimplexNoise2D(x, z);
				peaksVal = peaks.getSimplexNoise2D(x, z);
				soilDepthVal = soilDepth.getSimplexNoise2D(x, z);
				// Calculate ground level considering ground Level Dynamic and centered around water Level
				if (groundBaseVal > waterLevel){
					blockHeight = (int)((groundBaseVal - waterLevel) * groundDynamicVal) + waterLevel;
				} else {
					blockHeight =  (int)(waterLevel - ((waterLevel - groundBaseVal) * groundDynamicVal));
				}
				// Check climate
//				if (climateVal>=DesertZone && climateVal<=DesertZone+9) blockType=Material.m_sand;
//				else if (climateVal>=DryZone && climateVal<=DryZone+9) blockType=Material.m_gravel; 	// Replace with Pine Nail
//				else if (climateVal>=DryZone && climateVal<=DryZone+9) blockType=Material.m_grass; 		// Replace with lush
//				else if (climateVal>=SwampZone && climateVal<=SwampZone+9) blockType=Material.m_clay;	// Replace with mud
//				else blockType=Material.m_dirt;															// Default
//				// Check soil depth and build ground
//				for (long y=1; y<blockHeight; y++) {
//					if (y<blockHeight-soilDepthVal) chunk.setVoxel(x, waterLevel, z, Material.m_rock);
//					else chunk.setVoxel(x, waterLevel, z, blockType);
//				}
//				// Check if swamp zone and lower terrain to waterlevel
//				if (climateVal>=SwampZone-9 && climateVal<=SwampZone+19){
//					if (climateVal>=SwampZone && climateVal<=SwampZone+9){ 		// swamp, Always at waterlevel				
//						chunk.setVoxel(x, waterLevel, z, Material.m_water);				
//					}
//					else {
//						// Build slopes down to swamp
//						slopeHight = ((blockHeight-waterLevel)/10)*Math.abs(SwampZone-climateVal);
//						chunk.setVoxel(x, slopeHight, z, Material.m_grass);
//						for (long y = slopeHight+1; y<blockHeight+1; y++){
//							chunk.setVoxel(x, slopeHight, z, Material.m_air);
//						}
//						blockHeight=slopeHight;
//					}					
//				}
//				// Add peak considering peak dynamic and threshold.
//				if (peakDynamicVal > peakDynamicTreshold){
//					peakDynamicVal = ((peakDynamicMax / (peakDynamicMax-peakDynamicTreshold)) * (peakDynamicVal-peakDynamicTreshold))/100;
//					peaksVal = peaksVal * peakDynamicVal;
//					if (peaksVal > blockHeight)	peakHeight = (int)(blockHeight + peaksVal);
//					else peakHeight = (int)(blockHeight - peaksVal);
//					for (long y = blockHeight-soilDepthVal; y<peakHeight; y++){
//						chunk.setVoxel(x, y, z, Material.m_rock);
//					}
//					blockHeight=peakHeight;
//				}				
				if (blockHeight<10)System.out.print((int)(blockHeight)+"   ");
		  		else if(blockHeight<100) System.out.print((int)(blockHeight)+"  ");
		  		else System.out.print((int)(blockHeight)+" ");
			}
			System.out.println(" ");
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("hej");
		ConfigurationReader cr = new ConfigurationReader("res/confw.xml");
		System.out.println(cr.getConfig("world", "default", "properties", "seed"));
		Chunk3 chunk = new Chunk3(20, new GPS(), null);
		Gt gt = new Gt(1234,10);
		gt.generate(chunk);

	}

}
