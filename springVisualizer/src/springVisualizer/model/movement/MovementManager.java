package springVisualizer.model.movement;

import springVisualizer.model.Player;

/**
 * For the movement of a player. Got a move method which is called to know next momevement of a player.
 *
 * @author Guillaume Turchini
 */
public interface MovementManager {
	/**
	 * The method called when a player is moving.
	 * @param p The player moving
	 */
	public void move(Player p);
}
