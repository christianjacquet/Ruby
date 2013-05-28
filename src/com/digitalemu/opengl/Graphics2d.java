package com.digitalemu.opengl;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.util.BufferedImageUtil;

public class Graphics2d {
	
	/**
	 * Load and return a BufferedImage
	 * @param filepath
	 * @return BufferedImage
	 */	
	public static BufferedImage loadBufferedImage(String filepath) {
		BufferedImage bimg = null;
		try {
			bimg = ImageIO.read(new File(filepath));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bimg;
	}
	
	/**
	 * Split a single Buffered Image and return a Texture[] array.
	 * @param img A buffered image a single Buffered Image into a Texture[] array.
	 * @param cols
	 * @param rows
	 * @return
	 */
    public static Texture[] splitImage(BufferedImage img, int cols, int rows) {
		int w = img.getWidth()/cols;
		int h = img.getHeight()/rows;
		int num = 0;
		Texture tex[] = new Texture[w*h];
		BufferedImage imgs[] = new BufferedImage[w*h];
		try {
		for(int y = 0; y < rows; y++) {
			for(int x = 0; x < cols; x++) {
				imgs[num] = new BufferedImage(w, h, img.getType());
				// Tell the graphics to draw only one block of the image
				Graphics2D g = imgs[num].createGraphics();
				g.drawImage(img, 0, 0, w, h, w*x, h*y, w*x+w, h*y+h, null);
				g.dispose();
				tex[num] = BufferedImageUtil.getTexture("klas",imgs[num]);
				num++;
			}
		}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tex;
	}
    
	/**
	 * Loads a texturemap image and return an Texture[] array
	 * @param filename of texturemap
	 * @param columns
	 * @param rows
	 * @return Texture[]
	 */ 
	public static Texture[] loadTextureMap(String filename, int columns, int rows){
		return splitImage(loadBufferedImage(filename),columns,rows);
	}

}
