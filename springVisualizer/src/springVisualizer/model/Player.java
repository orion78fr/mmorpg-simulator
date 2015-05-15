package springVisualizer.model;

import java.awt.Color;

import springVisualizer.MovementLogger;

public class Player {
	private static long playerID = 0;
	
	private Point p;
	private Color color;
	private Point obj;
	private long id;
	
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
	
	private synchronized long getNewId(){
		return playerID++;
	}
	
	public long getId() {
		return id;
	}

	public Player(double x, double y) {
		super();
		this.p = new Point(x,y);
		this.color = Color.RED;
		this.id = getNewId();
		MovementLogger.logConnect(id,x,y);
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
