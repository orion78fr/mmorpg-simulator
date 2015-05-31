package springVisualizer;

import springVisualizer.view.ControlDialog;
import springVisualizer.view.MainWindow;

public class SpringVisualizer {
	/** */
	public SpringVisualizer() {
		throw new RuntimeException("You can't instanciate this class!");
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
