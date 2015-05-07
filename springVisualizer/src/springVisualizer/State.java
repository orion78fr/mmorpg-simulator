package springVisualizer;

import java.awt.Color;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import springVisualizer.XML.XMLComment;
import springVisualizer.XML.XMLObj;
import springVisualizer.model.Hotspot;
import springVisualizer.model.Player;
import springVisualizer.model.Point;

/**
 * The class containing all the state of the simulated world
 * 
 * @author Guillaume Turchini
 */
public class State {
	/** The list of all the players */
	static public List<Player> playerList = new ArrayList<Player>();
	/** The list of the hotspots */
	static public List<Hotspot> hotspots = new ArrayList<Hotspot>();
	/** The common RNG for the movement of players */
	static private Random r = new Random(Parameters.seedMovement);
	/** The RNG for the color */
	private static Random rcolor = new Random(1234);
	
	/** Move all players randomly */
	public static void moveAllRandom(){
		for(Player p : State.playerList){
			moveRandom(p, Parameters.defaultRandomMoveDistance);
		}
	}
	
	/**
	 * Do a random move to a certain distance
	 * @param p The player moving
	 * @param radius Radius of the circle
	 */
	private static void moveRandom(Player p, double radius){
		double x = p.getX();
		double y = p.getY();
		
		/* Pick a random angle and move to distance "radius" with this angle */ 
		double angle = State.r.nextDouble()*2*Math.PI;
		
		x += radius * Math.cos(angle);
		y += radius * Math.sin(angle);
		
		moveToWithinBouds(p, x, y);
	}
	
	/**
	 * Move to coords but correct the position to be within the bounds of the world
	 * @param p The player moving
	 * @param x Coord x
	 * @param y Coord y
	 */
	private static void moveToWithinBouds(Player p, double x, double y){
		p.setX(Math.max(0, Math.min(Parameters.sizex, x)));
		p.setY(Math.max(0, Math.min(Parameters.sizey, y)));
	}
	
	/**
	 * Move to point but correct the position to be within the bounds of the world
	 * @param p The player moving
	 * @param coords The point to move to
	 */
	private static void moveToWithinBouds(Player p, Point coords){
		moveToWithinBouds(p, coords.getX(), coords.getY());
	}
	
	
	/** Move all players to their nearest hotspot */
	public static void moveAllToNearestHotspot(){
		for(Player p : State.playerList){
			moveToNearestHotspot(p, Parameters.defaultToHotspotMoveDistance);
		}
	}
	
	/**
	 * Move a player to its nearest hotspot 
	 * @param p The player moving
	 * @param distance The distance to move
	 */
	private static void moveToNearestHotspot(Player p, double distance){
		// Find nearest hotspot
		double tmpimportance, maximportance = Double.MAX_VALUE;
		
		Hotspot nearest = null;
		for(Hotspot h : State.hotspots){
			if((tmpimportance = p.getPoint().distanceTo(h.getPoint()))/h.getHotness() < maximportance){
				maximportance = tmpimportance;
				nearest = h;
			}
		}
		
		moveTowardsPoint(p, distance, nearest.getPoint());
	}
	
	/**
	 * Move a player towards a point.<br />
	 * If the distance of the point is less than the distance argument, move directly to the point
	 * 
	 * @param p The player moving
	 * @param distance The distance to move
	 * @param coords The point to move to
	 */
	public static void moveTowardsPoint(Player p, double distance, Point coords){
		double distBetween = p.getPoint().distanceTo(coords);
		if(distance > distBetween){
			moveToWithinBouds(p, coords);
		} else {
			double x = p.getX();
			double y = p.getY();
			
			double ratio = distance / distBetween;
			
			x += ratio * (coords.getX() / x);
			y += ratio * (coords.getY() - y);
			
			moveToWithinBouds(p, x, y);
		}
	}
	
