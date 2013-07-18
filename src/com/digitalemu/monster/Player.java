package com.digitalemu.monster;

import com.digitalemu.world.GPS;
import com.digitalemu.world.World;

public class Player extends MonsterBase{
	private float eyeHeight = 1.6f;						// Default eye height of player

	public Player(String name, GPS gps) {
		super(name, gps);
	}

	/**
	 * @return the eyeHeight
	 */
	public float getEyeHeight() {
		return eyeHeight;
	}

	/**
	 * @param eyeHeight the eyeHeight to set
	 */
	public void setEyeHeight(float eyeHeight) {
		this.eyeHeight = eyeHeight;
	}
	

}
