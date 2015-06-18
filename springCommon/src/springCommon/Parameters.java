package springCommon;

/**
 * This is the class containing all the parameters of the simulation
 * 
 * @author Guillaume Turchini
 */
public final class Parameters {
	/** The seed of the movement of the players */
	static final public int seedMovement = 1234;
	
	/** Internal x size of the map, stretched when displayed */
	static final public double sizex = 4096;
	/** Internal y size of the map, stretched when displayed */
	static final public double sizey = 4096;
	
	/* Moving defaults */
		/** Standard random move distance */
		static final public double defaultRandomMoveDistance = 10;
		/** Standard move distance to an hotspot */
		static final public double defaultToHotspotMoveDistance = 10;
	
	/* Configuration of the blue banana model */
		/** Probability to choose a new hotspot to go to */
		static final public double bbProbaGoToNewHotspot = 0.05;
		/** Distance when doing random moves inside hotspot */
		static final public double bbInHotspotRandomMoveDistance = 10;
		/** Distance when going to an hotspot */
		static final public double bbBetweenHotspotMoveDistance = 20;
		/** Distance to randomize movement between hotspot */
		static final public double bbBetweenHotspotRandomMoveDistance = 5;
	
	/* Configuration of the exported platform file */
		/** Generate comments in the exported platform file */
		static final public boolean platformFileGenerateComments = true;
		/** Activate tabs and newlines in the exported platform file */
		static final public boolean platformFilePrettyXml = true;
		/** Speed of the local network between the servers in Mbps (symmetrical) */
		static final public long serverLocalNetworkSpeed = 10000;
		/** Latency of the local network between the servers in µs */
		static final public long serverLocalNetworkLatency = 50;
		/** Speed of the server gateway download in Mbps */
		static final public long serverGatewayDownloadSpeed = 1000;
		/** Speed of the server gateway download in Mbps */
		static final public long serverGatewayUploadSpeed = 1000;
		/** Latency of the server gateway in µs */
		static final public long serverGatewayLatency = 1000;
		/** Download Speed of the client in Mbps*/
		static final public long clientDownloadSpeed = 20;
		/** Upload Speed of the client in Mbps*/
		static final public long clientUploadSpeed = 2;
		/** Latency of the clients in ms*/
		static final public long clientLatency = 10;
		/** Maximum number of server */
		static final public long maxNumberOfServer = 100;
		/** Server processing power in GFlops */
		static final public long serverProcessingPower = 100;
		/** Client processing power in GFlops */
		static final public long clientProcessingPower = 50;
		
	/* Configuration of the visualizer */
		/** Default play speed (in tick per second) */
		static final public int defaultPlaySpeed = 10;
		/** Minimum play speed (in tick per second) */
		static final public int minPlaySpeed = 4;
		/** Maximum play speed (in tick per second) */
		static final public int maxPlaySpeed = 300;
	
	/* Configuration of the exported movement file */
		/** Number of tick in one second */
		static final public long tickrate = 64;
		/** Exported move folder, containing one move file per player */
		static final public String exportedMoveFolder = "movements"; 
	
		static final public int playerOverlayPlayerRadius = 20;
	/** */
	private Parameters(){
		throw new RuntimeException("You can't instanciate this class!");
	}
}
