package com.digitalemu.opengl;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class Window {
	boolean fullScreen = false;
	private DisplayMode displayMode;
	private final String windowTitle = "MyCraft";

	public Window(boolean fullscreen){
        this.fullScreen = fullscreen;
        try {
			createWindow();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	private void createWindow() throws Exception {
        Display.setFullscreen(fullScreen);
        DisplayMode d[] = Display.getAvailableDisplayModes();
        for (int i = 0; i < d.length; i++) {
            if (d[i].getWidth() == 640
                && d[i].getHeight() == 480
                && d[i].getBitsPerPixel() == 32) {
                displayMode = d[i];
                break;
            }
        }
        Display.setDisplayMode(displayMode);
        Display.setTitle(windowTitle);
        Display.create();
    }
    
}
