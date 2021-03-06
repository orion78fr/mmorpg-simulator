package springVisualizer.model;

import java.awt.Color;

import springCommon.Point2d;
import springVisualizer.MovementLogger;
import springVisualizer.model.movement.BBMovementManager;
import springVisualizer.model.movement.BBPathFollowerMovementManager;
import springVisualizer.model.movement.MovementManager;

public class Player {
	public static final Color defaultColor = Color.RED;
	
	private static long playerID = 0;
	
	private Point2d p;
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
	
	private synchronized static long getNewId(){
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
		this(x, y, new BBPathFollowerMovementManager(), defaultColor);
	}
	
	/**
	 * Creates a player that is connected at coordinates (x,y) with color color
	 * @param x The x coordinate
	 * @param y The y coordinate
	 * @param color The color
	 */
	public Player(double x, double y, Color color) {
		this(x, y, new BBPathFollowerMovementManager(), color);
	}
	
	public Player(double x, double y, MovementManager movement){
		this(x, y, movement, defaultColor);
	}
	
	public Player(double x, double y, MovementManager movement, Color color){
		super();
		this.p = new Point2d(x,y);
		this.color = color;
		this.id = getNewId();
		this.movement = movement;
		connect();
	}
	
	public Player(Point2d p){
		this(p.getX(), p.getY());
	}
	
	public Player(Point2d p, Color color){
		this(p.getX(), p.getY(), color);
	}
	
	public Player(Point2d p, MovementManager movement){
		this(p.getX(), p.getY(), movement);
	}
	
	public Player(Point2d p, MovementManager movement, Color color){
		this(p.getX(), p.getY(), movement, color);
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
	
	public void connect(){
		if(!this.connected){
			this.connected = true;
			MovementLogger.logConnect(this.id,this.p.getX(),this.p.getY());
		}
	}
	
	public void connect(double x, double y){
		this.p = new Point2d(x, y);
		connect();
	}
	
	public void connect(Point2d p){
		connect(p.getX(), p.getY());
	}
	
	public void disconnect(){
		if(this.connected){
			this.connected = false;
			MovementLogger.logDisonnect(this.id);
		}
	}
	public MovementManager getMovement() {
		return movement;
	}
	public void setMovement(MovementManager movement) {
		this.movement = movement;
	}
	public void move(){
		if(this.connected){
			this.movement.move(this);
		}
	}
}
