package com.digitalemu.world;

public class Material {
	
	public final static short m_grass = 0;
	public final static short m_rock = 1;
	public final static short m_dirt = 2;
	public final static short m_dirt_with_grass_top = 3;
	public final static short m_wood_plank = 4;	
	public final static short m_stone_brick = 5;
	public final static short m_stone = 6;
	public final static short m_brick = 7;
	public final static short m_tnt = 8;
	public final static short m_wood_box1 = 9;
	public final static short m_wood_box2 = 10;
	public final static short m_spiderweb = 11;
	public final static short m_flower_red = 12;
	public final static short m_flower_yellow = 13;
	public final static short m_air = 14;
	public final static short m_sapling1 = 15;
	public final static short m_stone_wall1 = 16;
	public final static short m_bedrock = 17;
	public final static short m_sand = 18;
	public final static short m_gravel = 19;
	public final static short m_pinetree = 20;
	public final static short m_treeCore = 21;
	public final static short m_metalBox = 22;
	public final static short m_goldBox = 23;
	public final static short m_water = 24;
	public final static short m_woodBox = 25;
	public final static short m_chestSide1 = 26;
	public final static short m_chestFrontSmall = 27;
	public final static short m_mushroom_red = 28;
	public final static short m_mushroom_brown = 29;
	public final static short m_black = 30;
	public final static short m_rock_gold = 32;
	public final static short m_rock_copper = 33;
	public final static short m_rock_coal = 34;
	public final static short m_bookShelf = 35;
	public final static short m_stone_wall2 = 36;
	public final static short m_bedrock2 = 37;
	public final static short m_grass_upperside = 38;
	public final static short m_busch1 = 39;
	public final static short m_grass2 = 40;
	public final static short m_chestFrontLargeLeft = 41;
	public final static short m_chestFrontLargeRight = 42;
	public final static short m_workbenchTop = 43;
	public final static short m_stove_front = 44;
	public final static short m_stove_side = 45;
	public final static short m_stove_top = 46;
	public final static short m_neitherLight = 48;
	public final static short m_window=49;
	public final static short m_rock_diamond=50;
	public final static short m_rock_redstone=51;
	public final static short m_busch2 = 55;
	public final static short m_busch3 = 56;
	public final static short m_chestLargeBackLeft = 57;
	public final static short m_chestLargeBackRight = 58;
	public final static short m_workbench_side = 59;
	public final static short m_workbench_front = 60;
	public final static short m_stove_front_lit = 61;
	public final static short m_stove_back = 62;
	public final static short m_sapling2 = 63;
	public final static short m_cactus_top = 69;
	public final static short m_cactus_side = 70;
	public final static short m_cactus_core = 71;
	public final static short m_clay = 72;
	public final static short m_sugarCane = 73;
	public final static short m_musicbox_side = 74;
	public final static short m_musicbox_top = 75;
	public final static short m_sapling3 = 79;
	public final static short m_tourch_lit = 80;
	public final static short m_door_wood_upper = 81;
	public final static short m_door_metal_upper = 82;
	public final static short m_ladder = 83;
	public final static short m_hatch_wood = 84;
	public final static short m_fence_metal = 85;
	public final static short m_cropfield_wet = 86;
	public final static short m_cropfield_dry = 87;
	public final static short m_crop1 = 88;
	public final static short m_crop2 = 89;
	public final static short m_crop3 = 90;
	public final static short m_crop4 = 91;
	public final static short m_crop5 = 92;
	public final static short m_crop6 = 93;
	public final static short m_crop7 = 94;
	public final static short m_crop8 = 95;
	public final static short m_tourch_off = 96;
	public final static short m_door_wood_bottom = 97;
	public final static short m_door_metal_bottom = 98;
	public final static short m_tourch_red = 99;
	public final static short m_stoneBrick_mossy = 100;
	public final static short m_stoneBrick = 101;
	public final static short m_pumpkin_top = 102;
	public final static short m_tourch_black = 115;
	public final static short m_tree_pine = 116;
	public final static short m_tree_birch = 117;
	public final static short m_pumpkin_side = 118;
	public final static short m_pumpkin_face_off = 119;
	public final static short m_pupmkin_face_lit = 120;
	public final static short m_cake_top = 121;
	public final static short m_cace_side1 = 122;
	public final static short m_cake_side2 = 123;
	public final static short m_water1 = 205;
	public final static short m_water2 = 206;
	public final static short m_water3 = 207;
	public final static short m_water4 = 222;
	public final static short m_water5 = 223;
	public final static short m_lava1 = 237;
	public final static short m_lava2 = 238;
	public final static short m_lava3 = 239;
	public final static short m_lava4 = 254;
	public final static short m_lava5 = 255;
	public final static short m_crack1 = 240;
	public final static short m_crack2 = 241;
	public final static short m_crack3 = 242;
	public final static short m_crack4 = 243;
	public final static short m_crack5 = 244;
	public final static short m_crack6 = 245;
	public final static short m_crack7 = 246;
	public final static short m_crack8 = 247;
	public final static short m_crack9 = 248;
	public final static short m_crack10 = 249;
	public final static short m_null = -1;
	
	public final static String[] materialName = 
		{"grass","rock","dirt","dirt with grass top","wood plank","stone brick","stone","brick","tnt","wood box 1",
		"wood box 2","spiderweb","flower red"+"flower yellow"+"air","sapling","stone wall","bedrock","sand","gravel",
		"pinetree","treecore","metalbox","goldbox","water"};

		
	public String getName(int material){
		return materialName[material];
	}

	
	


}
