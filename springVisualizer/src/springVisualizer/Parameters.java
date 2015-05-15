package springVisualizer;

/**
 * This is the class containing all the parameters of the simulation
 * 
 * @author Guillaume Turchini
 */
public class Parameters {
	/** The seed of the movement of the players */
	static final public int seedMovement = 1234;
	
	/** Internal x size of the map, stretched when displayed */
	static final public double sizex = 5000;
	/** Internal y size of the map, stretched when displayed */
	static final public double sizey = 5000;
	
	/* Moving defaults */
		/** Standard random move distance */
		static final double defaultRandomMoveDistance = 10;
		/** Standard move distance to an hotspot */
		static final double defaultToHotspotMoveDistance = 10;
	
	/* Configuration of the blue banana model */
		/** Probability to choose a new hotspot to go to */
		static final public double bbProbaGoToNewHotspot = 0.01;
		/** Distance when doing random moves inside hotspot */
		static final public double bbInHotspotRandomMoveDistance = 10;
		/** Distance when going to an hotspot */
		static final public double bbBetweenHotspotMoveDistance = 10;
		/** Distance to randomize movement between hotspot */
		static final public double bbBetweenHotspotRandomMoveDistance = 5;
	
	/* Configuration of the exported platform file */
		/** Generate comments in the exported platform file */
		static final public boolean platformFileGenerateComments = true;
		/** Activate tabs and newlines in the exported platform file */
		static final public boolean platformFilePrettyXml = true;
		
	/* Configuration of the visualizer */
		/** Default play speed (in tick per second) */
		static final public int defaultPlaySpeed = 10;
		/** Minimum play speed (in tick per second) */
		static final public int minPlaySpeed = 4;
		/** Maximum play speed (in tick per second) */
		static final public int maxPlaySpeed = 200;
	
	/** */
	private Parameters(){
		throw new RuntimeException("You can't instanciate this class!");
	}
}