	/** Move all players between hotspots (blue banana model) */
	public static void moveAllBetweenHotspots(){
		for(Player p : State.playerList){
			moveBetweenHotspots(p);
		}
	}
	
	// BlueBanana
	/**
	 * Move a player between hotspot following the blue banana model
	 * @param p
	 */
	public static void moveBetweenHotspots(Player p){
		// Pick a new hotspot to go to
		if(State.r.nextDouble() < Parameters.bbProbaGoToNewHotspot){
			if(State.hotspots.size() != 0){
				p.setObj(pickRandomHotspotWithHotness().getPoint());
			}
		}
		
		if(p.getObj() != null){
			moveTowardsPoint(p, Parameters.bbBetweenHotspotMoveDistance, p.getObj());
			moveRandom(p, Parameters.bbBetweenHotspotRandomMoveDistance);
		} else {
			moveRandom(p, Parameters.bbInHotspotRandomMoveDistance);
		}
	}
	
	/**
	 * Pick a random hotspot while taking into account the hotness
	 * @return The hotspot choosed
	 */
	public static Hotspot pickRandomHotspotWithHotness() {
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
		
		return State.hotspots.get(selected);
	}

	/** */
	private State() {
		throw new RuntimeException("You can't instanciate this class!");
	}
	
	/** 
	 * Add a comment to an XMLObj if asked by the config file 
	 * @param obj The XMLObj to add the comment to
	 * @param comment The content of the comment
	 */
	private static void addComment(XMLObj obj, String comment){
		if(Parameters.platformFileGenerateComments){
			obj.addChild(new XMLComment(comment));
		}
	}
	
	/**
	 * Export the platform file to the writer w
	 * @param w The writer
	 * @return True if succeed
	 */
	public static boolean exportPlatform(Writer w) {
		try {
			w.write("<?xml version='1.0'?>\n");
			w.write("<!DOCTYPE platform SYSTEM \"http://simgrid.gforge.inria.fr/simgrid.dtd\">\n");
			
			XMLObj content, thePlatform, as, elem;
			
			content = new XMLObj("platform").addAttr("version", "3");
			thePlatform = new XMLObj("AS").addAttr("id", "theplatform").addAttr("routing", "Full");
			content.addChild(thePlatform);
			
			addComment(thePlatform, "Server system declaration");
			as = new XMLObj("AS").addAttr("id", "theservers").addAttr("routing", "Full");
			
			addComment(as, "Hosts of the server");
			for(int i = 0; i < 4; i++){
				elem = new XMLObj("host")
					.addAttr("id", "server_"+i)
					.addAttr("power", "100Gf");
				as.addChild(elem);
			}
			
			addComment(as, "The physical links of the system");
			for(int i = 0; i < 4; i++){
				elem = new XMLObj("link")
					.addAttr("id", "server_"+i+"_link")
					.addAttr("bandwidth", "10GBps")
					.addAttr("latency", "50us");
				as.addChild(elem);
			}
			
			addComment(as, "The router of the server architecture, the gateway of this system");
			elem = new XMLObj("router").addAttr("id", "server_router");
			as.addChild(elem);
			
			addComment(as, "The routes within this system, giving topological informations");
			for(int i = 0; i < 4; i++){
				elem = new XMLObj("route")
					.addAttr("src", "server_"+i)
					.addAttr("dst", "server_router")
					.addAttr("symmetrical", "YES")
					.addChild(new XMLObj("link_ctn").addAttr("id", "server_"+i+"_link"));
				as.addChild(elem);
			}
			thePlatform.addChild(as);
			
			addComment(thePlatform, "Clients declaration");
			as = new XMLObj("AS").addAttr("id", "theclients").addAttr("routing", "Full");
			
			addComment(as, "The clients computers");
			for(int i = 0; i < playerList.size(); i++){
				elem = new XMLObj("host")
					.addAttr("id", "client_"+i)
					.addAttr("power", "50Gf");
				as.addChild(elem);
			}
			
			addComment(as, "The internet connection of each player");
			for(int i = 0; i < playerList.size(); i++){
				elem = new XMLObj("link")
					.addAttr("id", "client_"+i+"_internet_down")
					.addAttr("bandwidth", "20MBps")
					.addAttr("latency", "10ms");
				as.addChild(elem);
				elem = new XMLObj("link")
					.addAttr("id", "client_"+i+"_internet_up")
					.addAttr("bandwidth", "2MBps")
					.addAttr("latency", "10ms");
				as.addChild(elem);
			}
			
			addComment(as, "The \"internet\" router, giving a single entry point to the servers to simulate server internet connection");
			elem = new XMLObj("router").addAttr("id", "internet_router");
			as.addChild(elem);
			
			addComment(as, "All the client connections");
			for(int i = 0; i < playerList.size(); i++){
				elem = new XMLObj("route")
					.addAttr("src", "client_"+i)
					.addAttr("dst", "internet_router")
					.addAttr("symmetrical", "NO")
					.addChild(new XMLObj("link_ctn").addAttr("id", "client_"+i+"_internet_up"));
				as.addChild(elem);
				elem = new XMLObj("route")
					.addAttr("src", "internet_router")
					.addAttr("dst", "client_"+i)
					.addAttr("symmetrical", "NO")
					.addChild(new XMLObj("link_ctn").addAttr("id", "client_"+i+"_internet_down"));
				as.addChild(elem);
			}
			thePlatform.addChild(as);
			
			addComment(thePlatform, "The server gateway link to the internet");
			elem = new XMLObj("link")
				.addAttr("id", "servers_connection")
				.addAttr("bandwidth", "1GBps")
				.addAttr("latency", "1ms");
			thePlatform.addChild(elem);
			
			elem = new XMLObj("ASroute")
				.addAttr("src", "theservers")
				.addAttr("dst", "theclients")
				.addAttr("gw_src", "server_router")
				.addAttr("gw_dst", "internet_router")
				.addAttr("symmetrical", "YES")
				.addChild(new XMLObj("link_ctn").addAttr("id", "servers_connection"));
			thePlatform.addChild(elem);
			
			
			w.write(content.toString());
			return true;
		} catch (Exception e) {
			
		}
		return false;
	}
	
