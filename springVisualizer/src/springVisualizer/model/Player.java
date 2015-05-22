package springVisualizer.model;

import java.awt.Color;

import springVisualizer.MovementLogger;
import springVisualizer.model.movement.MovementManager;

public class Player {
	private static long playerID = 0;
	
	private Point p;
	private Color color;
	private long id;
	
	private boolean connected = false;
	
	private MovementManager movement;
	
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

	/**
	 * Creates a player that is connected at coordinates (x,y) with default color
	 * @param x The x coordinate
	 * @param y The y coordinate
	 */
	public Player(double x, double y) {
		super();
		this.p = new Point(x,y);
		this.color = Color.RED;
		this.id = getNewId();
		connect();
	}
	
	/**
	 * Creates a player that is connected at coordinates (x,y) with color color
	 * @param x The x coordinate
	 * @param y The y coordinate
	 * @param color The color
	 */
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
	
	public void connect(){
		assert(!this.connected);
		
		this.connected = true;
		MovementLogger.logConnect(this.id,this.p.getX(),this.p.getY());
	}
	
	public void connect(double x, double y){
		this.p = new Point(x, y);
		connect();
	}
	
	public void connect(Point p){
		connect(p.getX(), p.getY());
	}
	
	public void disconnect(){
		assert(this.connected);
		
		this.connected = false;
		MovementLogger.logDisonnect(this.id);
	}
	public MovementManager getMovement() {
		return movement;
	}
	public void setMovement(MovementManager movement) {
		this.movement = movement;
	}
	public void move(){
		this.movement.move(this);
	}
}
