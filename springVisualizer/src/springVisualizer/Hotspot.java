package springVisualizer;

import java.awt.Color;

public class Hotspot {
	private static Color DEFAULT_COLOR = new Color(0,105,0);
	private Point p;
	private double hotness;
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
