package springVisualizer;

import springVisualizer.view.ControlDialog;
import springVisualizer.view.MainWindow;
import springVisualizer.view.ViewCommon;

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
		ViewCommon.init();
		MainWindow.start();
		ControlDialog.start();
	}
}
