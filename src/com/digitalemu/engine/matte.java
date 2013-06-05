package com.digitalemu.engine;

public class matte {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	float x = 10.25f;
	float y = 10.5f;
	float z = 10.75f;
	float x2;
	float y2;
	float z2;
	float distanceX; 
	float distanceY;
	float distanceZ;
	float yaw = 0;
	float pitch = 0;
	float distance = 0.1f;
	float mathx;
	float mathy;
	float mathz;
	float diffx;
	float diffy;
	float diffz;
	
	for(yaw=0; yaw<366; yaw=yaw+15){
		mathx = (float)Math.sin(Math.toRadians(yaw));
		mathz = (float)Math.cos(Math.toRadians(yaw));
		mathy = (float)Math.sin(Math.toRadians(pitch));
		if (mathx>0){
			x2=(int)x+1;
			diffx=x2-x;
			distanceX= (x2-x) / mathx;
		}
		else{
			x2=(int)x;
			diffx=x-x2;
			distanceX= (x-x2) / mathx;
		}
		if (mathz>0){
			z2=(int)z+1;
			diffz=z2-z;
			distanceZ= 0- (z2-z) / mathz;
		}
		else{
			z2=(int)z;
			diffz=z-z2;
			distanceZ= 0- (z-z2) / mathz;
		}
		if (mathy>0){
			y2=(int)y+1;
			diffy=y2-y;
			distanceY= (y2-y) / mathy;
		}
		else{
			y2=(int)y;
			diffy=y-y2;
			distanceY= (y-y2) / mathy;
		}

		

		System.out.print("yaw ");
        System.out.printf("%03.0f", yaw);
		if (Math.abs(distanceX)>Math.abs(distanceZ)){
			System.out.print("   Z first  ");
		}
		else {
			System.out.print("   X first  ");
		}
		System.out.print(" x2 ");
        System.out.printf("%4.1f", x2);
        System.out.print("  z2 ");
        System.out.printf("%4.1f", z2);
        System.out.print("  diffX ");
        System.out.printf("%4.2f", diffx);
        System.out.print("  diffZ ");
        System.out.printf("%4.2f", diffz);
        System.out.print("  dist x ");
        System.out.printf("%10.3f", distanceX);
        System.out.print("  dist z ");
        System.out.printf("%10.3f", distanceZ);
        System.out.print("  x ");
        System.out.printf("%10.3f", x);
        System.out.print("  z ");
        System.out.printf("%10.3f", z);
        System.out.print("  mathx ");
        System.out.printf("%10.3f", mathx);
        System.out.print("  mathz ");
        System.out.printf("%10.3f%n", mathz);
        //System.out.println(" dx "+x2+" dz "+z2+" x: "+x+" z: "+z+" mathx "+mathx+" mathz "+mathz);
        
	}
	System.out.println("toRadians(yaw) "+Math.toRadians(yaw));
	}

}
