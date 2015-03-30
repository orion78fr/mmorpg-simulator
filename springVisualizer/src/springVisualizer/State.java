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
			int x = p.getX();
			int y = p.getY();
			
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
		int x = p.getX();
		int y = p.getY();
		
		int moveTo = State.r.nextInt(maxMove);
		
		
		double dist = 0, tmp, importance, maximportance = Double.MAX_VALUE;
		Hotspot nearest = null;
		// Find nearest hotspot
		for(Hotspot h : State.hotspots){
			if((importance = (tmp = p.getPoint().distanceTo(h.getPoint()))/h.getHotness()) < maximportance){
				maximportance = importance;
				dist = tmp;
				nearest = h;
			}
		}
		
		if(nearest == null){
			return;
		}
		
		// Move toward it
		x = (int) (moveTo > dist ?  nearest.getX() : ((moveTo / dist) * (nearest.getX() - x) + x));
		y = (int) (moveTo > dist ?  nearest.getY() : ((moveTo / dist) * (nearest.getY() - y) + y));
		
		// Randomise a little
		double angle = State.r.nextDouble()*2*Math.PI;
		int rdist = maxMove - moveTo;
		
		x += rdist * Math.cos(angle);
		y += rdist * Math.sin(angle);
		
		p.setX(Math.max(0, Math.min(Parameters.size, x)));
		p.setY(Math.max(0, Math.min(Parameters.size, y)));
	}
	
	public State() {
		// TODO Auto-generated constructor stub
	}

}
