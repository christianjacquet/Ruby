package com.digitalemu.terrain;

import java.util.Random;

/*
 * A speed-improved simplex noise algorithm for 2D, 3D and 4D in Java.
 *
 * Based on example code by Stefan Gustavson (stegu@itn.liu.se).
 * Optimisations by Peter Eastman (peastman@drizzle.stanford.edu).
 * Better rank ordering method by Stefan Gustavson in 2012.
 *
 * This could be speeded up even further, but it's useful as it is.
 *
 * Version 2012-03-09
 *
 * This code was placed in the public domain by its original author,
 * Stefan Gustavson. You may use it as you see fit, but
 * attribution is appreciated.
 *
 */

public class SimplexNoise3d {  // Simplex noise in 2D, 3D and 4D
	private  long seed;
	private  float multiplicator3DX;
	private  float multiplicator3DY;
	private  float multiplicator3DZ;
	private  int minVal3D;
	private  float numberSpan;



  private Grad grad3[] = {new Grad(1,1,0),new Grad(-1,1,0),new Grad(1,-1,0),new Grad(-1,-1,0),
                                 new Grad(1,0,1),new Grad(-1,0,1),new Grad(1,0,-1),new Grad(-1,0,-1),
                                 new Grad(0,1,1),new Grad(0,-1,1),new Grad(0,1,-1),new Grad(0,-1,-1)};

  
  
  //private static short p[] = new short[256];

  private short p[] = new short[256];
  // To remove the need for index wrapping, double the permutation table length
  
  private short perm[] = new short[512];
  private short permMod12[] = new short[512];

  // Skewing and unskewing factors for 2, 3, and 4 dimensions
  private double F3,G3;
  
  public SimplexNoise3d(long seed, int smear, int minVal3D, int maxVal3D)
  {
	  this.numberSpan=(maxVal3D-minVal3D)/2;
	  this.multiplicator3DX = 1.0f/smear;
	  this.multiplicator3DY = 1.0f/smear;
	  this.multiplicator3DZ = 1.0f/smear;
	  this.minVal3D = minVal3D;
	  this.seed = seed;
	  int i;
	  Random generator = new Random(this.seed);
	  for (i=0; i<256; i++){
		  p[i]=(short) generator.nextInt(255);
	  }

	    for(int x2=0; x2<512; x2++)
	    {
	      perm[x2]=p[x2 & 255];
	      permMod12[x2] = (short)(perm[x2] % 12);
	    }
	 

	  // Skewing and unskewing factors for 2, 3, and 4 dimensions
	  this.F3 = 1.0/3.0;
	  this.G3 = 1.0/6.0;
  }

  // This method is a *lot* faster than using (int)Math.floor(x)
  private static int fastfloor(double x) {
    int xi = (int)x;
    return x<xi ? xi-1 : xi;
  }

 

  private static double dot(Grad g, double x, double y, double z) {
    return g.x*x + g.y*y + g.z*z; }

  



