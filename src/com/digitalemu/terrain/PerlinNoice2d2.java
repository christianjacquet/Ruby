package com.digitalemu.terrain;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class PerlinNoice2d2
{
	BufferedImage			image;
	public static byte[]	lookup;
	public static  Random random;
	
	public PerlinNoice2d2(){
		
	}

	public BufferedImage getPerlin2dPng(long seed){
		random = new Random(seed); // seed
		System.out.println(random+" "+random.nextInt(255));
		// create the lookup table
		long start;
		System.out.println("generating lookup table... ");
		start = System.currentTimeMillis();
		lookup = new byte[255];
		for (int i = 0; i < 255; i++)
		{
			lookup[i] = (byte) (Math.pow((i & 0xFF) / 255.0f, 5) * 0xFF);
			System.out.print(lookup[i]+":");
		}
		// generate a perlin noise map
		System.out.println((System.currentTimeMillis() - start));
		System.out.println("generating noise maps...");
		start = System.currentTimeMillis();
		
		int iterations = 5;
		int startvalue = 2;
		PerlinNoice2d2[] noise = new PerlinNoice2d2[iterations];
		float amplitude = 1.0f;
		double persistence = 1.5;
		int width = 128;
		int height = 128;
		for(int ii=0; ii<noise.length; ii++){
			noise[ii] = new PerlinNoice2d2(width, height, startvalue, startvalue, amplitude);
			amplitude /= persistence;
			startvalue = startvalue+startvalue;
		}

		BufferedImage perlin = new BufferedImage(width, height,
				BufferedImage.TYPE_3BYTE_BGR);
		System.out.println((System.currentTimeMillis() - start));
		System.out.println("combining noise maps... ");
		start = System.currentTimeMillis();
		Graphics2D g = perlin.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
				RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		for (int i = 0; i < noise.length; i++)
		{
			g.drawImage(noise[i].image, null, 0, 0);
		}
		System.out.println((System.currentTimeMillis() - start));
		System.out.println("calculating density values... ");
		start = System.currentTimeMillis();
		// Manually re-calculate the RGB values
		// this is the section of code I would ideally like to replace
		for (int i = 0; i < perlin.getWidth(); i++)
		{
			for (int j = 0; j < perlin.getHeight(); j++)
			{
				int color = perlin.getRGB(i, j);
				byte val = lookup[color & 0xFF];
				// float val = (float) Math.pow((color & 0xFF) / 255.0f, 5.0);
				color = (((val)) << 16) + (((val)) << 8) + ((val));
				// color = (((int) (val * 0xFF)) << 16)
				// + ((int) ((val * 0xFF)) << 8) + ((int) (val * 0xFF));
				perlin.setRGB(i, j, color);
			}
		}
		System.out.println((System.currentTimeMillis() - start));
		System.out.println("writing file... ");
		start = System.currentTimeMillis();
		// write the image out to test.png so I can view it easily
		try
		{
			ImageIO.write(perlin, "png", new File("test2.png"));


			
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println((System.currentTimeMillis() - start));
		System.out.println("done! ");
		return perlin;
	}

	public PerlinNoice2d2(int width, int height, int freqX, int freqY, float alpha)
	{
		BufferedImage temp = new BufferedImage(freqX, freqY,
				BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = temp.createGraphics();
		// generate a low-res random image
		for (int i = 0; i < freqX; i++)
		{
			for (int j = 0; j < freqY; j++)
			{
				int val = random.nextInt(255);
				g.setColor(new Color(val, val, val, (int) (alpha * 0xFF)));
				g.fillRect(i, j, 1, 1);
			}
		}
		g.dispose();
		// re-scale the image up using interpolation (in this case, linear)
		image = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
		g = image.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);

		g.drawImage(temp, 0, 0, width, height, 0, 0, freqX, freqY, null);
		g.dispose();
	}



}


