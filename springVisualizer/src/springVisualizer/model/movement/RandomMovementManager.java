package springVisualizer.model.movement;

import springCommon.Parameters;
import springVisualizer.State;
import springVisualizer.model.Player;

public class RandomMovementManager extends AbstractMovementManager {
	private double radius;
	public RandomMovementManager(double radius) {
		this.radius = radius;
	}
	
	public RandomMovementManager(){
		this(Parameters.defaultRandomMoveDistance);
	}
	
	/**
	 * Do a random move to a certain distance
	 * @param p The player moving
	 * @param radius Radius of the circle
	 */
	@Override
	public void move(Player p) {
		double x = p.getX();
		double y = p.getY();
		
		/* Pick a random angle and move to distance "radius" with this angle */ 
		double angle = State.r.nextDouble()*2*Math.PI;
		
		x += radius * Math.cos(angle);
		y += radius * Math.sin(angle);
		
		moveToWithinBouds(p, x, y);
	}
	public double getRadius() {
		return radius;
	}
	public void setRadius(double radius) {
		this.radius = radius;
	}
}
