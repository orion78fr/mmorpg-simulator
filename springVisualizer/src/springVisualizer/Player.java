package springVisualizer;

import java.awt.Color;

public class Player {
	private Point p;
	private Color color;
	private Point obj;
	
	public Point getObj() {
		return obj;
	}
	public void setObj(Point obj) {
		this.obj = obj;
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
	
	public Player(double x, double y) {
		super();
		this.p = new Point(x,y);
		this.color = Color.RED;
	}
	
	public Player(double x, double y, Color color) {
		this(x,y);
		this.color = color;
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
