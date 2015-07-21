package springVisualizer.model.movement;

import springCommon.Parameters;
import springVisualizer.State;
import springVisualizer.model.Hotspot;
import springVisualizer.model.Player;

public class BBPathFollowerMovementManager implements MovementManager {
	private Hotspot h = null;
	private double bbProbaGoToNewHotspot;
	private double bbBetweenHotspotMoveDistance;

	
	public BBPathFollowerMovementManager(double bbProbaGoToNewHotspot, double bbBetweenHotspotMoveDistance, double bbBetweenHotspotRandomMoveDistance, double bbInHotspotRandomMoveDistance) {
		super();
		this.bbProbaGoToNewHotspot = bbProbaGoToNewHotspot;
		this.bbBetweenHotspotMoveDistance = bbBetweenHotspotMoveDistance;
		
		
	}
	
	public BBPathFollowerMovementManager() {
		this(Parameters.bbProbaGoToNewHotspot, 
				Parameters.bbBetweenHotspotMoveDistance,
				Parameters.bbBetweenHotspotRandomMoveDistance,
				Parameters.bbInHotspotRandomMoveDistance);
	}
	
	@Override
	public void move(Player p) {
		// Pick a new hotspot to go to
		if(State.r.nextDouble() < this.bbProbaGoToNewHotspot){
			if(State.hotspots.size() != 0){
				this.h = State.pickRandomHotspotWithHotness();
			}
		}
		
		
	}

}
