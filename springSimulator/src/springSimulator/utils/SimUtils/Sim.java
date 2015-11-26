package springSimulator.utils.SimUtils;

import java.util.List;

import springSimulator.utils.SimUtils.interfaces.SimComm;
import springSimulator.utils.SimUtils.interfaces.SimHost;
import springSimulator.utils.SimUtils.interfaces.SimMessage;
import springSimulator.utils.SimUtils.interfaces.SimUtils;

public class Sim {
	private static SimUtils su = null;
	
	public static void init(String engine){
		try {
			if(engine.equals("simgrid")){
				Class.forName("springSimulator.utils.SimUtils.simgrid.SimgridSimUtils");
			} else {
				System.err.println("Unknown simulation engine : " + engine);
			}
		} catch (ClassNotFoundException e) {
			// TODO Exception?
			System.err.println(engine + " simulation engine not found, exiting...");
			System.exit(-1);
		}
		init();
	}
	
	public static void init(){
		if(su == null){
			System.err.println("No simulation engine specified, defaulting to SimGrid (!)");
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
		if(Sim.su != null){
			// TODO Exception?
			System.err.println("Multiple simulation engine loaded, exiting...");
			System.exit(-1);
		}
		Sim.su = su;
	}
	
	public static void compute(double flops){
		su.compute(flops);
	}
	public static void waitFor(double time){
		su.waitFor(time);
	}
	public static void waitUntil(double time){
		su.waitUntil(time);
	}
	
	public static SimHost getHostByName(String name){
		return su.getHostByName(name);
	}
	public static SimHost getCurrentHost(){
		return su.getCurrentHost();
	}
	
	public static SimMessage receive(){
		return su.receive();
	}
	public static SimMessage receive(double timeout){
		return su.receive(timeout);
	}
	public static SimMessage receiveUntil(double time){
		return su.receiveUntil(time);
	}
	public static SimComm ireceive(){
		return su.ireceive();
	}
	
	public static List<SimComm> ireceiveAllUntil(double time){
		return su.ireceiveAllUntil(time);
	}
	public static List<SimComm> ireceiveAllFor(double time){
		return su.ireceiveAllUntil(time);
	}
	
	public static void send(SimHost host, SimMessage message){
		su.send(host, message);
	}
	public static void send(SimHost host, SimMessage message, double timeout){
		su.send(host, message, timeout);
	}
	public static void sendUntil(SimHost host, SimMessage message, double time){
		su.sendUntil(host, message, time);
	}
	public static SimComm isend(SimHost host, SimMessage message){
		return su.isend(host, message);
	}
	
	
}