  // 3D simplex noise
  public  double noise(double xin, double yin, double zin) {
    double n0, n1, n2, n3; // Noise contributions from the four corners
    // Skew the input space to determine which simplex cell we're in
    double s = (xin+yin+zin)*F3; // Very nice and simple skew factor for 3D
    int i = fastfloor(xin+s);
    int j = fastfloor(yin+s);
    int k = fastfloor(zin+s);
    double t = (i+j+k)*G3;
    double X0 = i-t; // Unskew the cell origin back to (x,y,z) space
    double Y0 = j-t;
    double Z0 = k-t;
    double x0 = xin-X0; // The x,y,z distances from the cell origin
    double y0 = yin-Y0;
    double z0 = zin-Z0;
    // For the 3D case, the simplex shape is a slightly irregular tetrahedron.
    // Determine which simplex we are in.
    int i1, j1, k1; // Offsets for second corner of simplex in (i,j,k) coords
    int i2, j2, k2; // Offsets for third corner of simplex in (i,j,k) coords
    if(x0>=y0) {
      if(y0>=z0)
        { i1=1; j1=0; k1=0; i2=1; j2=1; k2=0; } // X Y Z order
        else if(x0>=z0) { i1=1; j1=0; k1=0; i2=1; j2=0; k2=1; } // X Z Y order
        else { i1=0; j1=0; k1=1; i2=1; j2=0; k2=1; } // Z X Y order
      }
    else { // x0<y0
      if(y0<z0) { i1=0; j1=0; k1=1; i2=0; j2=1; k2=1; } // Z Y X order
      else if(x0<z0) { i1=0; j1=1; k1=0; i2=0; j2=1; k2=1; } // Y Z X order
      else { i1=0; j1=1; k1=0; i2=1; j2=1; k2=0; } // Y X Z order
    }
    // A step of (1,0,0) in (i,j,k) means a step of (1-c,-c,-c) in (x,y,z),
    // a step of (0,1,0) in (i,j,k) means a step of (-c,1-c,-c) in (x,y,z), and
    // a step of (0,0,1) in (i,j,k) means a step of (-c,-c,1-c) in (x,y,z), where
    // c = 1/6.
    double x1 = x0 - i1 + G3; // Offsets for second corner in (x,y,z) coords
    double y1 = y0 - j1 + G3;
    double z1 = z0 - k1 + G3;
    double x2 = x0 - i2 + 2.0*G3; // Offsets for third corner in (x,y,z) coords
    double y2 = y0 - j2 + 2.0*G3;
    double z2 = z0 - k2 + 2.0*G3;
    double x3 = x0 - 1.0 + 3.0*G3; // Offsets for last corner in (x,y,z) coords
    double y3 = y0 - 1.0 + 3.0*G3;
    double z3 = z0 - 1.0 + 3.0*G3;
    // Work out the hashed gradient indices of the four simplex corners
    int ii = i & 255;
    int jj = j & 255;
    int kk = k & 255;
    int gi0 = permMod12[ii+perm[jj+perm[kk]]];
    int gi1 = permMod12[ii+i1+perm[jj+j1+perm[kk+k1]]];
    int gi2 = permMod12[ii+i2+perm[jj+j2+perm[kk+k2]]];
    int gi3 = permMod12[ii+1+perm[jj+1+perm[kk+1]]];
    // Calculate the contribution from the four corners
    double t0 = 0.6 - x0*x0 - y0*y0 - z0*z0;
    if(t0<0) n0 = 0.0;
    else {
      t0 *= t0;
      n0 = t0 * t0 * dot(grad3[gi0], x0, y0, z0);
    }
    double t1 = 0.6 - x1*x1 - y1*y1 - z1*z1;
    if(t1<0) n1 = 0.0;
    else {
      t1 *= t1;
      n1 = t1 * t1 * dot(grad3[gi1], x1, y1, z1);
    }
    double t2 = 0.6 - x2*x2 - y2*y2 - z2*z2;
    if(t2<0) n2 = 0.0;
    else {
      t2 *= t2;
      n2 = t2 * t2 * dot(grad3[gi2], x2, y2, z2);
    }
    double t3 = 0.6 - x3*x3 - y3*y3 - z3*z3;
    if(t3<0) n3 = 0.0;
    else {
      t3 *= t3;
      n3 = t3 * t3 * dot(grad3[gi3], x3, y3, z3);
    }
    // Add contributions from each corner to get the final noise value.
    // The result is scaled to stay just inside [-1,1]
    return 32.0*(n0 + n1 + n2 + n3);
  }


  

  // Inner class to speed upp gradient computations
  // (array access is a lot slower than member access)
  private static class Grad
  {
    double x, y, z;

    Grad(double x, double y, double z)
    {
      this.x = x;
      this.y = y;
      this.z = z;
    }

  }
  
  public int getSimplexNoise3D(long x, long y, long z){
	  return (int) (((1+this.noise(x*multiplicator3DX,y*multiplicator3DY,z*multiplicator3DZ)))*this.numberSpan+this.minVal3D);
  }
  
  
  
  public static void main(String argv[]) {
	  int x,y,z;
	  SimplexNoise3d sn = new SimplexNoise3d(14,20, 0, 1000);
	  int sample;
	  for (y=0; y<20; y++){
		  for (z=0; z<4; z++){
			  for (x=0; x<20; x++){ 
				  sample=sn.getSimplexNoise3D(x,y,z);
			  	if (sample>600)System.out.print("    ");
			  	else {
			  		if (sample<10)System.out.print((int)(sample)+"   ");
			  		else if(sample<100) System.out.print((int)(sample)+"  ");
			  		else System.out.print((int)(sample)+" ");
			  	}
			  }
			  System.out.print(" - ");
		  	}
		  System.out.println("");
	  }
  }
}