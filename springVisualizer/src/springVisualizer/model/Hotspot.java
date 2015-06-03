package springVisualizer.model;

import java.awt.Color;

import springVisualizer.util2D.Point2d;

/**
 * A Hotspot corresponds to something interesting in the world that will attract players.<br />
 * It's defined by coordinates and a double that corresponds to the attractiveness of the hotspot (hotness).
 * 
 * @author Guillaume Turchini
 */
public class Hotspot {
	/** The default color of the hotspot */
	private static Color DEFAULT_COLOR = new Color(0,105,0);
	/** The coordinates of the hotspot */
	private Point2d p;
	/** The attractiveness of the hotspot */
	private double hotness;
	/** The current color of the hotspot */
	private Color color;
	
	public Hotspot(double x, double y, double hotness, Color color) {
		super();
		this.p = new Point2d(x,y);
		this.hotness = hotness;
		this.setColor(color);
	}
	public Hotspot(double x, double y, double hotness) {
		this(x,y,hotness,DEFAULT_COLOR);
	}
	
	public Hotspot(Point2d p, int hotness){
		this(p.getX(), p.getY(), hotness); // Defensive copy for the object point
	}
	
	public double getX() {
		return p.getX();
	}
	public void setX(double x) {
		p.setX(x);
	}
	public double getY() {
		return p.getY();
	}
	public void setY(double y) {
		p.setY(y);
	}

	public double getHotness() {
		return hotness;
	}

	public void setHotness(double hotness) {
		this.hotness = hotness;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	
	public Point2d getPoint(){
		return new Point2d(p.getX(), p.getY());
	}
}
