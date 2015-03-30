package springVisualizer;

import java.awt.Color;

public class Player {
	private Point p;
	private Color color;
	
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
	
	public Player(int x, int y) {
		super();
		this.p = new Point(x,y);
		this.color = null;
	}
	
	public Player(int x, int y, Color color) {
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
