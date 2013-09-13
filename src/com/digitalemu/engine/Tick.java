package com.digitalemu.engine;

public class Tick implements Runnable {
    private boolean keepRunning = true;
    private int ms;
    
    
    public Tick(int ms){
    	this.ms=ms;
    }



	/**
     * This class is responsible for generating a stable clock pulse, tick,
     * that controls game time.
     */
	@Override
	public void run() {
		System.out.println("Tick "+ms+" start on thread "+Thread.currentThread());
        do {
            try {
                Thread.sleep(ms);
            } catch (InterruptedException e) {
                // Do nothing
            }               
            TickEngine.tick(ms);
        }while(keepRunning);  
    }
	
	public void close(){
		keepRunning = false;
	}

}
