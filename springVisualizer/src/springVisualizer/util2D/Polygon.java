package springVisualizer.util2D;

import java.awt.Color;

import springVisualizer.view.ViewCommon.Dimentions;

public class Polygon extends java.awt.Polygon {
	private Color color = new Color((float)Math.random(), (float)Math.random(), (float)Math.random());
	public Polygon() {
		super();
	}

	public Polygon(int[] xpoints, int[] ypoints, int npoints) {
		super(xpoints, ypoints, npoints);
	}

	private static final long serialVersionUID = 1L;
	public Color getColor() {
		return color;
	}

	public void setColor(Color c) {
		this.color = c;
	}
	
	public Polygon drawCoords(){
		int[] x = xpoints.clone();
		int[] y = ypoints.clone();
		
		for(int i = 0; i < npoints; i++){
			x[i] = (int)(((x[i]/Dimentions.ratiox)-Dimentions.posx)*Dimentions.zoom);
			y[i] = (int)(((y[i]/Dimentions.ratioy)-Dimentions.posy)*Dimentions.zoom);
		}
		
		return new Polygon(x, y, npoints);
	}
}
