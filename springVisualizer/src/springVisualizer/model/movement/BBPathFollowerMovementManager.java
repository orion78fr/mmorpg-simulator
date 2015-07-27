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
	private double bbInHotspotRandomMoveDistance;

	
	public BBPathFollowerMovementManager(double bbProbaGoToNewHotspot, double bbBetweenHotspotMoveDistance, double bbInHotspotRandomMoveDistance) {
		super();
		this.bbProbaGoToNewHotspot = bbProbaGoToNewHotspot;
		this.bbBetweenHotspotMoveDistance = bbBetweenHotspotMoveDistance;
		this.bbInHotspotRandomMoveDistance = bbInHotspotRandomMoveDistance;
	}
	
	public BBPathFollowerMovementManager() {
		this(Parameters.bbProbaGoToNewHotspot, 
				Parameters.bbBetweenHotspotMoveDistance,
				Parameters.bbInHotspotRandomMoveDistance);
	}
	
	private Point2d getNextInPath(){
		if(this.currentPath != null){
			if(this.currentPath.hasIntermediatePoints()){
				return this.currentPath.removeNextInPath();
			} else {
				this.currentPath = null;
				return this.h.getPoint();
			}
		} else {
			return null;
		}
	}
	
	static AStar_JPS jps = new AStar_JPS(State.tree);
	
	@Override
	public void move(Player p) {
		while(!State.tree.isTraversable(p.getPoint())){
			p.setX(State.r.nextDouble() * Parameters.sizex);
			p.setY(State.r.nextDouble() * Parameters.sizey);
		}
		// Pick a new hotspot to go to
		if(State.r.nextDouble() < this.bbProbaGoToNewHotspot){
			if(State.hotspots.size() != 0){
				this.h = State.pickRandomHotspotWithHotness();
				this.currentPath = jps.findPath(p.getPoint(), this.h.getPoint());
				if(this.currentPath == null){
					throw new RuntimeException("Your world is not fully connected, a hotspot is in a non reachable zone or the player is in a non reacheable zone (which should not happen)");
				}
				this.currentObj = getNextInPath();
			}
		}
		
		boolean firstMove = true;
		
		if(this.currentObj != null){
			double remainingDistance = (this.currentPath != null && this.currentPath.getTo().equals(this.h)) ? this.bbBetweenHotspotMoveDistance : this.bbInHotspotRandomMoveDistance;
			//double remainingDistance = this.bbBetweenHotspotMoveDistance;
			while(remainingDistance > 0){
				remainingDistance -= moveTowardsPoint(p, remainingDistance, this.currentObj);
				if(remainingDistance != 0){
					this.currentObj = getNextInPath();
					while(this.currentObj == null){
						// Get new path inside targetted hotspot in power law
						double radius = this.h.getProbabilisticDistance(State.r.nextDouble());
						double angle = State.r.nextDouble() * 2 * Math.PI;
						double x = this.h.getX() + radius * Math.cos(angle);
						double y = this.h.getY() + radius * Math.sin(angle);
						Point2d newObj = new Point2d(x, y);
						if(State.tree.isTraversable(newObj)){
							this.currentPath = jps.findPath(p.getPoint(), newObj);
							this.currentObj = getNextInPath();
						}
					}
					if(firstMove){
						firstMove = false;
					} else {
						remainingDistance = 0;
					}
				}
			}
		}
	}

}
