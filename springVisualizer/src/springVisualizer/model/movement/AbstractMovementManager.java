package springVisualizer.model.movement;

import springVisualizer.MovementLogger;
import springVisualizer.Parameters;
import springVisualizer.model.Player;
import springVisualizer.model.Point;

/**
 * This class contains many static useful methods that may be used by your implementation of a movement manager.
 *
 * @author Guillaume Turchini
 */
public abstract class AbstractMovementManager implements MovementManager{
	/**
	 * Move to coords but correct the position to be within the bounds of the world. Prefer this to setting coordinates directly.
	 * @param p The player moving
	 * @param x Coord x
	 * @param y Coord y
	 */
	protected static void moveToWithinBouds(Player p, double x, double y){
		moveTo(p, Math.max(0, Math.min(Parameters.sizex, x)), Math.max(0, Math.min(Parameters.sizey, y)));
	}
	
	/**
	 * Move to coords without checking coordinates
	 * @param p The player moving
	 * @param x Coord x
	 * @param y Coord y
	 */
	private static void moveTo(Player p, double x, double y){
		p.setX(x);
		p.setY(y);
		MovementLogger.logMove(p.getId(), x, y);
	}
	
	/**
	 * Move to point but correct the position to be within the bounds of the world. Prefer this to setting coordinates directly.
	 * @param p The player moving
	 * @param coords The point to move to
	 */
	protected static void moveToWithinBouds(Player p, Point coords){
		moveToWithinBouds(p, coords.getX(), coords.getY());
	}
	
	/**
	 * Move a player towards a point.<br />
	 * If the distance of the point is less than the distance argument, move directly to the point
	 * 
	 * @param p The player moving
	 * @param distance The distance to move
	 * @param coords The point to move to
	 */
	protected static void moveTowardsPoint(Player p, double distance, Point coords){
		double distBetween = p.getPoint().distanceTo(coords);
		if(distance > distBetween){
			moveToWithinBouds(p, coords);
		} else {
			double x = p.getX();
			double y = p.getY();
			
			double ratio = distance / distBetween;
			
			x += ratio * (coords.getX() - x);
			y += ratio * (coords.getY() - y);
			
			moveToWithinBouds(p, x, y);
		}
	}
}
