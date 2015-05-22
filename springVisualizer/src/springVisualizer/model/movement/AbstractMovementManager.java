package springVisualizer.model.movement;

import springVisualizer.MovementLogger;
import springVisualizer.Parameters;
import springVisualizer.model.Player;
import springVisualizer.model.Point;

public abstract class AbstractMovementManager implements MovementManager{
	/**
	 * Move to coords but correct the position to be within the bounds of the world. Prefer this to setting coordinates directly.
	 * @param p The player moving
	 * @param x Coord x
	 * @param y Coord y
	 */
	protected void moveToWithinBouds(Player p, double x, double y){
		moveTo(p, Math.max(0, Math.min(Parameters.sizex, x)), Math.max(0, Math.min(Parameters.sizey, y)));
	}
	
	/**
	 * Move to coords without checking coordinates
	 * @param p The player moving
	 * @param x Coord x
	 * @param y Coord y
	 */
	private void moveTo(Player p, double x, double y){
		p.setX(x);
		p.setY(y);
		MovementLogger.logMove(p.getId(), x, y);
	}
	
	/**
	 * Move to point but correct the position to be within the bounds of the world. Prefer this to setting coordinates directly.
	 * @param p The player moving
	 * @param coords The point to move to
	 */
	protected void moveToWithinBouds(Player p, Point coords){
		moveToWithinBouds(p, coords.getX(), coords.getY());
	}
}
