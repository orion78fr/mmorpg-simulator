package springVisualizer;

import java.awt.Color;

public class Hotspot {
	private static Color DEFAULT_COLOR = new Color(0,105,0);
	private Point p;
	private float hotness;
	private Color color;
	
	public Hotspot(int x, int y, float hotness, Color color) {
		super();
		this.p = new Point(x,y);
		this.hotness = hotness;
		this.setColor(color);
	}
	public Hotspot(int x, int y, float hotness) {
		this(x,y,hotness,DEFAULT_COLOR);
	}
	
	public Hotspot(Point p, int hotness){
		this(p.getX(), p.getY(), hotness); // Defensive copy for the object point
	}
	
	public int getX() {
		return p.getX();
	}
	public void setX(int x) {
		p.setX(x);
	}
	public int getY() {
		return p.getY();
	}
	public void setY(int y) {
		p.setY(y);
	}

	public float getHotness() {
		return hotness;
	}

	public void setHotness(float hotness) {
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
