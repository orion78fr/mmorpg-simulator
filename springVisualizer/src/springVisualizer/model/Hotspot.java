package springVisualizer.model;

import java.awt.Color;

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
	private Point p;
	/** The attractiveness of the hotspot */
	private double hotness;
	/** The current color of the hotspot */
	private Color color;
	
	public Hotspot(double x, double y, double hotness, Color color) {
		super();
		this.p = new Point(x,y);
		this.hotness = hotness;
		this.setColor(color);
	}
	public Hotspot(double x, double y, double hotness) {
		this(x,y,hotness,DEFAULT_COLOR);
	}
	
	public Hotspot(Point p, int hotness){
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
	
	public Point getPoint(){
		return new Point(p.getX(), p.getY());
	}
}
