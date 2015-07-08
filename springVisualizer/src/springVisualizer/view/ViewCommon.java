package springVisualizer.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;

import javax.swing.Timer;

import springCommon.Parameters;

public class ViewCommon {
	public static class Dimentions {
		/** Zoom factor */
		static public double zoom = 1;
		
		/** x position of the scrolling */
		static public int posx = 0;
		/** y position of the scrolling */
		static public int posy = 0;
		
		/** x size of the image */
		static public int xSize = 0;
		/** y size of the image */
		static public int ySize = 0;
		
		/** x layout size */
		static public int layoutw = 0;
		/** y layout size */
		static public int layouth = 0;
		
		/** x view size (shown image pixels) */
		static public int viewWidth = 0;
		/** y view size (shown image pixels) */
		static public int viewHeight = 0;
		
		/** x ratio between internal and image */
		static public double ratiox = 0;
		/** y ratio between internal and image */
		static public double ratioy = 0;
		
		/** y offset for the drawing */
		static public int yoffset = 0;
		

		public static int xToDrawCoords(double x){
			return (int)(((x/ratiox)-posx)*zoom);
		}
		
		public static int yToDrawCoords(double y){
			return (int)(((y/ratioy)-posy)*zoom);
		}
		
		public static double xDrawToInternal(double x){
			return ((x/zoom) + posx)*ratiox;
		}
		
		public static double yDrawToInternal(double y){
			return ((y/zoom) + posy)*ratioy;
		}
		
		static void debugPrint(){
			for(Field f : Dimentions.class.getDeclaredFields()){
				try {
					System.out.println(f.getName() + " = " + f.getDouble(null));
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			System.out.println("");
		}
	}

	public static boolean needsRefresh = false;
	
	public static Timer refreshTimer;
	
	public static void init(){
		refreshTimer = new Timer(50, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(needsRefresh){
					ViewCommon.refresh();
					needsRefresh = false;
				}
			}
		});
		refreshTimer.start();
	}
	
	/** Refresh all the drawing of the state in the window */
	private static void refresh(){
		ViewCommon.refreshDimensions();
		
		MainWindow.refresh();
		ControlDialog.refresh();
	}

	/** Refresh all the dimensions of the window, useful for the drawing */
	private static void refreshDimensions(){
		/* Size of the viewport */
		Dimentions.layouth = MainWindow.fakeLabel.getHeight();
		Dimentions.layoutw = MainWindow.fakeLabel.getWidth();
		
		/* To not draw on the menubar */
		Dimentions.yoffset = MainWindow.bar.getHeight();
		
		/* Limits of the zoom factor */
	    if(Dimentions.zoom > Math.pow(2, Parameters.maxZoomFactor)){
			Dimentions.zoom = Math.pow(2, Parameters.maxZoomFactor);
		}
	    if(Dimentions.zoom < (1.0 / Math.pow(2, Parameters.maxDezoomFactor))){
			Dimentions.zoom = 1.0 / Math.pow(2, Parameters.maxDezoomFactor);
		}
	    
	    /* Set the view dimention, ceiling for partial pixel */
	    Dimentions.viewWidth = (int) Math.ceil(Dimentions.layoutw/Dimentions.zoom);
	    Dimentions.viewHeight = (int) Math.ceil(Dimentions.layouth/Dimentions.zoom);
	    
	    /* If the view dimentions exceeds the size, correct the initial position */
	    if(Dimentions.posx + Dimentions.viewWidth > Dimentions.xSize) {
	    	Dimentions.posx = Dimentions.xSize - Dimentions.viewWidth;
	    	/* If this is still too large, the picture is too small to fill the whole window */
	    	if(Dimentions.posx < 0){
	    		Dimentions.posx = 0;
	    		Dimentions.viewWidth = Dimentions.xSize;
	    	}
	    }
	    if(Dimentions.posy + Dimentions.viewHeight > Dimentions.ySize){
	    	Dimentions.posy = Dimentions.ySize - Dimentions.viewHeight;
	    	if(Dimentions.posy < 0){
	    		Dimentions.posy = 0;
	    		Dimentions.viewHeight = Dimentions.ySize;
	    	}
	    }
	    
	    /* Disable bar refresh */
	    MainWindow.barRefresh = false;
	    MainWindow.hBar.setValue(Dimentions.posx);
	    MainWindow.hBar.setVisibleAmount(Dimentions.viewWidth);
	    MainWindow.hBar.setMaximum(Dimentions.xSize);
	    MainWindow.vBar.setValue(Dimentions.posy);
	    MainWindow.vBar.setVisibleAmount(Dimentions.viewHeight);
	    MainWindow.vBar.setMaximum(Dimentions.ySize);
	    MainWindow.barRefresh = true;
	}
}
