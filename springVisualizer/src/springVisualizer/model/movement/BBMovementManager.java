package springVisualizer.model.movement;

import springVisualizer.Parameters;
import springVisualizer.State;
import springVisualizer.model.Hotspot;
import springVisualizer.model.Player;

public class BBMovementManager extends AbstractMovementManager {
	private Hotspot h = null;
	private double bbProbaGoToNewHotspot;
	private double bbBetweenHotspotMoveDistance;
	private double bbBetweenHotspotRandomMoveDistance;
	private double bbInHotspotRandomMoveDistance;
	
	
	
	public BBMovementManager(double bbProbaGoToNewHotspot, double bbBetweenHotspotMoveDistance, double bbBetweenHotspotRandomMoveDistance, double bbInHotspotRandomMoveDistance) {
		super();
		this.bbProbaGoToNewHotspot = bbProbaGoToNewHotspot;
		this.bbBetweenHotspotMoveDistance = bbBetweenHotspotMoveDistance;
		this.bbBetweenHotspotRandomMoveDistance = bbBetweenHotspotRandomMoveDistance;
		this.bbInHotspotRandomMoveDistance = bbInHotspotRandomMoveDistance;
	}
	
	public BBMovementManager() {
		this(Parameters.bbProbaGoToNewHotspot, 
				Parameters.bbBetweenHotspotMoveDistance,
				Parameters.bbBetweenHotspotRandomMoveDistance,
				Parameters.bbInHotspotRandomMoveDistance);
	}

	/**
	 * Move a player between hotspot following the blue banana model (kindof)
	 * @param p
	 */
	@Override
	public void move(Player p) {
		// Pick a new hotspot to go to
		if(State.r.nextDouble() < this.bbProbaGoToNewHotspot){
			if(State.hotspots.size() != 0){
				this.h = State.pickRandomHotspotWithHotness();
			}
		}
		
		if(this.h != null){
			moveTowardsPoint(p, this.bbBetweenHotspotMoveDistance, h.getPoint());
			moveRandom(p, this.bbBetweenHotspotRandomMoveDistance);
		} else {
			moveRandom(p, this.bbInHotspotRandomMoveDistance);
		}
	}
	
	private void moveRandom(Player p, double radius){
		// TODO
	}
}
