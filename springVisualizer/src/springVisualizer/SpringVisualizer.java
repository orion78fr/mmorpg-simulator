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
		MainWindow.start();
		new ControlDialog().setVisible(true);
	}
}
