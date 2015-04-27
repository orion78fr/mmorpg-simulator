package springVisualizer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class State {

	static List<Player> playerList = new ArrayList<Player>();
	static List<Hotspot> hotspots = new ArrayList<Hotspot>();
	static Random r = new Random(Parameters.seed);
	
	public static void randomMove(){
		for(Player p : State.playerList){
			double x = p.getX();
			double y = p.getY();
			
			x += State.r.nextInt(10) - 5;
			y += State.r.nextInt(10) - 5;
			
			p.setX(Math.max(0, Math.min(Parameters.size, x)));
			p.setY(Math.max(0, Math.min(Parameters.size, y)));
		}
	}
	
	static int maxMove = 50;
	
	public static void moveAllToNearestHotspot(){
		for(Player p : State.playerList){
			moveToNearestHotspot(p);
		}
	}
	
	public static void moveToNearestHotspot(Player p){
		double x = p.getX();
		double y = p.getY();
		
		int moveTo = State.r.nextInt(maxMove) + 1;
		
		double maxdist = 0, tmpdist, tmpimportance, maximportance = Double.MAX_VALUE;
		Hotspot nearest = null;
		// Find nearest hotspot
		for(Hotspot h : State.hotspots){
			if((tmpimportance = (tmpdist = p.getPoint().distanceTo(h.getPoint()))/h.getHotness()) < maximportance){
				maximportance = tmpimportance;
				maxdist = tmpdist;
				nearest = h;
			}
		}
		
		if(nearest == null){
			return;
		}
		
		// Move toward it
		if(moveTo > maxdist){
			x = nearest.getX();
			y = nearest.getY();
		} else {
			x += (moveTo / maxdist) * (nearest.getX() - x);
			y += (moveTo / maxdist) * (nearest.getY() - y);
		}
		
		// Randomise a little
		double angle = State.r.nextDouble()*2*Math.PI;
		int rdist = maxMove - moveTo;
		
		x += rdist * Math.cos(angle);
		y += rdist * Math.sin(angle);
		
		p.setX(Math.max(0, Math.min(Parameters.size, x)));
		p.setY(Math.max(0, Math.min(Parameters.size, y)));
	}
	
	public static void moveAllBetweenHotspots(){
		for(Player p : State.playerList){
			moveBetweenHotspots(p);
		}
	}
	
	// BlueBanana
	public static void moveBetweenHotspots(Player p){
		if(State.r.nextDouble() < Parameters.bbProbaGoToNewHotspot){
			if(State.hotspots.size() != 0){
				int hNbr = pickHotspotWithHotness();
				p.setObj(State.hotspots.get(hNbr).getPoint());
			}
		}
		
		double x = p.getX();
		double y = p.getY();
		
		Point obj = p.getObj();
		
		if(obj != null){
			double dist = obj.distanceTo(p.getPoint());
			// Move toward it
			if(Parameters.bbBetweenHotspotMoveDistance > dist){
				x = obj.getX();
				y = obj.getY();
				p.setObj(null);
			} else {
				x += (Parameters.bbBetweenHotspotMoveDistance / dist) * (obj.getX() - x);
				y += (Parameters.bbBetweenHotspotMoveDistance / dist) * (obj.getY() - y);
			}
		}
		
		double angle = State.r.nextDouble()*2*Math.PI;
		x += Parameters.bbInHotspotRandomMoveDistance * Math.cos(angle);
		y += Parameters.bbInHotspotRandomMoveDistance * Math.sin(angle);
		
		p.setX(Math.max(0, Math.min(Parameters.sizex, x)));
		p.setY(Math.max(0, Math.min(Parameters.sizey, y)));
	}
	
	public static int pickHotspotWithHotness() {
		double total = 0;
		for(Hotspot h : State.hotspots){
			total += h.getHotness();
		}
		
		double r = State.r.nextDouble() * total;
		
		int selected;
		
		for(selected = 0; selected < State.hotspots.size(); selected++){
			r -= State.hotspots.get(selected).getHotness();
			if(r < 0){
				break;
			}
		}
		return selected;
	}

	public State() {
		// TODO Auto-generated constructor stub
	}

}
