package springVisualizer.model.movement;

import springCommon.Parameters;
import springCommon.Point2d;
import springVisualizer.MovementLogger;
import springVisualizer.model.Player;

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
	protected static void moveToWithinBouds(Player p, Point2d coords){
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
	protected static double moveTowardsPoint(Player p, double distance, Point2d coords){
		double distBetween = p.getPoint().distance(coords);
		if(distance > distBetween){
			moveToWithinBouds(p, coords);
			return distBetween;
		} else {
			double x = p.getX();
			double y = p.getY();
			
			double ratio = distance / distBetween;
			
			x += ratio * (coords.getX() - x);
			y += ratio * (coords.getY() - y);
			
			moveToWithinBouds(p, x, y);
			return distance;
		}
	}
}
