package springVisualizer;

public class SpringVisualizer {
	/** */
	public SpringVisualizer() {
	}
	
	/**
	 * The main function of the Visualizer.<br />
	 * Initializes variables and start the windows.
	 * 
	 * @param args
	 */
	public static void main(String args[]){
		int numPlayer = 5000;
		int maxIter = 100;
		
		//State.hotspots.add(new Hotspot(Parameters.size/2, Parameters.size/2, 50, Color.RED));
		
		/*for(int i = 0; i < 50; i++){
			State.hotspots.add(new Hotspot(State.r.nextInt((int)Parameters.sizex), State.r.nextInt((int)Parameters.sizey), State.r.nextInt(50) + 20));
		}*/
		
		//addHaltonPlayers(numPlayer);
		
		MainWindow.start();
		new ControlDialog().setVisible(true);
	}

}