	/**
	 * Adds a player to the simulation
	 * @param x The x coordinate
	 * @param y The y coordinate
	 */
	public static void addPlayer(double x, double y){
		State.playerList.add(new Player(x, y, new Color(rcolor.nextInt(255), rcolor.nextInt(255), rcolor.nextInt(255))));
	}
	
	/**
	 * Randomly adds players to the simulation
	 * @param numPlayer The number of player to add
	 */
	public static void addRandomPlayers(int numPlayer){
		for(int i = 0; i < numPlayer; i++){
			addPlayer(State.r.nextDouble() * Parameters.sizex, State.r.nextDouble() * Parameters.sizey);
		}
	}
	
	/**
	 * Adds players to the simulation uniformly using the halton sequence
	 * @param numPlayer The number of player to add
	 */
	public static void addHaltonPlayers(int numPlayer){
		HaltonSequence h = new HaltonSequence(2);
		for(int i = 0; i < numPlayer; i++){
			double[] v = h.nextVector();
			addPlayer(v[0] * Parameters.sizex, v[1] * Parameters.sizey);
		}
	}
	
	/**
	 * Adds an hotspot to the simulation
	 * @param h The hotspot to add
	 */
	public static void addHotspot(Hotspot h){
		State.hotspots.add(h);
	}
	
	/**
	 * Adds an hotspot to the simulation
	 * @param x The x coordinate
	 * @param y The y coordinate
	 * @param hotness The hotness of the Hotspot
	 */
	public static void addHotspot(double x, double y, double hotness){
		addHotspot(new Hotspot(x, y, hotness));
	}
}
