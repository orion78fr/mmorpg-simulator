package springSimulator.utils.SimUtils;

import springSimulator.utils.SimUtils.interfaces.SimUtils;

public class Sim {
	private static SimUtils su = null;
	
	public static void init(){
		if(su == null){
			System.err.println("No simulation engine specified, defaulting to SimGrid!");
			try {
				Class.forName("springSimulator.utils.SimUtils.simgrid.SimgridSimUtils");
			} catch (ClassNotFoundException e) {
				// TODO Exception?
				System.err.println("Simgrid simulation engine not found, exiting...");
				System.exit(-1);
			}
		}
		su.init();
	}
	
	public static void setSu(SimUtils su){
		if(su != null){
			// TODO Exception?
			System.err.println("Multiple simulation engine loaded, exiting...");
			System.exit(-1);
		}
		Sim.su = su;
	}
}