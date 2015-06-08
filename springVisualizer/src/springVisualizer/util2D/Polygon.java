package springVisualizer.util2D;

import java.awt.Color;
import java.awt.geom.Area;

import springVisualizer.view.ViewCommon.Dimentions;

public class Polygon extends java.awt.Polygon {
	private Color color = new Color((float)Math.random(), (float)Math.random(), (float)Math.random(), 0.5f);
	public Polygon() {
		super();
	}

	public Polygon(int[] xpoints, int[] ypoints, int npoints) {
		super(xpoints, ypoints, npoints);
	}
	
	@Override
	public void addPoint(int x, int y) {
		// Verify if convex
		java.awt.Polygon p2 = this.awtClone();
		p2.addPoint(x, y);
		
		verifyNonReentrant(p2);
		
		super.addPoint(x, y);
	}
	
	public static void verifyNonReentrant(java.awt.Polygon p) {
		if(p.npoints <= 3){
			return;
		}
		
		for(int i = 0; i < p.npoints - 1; i++){
			for(int j = i+2; j < p.npoints; j++){
				if(i == 0 && j == p.npoints - 1){
					continue; // Adjascent, so intersect at vertex
				}
				
				int jp1 = (j + 1) % p.npoints;
				
				// Using long because int overflows and gives incorrect results...
				
				long x1 = p.xpoints[i], x2 = p.xpoints[i+1], x3 = p.xpoints[j], x4 = p.xpoints[jp1];
				long y1 = p.ypoints[i], y2 = p.ypoints[i+1], y3 = p.ypoints[j], y4 = p.ypoints[jp1];
				
				long ux = x1 - x2,
					uy = y1 - y2,
					vx = x3 - x4,
					vy = y3 - y4;
				
				long det = ux * vy - uy * vx;
				if(det == 0){
					continue; // Collinear
				}
				
				double px = (1.0 / det) * ( (x1*y2 - y1*x2) * vx - ux * (x3*y4 - y3*x4) );
				double py = (1.0 / det) * ( (x1*y2 - y1*x2) * vy - uy * (x3*y4 - y3*x4) );
								
				long xmin = Math.max(Math.min(x1, x2), Math.min(x3, x4)),
					xmax = Math.min(Math.max(x1, x2), Math.max(x3, x4));
				
				if(px < xmin || px > xmax){
					continue; // x coordinate outside of range
				}
				long ymin = Math.max(Math.min(y1, y2), Math.min(y3, y4)),
					ymax = Math.min(Math.max(y1, y2), Math.max(y3, y4));
					
				if(py < ymin || py > ymax){
					continue; // y coordinate outside of range
				}
				
				throw new ReentrantPolygonException();
			}
		}
	}
	
	public static void main(String[] args){
		java.awt.Polygon p = new java.awt.Polygon();
		
		p.addPoint(1730, 1240);
		p.addPoint(2500, 3450);
		p.addPoint(4480, 3220);
		p.addPoint(4360, 740);
		p.addPoint(3390, 1870);
		p.addPoint(4600, 2110);
		
		try{
			verifyNonReentrant(p);
			System.out.println("non reentrant");
		} catch (ReentrantPolygonException e) {
			System.out.println("Polygon reentrant!!!");
		}
	}
	
	protected java.awt.Polygon awtClone() {
		int[] x = xpoints.clone();
		int[] y = ypoints.clone();
		
		return new java.awt.Polygon(x, y, npoints);
	}

	private static final long serialVersionUID = 1L;
	public Color getColor() {
		return color;
	}

	public void setColor(Color c) {
		this.color = c;
	}
	
	public Polygon toDrawCoords(){
		int[] x = xpoints.clone();
		int[] y = ypoints.clone();
		
		for(int i = 0; i < npoints; i++){
			x[i] = (int)(((x[i]/Dimentions.ratiox)-Dimentions.posx)*Dimentions.zoom);
			y[i] = (int)(((y[i]/Dimentions.ratioy)-Dimentions.posy)*Dimentions.zoom);
		}
		
		return new Polygon(x, y, npoints);
	}
	
	public static class ReentrantPolygonException extends RuntimeException{
		private static final long serialVersionUID = 1L;
		
	}
}
