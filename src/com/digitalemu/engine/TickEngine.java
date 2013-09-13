package com.digitalemu.engine;

import java.util.ArrayList;
import java.util.Iterator;

import com.digitalemu.monster.MovableEntity;

public class TickEngine implements Runnable {
	private Tick fastTick;
	private Thread fastTickThread;
	private Tick slowTick;
	private Thread slowTickThread;
	private static ArrayList<MovableEntity>timer10ms = new ArrayList<MovableEntity>();
	private ArrayList<Object>timer100ms = new ArrayList<Object>();
	private ArrayList<Object>timer60000ms = new ArrayList<Object>();


	
	
	@Override
	public void run() {
		System.out.println("TickEngine start on thread "+Thread.currentThread());
		startTickGenerator();		
	}
	
	
	public void addMOvableEntity2Tick10(MovableEntity me){
		timer10ms.add(me);
	}
	
	public void deleteMovableObject2Tick10(MovableEntity me){
		timer10ms.remove(me);
	}
	
	public void addObject2Tick100(Object object){
		timer100ms.add(object);
	}
	
	public void addObject2Tick60000(Object object){
		timer60000ms.add(object);
	}
	
	
	
	private void startTickGenerator(){
		fastTick = new Tick(10);
        fastTickThread = new Thread(fastTick);
        fastTickThread.start();
		slowTick = new Tick(100);
        slowTickThread = new Thread(slowTick);
        slowTickThread.start();
	}
	
	public static void tick(int ms){
		if(ms==10){
			KeyboardAndMouse.Tick();
			Iterator<MovableEntity> it = timer10ms.iterator();
			while(it.hasNext())
			{
				it.next().tick();
			}
		}
	}
	

}
