package com.digitalemu.world;

import java.io.IOException;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class Textures {
	private Texture texture;
	private String path;

	public Textures(String path){
		this.path = path;
        // load texture from PNG file
        try {
			texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getPath(){
		return path;
	}
	
	public Texture getTexture(){
		return texture;
	}
	
    

}
