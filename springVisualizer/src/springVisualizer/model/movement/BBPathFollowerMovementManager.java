package springVisualizer.model.movement;

import springCommon.Parameters;
import springCommon.Point2d;
import springCommon.QTree.AStar_JPS;
import springCommon.QTree.TravelPath;
import springVisualizer.State;
import springVisualizer.model.Hotspot;
import springVisualizer.model.Player;

public class BBPathFollowerMovementManager extends AbstractMovementManager {
	private Hotspot h = null;
	private TravelPath currentPath = null;
	private Point2d currentObj = null;
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
	
	private Point2d getNextInPath(){
		if(this.currentPath.hasIntermediatePoints()){
			return this.currentPath.removeNextInPath();
		} else {
			return this.h.getPoint();
		}
	}
	
	@Override
	public void move(Player p) {
		// Pick a new hotspot to go to
		if(State.r.nextDouble() < this.bbProbaGoToNewHotspot){
			if(State.hotspots.size() != 0){
				this.h = State.pickRandomHotspotWithHotness();
				this.currentPath = new AStar_JPS(State.tree).findPath(p.getPoint(), this.h.getPoint());
				if(this.currentPath == null){
					throw new RuntimeException("Your world is not fully connected, a hotspot is in a non reachable zone or the player is in a non reacheable zone (which should not happen)");
				}
				this.currentObj = this.getNextInPath();
			}
		}
		
		double remainingDistance = this.bbBetweenHotspotMoveDistance;
		
		if(this.currentObj != null){
			while(remainingDistance > 0){
				remainingDistance -= moveTowardsPoint(p, remainingDistance, h.getPoint());
			}
		} else {
			// TODO move in power law density...
			//this.inHotspotRandomMovementManager.move(p);
		}
	}

}
