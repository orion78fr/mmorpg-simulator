package springVisualizer.model;

public class Point {
	private double x;
	private double y;
	
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	
	public Point(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}
	
	public double distanceTo(Point p){
		return Math.sqrt(Math.pow(this.x - p.getX(), 2) + Math.pow(this.y - p.getY(), 2));
	}
}
