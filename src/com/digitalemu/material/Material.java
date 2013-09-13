package com.digitalemu.material;

public interface Material {
	public String getName();
	public boolean isSolid();
	public boolean isTransparant();
	public boolean hit();
	public byte getFriction();
}
